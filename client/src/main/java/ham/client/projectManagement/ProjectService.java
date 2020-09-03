package ham.client.projectManagement;

import com.google.gson.Gson;
import ham.client.Connector;
import ham.client.userManagement.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Enrico Gruner
 */
public class ProjectService {
    private static final UserService userService = UserService.getInstance();
    private static ProjectService instance = null;
    private static final Gson GSON = new Gson();
    private Project selected = null;
    
    // JFX Binding-Preparation, TODO: swap to respective entity
    private SimpleStringProperty selectedName = new SimpleStringProperty();
    
    public static ProjectService getInstance() {
        if(instance == null)
            instance = new ProjectService();
        return instance;
    }
    
    public Project getSelected() {
        return this.selected;
    }
    
    public SimpleStringProperty bindSelectedName() {
        return this.selectedName;
    }
    
    public void setSelected(Project p) {
        this.selectedName.set(p.getName());
        this.selected = p;
    }
    
    private ProjectService() {}
    
    public List<Project> getAllFromCurrentUser() {
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());

            Connector con = new Connector();
            con.setHeader(hm);
            
            JSONObject obj = new JSONObject();
            obj.put("uid", GSON.toJson(userService.getCurrentUser().getId()));
            String responseString = con.post("/project/byUser", obj.toString());
            
            JSONArray projects = new JSONArray(responseString);
            List<Project> projs = new ArrayList<>();
            for(int i = 0; i < projects.length(); i++)
                projs.add(( (Project) new Project().fromJSON(projects.getString(i)) ));
            
            return projs;
        }
        catch(Exception e) {
            System.out.println("ERR "+e.getMessage());
            return null;
        }
    }
    
    public List<Project> getLearningProjects() {
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());

            Connector con = new Connector();
            con.setHeader(hm);
            
            JSONObject obj = new JSONObject();
            obj.put("uid", GSON.toJson(userService.getCurrentUser().getId()));
            String responseString = con.post("/project/getLearningProjects", obj.toString());
            
            JSONArray projects = new JSONArray(responseString);
            List<Project> projs = new ArrayList<>();
            for(int i = 0; i < projects.length(); i++)
                projs.add(( (Project) new Project().fromJSON(projects.getString(i)) ));
            
            return projs;
        }
        catch(Exception e) {
            System.out.println("ERR "+e.getMessage());
            return null;
        }
    }
    
    public Project createProject(Project p) 
    {
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());

            Connector con = new Connector();
            con.setHeader(hm);
            
            String responseString = con.post("/project/create", p.toString());
            
            if(con.getStatus() == 200) {
                return ((Project)new Project().fromJSON(responseString));
            }
            else
                return null;
        }
        catch(Exception e) {
            System.out.println("ERR "+e.getMessage());
            return null;
        }
    }
    
    public ProjectFile getFileById(ObjectId id) {
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());

            Connector con = new Connector();
            con.setHeader(hm);

            JSONObject obj = new JSONObject();
            obj.put("id", GSON.toJson(id));
            
            String responseString = con.post("/project/getFileById", obj.toString());
            if(con.getStatus() == 200)
                return ((ProjectFile)new ProjectFile().fromJSON(responseString));
            else
                return null;
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public Project getProjectByName(String name) {
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());

            Connector con = new Connector();
            con.setHeader(hm);

            JSONObject obj = new JSONObject();
            obj.put("name", name);
                   
            String responseString = con.post("/project/getByName", obj.toString());

            if(con.getStatus() == 200)
                return ((Project)new Project().fromJSON(responseString));
            else
                return null;
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public Project getProjectById(ObjectId pid) {
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());
            
            Connector con = new Connector();
            con.setHeader(hm);

            JSONObject obj = new JSONObject();
            obj.put("pid", GSON.toJson(pid));
            
            String responseString = con.post("/project/getById", obj.toString());
            
            if(con.getStatus() == 200)
                return ((Project) new Project().fromJSON(responseString));
            else
                return null;
        }
        catch(Exception e) {
            return null;
        }
    }
    
    public ProjectFile addFileToProject(ProjectFile file, ObjectId pid) {
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());

            Connector con = new Connector();
            con.setHeader(hm);

            JSONObject obj = new JSONObject();
            obj.put("pid", GSON.toJson(pid));
            obj.put("file", file.toString());
            
            String responseString = con.put("/project/addFile", obj.toString());
            if(con.getStatus() == 200)
                return ((ProjectFile) new ProjectFile().fromJSON(responseString));
            else
                return null;
        }
        catch(Exception e)
        {
            return null;
        }
    }
}
