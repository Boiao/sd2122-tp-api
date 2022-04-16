package tp1.clients.REST;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import tp1.api.FileInfo;
import tp1.api.service.rest.RestDirectory;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

import java.net.URI;
import java.util.List;

public class RestDirClient extends RestClient implements Directory {

    final WebTarget target;

    public RestDirClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestDirectory.PATH);
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        return null;
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        return null;
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        return null;
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        return null;
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        return null;
    }
/*
    private FileInfo clt_writeFile(String filename, byte[] data, String userId, String password){
            Response r = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(new FileInfo(userId,filename,userId + "/" + filename)),MediaType.APPLICATION_JSON);
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
            return r.readEntity(FileInfo.class);
        else
            System.out.println("Error, HTTP error status: " + r.getStatus());

        return null;
    }

    private Void clt_shareFile(String filename, String userId, String userIdShare, String password){

    }

    private Void clt_unshareFile(String filename, String userId, String userIdShare, String password){

    }

    private byte[] clt_getFile(String filename, String userId, String accUserId, String password){

*/
}
