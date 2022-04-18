package tp1.server.REST.resources;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.service.rest.RestFiles;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class FilesResources implements RestFiles {

    public Map<String,byte[]> files = new HashMap<>();

    public FilesResources(){

    }
    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        try{
            files.put(fileId,data);
        } catch (Exception e){
        throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

    }

    @Override
    public void deleteFile(String fileId, String token) {

    }

    @Override
    public byte[] getFile(String fileId, String token) {
        byte[] file = files.get(fileId);
        if(file != null)
            return file;
        else if(file == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
    }

}
