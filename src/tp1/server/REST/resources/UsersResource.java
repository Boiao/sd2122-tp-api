package tp1.server.REST.resources;

import java.net.URI;
import java.util.*;
import java.util.logging.Logger;

import jakarta.inject.Singleton;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import tp1.Discovery;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Users;
import tp1.clients.REST.RestDirClient;
import tp1.service.JavaUsers;

@Singleton
public class UsersResource implements RestUsers {

    final Users impl = new JavaUsers();

    public UsersResource() {

    }

    @Override
    public String createUser(User user) {
        var result = impl.createUser( user );
        if( result.isOK() )
            return result.value();
        else
        throw new WebApplicationException(result.toString(), Response.Status.valueOf(result.error().toString()));
    }

    @Override
    public User getUser(String userId, String password) {
        var result = impl.getUser(userId,password);
        if(result.isOK())
            return result.value();
        else
            throw new WebApplicationException(result.toString(), Response.Status.valueOf(result.error().toString()));
    }


    @Override
    public User updateUser(String userId, String password, User updatedUser) {
        var result = impl.updateUser(userId,password,updatedUser);
        if(result.isOK())
            return result.value();
        else
            throw new WebApplicationException(result.toString(), Response.Status.valueOf(result.error().toString()));
    }


    public User deleteUser(String userId, String password) {
        var result = impl.deleteUser(userId,password);
        if(result.isOK())
            return result.value();
        else
            throw new WebApplicationException(result.toString(), Response.Status.valueOf(result.error().toString()));
    }


    @Override
    public List<User> searchUsers(String pattern) {
        var result = impl.searchUsers(pattern);
        if(result.isOK())
            return result.value();
        else
            throw new WebApplicationException(result.toString(), Response.Status.valueOf(result.error().toString()));
    }

}
