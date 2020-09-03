package ham.client;

import com.google.gson.Gson;
import ham.client.editor.EditorService;
import ham.client.userManagement.UserService;
import io.reactivex.Observable;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Main Component, display the main frame of the app. 
 *
 * @author Martin
 */
public class MainComponent implements Initializable {
    private final static WorkSpace workSpace = WorkSpace.getInstance();
    private final static UserService userService = UserService.getInstance();
    private final Logger mainComponentLogger = LoggerFactory.getLogger(MainComponent.class);
    
    private Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    private static final Gson GSON = new Gson();

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        mainComponentLogger.info("Initializing Main Component.");    
    }
    
    @FXML
    private void showLearnProjectsAction(ActionEvent event) 
    {
        mainComponentLogger.info("Trying to add Exercise Tab");
        workSpace.addToWorkSpace("TAB", "ExerciseOverview", "/ham/client/projectManagement/ExerciseOverviewView.fxml");
    }
    
    @FXML
    private void showProjectOverviewAction(ActionEvent event) 
    {
        mainComponentLogger.info("Trying to add Project Overview Tab");
        workSpace.addToWorkSpace("TAB", "ProjectOverview", "/ham/client/projectManagement/ProjectOverviewView.fxml");
    }
    
    @FXML
    private void menuNewMdAction(ActionEvent event) 
    {
        mainComponentLogger.info("Trying to add Tab for creating Markdown.");
        workSpace.addToWorkSpace("TAB", "ProjectNewMDView", "/ham/client/projectManagement/ProjectNewMDView.fxml");
    }
    
    @FXML
    private void changeLocaleAction(ActionEvent event) 
    {
        mainComponentLogger.info("Trying to add modal to change language.");
        
        workSpace.addModal("msg.app.close");
        MenuItem i = ( (MenuItem) event.getSource() );
        if(i.getText().equals("DE"))
            clientPref.put("locale", GSON.toJson(new Locale("de","DE")));
        else
            clientPref.put("locale", GSON.toJson(new Locale("en","US")));
    }
    
    @FXML
    private void showCreateProjectAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Tab for creating Project.");
        workSpace.addToWorkSpace("TAB", "ProjectCreateView", "/ham/client/projectManagement/ProjectCreateView.fxml");
    }
    
    @FXML
    private void showProjectListAction(ActionEvent event) {
        mainComponentLogger.info("Show Project List Action.");
    }
    
    @FXML
    private void showAdminAreaAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Tab for Admin view.");
        workSpace.addToWorkSpace("TAB", "AdminView", "/ham/client/userManagement/AdminView.fxml");
    }
    
    @FXML
    private void showAdminCreateUserAction(ActionEvent event) {
        mainComponentLogger.info("Admin trying to create new User. Open Tab.");
        workSpace.addToWorkSpace("TAB", "newuser", "/ham/client/userManagement/AdminNewUserView.fxml");
    }
    
    @FXML
    private void showProfileTabAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Tab for Profile view.");
        workSpace.addToWorkSpace("TAB", "ProfileView", "/ham/client/userManagement/ProfileView.fxml");
    }
    
    @FXML
    private void showWelcomeTabAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Tab for Welcome view");
        workSpace.addToWorkSpace("TAB", "WelcomeView", "/ham/client/WelcomeView.fxml");
    }
    
    @FXML
    private void showConnectTabAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Connect SideView.");
        
        if(this.userService.getCurrentUser()==null)
            workSpace.addToWorkSpace("LEFT", "LoginView", "/ham/client/userManagement/LoginView.fxml");
        else
            workSpace.addToWorkSpace("LEFT", "ConnectionView", "/ham/client/userManagement/ConnectionView.fxml");
    }
    
    @FXML
    private void showProjectsAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add ProjectList to SideView.");
        workSpace.addToWorkSpace("LEFT", "ProjectListView", "/ham/client/projectManagement/ProjectListView.fxml");
    }
    
    @FXML
    private void showTicketsAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Ticket-Actions to SideView.");
        workSpace.addToWorkSpace("RIGHT", "TicketListView", "/ham/client/ticketManagement/TicketListView.fxml");
    }

    @FXML
    private void showToolBoxAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add ToolBox to SideView.");
        workSpace.addToWorkSpace("LEFT", "ToolBoxView", "/ham/client/editor/ToolBoxView.fxml");
    }
    
    @FXML
    private void menuNewAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Tab for a new Diagram.");
        workSpace.addToWorkSpace("TAB", "NewDiagramView", "/ham/client/editor/DiagramCreateView.fxml");
    }
    
    @FXML
    private void editorGlobalSettingsAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Tab for editor global settings.");
        workSpace.addToWorkSpace("TAB", "GlobalEditorSetting", "/ham/client/editor/GlobalEditorSettingView.fxml");
    }
    
    @FXML
    private void showPropertyAction(ActionEvent event) {
        mainComponentLogger.info("Trying to add Property SideView.");
        workSpace.addToWorkSpace("RIGHT", "PropertyView", "/ham/client/editor/PropertyView.fxml");
    }
     
    @FXML
    private void menuCloseAction(ActionEvent event) {
        Platform.exit();
    }
    
    @FXML
    private void menuSaveAction(ActionEvent event) {
        mainComponentLogger.info("Trying to Save Diagram.");
        try{
            EditorService.getInstance().saveDiagram();
        }
        catch(Exception e)
        {
            mainComponentLogger.error("An error occured trying to save diagram. "+e.getMessage());
        }
    }
    
    @FXML
    private void menuOpenAction(ActionEvent event) {
        mainComponentLogger.info("Trying to open diagram.");
        try{
            EditorService.getInstance().openDiagram();
        }
        catch(Exception e)
        {
            mainComponentLogger.error("An error occured trying to open diagram. "+e.getMessage());
        }
    }
    
        @FXML
    void chatViewAction(ActionEvent event) {
        mainComponentLogger.info("Trying to open diagram.");
        workSpace.addToWorkSpace("RIGHT", "ChatView", "/ham/client/editor/ChatView.fxml");
    }
    
    @FXML
    void showHistoryAction(ActionEvent event) {
        mainComponentLogger.info("Trying to open diagram.");
        workSpace.addToWorkSpace("RIGHT", "HistoryView", "/ham/client/editor/HistoryView.fxml");
    }
}
