package tp1.service;

import tp1.api.service.util.Result;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JavaFiles implements tp1.api.service.util.Files {

    public Map<String, Path> files = new ConcurrentHashMap<>();

    public JavaFiles() {

    }

    @Override
    public Result<Void> writeFile(String fileId, byte[] data, String token) {
        try {
            Files.write(Path.of(fileId), data);
            files.put(fileId, Path.of( fileId));
        } catch (Exception e) {
            return Result.error(Result.ErrorCode.BAD_REQUEST);
        }

        return Result.ok();
    }

    @Override
    public Result<Void> deleteFile(String fileId, String token) {

        if (files.containsKey(fileId)) {

            Path path = files.get(fileId);
            files.remove(fileId);
            try {
                Files.delete(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (!files.containsKey(fileId))
            return Result.error(Result.ErrorCode.NOT_FOUND);
        else
            return Result.error(Result.ErrorCode.BAD_REQUEST);

        return Result.ok();
    }

    @Override
    public Result<byte[]> getFile(String fileId, String token) {
        if (files.containsKey(fileId)) {
            Path path = files.get(fileId);
            try {
                return Result.ok(Files.readAllBytes(path));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else if (!files.containsKey(fileId))
            return Result.error(Result.ErrorCode.NOT_FOUND);
        else
            return Result.error(Result.ErrorCode.CONFLICT);


    }
}
