package tp1.service;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.Discovery;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;
import tp1.clients.REST.RestFilesClient;
import tp1.clients.REST.RestUsersClient;
import tp1.server.REST.resources.DirResources;

import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;



public class JavaDirectory implements Directory {

    private final Map<String, Map<String, FileInfo>> directories = new ConcurrentHashMap<>();
    private final Map<String, Map<String, FileInfo>> access = new ConcurrentHashMap<>();
    private final Map<String, String> filesIDs = new ConcurrentHashMap<>();
    private static Logger Log = Logger.getLogger(DirResources.class.getName());
    private RestUsersClient usersClient;
    private RestFilesClient filesClient;
    private Discovery discv = new Discovery(null, "directory", null);
    private URI usersURI;
    private URI filesURI;
    private int fileid;


    public JavaDirectory() {
        usersURI = getServiceURI("users");
        filesURI = getServiceURI("files");
        usersClient = new RestUsersClient(usersURI);
        filesClient = new RestFilesClient(filesURI);
        fileid = 0;
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {


        User u = usersClient.getUser(userId, password).value();
        int status = usersClient.checkPasssword(userId,password);

        if (u != null && u.getPassword().equals(password) ) {
            String oldID = filesIDs.get(userId + "/" + filename);
            FileInfo file;
            if(oldID == null) {
                file = new FileInfo(userId, filename, filesURI + "/files/" + fileid, new HashSet<>());
                filesIDs.put(userId + "/" + filename, String.valueOf(fileid));
                filesClient.writeFile(String.valueOf(fileid), data, "");

                fileid++;
            } else {
                Set<String> sharedwith = directories.get(userId).get(filename).getSharedWith();
                file = new FileInfo(userId, filename, filesURI + "/files/" + oldID, sharedwith);
                filesClient.writeFile(oldID, data, "");
            }
            giveAccess(userId,filename,file);
            if(directories.containsKey(userId))
                directories.get(userId).put(filename,file);
            else{
                Map<String,FileInfo> usersfiles = new ConcurrentHashMap<>();
                usersfiles.put(filename,file);
                directories.put(userId,usersfiles);
            }

            return Result.ok(file);
        } else if (status == 404)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if (status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);


    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        User u = usersClient.getUser(userId, password).value();
        int status = usersClient.checkPasssword(userId,password);
        if (u != null && directories.containsKey(userId) && directories.get(userId).containsKey(filename)) {
            FileInfo file = directories.get(userId).get(filename);
            filesClient.deleteFile(filesIDs.get(userId + "/" + filename),"");
            directories.get(userId).remove(filename);
            filesIDs.remove(userId + "/" + filename);
            removeAccess(filename,file);
            return Result.ok();
        } else if (status == 404 || !directories.containsKey(userId) || !directories.get(userId).containsKey(filename))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

    }

    @Override
    public Result<Void> deleteUserFiles(String userId, String password) {
        Collection<FileInfo> userfiles = directories.get(userId).values();
        for(FileInfo f : userfiles){
            Set<String> users = access.keySet();
            String filename = f.getFilename();
            filesClient.deleteFile(filesIDs.get(userId + "/" + filename),"");
            for(String id : users)
                access.get(id).remove(filename, f);

        }
        directories.remove(userId);
        access.remove(userId);

        return Result.ok();
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {

        User u = usersClient.getUser(userId, password).value();
        int ushstatus = usersClient.checkPasssword(userIdShare,null);
        int status = usersClient.checkPasssword(userId,password);
        FileInfo file = directories.get(userId).get(filename);
        if ((ushstatus == 403 || ushstatus == 200) && u != null && directories.containsKey(userId) && directories.get(userId).containsKey(filename)) {
            Set<String> sharedWith = file.getSharedWith();
            sharedWith.add(userIdShare);
            file.setSharedWith(sharedWith);
            giveAccess(userIdShare,filename, file);
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        } else if (ushstatus == 404 || status == 404 || !directories.containsKey(userId) || !directories.get(userId).containsKey(filename))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        User u = usersClient.getUser(userId, password).value();
        int ushstatus = usersClient.checkPasssword(userIdShare,null);
        int status = usersClient.checkPasssword(userId,password);
        FileInfo file = directories.get(userId).get(filename);
        if ((ushstatus == 403 || ushstatus == 200) && u != null && directories.containsKey(userId) && directories.get(userId).containsKey(filename)) {
            Set<String> sharedWith = file.getSharedWith();
            sharedWith.remove(userIdShare);
            file.setSharedWith(sharedWith);
            if(!userId.equals(userIdShare))
                access.get(userIdShare).remove(filename);
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        } else if (ushstatus == 404 || status == 404 || !directories.containsKey(userId) || !directories.get(userId).containsKey(filename))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {

        int status = usersClient.checkPasssword(userId,null);
        User accu = usersClient.getUser(accUserId, password).value();
        int accstatus = usersClient.checkPasssword(accUserId,password);
        FileInfo file;
        //System.out.println("STATUS: " + accstatus + " " + status);
        if ((status == 403 || status == 200) && accu != null && directories.containsKey(userId) && directories.get(userId).containsKey(filename)) {
            file = directories.get(userId).get(filename);
            if(!file.getSharedWith().contains(accUserId) && !accUserId.equals(userId))
                throw new WebApplicationException(Response.Status.FORBIDDEN);
            throw new WebApplicationException(
                    Response.temporaryRedirect(
                            URI.create(file.getFileURL())).build());
        } else if (status == 404 || status == 404 || !directories.containsKey(userId) || !directories.get(userId).containsKey(filename))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }


    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        int status = usersClient.checkPasssword(userId,password);
        User u = usersClient.getUser(userId,password).value();
        if(u != null) {
            List<FileInfo> res = new ArrayList<>();
            if(access.containsKey(userId))
                res = new ArrayList<>(access.get(userId).values());
            return Result.ok(res);
        }else if( status == 404)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if( status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    private URI getServiceURI(String serviceName) {

        URI uri = null;
        discv.listener();
        int tries = 0;
        while (tries < 10) {
            if (discv.knownUrisOf(serviceName).length > 0) {
                uri = discv.knownUrisOf(serviceName)[0];
                break;
            }
        }
        return uri;
    }
    private void giveAccess(String userId,String filename, FileInfo file){
        if(access.containsKey(userId))
            access.get(userId).put(filename, file);
        else {
            Map<String,FileInfo> filesacc = new ConcurrentHashMap();
            filesacc.put(filename,file);
            access.put(userId,filesacc);
        }
    }
    private void removeAccess(String filename ,FileInfo file){
        for(String id : access.keySet()){
            access.get(id).remove(filename,file);
        }
    }
}