package tp1.clients.REST;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.rest.RestFiles;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

import java.net.URI;

public class RestFilesClient extends RestClient implements RestFiles {

    final WebTarget target;

    public RestFilesClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestDirectory.PATH);
    }
    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        super.reTry(() -> {clt_writeFile(data);
        });
    }

    @Override
    public void deleteFile(String fileId, String token) {

    }

    @Override
    public byte[] getFile(String fileId, String token) {
        Response r = target.path(fileId)
                .queryParam(TOKEN,token).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {
            return r.readEntity(byte[].class);
        } else
            System.out.println("Error, HTTP error status: " + r.getStatus());
        return null;
    }

    public void clt_writeFile(byte[] data){
        Response r = target.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data,MediaType.APPLICATION_OCTET_STREAM));
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {

        } else
            System.out.println("Error, HTTP error status: " + r.getStatus());


    }
}
