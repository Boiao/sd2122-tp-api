package tp1.clients;

import tp1.api.service.util.Directory;
import tp1.api.service.util.Users;
import tp1.clients.REST.RestDirClient;
import tp1.clients.REST.RestUsersClient;

import java.net.URI;

public class DirFactory{

    public static Directory getClient(String serverURI) {
        // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
            return new RestDirClient( URI.create(serverURI) );
        else
            return null;
    }
}
