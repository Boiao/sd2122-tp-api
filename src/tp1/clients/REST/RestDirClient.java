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

public class RestDirClient extends RestClient implements RestDirectory {

    final WebTarget target;

    public RestDirClient(URI serverURI) {
        super(serverURI);
        target = client.target(serverURI).path(RestDirectory.PATH);
    }

    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry(() -> {
            return clt_writeFile(data);
        });
    }

    @Override
    public void deleteFile(String filename, String userId, String password) {

    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) {
    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) {

    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) {

        return super.reTry(() -> {
            return clt_getFile(filename,userId);
        });
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) {
        return null;
    }

    private FileInfo clt_writeFile(byte[] data){
            Response r = target.request()
                    .accept(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(data,MediaType.APPLICATION_OCTET_STREAM));
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity())
            return r.readEntity(FileInfo.class);
        else
            System.out.println("Error, HTTP error status: " + r.getStatus());

        return null;
    }
/*
    private Void clt_shareFile(String filename, String userId, String userIdShare, String password){

    }

    private Void clt_unshareFile(String filename, String userId, String userIdShare, String password){

    }
*/
    private byte[] clt_getFile(String filename, String userId){
        Response r = target.path(userId)
                .queryParam(FILENAME,filename).request()
                .accept(MediaType.APPLICATION_OCTET_STREAM)
                .get();
        if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {
            return r.readEntity(byte[].class);
        } else
            System.out.println("Error, HTTP error status: " + r.getStatus());
        return null;
    }


}
