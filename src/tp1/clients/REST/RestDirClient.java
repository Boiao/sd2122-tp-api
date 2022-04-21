package tp1.clients.REST;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.GenericEntity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import javassist.bytecode.ByteArray;
import tp1.api.FileInfo;
import tp1.api.User;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

import javax.print.DocFlavor;
import java.net.URI;
import java.util.HashSet;
import java.util.List;

public class RestDirClient extends RestClient implements Directory {

    final WebTarget target;

    public RestDirClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestDirectory.PATH);
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry(() -> {
            return Result.ok(clt_writeFile(filename,data,userId,password));
        });
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        return super.reTry(() -> {
             return Result.ok(clt_deleteFile(filename, userId, password));

        });
    }

    public Result<Void> deleteUserFiles(String userId, String password){
       return super.reTry(() -> {
            return Result.ok(clt_deleteUserFiles(userId, password));
        });

    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {
            return Result.ok(clt_shareFile(filename, userId, userIdShare, password));

        });
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {
           return Result.ok(clt_unshareFile(filename, userId, userIdShare, password));

        });
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {

        return super.reTry(() -> {
            return Result.ok(clt_getFile(filename,userId,accUserId,password));
        });
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {

        return super.reTry(()->{
            return Result.ok(clt_lsFile(userId, password));
        });
    }



    private FileInfo clt_writeFile(String filename, byte[] data, String userId, String password){
            Response r = target.path(userId).path(filename)
                    .queryParam(PASSWORD,password)
                    .request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(data,MediaType.APPLICATION_OCTET_STREAM));
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
            return r.readEntity(FileInfo.class);
        else
            System.out.println("Error, HTTP error status: " + r.getStatus());

        return null;
    }

    private Void clt_deleteFile(String filename, String userId, String password){
        Response r = target.path(userId).path(filename)
                .queryParam(PASSWORD,password)
                .request()
                .delete();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
            System.out.println("Deleted successfully");
            else
        System.out.println("Error, HTTP error status: " + r.getStatus());
        return null;
    }

    private Void clt_deleteUserFiles(String userId, String password){

        Response r = target.path(userId)
                .queryParam(PASSWORD,password)
                .request()
                .delete();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
            System.out.println("Deleted successfully");
        else
            System.out.println("Error, HTTP error status: " + r.getStatus());

        return null;
    }

    private Void clt_shareFile(String filename, String userId, String userIdShare, String password){
        Response r = target.path(userId).path(filename).path("share").path(userIdShare)
                .queryParam(PASSWORD,password)
                .request()
                .post(Entity.entity(null,MediaType.APPLICATION_JSON));

        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
            System.out.println("Shared successfully");
        else
            System.out.println("Error, HTTP error status: " + r.getStatus());
        return null;
    }

        private Void clt_unshareFile(String filename, String userId, String userIdShare, String password){
            Response r = target.path(userId).path(filename).path("share").path(userIdShare)
                    .queryParam(PASSWORD,password)
                    .request()
                    .post(Entity.entity(null,MediaType.APPLICATION_JSON));

            if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
                System.out.println("Shared successfully");
            else
                System.out.println("Error, HTTP error status: " + r.getStatus());
            return null;
        }

    private byte[] clt_getFile(String filename, String userId, String accUserId, String password){
        Response r = target.path(userId).path(filename)
                .queryParam(ACCUSER,accUserId)
                .queryParam(PASSWORD,password).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {
            return r.readEntity(byte[].class);
        } else
            System.out.println("Error, HTTP error status: " + r.getStatus());
        return null;
    }

    private List<FileInfo> clt_lsFile(String userId, String password) {

        Response r = target.path(userId)
                .queryParam(PASSWORD,password).request()
                .accept(MediaType.APPLICATION_JSON)
                .get();

        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {
            return r.readEntity(new GenericType<List<FileInfo>>() {
            });
        } else
            System.out.println("Error, HTTP error status: " + r.getStatus());
        return null;
    }


}
