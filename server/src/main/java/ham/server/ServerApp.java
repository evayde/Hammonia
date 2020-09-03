package ham.server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import static spark.Spark.*;
import ham.server.routes.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ham.server.sockets.ProjectSocket;

/**
 * Main App exposing all routes to the server.
 * 
 * @author Enrico Gruner
 * @version 0.3
 */
public class ServerApp {
    
    /**
     * Declaring routes and middleware
     * 
     * @param port Port number to serve
     * @param ssl true if use ssl, false otherwise
     * @param keyfile provide path and filename to the keyfile
     * @param keypass provide keyfile password
     * @param host set Host for API/Socket
     */
    public void start(int port, boolean ssl, String keyfile, String keypass, String host ) {
        // Setup
        Logger serverLogger = LoggerFactory.getLogger(ServerApp.class);
        port(port);
        if(ssl)
            secure(keyfile, keypass, null, null);
        serverLogger.info("Running Server on port "+port+". SSL is "+ (ssl ? "On." : "Off."));
        
        /** SOCKET IO **/
        Configuration cfg = new Configuration();
        cfg.setHostname(host);
        cfg.setPort(port+1);
        final SocketIOServer sockio = new SocketIOServer(cfg);
        
        sockio.addConnectListener((SocketIOClient c)-> {
            System.out.println("Someone Connected...");
            // Set User-Token
            c.set("token", c.getHandshakeData().getHttpHeaders().get("AUTHORIZATION-TOKEN"));
        });
        
        sockio.addDisconnectListener((SocketIOClient c) -> {
            System.out.println("Someone disconnected");
            c.disconnect();
        });
        
        // Register Socket Listeners
        ProjectSocket.getInstance().registerListeners(sockio);
        
        sockio.start();
        /** /SOCKET IO **/
        
        // Define UserManagement Routes
        path("/user", () -> {
            before("/*",(req,res) ->    new AuthRoute(req,res));
            post("/login",              UserRoute.login);
            post("/register",           UserRoute.register);
            get("/getLoggedIn",         UserRoute.getLoggedInUser);
            get("/get/:email",          UserRoute.getUserByEmail);
            get("/get/:email/projects", UserRoute.getProjectsByUser);
            get("/get",                 UserRoute.getAll);
            put("/logout",              UserRoute.logout);
            put("/edit",                UserRoute.edit);
            post("/remove",             UserRoute.remove);
        });
        
        // Define ProjectManagement Routes
        path("/project", () -> {
            before("/*",(req,res) ->    new AuthRoute(req,res));
            post("/create",             ProjectRoute.createProject);
            put("/addFile",             ProjectRoute.addFileToProject);
            post("/getByName",          ProjectRoute.getProjectByName);
            post("/byUser",             ProjectRoute.getProjectsByUser);
            put("/add/:role",           ProjectRoute.addUser);
            delete("/remove/user",      ProjectRoute.removeUser);
            post("/remove",             ProjectRoute.removeProject);
            post("/getFileById",        ProjectRoute.getFileById);
            put("/addDiagram",          ProjectRoute.addDiagramToProject);
            put("/saveDiagram",         ProjectRoute.saveDiagram);
            post("/getById",            ProjectRoute.getProjectById);
            post("/getDiagramById",     ProjectRoute.getDiagramById);
            post("/getLearningProjects",ProjectRoute.getLearningProjectsByUser);
        });
        
        // Define TicketManagement Routes
        path("/ticket", () -> {
            before("/*",(req,res) ->    new AuthRoute(req,res));
            post("/byProject",          TicketRoute.getProjectTickets);
            post("/create",             TicketRoute.create);
        });
        
        // Define Admin Routes
        path("/admin", ()-> {
            before("/*",(req,res) ->    new AuthRoute(req,res));
        });
        
        // Define custom error routes
        notFound((req, res) -> {
            res.status(404);
            serverLogger.info("Route not found: "+ req.pathInfo());
            return "{\"error\": \"400\"}";  
        });
        internalServerError((req, res) -> {
            res.status(500);
            serverLogger.error("Server encountered an error. @"+req.pathInfo());
            return "{\"error\": \"500\"}";
        });
        
        // Finalize response
        after((req, res) -> {
            // We just provide JSON format response
            res.type("application/json");
            // Set response Header to gzip is enough, dont request it...
            res.header("Content-Encoding", "gzip");
        });
    }
}