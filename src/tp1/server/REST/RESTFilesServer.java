package tp1.server.REST;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import tp1.Discovery;
import tp1.server.REST.resources.FilesResources;
import tp1.server.REST.resources.UsersResource;
import tp1.server.REST.util.CustomLoggingFilter;
import tp1.server.REST.util.GenericExceptionMapper;
import util.Debug;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RESTFilesServer {
    private static Logger Log = Logger.getLogger(RESTFilesServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static final int PORT = 8080;
    public static final String SERVICE = "files";
    private static final String SERVER_URI_FMT = "http://%s:%s/rest";

    public static void main(String[] args) {
        try {
            Debug.setLogLevel( Level.INFO, Debug.SD2122 );

            ResourceConfig config = new ResourceConfig();
            config.register(FilesResources.class);
            config.register(CustomLoggingFilter.class);
            config.register(GenericExceptionMapper.class);

            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_URI_FMT, ip, PORT);
            JdkHttpServerFactory.createHttpServer( URI.create(serverURI), config);

            Log.info(String.format("%s Server ready @ %s\n",  SERVICE, serverURI));
            Discovery discv = new Discovery(Discovery.DISCOVERY_ADDR, SERVICE, serverURI);
            discv.start();

            //More code can be executed here...
        } catch( Exception e) {
            Log.severe(e.getMessage());
        }
    }
}
