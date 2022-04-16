package tp1.server.REST.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Directory;

import java.util.*;
import java.util.logging.Logger;

public class DirResources implements RestDirectory {

    private final Map<String, FileInfo> directories = new HashMap<>();
    private final Map<String, User> users = new HashMap<>();
    private static Logger Log = Logger.getLogger(DirResources.class.getName());

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        User u = users.get(userId);
        if(u != null && u.getPassword().equals(password)) {
            FileInfo file = new FileInfo(userId, filename, userId + "/" + filename, new HashSet<String>());
            directories.put(userId + "/" + filename, file);
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
        return new byte[0];
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return null;
    }
}
