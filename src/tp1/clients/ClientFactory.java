package tp1.clients;

import tp1.Discovery;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Files;
import tp1.api.service.util.Users;
import tp1.clients.REST.RestDirClient;
import tp1.clients.REST.RestFilesClient;
import tp1.clients.REST.RestUsersClient;
import tp1.clients.SOAP.SoapDirClient;
import tp1.clients.SOAP.SoapFilesClient;
import tp1.clients.SOAP.SoapUsersClient;

import java.net.MalformedURLException;
import java.net.URI;

public class ClientFactory {
    static Discovery discv = new Discovery(null,null,null);

    public static Users getUserClient() {
        // use discovery to find a uri of the Users service;
        String serverURI = getServiceURI("users");
        if( serverURI.endsWith("rest"))
            return new RestUsersClient( URI.create(serverURI) );
        else
            return new SoapUsersClient(URI.create(serverURI));
    }

    public static Directory getDirClient() {
        // use discovery to find a uri of the Users service;
        String serverURI = getServiceURI("directory");
        if( serverURI.endsWith("rest"))
            return new RestDirClient( URI.create(serverURI) );
        else
            return new SoapDirClient();
    }

    public static Files getFilesClient() {
        // use discovery to find a uri of the Users service;
        String serverURI = getServiceURI("files");
        if( serverURI.endsWith("rest"))
            return new RestFilesClient( URI.create(serverURI) );
        else
            return new SoapFilesClient();
    }

    private static String getServiceURI(String serviceName) {

        URI uri = null;
        discv.listener();
        int tries = 0;
        while (tries < 10) {
            if (discv.knownUrisOf(serviceName).length > 0) {
                uri = discv.knownUrisOf(serviceName)[0];
                break;
            }
        }
        return uri.toString();
    }
}
