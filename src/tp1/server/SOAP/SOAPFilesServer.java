package tp1.server.SOAP;

import jakarta.xml.ws.Endpoint;
import tp1.server.SOAP.resources.SoapFilesWebService;
import tp1.server.SOAP.resources.SoapUsersWebService;

import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SOAPFilesServer {

    public static final int PORT = 8080;
    public static final String SERVICE_NAME = "files";
    public static String SERVER_BASE_URI = "http://%s:%s/soap";

    private static Logger Log = Logger.getLogger(SOAPFilesServer.class.getName());

    public static void main(String[] args) throws Exception {

        System.setProperty("com.sun.xml.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe.dump", "true");
        System.setProperty("com.sun.xml.ws.transport.http.HttpAdapter.dump", "true");
        System.setProperty("com.sun.xml.internal.ws.transport.http.HttpAdapter.dump", "true");

        Log.setLevel(Level.INFO);

        String ip = InetAddress.getLocalHost().getHostAddress();
        String serverURI = String.format(SERVER_BASE_URI, ip, PORT);

        Endpoint.publish(serverURI.replace(ip, "0.0.0.0"), new SoapFilesWebService());

        Log.info(String.format("%s Soap Server ready @ %s\n", SERVICE_NAME, serverURI));
    }
}
