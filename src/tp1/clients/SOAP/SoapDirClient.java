package tp1.clients.SOAP;

import tp1.api.FileInfo;
import tp1.api.service.util.Directory;
import tp1.api.service.util.Result;

import java.util.List;

public class SoapDirClient implements Directory {

    @Override
    public Result<FileInfo> writeFile(String filename, byte[] data, String userId, String password) {
        return null;
    }

    @Override
    public Result<Void> deleteFile(String filename, String userId, String password) {
        return null;
    }

    @Override
    public Result<Void> deleteUserFiles(String userId, String password) {
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
}
