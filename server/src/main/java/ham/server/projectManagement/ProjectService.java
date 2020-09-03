package ham.server.projectManagement;

import com.google.gson.Gson;
import ham.server.Db;
import ham.server.ServiceAbstract;
import ham.server.sockets.ProjectSocket;
import ham.server.userManagement.UserService;
import ham.shared.interfaces.Service;
import ham.server.userManagement.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serving all operations on Project entity
 * 
 * @author Enrico Gruner
 * @version 0.3
 */
public class ProjectService extends ServiceAbstract implements Service {
    private final UserService userService = UserService.getInstance();
    private final Logger projectServiceLogger = LoggerFactory.getLogger(ProjectService.class);
    private static ProjectService instance = null;
    private static final Gson GSON = new Gson();
    private final Db db = new Db();
    
    private ProjectService() { }
    
    /**
     * Get the Singleton instance of ProjectService
     * 
     * @return
     */
    public static ProjectService getInstance() {
        if(instance == null)        
            instance = new ProjectService();
        return instance;
    }
    
    /**
     * Creates a project and sets the currently logged in user to be the creator
     * 
     * @param p
     * @return
     */
    public HashMap<String,String> create(Project p) {        
        User creator = this.userService.getCurrent();
        
        if(p.getName().isEmpty()) 
        {
            this.setResult("400", GSON.toJson(new Error("Empty project name")));
            return this.result;
        }
        
        if(creator == null) {
            this.projectServiceLogger.error("Creating User not found on Project creation");
            this.setResult("400", GSON.toJson(new Error("Creating User not found on Project creation")));
            return this.result;
        }
        
        if(!this.getProjectByName(p.getName()).get("status").equals("404")) {
            this.setResult("400", GSON.toJson(new Error("Couldnt create project, there is already a project with that name")));
            return this.result;
        }
        
        p.setCreator(creator.getId());
        
        try {
            this.db.getDatastore().save(p);
            
            Project savedP = (Project)new Project().fromJSON(this.getProjectByName(p.getName()).get("payload"));

            if(!userService.addProjectToUser(savedP.getId(), creator.getId() ).get("status").equals("200")) {
                // Better remove project again
                this.db.getDatastore().delete(p);
                
                this.setResult("400", "Couldnt add Project to User");                
                this.projectServiceLogger.error("Couldnt add project to user");
                return this.result;
            }
            
            // adding students and docents
            savedP.getStudents().forEach( (uid) -> {
                userService.addProjectToUser(savedP.getId(), uid);
            });
            
            savedP.getDocents().forEach( (uid) -> {
                userService.addProjectToUser(savedP.getId(), uid);
            });

            // Emit all listeners that there is a new project
            // TODO: Slow (?), please fix.
            ProjectSocket.getInstance().emitProjectCreated(savedP);

            this.setResult("200", savedP.toString());
        }
        catch(Exception e) {
            this.projectServiceLogger.error("DB Error occured while trying to create project");
            this.projectServiceLogger.error(e.getMessage());
            this.setResult("500", "Error creating Project");
        }
        
        return this.result;
    }
    
    public HashMap<String,String> getProjectsByUser(ObjectId uid) {
        try {
            User u = this.db.getDatastore().get(User.class, uid);
            
            List<Project> projects = new ArrayList<>();
            
            u.getProjects().forEach((pid)-> {
                projects.add(this.db.getDatastore().get(Project.class, pid) );
            });
            
            this.setResult("200",GSON.toJson(projects));
        }
        catch(Exception e) {
            this.projectServiceLogger.error("Couldnt retrieve projects");
            this.setResult("500", GSON.toJson(new Error("Couldnt retrieve projects" + e.getMessage())));
        }
        return this.result;
    }
    
    public HashMap<String,String> addFileToProject(ProjectFile f, ObjectId pid) {
        try {
            Project p = this.db.getDatastore().get(Project.class, pid);
            
            f.setPid(pid);
            this.db.getDatastore().save(f);
            
            final Query<ProjectFile> fileQry = this.db.getDatastore().createQuery(ProjectFile.class).filter("pid ==",f.getPid()).filter("name ==", f.getName());
            ProjectFile pf = fileQry.get();

            p.addFile(pf.getId());

            final Query<Project> prjQry = this.db.getDatastore().createQuery(Project.class).filter("id ==",p.getId());
            final UpdateOperations<Project> update = this.db.getDatastore().createUpdateOperations(Project.class).set("files", p.getFiles());
   
            this.db.getDatastore().findAndModify(prjQry, update);
            
            // Emit new File
            ProjectSocket.getInstance().emitNewFile(pf);
            
            this.setResult("200", pf.toString());
        }
        catch(Exception e) {
            this.setResult("500", e.getMessage());
        }
        return this.result;
    }
    
