package ham.client.sockets;

import ham.client.WorkSpace;
import ham.client.projectManagement.Project;
import ham.client.projectManagement.ProjectFile;
import ham.client.projectManagement.UMLDiagram;

/**
 *
 * @author Enrico Gruner
 */
public class ProjectSocket {
    private static ProjectSocket instance = null;
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private io.socket.client.Socket socket;
    
    private ProjectSocket() {}
    
    public static ProjectSocket getInstance() {
        if(instance == null)
            instance = new ProjectSocket();
        return instance;
    }
    
    public void registerListeners(io.socket.client.Socket socket) {
        this.socket = socket;
        
        this.socket.on("projectCreated", (Object ...args)-> this.listenProjectCreated(args));
        this.socket.on("diagramCreated", (Object ...args)-> this.listenDiagramCreated(args));
        this.socket.on("diagramChanged", (Object ...args)-> this.listenDiagramChanged(args));
        this.socket.on("fileCreated", (Object ...args)-> this.listenFileCreated(args));
    }
    
    private void listenProjectCreated(Object ...args) {
        Project p = (Project) new Project().fromJSON(args[0].toString());

        workSpace.addProjectToList(p);
    }
    
    private void listenDiagramCreated(Object ...args) {
        UMLDiagram d = (UMLDiagram) new UMLDiagram().fromJSON(args[0].toString());        
        workSpace.addDiagramToProjectList(d);
    }
    
    private void listenFileCreated(Object ...args) {
        ProjectFile f = (ProjectFile) new ProjectFile().fromJSON(args[0].toString());
        workSpace.addFileToProjectList(f);
    }
    
    private void listenDiagramChanged(Object ...args) {
        System.out.println("ausgabe: "+args[0].toString());
        UMLDiagram d = (UMLDiagram) new UMLDiagram().fromJSON(args[0].toString());
        System.out.println("DIAGRAM CHANGE RECEIVED " + d.toString());       
        workSpace.updateDiagramView(d);
    }
}
