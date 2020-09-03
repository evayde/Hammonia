package ham.server.userManagement;

import com.google.gson.Gson;
import com.mongodb.WriteResult;
import ham.server.Db;
import ham.server.ServiceAbstract;
import ham.shared.interfaces.Service;
import ham.shared.userManagement.UserHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UserService providing the connection to the database for operations on User Object.
 * 
 * @author Enrico Gruner
 * @version 0.15
 */
public class UserService extends ServiceAbstract implements Service {
    private final Logger userServiceLogger = LoggerFactory.getLogger(UserService.class);
    private static UserService instance = null;
    private User current = null;
    final static Gson GSON = new Gson();
    private final Db db = new Db();
    
    private UserService() { }
    
    /** Internal Methods for Dependent services **/
    public User getCurrent() {
        return this.current;
    }
    
    private void updateSession(User u) {
        try {
            final Query<User> usrQuery = this.db.getDatastore().createQuery(User.class).filter("id ==", u.getId());
            final UpdateOperations<User> update = this.db.getDatastore().createUpdateOperations(User.class).set("token", u.getToken());
            this.current = this.db.getDatastore().findAndModify(usrQuery, update);
        }
        catch(Exception e) {
            this.userServiceLogger.error("Tried to save Token, but failed.");
            this.userServiceLogger.error(e.getMessage());
            this.current = null;
        }
    }
    
    /**
     * Get a User by his provided E-Mail address.
     * 
     * @param key
     * @param value
     * @return OnSuccess - User Object<br>
     *         OnFail - null
     */
    public User getUserBy(String key, String value) {
        try {
            final Query<User> usrQuery = this.db.getDatastore().createQuery(User.class).filter(key +" ==", value);

            return usrQuery.get();
        }
        catch(Exception e) {
            this.userServiceLogger.error("Tried to get User information by email, but DB connection failed.");
            this.userServiceLogger.error(e.getMessage());
            return null;
        }
    }
    
    public static UserService getInstance() {
        if(instance == null)        
            instance = new UserService();
        return instance;
    }
    
    public User getSession(String token) {
        if(token == null)
            return null;

        final Query<User> usrQuery = this.db.getDatastore().createQuery(User.class).filter("token ==", token);
        User u = usrQuery.get();
        
        this.current = u;
        return u;
    }
    
    /** Service Methods **/
    
    /**
     *
     * @return
     */
   
    /**
     * Login method checks if the Usermail has a representation in the datastore.
     * If so, it compares the password and returns the User on success.
     * 
     * @param email UserMail
     * @param password User Password
     * @return OnSuccess - User object<br>
     *         OnFail - null
     */
    public HashMap<String, String> login(String email, String password) {
        try {
            if(this.getLoggedInUser() != null) {
                this.setResult("409",GSON.toJson(new Error("Already logged in")));
            }
            
            final Query<User> usrCountQuery = this.db.getDatastore().find(User.class);
            if(usrCountQuery.count() == 0) {
                User u = new User();
                u.setEmail(email);
                u.setPassword(password);
                return this.register(u);
            }
            
            final Query<User> usrQuery = this.db.getDatastore().createQuery(User.class).filter("email ==", email).filter("password ==", password);
            User u = usrQuery.get();
            
            if(u != null) {
                u.setToken(UserHelper.getToken());
                this.updateSession(u);
                
                this.setResult("200", GSON.toJson(this.current));
            }
            else {
                this.setResult("400", GSON.toJson(new Error("Couldnt login user")));
            }

            return this.result;
        }
        catch(Exception e) {
            this.userServiceLogger.error("tried to login, but DB connection failed");
            this.userServiceLogger.error(e.getMessage());
            
            this.setResult("500", GSON.toJson(new Error("Server encountered an error")));
            return this.result;
        }
    }
    
    /**
     * Logout method invalidates all data bound to the session, if there was a 
     * user. 
     * 
     * @return OnSuccess - true<br>
     *         OnFail - false
     */
    public HashMap<String,String> logout() {
        if(this.current == null)
            this.setResult("404", GSON.toJson(new Error("No User found")));
        
        try {
            final Query<User> usrQuery = this.db.getDatastore().createQuery(User.class).filter("token ==", this.current.getToken());
            final UpdateOperations<User> update = this.db.getDatastore().createUpdateOperations(User.class).set("token", "");
            
            this.current = null;
            this.setResult("200", this.db.getDatastore().findAndModify(usrQuery, update).toString());
        }
        catch(Exception e) {
            this.userServiceLogger.error(e.getMessage());
            this.setResult("500", GSON.toJson(new Error("Server encountered an error")));
            return this.result;
        }
        
        return this.result;
    }
    
    /**
     * Method to return the currently logged in User.
     * 
     * @return OnSuccess - User Object<br>
     *         OnFail - null
     */
    public HashMap<String,String> getLoggedInUser() {
        if(this.current != null) {
            this.setResult("200", this.current.toString());
        }
        else {
            this.setResult("404", GSON.toJson("No User seems to be logged in"));
        }
        
        return this.result;
    }
    
