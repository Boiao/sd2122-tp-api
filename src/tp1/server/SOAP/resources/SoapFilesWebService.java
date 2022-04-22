package tp1.server.SOAP.resources;

import tp1.api.service.soap.FilesException;
import tp1.api.service.soap.SoapFiles;

public class SoapFilesWebService implements SoapFiles {
    @Override
    public byte[] getFile(String fileId, String token) throws FilesException {
        return new byte[0];
    }

    @Override
    public void deleteFile(String fileId, String token) throws FilesException {

    }

    @Override
    public void writeFile(String fileId, byte[] data, String token) throws FilesException {

    }
}
