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
    private Discovery discv = new Discovery(null,"Directory",null);
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


        User u = usersClient.getUser(userId,password);

        if(u != null && u.getPassword().equals(password)) {
            FileInfo file = new FileInfo(userId, filename, filesURI + "/" + fileid, new HashSet<>());
            directories.put(userId + "/" + filename, file);
            filesIDs.put(userId + "/" + filename, String.valueOf(fileid));
            filesClient.writeFile(String.valueOf(fileid),data,"");
            fileid++;
            return file;
        }
        else if(u == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(!u.getPassword().equals(password))
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);


    }

    @Override
    public void deleteFile(String filename, String userId, String password) {

    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {

    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {

    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {

        User u = usersClient.getUser(accUserId,password);
        FileInfo file = directories.get(userId + "/" + filename);
        if(u != null && file != null){
            String fileid = filesIDs.get(userId + "/" + filename);
            return filesClient.getFile(fileid,"");
        }else if(u == null || file == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else if(!u.getPassword().equals(password))
            throw new WebApplicationException(Response.Status.FORBIDDEN);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return null;
    }

    private URI getServiceURI(String serviceName){

        URI uri = null;
        discv.listener();
        while(true) {
            if(discv.knownUrisOf(serviceName).length > 0) {
                uri = discv.knownUrisOf(serviceName)[0];
                break;
            }
        }

        return uri;


    }
}