    public HashMap<String, String> addDiagramToProject(UMLDiagram diagram, ObjectId pid)
    {
        try {
            Project p = this.db.getDatastore().get(Project.class, pid);
            diagram.setPid(pid);
            
            this.db.getDatastore().save(diagram);
            
            final Query<UMLDiagram> diagramQry = this.db.getDatastore().createQuery(UMLDiagram.class).filter("pid ==", p.getId()).filter("name ==", diagram.getName());
            UMLDiagram dia = diagramQry.get();
                        
            p.addDiagram(dia.getId());
            
            final Query<Project> prjQry = this.db.getDatastore().createQuery(Project.class).filter("id ==", p.getId());
            final UpdateOperations<Project> update = this.db.getDatastore().createUpdateOperations(Project.class).set("diagrams", p.getDiagrams());
            this.db.getDatastore().findAndModify(prjQry, update);
            
            // Emit new Diagram
            ProjectSocket.getInstance().emitNewDiagram(dia);
            
            this.setResult("200", dia.toString());
            return this.result;
        }
        catch(Exception e) {
            this.setResult("500", e.getMessage());
            return this.result;
        }
    }
    
    public HashMap<String,String> getProjectById(ObjectId pid) {
        try {
            Project p = this.db.getDatastore().get(Project.class, pid);
            if(p == null) {
                this.setResult("404", GSON.toJson(new Error("Project not found.")));
                return this.result;
            }
            
            this.setResult("200", p.toString());
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error("Server encountered an error retrieving a project")));
        }
        return this.result;
    }
    
    public HashMap<String,String> getLearningProjectsByUser(ObjectId uid) 
    {
        try {
            User u = this.db.getDatastore().get(User.class, uid);
            List<Project> projects = new ArrayList<>();
            
            u.getProjects().forEach(pid -> {
                Project p = this.db.getDatastore().get(Project.class, pid);
                if(p.isIsLearnproject())
                    projects.add(p);
            });
            
            this.setResult("200", GSON.toJson(projects));
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error(e.getMessage())));
        }
        return this.result;
    }
    
    public HashMap<String,String> getDiagramById(ObjectId did) {
        try {
            UMLDiagram d = this.db.getDatastore().get(UMLDiagram.class, did);
            
            if(d == null) {
                this.setResult("404", GSON.toJson(new Error("Diagram not found.")));
                return this.result;
            }
            
            this.setResult("200", d.toString());
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error("Server encountered an error retrieving a Diagram")));
        }
        return this.result;
    }
    
    public HashMap<String,String> saveDiagram(UMLDiagram diagram, ObjectId pid) {
        try {            
            this.db.getDatastore().save(diagram);
            UMLDiagram newDia = this.db.getDatastore().get(UMLDiagram.class, diagram.getId());
            
            ProjectSocket.getInstance().emitDiagramChange(newDia);
            
            this.setResult("200", newDia.toString());
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error("Server encountered an error saving a diagram")));
        }
        return this.result;
    }
    
    public HashMap<String,String> getFileById(ObjectId fid) {
        try {
            ProjectFile pf = this.db.getDatastore().get(ProjectFile.class, fid);
            
            if(pf == null) {
                this.setResult("404", GSON.toJson(new Error("File not found")));
                return this.result;
            }
            
            this.setResult("200", pf.toString());
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error("Couldnt retrieve Project file")));
        }
        return this.result;
    }
    
    /**
     * Adds a single User u to a Project with pname with a given role.
     * The role might be student or docent
     * 
     * @param uid
     * @param pid
     * @param role
     * @return
     */
    public HashMap<String,String> addUserToProject(ObjectId uid, ObjectId pid, String role) {
        try {
            User u = this.db.getDatastore().get(User.class, uid);
            Project p = this.db.getDatastore().get(Project.class, pid);

            if(!p.isCreator(userService.getCurrent().getId()) && !p.isDocent(userService.getCurrent().getId()) && !userService.getCurrent().isAdmin()) {
                this.setResult("400", GSON.toJson(new Error("User is not allowed to add new Users to Project")));
                return this.result;
            }

            if( u.getProjects().contains(pid) ) {
                this.setResult("400", GSON.toJson(new Error("User is not part of project group")));
                return this.result;
            }

            final Query<Project> prjQuery = this.db.getDatastore().createQuery(Project.class).filter("id ==", pid);
            final UpdateOperations<Project> update;
            if(role.equals("student")) {
                p.addStudent(uid);
                update = this.db.getDatastore().createUpdateOperations(Project.class).set("students", p.getStudents());
            }
            else {
                p.addDocent(uid);
                update = this.db.getDatastore().createUpdateOperations(Project.class).set("docents", p.getDocents());
            }
            userService.addProjectToUser(pid, uid);
            
            this.setResult("200", this.db.getDatastore().findAndModify(prjQuery, update).toString());
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error("Couldnt add User to Project")));
        }
        return this.result;
    }
    
    /**
     * Removes a Project and unlinks all references in User to it
     *
     * @param name
     * @return
     */
    public HashMap<String,String> removeProject(ObjectId id) {        
        try {            
            Project p = this.db.getDatastore().get(Project.class, id);
            
            if(p == null) {
                this.projectServiceLogger.warn("Tried to remove Project, but no Project was found.");
                this.setResult("404", GSON.toJson(new Error("Tried to remove project, but project wasnt found")));
                return this.result;
            }
            
            // Remove Project from all users
            if(!p.getDocents().isEmpty())
                p.getDocents().forEach((docent) -> {
                    this.userService.removeProjectFromUser(p.getId(), docent);
                });
            if(!p.getStudents().isEmpty())
                p.getStudents().forEach((student) -> {
                    this.userService.removeProjectFromUser(p.getId(), student);
                });
            this.userService.removeProjectFromUser(p.getId(), p.getCreator());
            
            this.db.getDatastore().delete(Project.class, id);
            this.setResult("200", GSON.toJson(id) );
        }
        catch(Exception e) {
            this.projectServiceLogger.error("Couldnt remove Project due to a database error");
            this.projectServiceLogger.error(e.getMessage());
            this.setResult("500", GSON.toJson(new Error("Server encountered an error trying to remove a project")));
        }
        return this.result;
    }
    
    /**
     * Removes a single user from a project
     * if the user created the project the project will be nuked
     *
     * @param pname
     * @param u
     * @return
     */
    public HashMap<String,String> removeUserFromProject(ObjectId uid, ObjectId pid) {
        try {
            User u = this.db.getDatastore().get(User.class, uid);
            Project p = this.db.getDatastore().get(Project.class, pid);

            if(p.isCreator(uid)) {
                return removeProject(p.getId());
            }

            final Query<Project> prjQuery = this.db.getDatastore().createQuery(Project.class).filter("id ==", pid);
            final UpdateOperations<Project> update;

            if(p.isDocent(uid)) {
                p.removeDocent(uid);
                update = this.db.getDatastore().createUpdateOperations(Project.class).set("docents", p.getDocents());
            }
            else {
                p.removeStudent(uid);
                update = this.db.getDatastore().createUpdateOperations(Project.class).set("students", p.getStudents());
            }

            userService.removeProjectFromUser(pid, uid);

            this.setResult("200", this.db.getDatastore().findAndModify(prjQuery, update).toString());
        }
        catch(Exception e) {
            this.setResult("500", GSON.toJson(new Error("Server encountered an error removing a user from a project")));
        }
        return this.result;
    } 
    
    /**
     * Get a single project by name
     * 
     * @param name
     * @return
     */
    public HashMap<String,String> getProjectByName(String name) {
        try {
            final Query<Project> prjQuery = this.db.getDatastore().createQuery(Project.class).filter("name ==",name);
            Project p = prjQuery.get();
            
            if(p == null) {
                this.projectServiceLogger.warn("Tried to fetch a Project, but Project wasnt found");
                this.setResult("404", GSON.toJson(new Error("Project wasnt found")));
                return this.result;
            }
            
            this.setResult("200", p.toString());
        }
        catch(Exception e) {
            this.projectServiceLogger.error("DB Error occured while trying to fetch Project info.");
            this.projectServiceLogger.error(e.getMessage());
            this.setResult("500", GSON.toJson(new Error("Server encountered an error fetching a project by name")));
        }
        return this.result;
    }
}