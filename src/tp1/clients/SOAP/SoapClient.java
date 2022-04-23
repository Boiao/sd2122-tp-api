package tp1.clients.SOAP;

import com.sun.xml.ws.client.BindingProviderProperties;
import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.xml.ws.BindingProvider;
import jakarta.xml.ws.Service;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import tp1.api.User;
import tp1.api.service.soap.SoapUsers;
import tp1.api.service.soap.UsersException;
import tp1.clients.REST.RestClient;

import javax.xml.namespace.QName;
import java.io.IOException;
import java.net.URI;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class SoapClient {
    protected static final int READ_TIMEOUT = 5000;
    protected static final int CONNECT_TIMEOUT = 5000;

    public static void main(String[] args ) throws IOException {

        if( args.length != 5) {
            System.err.println( "Use: java sd2122.aula2.clients.CreateUserClient url userId fullName email password");
            return;
        }

        String serverUrl = args[0];
        String userId = args[1];
        String fullName = args[2];
        String email = args[3];
        String password = args[4];



        QName qname = new QName(SoapUsers.NAMESPACE, SoapUsers.NAME);
        Service service = Service.create( URI.create(serverUrl + "?wsdl").toURL(), qname);
        SoapUsers users = service.getPort(tp1.api.service.soap.SoapUsers.class);

        setClientTimeouts( (BindingProvider) users );

        System.out.println("Sending request to server.");

        User u = new User( userId, fullName, email, password);

        try {
            var result = users.createUser( u );
            System.out.println("Result: " + result);
        } catch( UsersException x ) {
            x.printStackTrace();
        }
    }



    static void setClientTimeouts(BindingProvider port ) {
        port.getRequestContext().put(BindingProviderProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);
        port.getRequestContext().put(BindingProviderProperties.REQUEST_TIMEOUT, READ_TIMEOUT);
    }
}
