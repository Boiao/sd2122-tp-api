package tp1.clients.SOAP;

import tp1.api.User;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

import java.util.List;

public class SoapUsersClient implements Users {
    @Override
    public Result<String> createUser(User user) {
        return null;
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        return null;
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        return null;
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        return null;
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        return null;
    }
}
