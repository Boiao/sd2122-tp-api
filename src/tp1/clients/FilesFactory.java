package tp1.clients;


import tp1.api.service.util.Files;

import tp1.clients.REST.RestFilesClient;


import java.net.URI;

public class FilesFactory {

    public static Files getClient(String serverURI) {
        // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
            return new RestFilesClient( URI.create(serverURI) );
        else
            return null;
    }
}
