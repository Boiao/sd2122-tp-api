package tp1.clients;

import tp1.Discovery;
import tp1.api.service.util.Users;
import tp1.clients.REST.RestUsersClient;

public class ClientFactory {

    private static Discovery discovery;
/*
    public static Users getClient() {
        var serverURI = discovery.knownUrisOf("UsersService");  // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
        return new RestUsersClient( serverURI );
       else
        return new SoapUsersClient( serverURI );
    }

 */
}
