package ham.server.routes;

import com.google.gson.Gson;
import ham.server.userManagement.UserService;
import spark.Request;
import spark.Response;
import static spark.Spark.*;

/**
 * Defines authentification mechanism
 * 
 * @author Enrico Gruner
 * @version 0.3
 */
public class AuthRoute {
    private final static Gson GSON = new Gson();
    private final static UserService userService = UserService.getInstance();
    
    /**
     * Let requests to login and register pass freely.
     * Else: Check if the user is authenticated (has valid token)
     *
     * @param req Spark Request
     * @param res Spark Response
     */
    public AuthRoute(Request req, Response res) {
        // Assume user is not authed
        boolean auth = false;
            
        if( (req.pathInfo().equals("/user/login") && req.headers("Authorization") != null) || req.pathInfo().equals("/user/register") ) {
            auth = true;
        }
        else {
            if( userService.getSession(req.headers("Authorization-Token")) != null ) {
                auth = true;
            }
        }        
        // if user not authed, HE SHALL NOT PASS, send 401.
        if(!auth) {
            res.type("application/json");
            halt(401, GSON.toJson(new Error("Auth required")));
        }
    }
}