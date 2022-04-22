package tp1.service;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.Discovery;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;
import tp1.clients.ClientFactory;
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
    private Users usersClient;
    private Files filesClient;
    private Discovery discv = new Discovery(null, "directory", null);
    private URI usersURI;
    private URI filesURI;
    private int fileid;


    public JavaDirectory() {
        usersURI = getServiceURI("users");
        filesURI = getServiceURI("files");
        usersClient = ClientFactory.getUserClient(usersURI.toString());
        filesClient = ClientFactory.getFilesClient(filesURI.toString());
        fileid = 0;
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {


        var u = usersClient.getUser(userId, password);
        var status = u.error();

        if (status == Result.ErrorCode.OK && u.value().getPassword().equals(password) ) {
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
        } else if (status == Result.ErrorCode.NOT_FOUND)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if (status == Result.ErrorCode.FORBIDDEN)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);


    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        var u = usersClient.getUser(userId, password);
        var status = u.error();
        if (status == Result.ErrorCode.OK && directories.containsKey(userId) && directories.get(userId).containsKey(filename)) {
            FileInfo file = directories.get(userId).get(filename);
            filesClient.deleteFile(filesIDs.get(userId + "/" + filename),"");
            directories.get(userId).remove(filename);
            filesIDs.remove(userId + "/" + filename);
            removeAccess(filename,file);
            return Result.ok();
        } else if (status == Result.ErrorCode.NOT_FOUND || !directories.containsKey(userId) || !directories.get(userId).containsKey(filename))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == Result.ErrorCode.FORBIDDEN)
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

        var ush = usersClient.getUser(userIdShare, null);
        var u = usersClient.getUser(userId, password);

        var status = u.error();
        var ushstatus = ush.error();
        FileInfo file = directories.get(userId).get(filename);
        System.out.println("STATUS: " + status.toString() + " " + ushstatus.toString());
        if ((ushstatus != Result.ErrorCode.NOT_FOUND && status == Result.ErrorCode.OK && directories.containsKey(userId) && directories.get(userId).containsKey(filename))) {
            Set<String> sharedWith = file.getSharedWith();
            sharedWith.add(userIdShare);
            file.setSharedWith(sharedWith);
            giveAccess(userIdShare,filename, file);
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        } else if (ushstatus == Result.ErrorCode.NOT_FOUND || status == Result.ErrorCode.NOT_FOUND || !directories.containsKey(userId) || !directories.get(userId).containsKey(filename))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == Result.ErrorCode.FORBIDDEN)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        var ush = usersClient.getUser(userIdShare, null);
        var u = usersClient.getUser(userId, password);

        var status = u.error();
        var ushstatus = ush.error();
        FileInfo file = directories.get(userId).get(filename);
        if ((ushstatus != Result.ErrorCode.NOT_FOUND && status == Result.ErrorCode.OK && directories.containsKey(userId) && directories.get(userId).containsKey(filename))) {
            Set<String> sharedWith = file.getSharedWith();
            sharedWith.remove(userIdShare);
            file.setSharedWith(sharedWith);
            if(!userId.equals(userIdShare))
                access.get(userIdShare).remove(filename);
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        } else if (ushstatus == Result.ErrorCode.NOT_FOUND || status == Result.ErrorCode.NOT_FOUND || !directories.containsKey(userId) || !directories.get(userId).containsKey(filename))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == Result.ErrorCode.FORBIDDEN)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        var accu = usersClient.getUser(accUserId, password);
        var u = usersClient.getUser(userId, null);

        var status = u.error();
        var accstatus = accu.error();
        FileInfo file;

        if (status != Result.ErrorCode.NOT_FOUND && accstatus == Result.ErrorCode.OK && directories.containsKey(userId) && directories.get(userId).containsKey(filename)) {
            file = directories.get(userId).get(filename);

            if(!file.getSharedWith().contains(accUserId) && !accUserId.equals(userId))
                throw new WebApplicationException(Response.Status.FORBIDDEN);

            throw new WebApplicationException(
                    Response.temporaryRedirect(
                            URI.create(file.getFileURL())).build());
        } else if (status == Result.ErrorCode.NOT_FOUND || status == Result.ErrorCode.NOT_FOUND || !directories.containsKey(userId) || !directories.get(userId).containsKey(filename))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(accstatus == Result.ErrorCode.FORBIDDEN)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }


    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        var u = usersClient.getUser(userId, password);
        var status = u.error();
        if(status == Result.ErrorCode.OK) {
            List<FileInfo> res = new ArrayList<>();
            if(access.containsKey(userId))
                res = new ArrayList<>(access.get(userId).values());
            return Result.ok(res);
        }else if( status == Result.ErrorCode.NOT_FOUND)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if( status == Result.ErrorCode.FORBIDDEN)
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