package tp1.server.SOAP.resources;

import tp1.api.FileInfo;
import tp1.api.service.soap.DirectoryException;
import tp1.api.service.soap.SoapDirectory;

import java.util.List;

public class SoapDirWebService implements SoapDirectory {
    @Override
    public FileInfo writeFile(String filename, byte[] data, String userId, String password) throws DirectoryException {
        return null;
    }

    @Override
    public void deleteFile(String filename, String userId, String password) throws DirectoryException {

    }

    @Override
    public void shareFile(String filename, String userId, String userIdShare, String password) throws DirectoryException {

    }

    @Override
    public void unshareFile(String filename, String userId, String userIdShare, String password) throws DirectoryException {

    }

    @Override
    public byte[] getFile(String filename, String userId, String accUserId, String password) throws DirectoryException {
        return new byte[0];
    }

    @Override
    public List<FileInfo> lsFile(String userId, String password) throws DirectoryException {
        return null;
    }
}