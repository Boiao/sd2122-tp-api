package tp1.clients.REST;

import jakarta.ws.rs.WebApplicationException;
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
        target = client.target(serverURI).path(RestFiles.PATH);
    }
    @Override
    public void writeFile(String fileId, byte[] data, String token) {
        super.reTry(() -> {clt_writeFile(data);
            return null;
        });
    }

    @Override
    public void deleteFile(String fileId, String token) {
        super.reTry(() -> {clt_deleteFile(fileId);
            return null;
        });
    }

    @Override
    public byte[] getFile(String fileId, String token) {

        return super.reTry(()->{
            return clt_getFile(fileId);
        });

    }

    public void clt_writeFile(byte[] data){
        Response r = target.request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data,MediaType.APPLICATION_OCTET_STREAM));
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {

        } else
            System.out.println("Error, HTTP error status: " + r.getStatus());


    }

    public void clt_deleteFile(String fileId) {
        Response r = target.path(fileId)
                .request()
                .delete();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {

        } else
            System.out.println("Error, HTTP error status: " + r.getStatus());
    }

    public byte[] clt_getFile(String fileId) {


        Response r = target.path(fileId)
                .request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {
            return r.readEntity(byte[].class);
        } else
            System.out.println("Error, HTTP error status: " + r.getStatus());
        return null;


    }

}
