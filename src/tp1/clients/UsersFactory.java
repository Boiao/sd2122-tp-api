package tp1.clients;

import tp1.Discovery;
import tp1.api.service.util.Users;
import tp1.clients.REST.RestUsersClient;
import tp1.clients.SOAP.SoapUsersClient;

import java.net.URI;

public class UsersFactory {

    public static Users getClient(String serverURI) {
        // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
        return new RestUsersClient( URI.create(serverURI) );
       else
        return null;
    }


}
