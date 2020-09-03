package ham.client;

import com.google.gson.Gson;
import ham.client.editor.EditorService;
import ham.client.projectManagement.Project;
import ham.client.projectManagement.ProjectFile;
import ham.client.projectManagement.UMLDiagram;
import ham.client.projectManagement.ProjectService;
import ham.client.sockets.Socket;
import ham.client.userManagement.UserService;
import ham.client.view.PartialFactory;
import ham.shared.helper.BetterTreeItem;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin
 */
public class WorkSpace {
    private static WorkSpace instance = null;
    private final static Logger workSpaceLogger = LoggerFactory.getLogger(WorkSpace.class);
    private final static UserService userService = UserService.getInstance();
    private final static ProjectService projectService = ProjectService.getInstance();
    private final static EditorService editorService = EditorService.getInstance();
    private PartialFactory partialFactory = new PartialFactory();
    private Socket socket = null;
    
    private Stage stage = null;
    private ResourceBundle rb = null;
    
    private Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    private static final Gson GSON = new Gson();
    
    private WorkSpace(){ }
    
    public static WorkSpace getInstance()
    {
        if(instance == null)
            instance = new WorkSpace();
        return instance;    
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public Node getElementById(String id) {
        return this.stage.getScene().lookup("#"+id);
    }
    
    public void setUpWorkspace() {
        workSpaceLogger.info("Setting up workspace...");
              
        this.getElementById("userMenu").setDisable(false);
        this.getElementById("projectMenu").setDisable(false);

        // Adding sockets to receive updates
        try {
            this.socket = new Socket();
        }
        catch(Exception e) {
            workSpaceLogger.error("Socket couldnt be opened "+ e.getMessage());
        }
        
        this.addToWorkSpace("LEFT", "ProjectListView", "/ham/client/projectManagement/ProjectListView.fxml");
    }
    
    public void setUp(Stage stage) throws Exception
    {
        this.stage = stage;
        this.rb = ResourceBundle.getBundle("ham/client/i18n/MainView", GSON.fromJson(this.clientPref.get("locale", GSON.toJson(new Locale("de","DE"))), Locale.class));
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ham/client/MainView.fxml"), rb);
        Scene scene = new Scene(loader.load());
        
        // set dynamic dimensions
        this.stage.setWidth(Screen.getPrimary().getBounds().getWidth());
        this.stage.setHeight(Screen.getPrimary().getBounds().getHeight());
        
        // set app icon
        this.stage.getIcons().add(new Image("/img/ham_small.png"));
        
        // set title and show window
        this.stage.setTitle(rb.getString("window.title"));
        this.stage.setScene(scene);
        this.stage.setMaximized(true);        
        this.stage.show();
        
        // initialize split pane divider positions
        this.getMainSplitPane().setDividerPosition(0, 0.01);
        this.getMainSplitPane().setDividerPosition(1, 0.9);
        
        this.getCenter().getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection)->{
            if(this.getCenter().getSelectionModel().getSelectedItem()==null)
                return;
            editorService.setDiagramID(this.getCenter().getSelectionModel().getSelectedItem().getId());
        });
        
