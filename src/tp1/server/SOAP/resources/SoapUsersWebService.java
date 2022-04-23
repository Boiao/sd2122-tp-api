package tp1.server.SOAP.resources;

import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Users;
import tp1.service.JavaUsers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class SoapUsersWebService implements SoapUsers{

    static Logger Log = Logger.getLogger(SoapUsersWebService.class.getName());

    final Users impl = new JavaUsers();

    @Override
    public String createUser(User user) throws UsersException {
        var result = impl.createUser( user );
        if( result.isOK() )
            return result.value();
        else
        throw new UsersException(result.error().toString()) ;

    }

    @Override
    public User getUser(String userId, String password) throws UsersException {
        var result = impl.getUser( userId, password);
        if( result.isOK() )
            return result.value();
        else
            throw new UsersException(result.error().toString()) ;

    }

    @Override
    public User updateUser(String userId, String password, User user) throws UsersException {
        var result = impl.updateUser( userId,password,user );
        if( result.isOK() )
            return result.value();
        else
            throw new UsersException(result.error().toString()) ;

    }

    @Override
    public User deleteUser(String userId, String password) throws UsersException {
        var result = impl.deleteUser( userId,password );
        if( result.isOK() )
            return result.value();
        else
            throw new UsersException(result.error().toString()) ;

    }

    @Override
    public List<User> searchUsers(String pattern) throws UsersException {
        var result = impl.searchUsers( pattern );
        if( result.isOK() )
            return result.value();
        else
            throw new UsersException(result.error().toString()) ;

    }
}
