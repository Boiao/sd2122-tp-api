package tp1.clients;

import tp1.Discovery;
public class ClientFactory {

    private static Discovery discovery;
/*
    public static Users getClient() {
        var serverURI = discovery.findUris("users");  // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
        return new RestUsersClient( serverURI );
       else
        return new SoapUsersClient( serverURI );
    }

 */
}
