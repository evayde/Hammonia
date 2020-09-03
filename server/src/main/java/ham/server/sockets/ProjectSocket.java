package ham.server.sockets;

import com.corundumstudio.socketio.SocketIOServer;
import ham.server.projectManagement.Project;
import ham.server.projectManagement.ProjectFile;
import ham.server.projectManagement.UMLDiagram;
import ham.server.userManagement.UserService;

/**
 * Providing Project related socket events
 * 
 * @author Enrico Gruner
 * @version 0.3
 */
public class ProjectSocket {
    private static ProjectSocket instance = null;
    private static final UserService userService = UserService.getInstance();
    private SocketIOServer sockio;
    
    private ProjectSocket() {}
    
    public static ProjectSocket getInstance() {
        if(instance == null)
            instance = new ProjectSocket();
        return instance;
    }
    
    public void registerListeners(SocketIOServer sockio) 
    {
        this.sockio = sockio;
        
        //sockio.addEventListener("projects", String.class, (SocketIOClient c, String u, AckRequest a) -> emitProjectList(c,u,a));
    }
    
    // SLOW! TODO: refactor
    public void emitProjectCreated(Project p) 
    {
        this.sockio.getAllClients().forEach((c) -> {
            if(userService.getUserBy("token", c.get("token")).getProjects().contains(p.getId()))
                c.sendEvent("projectCreated", p.toString());
        });
    }
    
    public void emitNewDiagram(UMLDiagram d) {
        this.sockio.getAllClients().forEach((c) -> {
            if(userService.getUserBy("token", c.get("token")).getProjects().contains(d.getPid()))
                c.sendEvent("diagramCreated", d.toString());
        });
    }
    
    public void emitNewFile(ProjectFile f) {
        this.sockio.getAllClients().forEach((c) -> {
            if(userService.getUserBy("token", c.get("token")).getProjects().contains(f.getPid()))
                c.sendEvent("fileCreated", f.toString());
        });
    }
    
    public void emitDiagramChange(UMLDiagram d) {
        System.out.println("EMITTING DIAGRAM CHANGE");
        this.sockio.getAllClients().forEach( (c) -> {
            if(userService.getUserBy("token", c.get("token")).getProjects().contains(d.getPid()))
                c.sendEvent("diagramChanged", d.toString());
        });
    }
}
