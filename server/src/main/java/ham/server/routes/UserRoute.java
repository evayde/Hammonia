package ham.server.routes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ham.server.userManagement.User;
import ham.server.userManagement.UserService;
import ham.shared.userManagement.UserHelper;
import java.util.HashMap;
import java.util.Map;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Providing the routes regarding all operations on Users.
 * 
 * @author Enrico Gruner
 * @version 0.15
 */
public class UserRoute {
    final static Gson GSON = new Gson();
    final static UserService userService = UserService.getInstance();
    final static Logger userLogger = LoggerFactory.getLogger(UserRoute.class);

    /**
     * General user login route.<br>
     * <b>OnSuccess</b> - Return User Object<br>
     * <b>OnFail</b> - Return error Message
     * 
     * @see ham.server.userManagement.UserService#login(ham.server.userManagement.User, spark.Session) 
     */
    public static Route login = (Request req, Response res) ->
    {
        userLogger.info("Login Route called");  
        
        String authentication = req.headers("Authorization");                
        String[] splittedCredentials = UserHelper.credentialsFromBase64(authentication);
        
        HashMap<String,String> result = userService.login(splittedCredentials[0], splittedCredentials[1]);
        
        res.status( Integer.parseInt(result.get("status")));
        return result.get("payload");
    };

    /**
     * Route to return the currently logged in user<br>
     * <b>OnSuccess</b> - Return the User Object<br>
     * <b>OnFail</b> - Return an error Message
     * 
     * @see ham.server.userManagement.UserService#getLoggedInUser(spark.Session) 
     */
    public static Route getLoggedInUser = (Request req, Response res) ->
    {
        userLogger.info("getLoggedIn Route called");
        HashMap<String,String> result = userService.getLoggedInUser();
        
        res.status( Integer.parseInt(result.get("status")) );
        return result.get("payload");
    };

    /**
     * Route to logout the current user<br>
     * <b>OnSuccess</b> - Return true in a JSON result Object<br>
     * <b>OnFail</b> - Return an error Message
     * 
     * @see ham.server.userManagement.UserService#logout(spark.Session) 
     */
    public static Route logout = (Request req, Response res) ->
    {
        userLogger.info("logout Route called");
        HashMap<String, String> result = userService.logout();
        
        res.status(Integer.parseInt(result.get("status")) );
        return result.get("payload");
    };

    /**
     * Route to Register a new User to the system. The first user should be
     * the administrator. All rights will be granted automatically.<br>
     * <b>OnSuccess</b> - Return the registered User Object<br>
     * <b>OnFail</b> - Return an error message
     * 
     * @see ham.server.userManagement.UserService#register(ham.server.userManagement.User)
     */
    public static Route register = (Request req, Response res) ->
    {
        userLogger.info("Register route called");

        User tu = (User) new User().fromJSON(req.body());
        HashMap<String,String> result = userService.register(tu);
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };

    /**
     * Route to get a specific user by his provided E-Mail address.<br>
     * <b>OnSuccess</b> - Return user object<br>
     * <b>OnFail</b> - Return an error message
     * 
     * @see ham.server.userManagement.UserService#getUserByEmail(java.lang.String) 
     */
    public static Route getUserByEmail = (Request req, Response res) ->
    {
        userLogger.info("Get User By Email route called");
        HashMap<String,String> result = userService.getUserByEmail(req.params("email"));
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    

    public static Route getAll = (Request req, Response res) -> {
        userLogger.info("Get all users");
        HashMap<String,String> result = userService.getAllUsers();
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    /**
     * Get all projects of a single user
     * 
     * @see ham.server.userManagement.User#getProjects() 
     */
    public static Route getProjectsByUser = (Request req, Response res) -> {
        User u = userService.getUserBy("email", req.params("email"));
        if(u != null)
            return GSON.toJson(u.getProjects());
        
        return GSON.toJson(new Error("Couldnt get users projects."));
    };
    
    public static Route edit = (Request req, Response res) -> {
        JSONObject obj = new JSONObject(req.body());
        
        ObjectId uid = GSON.fromJson(obj.getString("uid"), ObjectId.class);
        Map<String,String> uoptions = GSON.fromJson(obj.getString("updateOptions"), new TypeToken<Map<String,String>>(){}.getType() );
        
        HashMap<String, String> result = userService.editUser(uid, uoptions);
        res.status(Integer.parseInt(result.get("status")));
        
        return result.get("payload");
    };
    
    public static Route remove = (Request req, Response res) -> {
        HashMap<String,String> result = userService.removeUser( ((User) new User().fromJSON(req.body())).getId());
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
}