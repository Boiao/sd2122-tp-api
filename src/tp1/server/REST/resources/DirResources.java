package tp1.server.REST.resources;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.Discovery;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Directory;
import tp1.clients.REST.RestFilesClient;
import tp1.clients.REST.RestUsersClient;

import java.net.InetAddress;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Singleton
public class DirResources implements RestDirectory {

    private final Map<String, FileInfo> directories = new ConcurrentHashMap<>();
    private final Map<String, String> filesIDs = new ConcurrentHashMap<>();
    private static Logger Log = Logger.getLogger(DirResources.class.getName());
    private RestUsersClient usersClient;
    private RestFilesClient filesClient;
    private Discovery discv = new Discovery(null, "Directory", null);
    private URI usersURI;
    private URI filesURI;
    private int fileid;


    public DirResources() {
        discv.listener();
        usersURI = getServiceURI("users");
        filesURI = getServiceURI("files");
        usersClient = new RestUsersClient(usersURI);
        filesClient = new RestFilesClient(filesURI);
        fileid = 0;
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {


        User u = usersClient.getUser(userId, password);
        int status = usersClient.checkPasssword(userId,password);

        if (u != null && u.getPassword().equals(password)) {
            String oldID = filesIDs.get(userId + "/" + filename);
            FileInfo file;
            if(oldID == null) {
                file = new FileInfo(userId, filename, filesURI + "/files/" + fileid, new HashSet<>());
                directories.put(userId + "/" + filename, file);
                filesIDs.put(userId + "/" + filename, String.valueOf(fileid));
                filesClient.writeFile(String.valueOf(fileid), data, "");
                fileid++;
            } else {
                file = new FileInfo(userId, filename, filesURI + "/files/" + oldID, new HashSet<>());
                directories.put(userId + "/" + filename, file);
                filesClient.writeFile(oldID, data, "");
            }

            return file;
        } else if (status == 404)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if (status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);


    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
        User u = usersClient.getUser(userId, password);
        int status = usersClient.checkPasssword(userId,password);
        FileInfo file = directories.get(userId + "/" + filename);
        if (u != null && file != null) {
            filesClient.deleteFile(filesIDs.get(userId + "/" + filename),"");
            directories.remove(userId + "/" + filename);
            filesIDs.remove(userId + "/" + filename);
        } else if (status == 404 || file == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {

        User u = usersClient.getUser(userId, password);
        //User ush = usersClient.getUserbyId(userIdShare);
        int status = usersClient.checkPasssword(userId,password);
        FileInfo file = directories.get(userId + "/" + filename);
        if (u != null && file != null) {
            Set<String> sharedWith = file.getSharedWith();
            sharedWith.add(userIdShare);
            file.setSharedWith(sharedWith);
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        } else if (u == null || file == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        User u = usersClient.getUser(userId, password);
        //User ush = usersClient.getUserbyId(userIdShare);
        int status = usersClient.checkPasssword(userId,password);
        FileInfo file = directories.get(userId + "/" + filename);
        if (u != null && file != null) {
            Set<String> sharedWith = file.getSharedWith();
            sharedWith.remove(userIdShare);
            file.setSharedWith(sharedWith);
            throw new WebApplicationException(Response.Status.NO_CONTENT);
        } else if (u == null || file == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == 403)
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {

        User u = usersClient.getUser(userId,password);
        User accu = usersClient.getUser(accUserId, password);
        int status = usersClient.checkPasssword(accUserId,password);
        FileInfo file = directories.get(userId + "/" + filename);
        if (u != null && accu != null && file != null) {
            throw new WebApplicationException(
                    Response.temporaryRedirect(
                            URI.create(file.getFileURL())).build());
        } else if (u == null || accu == null || file == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(status == 403 || !file.getSharedWith().contains(accUserId))
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }


    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return null;
    }

    private URI getServiceURI(String serviceName) {

        URI uri = null;
        discv.listener();
        while (true) {
            if (discv.knownUrisOf(serviceName).length > 0) {
                uri = discv.knownUrisOf(serviceName)[0];
                break;
            }
        }

        return uri;


    }
}
