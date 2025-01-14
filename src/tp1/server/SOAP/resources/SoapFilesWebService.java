package tp1.server.SOAP.resources;

import jakarta.jws.WebService;
import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;
import tp1.service.JavaFiles;

@WebService(serviceName= SoapFiles.NAME, targetNamespace=SoapFiles.NAMESPACE, endpointInterface=SoapFiles.INTERFACE)
public class SoapFilesWebService implements SoapFiles {

    tp1.api.service.util.Files impl = new JavaFiles();

    @Override
    public byte[] getFile(String fileId, String token) throws FilesException {
        var result = impl.getFile( fileId, token);
        if( result.isOK() )
            return result.value();
        else
            throw new FilesException(result.error().toString());
    }

    @Override
    public void deleteFile(String fileId, String token) throws FilesException {
        var result = impl.deleteFile( fileId, token);
        if( !result.isOK() )
            throw new FilesException(result.error().toString());
    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) throws FilesException {
        var result = impl.writeFile( fileId, data, token);
        if( !result.isOK() )
            throw new FilesException(result.error().toString());
    }
}
