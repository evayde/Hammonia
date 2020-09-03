package ham.server;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bootstrapping the app
 * 
 * @author Enrico Gruner
 * @version 0.15
 */
public class Bootstrap {
    /**
     * Constructor used to bootstrap the app and set server arguments
     * 
     * @see ham.server.ServerApp#ServerApp(int, boolean, java.lang.String, java.lang.String)
     * @param args CMD args
     */
    public static void main(String[] args) throws ParseException {        
        Logger bootstrapLogger = LoggerFactory.getLogger(Bootstrap.class);
        
        int port = 4567;        // Default port is 4567
        boolean ssl = false;    // turn on SSL?
        String keyfile = "";    // path and name of keyfile
        String keypass = "";    // password to keyfile
        String host = "";       // Host for API/Socket
        
        // CLI Config
        Options options = new Options();
        options.addOption("ssl", false, "Turn SSL On/Off");
        options.addOption("port", true, "Specify the port number");
        options.addOption("keyfile", true, "Specify Keyfile");
        options.addOption("keypass", true, "Specify Keyfile password");
        options.addOption("host", true, "Specify the Host for API and Socket");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if(cmd.hasOption("port")) {
            port = Integer.parseInt(cmd.getOptionValue("port"));
            bootstrapLogger.info("Trying to change port to "+ port);
        }
        if(cmd.hasOption("ssl")) 
        {
            bootstrapLogger.info("Trying to turn on SSL.");
            ssl = true;
        }
        if(ssl) {
            if(cmd.hasOption("keyfile"))
                keyfile = cmd.getOptionValue("keyfile");
            else {
                bootstrapLogger.warn("SSL has been turned off, due to missing Keyfile!");
                ssl = false;
            }
            if(cmd.hasOption("keypass"))
                keypass = cmd.getOptionValue("keypass");
        }
        if(cmd.hasOption("host")) {
            host = cmd.getOptionValue("host");
        }
        else {
            host = "localhost";
        }
        
        bootstrapLogger.info("Setting host to "+host);
        ServerApp serverApp = new ServerApp();
        
        serverApp.start(port, ssl, keyfile, keypass, host);
    }
}
