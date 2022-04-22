package tp1.server.SOAP.resources;

import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;

import java.util.List;

public class SoapUsersWebService implements SoapUsers{
    @Override
    public String createUser(User user) throws UsersException {
        return null;
    }

    @Override
    public User getUser(String userId, String password) throws UsersException {
        return null;
    }

    @Override
    public User updateUser(String userId, String password, User user) throws UsersException {
        return null;
    }

    @Override
    public User deleteUser(String userId, String password) throws UsersException {
        return null;
    }

    @Override
    public List<User> searchUsers(String pattern) throws UsersException {
        return null;
    }
}
