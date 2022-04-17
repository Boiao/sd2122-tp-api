package tp1.server.REST.resources;

import jakarta.inject.Singleton;
import tp1.api.service.rest.RestFiles;

import java.util.HashMap;
import java.util.Map;

@Singleton
public class FilesResources implements RestFiles {
    public static FilesResources instance = new FilesResources();
    public Map<String,byte[]> files = new HashMap<>();

    public FilesResources(){

    }
    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        files.put(fileId,data);

    }

    @Override
    public void deleteFile(String fileId, String token) {

    }

    @Override
    public byte[] getFile(String fileId, String token) {
        return new byte[0];
    }

    public static FilesResources getInstance(){
        return instance;
    }
}
