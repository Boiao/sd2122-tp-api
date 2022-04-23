package tp1.clients.SOAP;

import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import tp1.api.FileInfo;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;

public class SoapDirClient extends SoapClient implements Directory {

    private SoapDirectory dir;
    public SoapDirClient(URI serverURI) {
        super(serverURI);
        System.out.println("entrouclient");
        QName qname = new QName(SoapDirectory.NAMESPACE, SoapDirectory.NAME);
        Service service = null;
        try {
            service = Service.create(URI.create(serverURI + "?wsdl").toURL(),qname);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        dir = service.getPort(SoapDirectory.class);
        ((BindingProvider) dir).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) dir).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
    }

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        return super.reTry(() -> {
            return clt_writefile(filename, data, userId, password);
        });
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        return super.reTry(() -> {
            return clt_deleteFile(filename, userId, password);
        });
    }

    @Override
    public Result<Void> deleteUserFiles(String userId, String password) {
        return super.reTry(() -> {
            return clt_deleteUserFiles(userId, password);
        });
    }

    @Override
    public Result<Void> shareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {
            return clt_shareFiles(filename, userId, userIdShare, password);
        });
    }

    @Override
    public Result<Void> unshareFile(String filename, String userId, String userIdShare, String password) {
        return super.reTry(() -> {
            return clt_unshareFiles(filename, userId, userIdShare, password);
        });
    }

    @Override
    public Result<byte[]> getFile(String filename, String userId, String accUserId, String password) {
        return super.reTry(() -> {
            return clt_getFile(filename, userId, accUserId, password);
        });
    }

    @Override
    public Result<List<FileInfo>> lsFile(String userId, String password) {
        return super.reTry(() -> {
            return clt_lsFile(userId, password);
        });
    }

    private Result<FileInfo> clt_writefile(String filename, byte[] data, String userId, String password){
        try{
            return Result.ok(dir.writeFile(filename, data, userId, password));
        } catch (DirectoryException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
    }

    private Result<Void> clt_deleteFile(String filename, String userId, String password){
        try{
             dir.deleteFile(filename, userId, password);
        } catch (DirectoryException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
        return Result.ok();
    }

    private Result<Void> clt_deleteUserFiles(String userId, String password){
        try{
            dir.deleteUserFiles(userId, password);
        } catch (DirectoryException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
        return Result.ok();
    }

    private Result<Void> clt_shareFiles(String filename, String userId, String userIdShare, String password){
        try{
            dir.shareFile(filename, userId, userIdShare, password);
        } catch (DirectoryException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
        return Result.ok();
    }

    private Result<Void> clt_unshareFiles(String filename, String userId, String userIdShare, String password){
        try{
            dir.unshareFile(filename, userId, userIdShare, password);
        } catch (DirectoryException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
        return Result.ok();
    }

    private Result<byte[]> clt_getFile(String filename, String userId, String accUserId, String password){
        try{
            return Result.ok(dir.getFile(filename, userId, accUserId, password));
        } catch (DirectoryException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
    }

    private Result<List<FileInfo>> clt_lsFile(String userId, String password){
        try{
            return Result.ok(dir.lsFile(userId, password));
        } catch (DirectoryException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
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