        this.addToWorkSpace("TAB", "WelcomeView", "/ham/client/WelcomeView.fxml");
        this.addToWorkSpace("LEFT", "LoginView", "/ham/client/userManagement/LoginView.fxml");                    
    }
    
    public void addToWorkSpace(String destiny, String name, String url)
    {
        workSpaceLogger.info("Trying to add Element to Workspace");
        
        if (getElementById(name) != null){
            workSpaceLogger.warn(name+" already exists");
            if(destiny.equals("TAB"))
            {
                for(Tab tab : this.getCenter().getTabs())
                {
                    if(tab.getContent().getId().equals(name))
                    {
                        this.getCenter().getSelectionModel().select(tab);
                    }
                }             
            }
            return;
        }
        
        Node node = partialFactory.getPartial(destiny, name, url);
        if (node == null){
            workSpaceLogger.error("wrong url or wrong name or error in language file/fxml file");
            return;
        }

        switch (destiny) 
        {
            case "TAB":
                addTab(name,node);
                break;
            case "LEFT":
                addItemLeftSideView(node);
                break;
            case "RIGHT":
                addItemRightSideView(node);
                break;
            default:
                workSpaceLogger.error("destination does not exist");
                break;
        }
    }

    public void removeFromWorkSpace(String name) 
    {
        Node node = getElementById(name);
        if(node == null){
            workSpaceLogger.error("node "+name+" does not exist");
            return;
        }
          
        switch (getDestination(node))
        {
            case "TAB":
                removeTab(node);
                break;
            default:
                ((SplitPane)node.getParent().getParent()).getItems().remove(node);
                break;
        }
        workSpaceLogger.info("remove "+node.getId()+" from workspace");
    }
    
    public void setDebugMessage(String text)
    {
        Label label = (Label) this.getElementById("debugLabel");
        label.setText(text);
        workSpaceLogger.debug(text);
    }
        
    private String getDestination(Node node)
    {
        if(getLeft().getItems().contains(node))
        {
            return "LEFT";
        }
        else if(getRight().getItems().contains(node))
        {
            return "RIGHT";
        }
        else 
        {
            for(Tab tab : getCenter().getTabs())
            {
                if(tab.getContent().equals(node))
                    return "TAB";
            }
        }
        workSpaceLogger.warn("Destination couldnt be found.");
        return null;
    }
    
    private void addTab(String name, Node node)
    {
        Tab tab;
        if(this.rb.containsKey(name))
            tab = new Tab( this.rb.getString(name), node);
        else
            tab = new Tab( name, node );
        
        AnchorPane pane = (AnchorPane)node;
        ScrollPane pane2 = (ScrollPane)pane.getChildren().get(0);             
        tab.setId(pane2.getContent().getId());
 
        getCenter().getTabs().add(tab);
        getCenter().getSelectionModel().select(tab);
        workSpaceLogger.info("add "+node.getId()+" on centerTabView");
    }
    
    public void addModal(String name)
    {
        Node n = null;
        try {
            n = partialFactory.getPartial("MODAL", name, null);
            Stage newModal = new Stage();
            Scene s = new Scene( (Parent)n );
            
            newModal.setScene(s);
            newModal.setResizable(false);
            newModal.initModality(Modality.APPLICATION_MODAL);
            newModal.setTitle(this.rb.getString(name+".title"));
            newModal.show();
        }
        catch(Exception e) {
            workSpaceLogger.error("ERROR "+e.getMessage());
        }
    }
    
    private void addItemLeftSideView(Node node)
    {
        workSpaceLogger.info("add "+node.getId()+" on leftSideView");
        getLeft().getItems().add(node);
    }
    
    private void addItemRightSideView(Node node)
    {
        workSpaceLogger.info("add "+node.getId()+" on rightSideView");
        getRight().getItems().add(node);
    }
    
    private void removeTab(Node node)
    {
        this.getCenter().getTabs().forEach((tab) -> {
            if(tab.getContent().equals(node)==true)
                this.getCenter().getTabs().remove(tab);
        });

        workSpaceLogger.info("remove "+node.getId()+" from centerTabView");
    }
    
    private SplitPane getLeft()
    {
        return (SplitPane) this.getElementById("leftSplitPane");
    }
    
    private SplitPane getRight()
    {
        return (SplitPane) this.getElementById("rightSplitPane");
    }
    
    private SplitPane getMainSplitPane()
    {
        return (SplitPane) this.getElementById("mainSplitPane");
    }
    
    private TabPane getCenter()
    {
        return (TabPane) this.getElementById("centerTabPane");
    }  

    public void addFiletab(Project p, ProjectFile f)
    {
        // TODO add this to generic addTab
        try {
            Tab tab = new Tab( f.getName(), FXMLLoader.load(getClass().getResource("/ham/client/view/FileTabView.fxml")));
            GridPane gridPane = (GridPane)tab.getContent();
            ((TextArea)gridPane.getChildren().get(0)).setText(f.getContent());
            getCenter().getTabs().add(tab);
            getCenter().getSelectionModel().select(tab);
        }
        catch(Exception e) {
            workSpaceLogger.error(e.getMessage());
        }
    }
    
    public void addProjectToList(Project p) {
        TreeView<String> projectTree = (TreeView<String>) this.getElementById("projectTreeView");
        BetterTreeItem<String> item = new BetterTreeItem<>(p.getName());
        item.setItemId(p.getId());
        item.setItemType("project");
        
        projectTree.getRoot().getChildren().add(item);
    }
    
    // TODO Refactor
    public void addDiagramToProjectList(UMLDiagram d) {
        TreeView<String> projectTree = (TreeView<String>) this.getElementById("projectTreeView");
        BetterTreeItem<String> item = new BetterTreeItem<>(d.getName());
        item.setItemId(d.getId());
        item.setItemType("diagram");

        projectTree.getRoot().getChildren().forEach((pitem) -> {
            if( ( (BetterTreeItem) pitem).getItemId().equals(projectService.getSelected().getId() ))
                pitem.getChildren().add(item); 
        });
    }
    
    public void addFileToProjectList(ProjectFile f) {     
       
               TreeView<String> projectTree = (TreeView<String>) this.getElementById("projectTreeView");
               
               BetterTreeItem<String> item = new BetterTreeItem<>(f.getName());
               item.setItemId(f.getId());
               item.setItemType("md");
    
               projectTree.getRoot().getChildren().forEach((pitem) -> {
                   if( ( (BetterTreeItem) pitem ).getItemId().equals(projectService.getSelected().getId()))
                       pitem.getChildren().add(item);               
               });      
    }
    // /TODO refactor

    // Just call this to clean up
    public void tearDown()
    {
        if(this.socket != null)
            this.socket.disconnect();
    }
    
    public void updateDiagramView(UMLDiagram d)
    {    
        Platform.runLater(()->{
            editorService.updateDiagram(d);
       });
        
    }
    
   
}