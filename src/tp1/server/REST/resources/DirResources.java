package tp1.server.REST.resources;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.Discovery;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Users;
import tp1.clients.REST.RestFilesClient;
import tp1.clients.REST.RestUsersClient;
import tp1.service.JavaDirectory;
import tp1.service.JavaUsers;

import javax.swing.*;
import java.io.File;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

@Singleton
public class DirResources implements RestDirectory {

    final Directory impl = new JavaDirectory();

    public DirResources() throws MalformedURLException {

    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        var result = impl.writeFile(filename, data, userId, password);
        if( result.isOK())
            return result.value();
        else
            throw new WebApplicationException(Response.Status.valueOf(result.error().toString()));
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {
        var result = impl.deleteFile(filename, userId, password);
        if( !result.isOK() )
            throw new WebApplicationException(Response.Status.valueOf(result.error().toString()));
    }

    @Override
    public void deleteUserFiles(String userId, String password) {
        var result = impl.deleteUserFiles(userId, password);
        if( !result.isOK() )
            throw new WebApplicationException(Response.Status.valueOf(result.error().toString()));
    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {

        var result = impl.shareFile(filename, userId, userIdShare, password);
        if( !result.isOK() )
            throw new WebApplicationException(Response.Status.valueOf(result.error().toString()));
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {
        var result = impl.unshareFile(filename, userId, userIdShare, password);
        if( !result.isOK() )
            throw new WebApplicationException(Response.Status.valueOf(result.error().toString()));
    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {

        var result = impl.getFile(filename, userId, accUserId, password);
        if( result.isOK() )
            return result.value();
        else
            throw new WebApplicationException(Response.Status.valueOf(result.error().toString()));
    }


    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        var result = impl.lsFile(userId, password);
        if( result.isOK() )
            return result.value();
        else
            throw new WebApplicationException(Response.Status.valueOf(result.error().toString()));
    }

}
