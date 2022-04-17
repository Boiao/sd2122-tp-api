package tp1.clients.REST;

import jakarta.ws.rs.client.WebTarget;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

import java.net.URI;

public class RestFilesClient extends RestClient implements Files {

    final WebTarget target;

    public RestFilesClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestDirectory.PATH);
    }
    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        return null;
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        return null;
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        return null;
    }
}
