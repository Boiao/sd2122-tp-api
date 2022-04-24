package tp1.clients.SOAP;

import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.api.service.util.Result;
import tp1.api.service.util.Users;

import javax.xml.namespace.QName;
import java.net.MalformedURLException;
import java.net.URI;
import java.security.Provider;
import java.util.List;

public class SoapUsersClient extends SoapClient implements Users {

    //final WebTarget target;

    private SoapUsers users;
    public SoapUsersClient(URI serverURI)  {
        super(serverURI);
        QName qname = new QName(SoapUsers.NAMESPACE, SoapUsers.NAME);
        Service service = null;
        try {
            service = Service.create(URI.create(serverURI + "?wsdl").toURL(),qname);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        users = service.getPort(SoapUsers.class);
        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
    }
    @Override
    public Result<String> createUser(User user) {
        return super.reTry(() -> {
            return clt_createUser(user);
        });
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        return super.reTry(() -> {
            return clt_getUser(userId, password);
        });
    }

    @Override
    public Result<User> updateUser(String userId, String password, User user) {
        return super.reTry(() -> {
            return  clt_updateUser(userId, password, user);
        });
    }

    @Override
    public Result<User> deleteUser(String userId, String password) {
        return super.reTry(() -> {
            return  clt_deleteUser(userId, password);
        });
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        return super.reTry(() -> {
            return clt_searchUsers(pattern);
        });
    }

    private Result<String> clt_createUser(User user){
        try{
            return Result.ok(users.createUser(user));
        } catch (UsersException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
    }

    private Result<User> clt_getUser(String userId, String password){
        try{
            return Result.ok(users.getUser(userId, password));
        } catch (UsersException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
    }

    private Result<User> clt_updateUser(String userId, String password, User user){
        try{
            return Result.ok(users.updateUser(userId, password, user));
        } catch (UsersException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
    }

    private Result<User> clt_deleteUser(String userId, String password){
        try{
            return Result.ok(users.deleteUser(userId, password));
        } catch (UsersException e){
            return Result.error(errorcheck(Response.Status.valueOf(e.getMessage())));
        }
    }

    private Result<List<User>> clt_searchUsers(String pattern){
        try{
            return Result.ok(users.searchUsers(pattern));
        } catch (UsersException e){
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
