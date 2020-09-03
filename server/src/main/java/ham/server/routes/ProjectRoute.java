package ham.server.routes;

import com.google.gson.Gson;
import ham.server.projectManagement.Project;
import ham.server.projectManagement.ProjectFile;
import ham.server.projectManagement.ProjectService;
import ham.server.projectManagement.UMLDiagram;
import java.util.HashMap;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author Enrico Gruner
 * @version 0.15
 */
public class ProjectRoute {
    final static Gson GSON = new Gson();
    final static ProjectService projectService = ProjectService.getInstance();
    final static Logger projectLogger = LoggerFactory.getLogger(UserRoute.class);
    
    /**
     * Create Project route
     * 
     * @see ham.server.projectManagement.ProjectService#create(java.lang.String) 
     */
    public static Route createProject = (Request req, Response res) -> 
    {
        projectLogger.info("Create Project route has been called");
        
        Project tp = (Project) new Project().fromJSON(req.body());        
        HashMap<String,String> result = projectService.create(tp);
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    /**
     * Remove a single Project
     * 
     * @see ham.server.projectManagement.ProjectService#removeProject(java.lang.String) 
     */
    public static Route removeProject = (Request req, Response res) -> 
    {
        projectLogger.info("Remove Project route called");
        
        JSONObject obj = new JSONObject(req.body());
        ObjectId pid = GSON.fromJson(obj.getString("id"), ObjectId.class);
        
        HashMap<String, String> result = projectService.removeProject(pid);
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    public static Route addUser = (Request req, Response res) ->
    {
        projectLogger.info("Add User to Project route called");

        JSONObject obj = new JSONObject(req.body());
        String role = req.params("role").equals("docent") ? "docent" : "student";
        ObjectId uid = GSON.fromJson(obj.getString("uid"), ObjectId.class);
        ObjectId pid = GSON.fromJson(obj.getString("pid"), ObjectId.class);
        
        HashMap<String, String> result = projectService.addUserToProject( uid, pid, role );
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    public static Route removeUser = (Request req, Response res) ->
    {
        projectLogger.info("remove User from Project route called");

        JSONObject obj = new JSONObject(req.body());
        ObjectId uid = GSON.fromJson(obj.getString("uid"), ObjectId.class);
        ObjectId pid = GSON.fromJson(obj.getString("pid"), ObjectId.class);
        
        HashMap<String,String> result = projectService.removeUserFromProject( uid, pid );
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    public static Route getProjectsByUser = (Request req, Response res) -> 
    {
        projectLogger.info("Trying to serve all Projects by a specific User");
        
        JSONObject obj = new JSONObject(req.body());
        HashMap<String,String> result = projectService.getProjectsByUser( GSON.fromJson(obj.get("uid").toString(), ObjectId.class) );
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    public static Route addFileToProject = (Request req, Response res) ->
    {
        projectLogger.info("Adding file to project");
        
        JSONObject obj = new JSONObject(req.body());
        ProjectFile f = GSON.fromJson(obj.getString("file"), ProjectFile.class);
        ObjectId pid = GSON.fromJson(obj.getString("pid"), ObjectId.class);
        
        HashMap<String, String> result = projectService.addFileToProject(f, pid);
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    public static Route addDiagramToProject = (Request req, Response res) -> 
    {
        projectLogger.info("Adding Diagram to project");
        
        JSONObject obj = new JSONObject(req.body());
        ObjectId pid = GSON.fromJson(obj.getString("pid"), ObjectId.class);
        UMLDiagram diagram = GSON.fromJson(obj.getString("diagram"), UMLDiagram.class);
        
        HashMap<String,String> result = projectService.addDiagramToProject(diagram, pid);
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    public static Route saveDiagram = (Request req, Response res) ->
    {
        try {
            projectLogger.info("Update diagram from Project");

            JSONObject obj = new JSONObject(req.body());
            ObjectId pid = GSON.fromJson(obj.getString("pid"), ObjectId.class);
            
            UMLDiagram diagram = GSON.fromJson(obj.getString("diagram"), UMLDiagram.class);

            HashMap<String,String> result = projectService.saveDiagram(diagram,pid);

            res.status(Integer.parseInt(result.get("status")));
            return result.get("payload");
        }
        catch(Exception e) {
            System.out.println("ERR " + e.getMessage());
            return e.getMessage();
        }
    };
    
    public static Route getDiagramById = (Request req, Response res) -> 
    {
        try {
            projectLogger.info("Trying to retrieve Diagram by ID");
            
            JSONObject obj = new JSONObject(req.body());
            ObjectId did = GSON.fromJson(obj.getString("did"), ObjectId.class);
            
            HashMap<String,String> result = projectService.getDiagramById(did);
            
            res.status(Integer.parseInt(result.get("status")));
            return result.get("payload");
        }
        catch(Exception e)
        {
            System.out.println("ERR "+ e.getMessage());
            return e.getMessage();
        }
    };
    
    public static Route getProjectById = (Request req, Response res) ->
    {
        try {
            projectLogger.info("Retrieving Project by ID");
            
            JSONObject obj = new JSONObject(req.body());
            ObjectId pid = GSON.fromJson(obj.getString("pid"), ObjectId.class);
            
            HashMap<String,String> result = projectService.getProjectById(pid);
            
            res.status(Integer.parseInt(result.get("status")));
            return result.get("payload");
        }
        catch(Exception e) 
        {
            System.out.println("ERR " + e.getMessage());
            return e.getMessage();
        }
    };
    
    public static Route getFileById = (Request req, Response res) ->
    {
        projectLogger.info("Trying to retrieve File");
        
        JSONObject obj = new JSONObject(req.body());
        ObjectId fid = GSON.fromJson(obj.getString("id"), ObjectId.class);
        
        HashMap<String, String> result = projectService.getFileById(fid);
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    /**
     * Get a single Project by name
     * 
     * @see ham.server.projectManagement.ProjectService#getProjectByName(java.lang.String) 
     */
    public static Route getProjectByName = (Request req, Response res) ->
    {
        projectLogger.info("Get Project Name Route called");
        
        JSONObject obj = new JSONObject(req.body());
        
        HashMap<String,String> result = projectService.getProjectByName(obj.getString("name"));
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
    
    public static Route getLearningProjectsByUser = (Request req, Response res) ->
    {
        projectLogger.info("Get learning projects by users");
        
        JSONObject obj = new JSONObject(req.body());
        ObjectId uid = GSON.fromJson(obj.getString("uid"), ObjectId.class);
        
        HashMap<String, String> result = projectService.getLearningProjectsByUser(uid);
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
}
