package tp1.clients.SOAP;

import jakarta.ws.rs.ProcessingException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import tp1.api.service.util.Result;

import java.net.URI;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class SoapClient {

    private static Logger Log = Logger.getLogger(tp1.clients.REST.RestClient.class.getName());

    protected static final int READ_TIMEOUT = 10000;
    protected static final int CONNECT_TIMEOUT = 10000;

    protected static final int RETRY_SLEEP = 1000;
    protected static final int MAX_RETRIES = 3;

    final URI serverURI;
    final Client client;
    final ClientConfig config;

    SoapClient(URI serverURI) {
        this.serverURI = serverURI;
        this.config = new ClientConfig();

        config.property(ClientProperties.READ_TIMEOUT, READ_TIMEOUT);
        config.property( ClientProperties.CONNECT_TIMEOUT, CONNECT_TIMEOUT);

        this.client = ClientBuilder.newClient(config);
    }

    protected <T> T reTry(Supplier<T> func) {
        for (int i = 0; i < MAX_RETRIES; i++)
            try {
                return func.get();
            } catch (ProcessingException x) {
                Log.fine("ProcessingException: " + x.getMessage());
                sleep(RETRY_SLEEP);
            } catch (Exception x) {
                Log.fine("Exception: " + x.getMessage());
                x.printStackTrace();
                break;
            }
        return (T) Result.error(Result.ErrorCode.TIMEOUT);
    }

    private void sleep(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException x) { // nothing to do...
        }
    }
}