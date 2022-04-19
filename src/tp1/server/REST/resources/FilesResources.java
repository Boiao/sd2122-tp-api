package tp1.server.REST.resources;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tp1.api.service.rest.RestFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Singleton
public class FilesResources implements RestFiles {

    public Map<String, Path> files = new ConcurrentHashMap<>();

    public FilesResources() {

    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        try {
            Files.write(Path.of(fileId), data);
            files.put(fileId, Path.of( fileId));
        } catch (Exception e) {
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }

    }

    @Override
    public void deleteFile(String fileId, String token) {
        if (files.containsKey(fileId)) {
            Path path = files.get(fileId);
            files.remove(fileId);
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (!files.containsKey(fileId))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);

    }

    @Override
    public byte[] getFile(String fileId, String token) {
        if (files.containsKey(fileId)) {
            Path path = files.get(fileId);
            try {
                return Files.readAllBytes(path);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else if (!files.containsKey(fileId))
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        else
            throw new WebApplicationException(Response.Status.BAD_REQUEST);


    }



}
