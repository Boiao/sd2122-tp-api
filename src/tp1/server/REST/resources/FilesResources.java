package tp1.server.REST.resources;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.service.rest.RestFiles;
import tp1.service.JavaFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class FilesResources implements RestFiles {

   tp1.api.service.util.Files impl = new JavaFiles();

    public FilesResources() {

    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        var result = impl.writeFile(fileId, data, token);
        if(!result.isOK() )
            throw new WebApplicationException(result.toString(), Response.Status.valueOf(result.error().toString()));
    }

    @Override
    public void deleteFile(String fileId, String token) {
        var result = impl.deleteFile(fileId, token);
        if(!result.isOK() )
            throw new WebApplicationException(result.toString(), Response.Status.valueOf(result.error().toString()));
    }

    @Override
    public byte[] getFile(String fileId, String token) {
        var result = impl.getFile(fileId, token);
        if( result.isOK() )
            return result.value();
        else
            throw new WebApplicationException(result.toString(), Response.Status.valueOf(result.error().toString()));
    }



}
