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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientFactory {
    static Discovery discv = new Discovery(null,null,null);

    public ClientFactory(){
        discv.listener();
    }

    public static Users getUserClient(String serverURI) {
        // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
            return new RestUsersClient( URI.create(serverURI) );
        else
            return new SoapUsersClient(URI.create(serverURI));
    }

    public static Directory getDirClient(String serverURI) {
        // use discovery to find a uri of the Users service;
        if( serverURI.endsWith("rest"))
            return new RestDirClient( URI.create(serverURI) );
        else
            return new SoapDirClient();
    }

    public static Files getFilesClient(String serverURI){
        if( serverURI.endsWith("rest"))
            return new RestFilesClient( URI.create(serverURI) );
        else
            return new SoapFilesClient();
    }
/*
    public static List<Files> getFilesClient() {
        // use discovery to find a uri of the Users service;
        List<URI> serverURI = getFilesURI("files");
        List<Files> servers = new ArrayList<>();
        for(URI uri : serverURI) {
            if (uri.toString().endsWith("rest"))
                servers.add(new RestFilesClient(uri));
            else
                servers.add(new SoapFilesClient());
        }
        return servers;
    }
*/
    public static URI getServiceURI(String serviceName) {

        URI uri = null;
        //int tries = 0;
        while (true) {
            if (discv.knownUrisOf(serviceName).length > 0) {
                uri = discv.knownUrisOf(serviceName)[0];
                break;
            }
        }
        return uri;
    }

    public static Map<URI,Integer> getFilesURI(String serviceName) {
        Map<URI,Integer> uri = new ConcurrentHashMap<>();
        while (true) {
            if(discv.knownUrisOf(serviceName).length > 0) {
                for(int i = 0 ; i < discv.knownUrisOf(serviceName).length ; i++)
                uri.put(discv.knownUrisOf(serviceName)[i],0);
                break;
            }
        }
        return uri;
    }
}
