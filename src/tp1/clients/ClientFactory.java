package tp1.clients;

import tp1.api.service.util.Directory;
import tp1.api.service.util.Files;
import tp1.api.service.util.Users;
import tp1.clients.REST.RestDirClient;
import tp1.clients.REST.RestFilesClient;
import tp1.clients.REST.RestUsersClient;
import tp1.clients.SOAP.SoapDirClient;
import tp1.clients.SOAP.SoapFilesClient;
import tp1.clients.SOAP.SoapUsersClient;

import java.net.URI;

public class ClientFactory {

    public static Users getUserClient(String serverURI) {
        // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
            return new RestUsersClient( URI.create(serverURI) );
        else
            return new SoapUsersClient();
    }

    public static Directory getDirClient(String serverURI) {
        // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
            return new RestDirClient( URI.create(serverURI) );
        else
            return new SoapDirClient();
    }

    public static Files getFilesClient(String serverURI) {
        // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
            return new RestFilesClient( URI.create(serverURI) );
        else
            return new SoapFilesClient();
    }
}
