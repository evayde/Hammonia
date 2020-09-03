package ham.client.userManagement;

import com.google.gson.Gson;
import ham.client.Connector;
import ham.client.WorkSpace;
import ham.shared.userManagement.UserHelper;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Enrico Gruner
 * @version 0.3
 */
public class UserService {
    private static UserService instance = null;
    private static final Gson GSON = new Gson();
    private User current;
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final Logger userServiceLogger = LoggerFactory.getLogger(UserService.class);
    
    private UserService() {}
    
    public User getCurrentUser() {
        return this.current;
    }
    
    public static UserService getInstance() {
        if(instance == null)
            instance = new UserService();
        return instance;
    }
    
    public boolean loginUser(String email, String password) throws Exception {
        try {
            HashMap<String, String> hm = new HashMap<>();
        
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] digest = md.digest();
        
            hm.put("Authorization", UserHelper.credentialsToBase64(email, String.format("%064x", new java.math.BigInteger(1, digest))));

            Connector con = new Connector();
            con.setHeader(hm);
            String responseString = con.post("/user/login");
            
            if(con.getStatus() != 200)
                return false;
            
            this.current = (User) new User().fromJSON(responseString);
            return true;
        }
        catch(Exception e) {
            userServiceLogger.error("Could not login User "+e.getMessage());
            return false;
        }        
    }
    
    public User registerUser(User u) {
        try {
            HashMap<String, String> hm = new HashMap<>();
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(u.getPassword().getBytes("UTF-8"));
            byte[] digest = md.digest();
            u.setPassword(String.format("%064x", new java.math.BigInteger(1, digest)));
            
            hm.put("Authorization-Token", this.current.getToken());
            
            Connector con = new Connector();
            con.setHeader(hm);
            String responseString = con.post("/user/register", u.toString());
            
            if(con.getStatus()!=200)
                return null;
            
            return ((User) new User().fromJSON(responseString));
        }
        catch(Exception e) {
            userServiceLogger.error("Couldnt register User "+e.getMessage());
            return null;
        }
    }
    
    public User getLoggedIn() 
    {
        try {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("Authorization-Token", this.current.getToken());

            Connector con = new Connector();
            con.setHeader(hm);
            String responseString = con.get("/user/getLoggedIn");

            return GSON.fromJson(responseString, User.class);
        }
        catch(Exception e) {
            userServiceLogger.error("Couldnt retrieve logged in User");
            return null;
        }
    }
    
    public boolean logoutUser() throws Exception {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("Authorization-Token", this.current.getToken());
        
        Connector con = new Connector();
        con.setHeader(hm);
        con.put("/user/logout");
        
        if(con.getStatus() == 200) {
            this.current = null;
            return true;
        }
        return false;
    }
    
    public User editUser(String name) throws Exception
    {
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Authorization-Token", this.current.getToken());
        
        Connector con = new Connector();
        con.setHeader(hm);
        
        JSONObject updates = new JSONObject();
        updates.put("name", name);
        
        JSONObject obj = new JSONObject();
        obj.put("uid", new JSONObject(this.current.getId()).toString());
        obj.put("updateOptions", updates);

        String response = con.put("/user/edit", obj.toString());
        
        if(con.getStatus() != 200)
            return null;
        return ( (User) new User().fromJSON(response) );
    }
    
    public List<User> getAll() throws Exception {
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Authorization-Token", this.current.getToken());
        
        Connector con = new Connector();
        con.setHeader(hm);
        
        String response = con.get("/user/get");
        
        if(con.getStatus() != 200)
            return null;
        JSONArray usersObj = new JSONArray(response);
        List<User> userList = new ArrayList<>();
        for(int i = 0; i < usersObj.length(); i++)
            userList.add((User)new User().fromJSON(usersObj.getString(i)));
        
        return userList;
    }
    
    public User getByEmail(String email) throws Exception 
    {
        HashMap<String,String> hm = new HashMap<>();
        hm.put("Authorization-Token", this.current.getToken());
        
        Connector con = new Connector();
        con.setHeader(hm);
        
        String response = con.get("/user/get/"+email);
        
        if(con.getStatus() != 200)
            return null;
        return (User) new User().fromJSON(response);
    }
}