    /**
     * Register a User with the given Data.
     * TODO: Sanitize user Input
     * 
     * @param u User Object containing the given user information
     * @return OnSuccess - User Object<br>
     *         OnFail - null
     */
    public HashMap<String,String> register(User u) {        
        try {
            // Check if User exists.
            if(this.getUserBy("email", u.getEmail()) != null) {
                this.userServiceLogger.error("Tried to register User, but it already exists");
                this.setResult("400", GSON.toJson(new Error("User with this E-Mail already exists")));
                return this.result;
            }
            
            final Query<User> usrQuery = this.db.getDatastore().find(User.class);
            if(usrQuery.count() == 0)
                u.setAdmin(true); // make first user admin
            
            if(u.isAdmin() || (this.getCurrent() != null && this.getCurrent().isAdmin()) ) {
                this.db.getDatastore().save(u);
                this.setResult("200", this.db.getDatastore().get(u).toString());
            }
            else {
                this.setResult("400", GSON.toJson(new Error("Only admins can register new Users")));
            }
        }
        catch(Exception e) {
            this.userServiceLogger.error("Tried to register User, but DB connection failed");
            this.userServiceLogger.error(e.getMessage());
            
            this.setResult("500", GSON.toJson(new Error("Internal server error occured")));
        }
        return this.result;
    }
    
    public HashMap<String,String> getUserByEmail(String email) {
        User u = this.getUserBy("email", email);
        if(u == null) {
            this.setResult("404", GSON.toJson(new Error("No User has been found")));
        }
        else {
            this.setResult("200", u.toString());
        }
        return this.result;
    }
    
    /**
     * @param pname
     * @param email
     * @param role
     * @return
     */
    public HashMap<String,String> addProjectToUser(ObjectId pid, ObjectId uid) {
        try {
            final Query<User> usrQuery = this.db.getDatastore().createQuery(User.class).filter("id ==", uid);
            User u = this.db.getDatastore().get(User.class, uid);
            u.addProject(pid);
            final UpdateOperations<User> update = this.db.getDatastore().createUpdateOperations(User.class).set("projects", u.getProjects());
            this.current = this.db.getDatastore().findAndModify(usrQuery, update);
            
            this.setResult("200", this.current.toString());
        }
        catch(Exception e) {
            userServiceLogger.error("Couldnt add Project to User");
            this.setResult("500", GSON.toJson(new Error("Couldnt add Project to User")));
        }
        return this.result;
    }
    
    public HashMap<String, String> getAllUsers()
    {
        try {
            List<User> users = db.getDatastore().find(User.class).asList();
            this.setResult("200", GSON.toJson(users));
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error("Failed to retrieve all Users")));
        }
        return this.result;
    }
    
    /**
     *
     * @param pid
     * @param uid
     * @return
    */
    public HashMap<String,String> removeProjectFromUser(ObjectId pid, ObjectId uid) {        
        try {            
            final Query<User> usrQuery = this.db.getDatastore().createQuery(User.class).filter("id ==", uid);
            User u = this.db.getDatastore().get(User.class, uid);
                       
            u.removeProject(pid);
            final UpdateOperations<User> update = this.db.getDatastore().createUpdateOperations(User.class).set("projects", u.getProjects());

            this.setResult("200", this.db.getDatastore().findAndModify(usrQuery, update).toString());
        }
        catch(Exception e) {
            this.userServiceLogger.error("Tried to remove Project from User, but failed.");
            this.userServiceLogger.error(e.getMessage());
            this.setResult("500", GSON.toJson(new Error(e.getMessage())));
        }
        
        return this.result;
    }
    
    public HashMap<String,String> editUser(ObjectId uid, Map<String, String> updateOptions) {
        try {
            final Query<User> usrQuery = this.db.getDatastore().createQuery(User.class).filter("id ==", uid);

            if( !(this.current.getId().equals( this.db.getDatastore().get(User.class, uid).getId())) && !this.current.isAdmin() )
                return null;

            updateOptions.forEach( (key, val) -> { 
                if(key.equals("admin") && !this.current.isAdmin())
                    return;
                if(key.equals("email") && this.db.getDatastore().createQuery(User.class).filter("email ==", val).get() != null)
                    return;
                if(key.equals("projects"))
                    return;

                final UpdateOperations<User> update = this.db.getDatastore().createUpdateOperations(User.class).set(key, val);
                this.db.getDatastore().findAndModify(usrQuery, update);
            });
            this.setResult("200", this.db.getDatastore().get(User.class, uid).toString());
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error(e.getMessage())));
        }
        return this.result;
    }
    
    public HashMap<String,String> removeUser(ObjectId uid) {        
        try {
            final WriteResult res = this.db.getDatastore().delete(User.class, uid);
            if(res.getN()>0)
                this.setResult("200", GSON.toJson(true));
            else
                this.setResult("400", GSON.toJson(new Error("User couldnt be removed")));
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error(e.getMessage())));
        }
        return this.result;
    }
}