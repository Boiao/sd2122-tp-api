package tp1.clients.SOAP;

import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.ws.rs.client.WebTarget;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import tp1.api.User;
import tp1.api.service.rest.RestUsers;
import tp1.api.service.soap.SoapDirectory;
import tp1.api.service.soap.SoapUsers;
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
      /*  QName qname = new QName(SoapUsers.NAMESPACE, SoapUsers.NAME);
        Service service = Service.create(URI.create(serverURI + "?wsdl").toURL(),qname);
        users = service.getPort(SoapUsers.class);
        ((BindingProvider) users).getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
*/
    }
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
