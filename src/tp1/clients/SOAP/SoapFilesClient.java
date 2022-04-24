package tp1.clients.SOAP;

import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import tp1.api.service.soap.*;
import tp1.api.service.util.Files;
import tp1.api.service.util.Result;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;

public class SoapFilesClient extends SoapClient implements Files {

    private SoapFiles files;

    public SoapFilesClient(URI serverURI) {
        super(serverURI);
        QName qname = new QName(SoapFiles.NAMESPACE, SoapFiles.NAME);
        Service service = null;
        try {
            service = Service.create(URI.create(serverURI + "?wsdl").toURL(),qname);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        files = service.getPort(SoapFiles.class);
        ((BindingProvider) files).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) files).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
    }

    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        return super.reTry(() -> {
            return clt_writeFile(fileId, data, token);
        });
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {
        return super.reTry(() -> {
            return clt_deleteFile(fileId, token);
        });
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        return super.reTry(() -> {
            return clt_getFile(fileId, token);
        });
    }

    private Result<Void> clt_writeFile(String fileId, byte[] data, String token){
        try{
            files.writeFile(fileId, data, token);
        } catch (FilesException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
        return Result.ok();
    }

    private Result<Void> clt_deleteFile(String fileId, String token){
        try{
            files.deleteFile(fileId, token);
        } catch (FilesException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
        return Result.ok();
    }

    private Result<byte[]> clt_getFile(String fileId, String token){
        try{
            return Result.ok(files.getFile(fileId, token));
        } catch (FilesException e){
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
