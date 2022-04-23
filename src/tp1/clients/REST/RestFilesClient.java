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

public class RestFilesClient extends RestClient implements Files {

    final WebTarget target;

    public RestFilesClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestFiles.PATH);
    }
    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
       return super.reTry(() -> {
           return clt_writeFile(fileId,data,token);

        });
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {
       return super.reTry(() -> {
          return clt_deleteFile(fileId);

        });

    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {

        return super.reTry(()->{
            return clt_getFile(fileId);
        });

    }

    public Result<Void> clt_writeFile(String fileId, byte[] data, String token){
        Response r = target.path(fileId).request()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(data,MediaType.APPLICATION_OCTET_STREAM));
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
            return Result.ok();
        else{
            System.out.println("Error, HTTP error status: " + r.getStatus());
            return Result.error(errorcheck(Response.Status.fromStatusCode(r.getStatus())));
        }
    }

    public Result<Void> clt_deleteFile(String fileId) {
        Response r = target.path(fileId)
                .queryParam(TOKEN,"")
                .request()
                .delete();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
            return Result.ok();
        else{
            System.out.println("Error, HTTP error status: " + r.getStatus());
            return Result.error(errorcheck(Response.Status.fromStatusCode(r.getStatus())));
        }
    }

    public Result<byte[]> clt_getFile(String fileId) {


        Response r = target.path(fileId)
                .queryParam(TOKEN,"").request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {
            return Result.ok(r.readEntity(byte[].class));
        } else{
            System.out.println("Error, HTTP error status: " + r.getStatus());
            return Result.error(errorcheck(Response.Status.fromStatusCode(r.getStatus())));
        }


    }

    private Result.ErrorCode errorcheck(Response.Status status){
        Result.ErrorCode res;
        switch (status){
            case FORBIDDEN:
                res = Result.ErrorCode.FORBIDDEN;
                break;
            case NOT_FOUND:
                res = Result.ErrorCode.NOT_FOUND;
                break;
            case CONFLICT:
                res = Result.ErrorCode.CONFLICT;
                break;
            default:
                res = Result.ErrorCode.BAD_REQUEST;
        }return res;
    }

}
