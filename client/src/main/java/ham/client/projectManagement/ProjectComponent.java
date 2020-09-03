package ham.client.projectManagement;

import ham.client.WorkSpace;
import ham.client.userManagement.User;
import ham.client.userManagement.UserService;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Enrico Gruner
 */
public class ProjectComponent implements Initializable {
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final ProjectService projectService = ProjectService.getInstance();
    private static final UserService userService = UserService.getInstance();
    private ResourceBundle rb;
    
    @FXML
    private TextField projectName;
    
    @FXML
    private CheckBox isLearnproject;
    
    @FXML
    private ComboBox userList;
    
    @FXML
    private ComboBox userRole;
    
    @FXML
    private ListView usersListView;
    
    private HashMap<String,String> projectUsers = new HashMap<>();
    
    @FXML
    private void saveProjectAction(ActionEvent event) 
    {
        Project p = new Project();
        p.setName(projectName.getText());
        p.setIsLearnproject(this.isLearnproject.isSelected());
        
        this.projectUsers.forEach((key, value) -> {
            try {
                if(value.equals(rb.getString("docent")))
                    p.addDocent(userService.getByEmail(key).getId());
                else
                    p.addStudent(userService.getByEmail(key).getId());
            }
            catch(Exception e) {

            }
        });
        
        projectService.createProject(p);
    }
    
    @FXML
    private void addUserAction(ActionEvent event) 
    {
        this.usersListView.getItems().add(this.userList.getValue() + " ("+ this.userRole.getValue() +")");
        this.projectUsers.put(this.userList.getValue().toString(), this.userRole.getValue().toString());
        
        this.userList.getItems().remove(this.userList.getValue());
        this.userList.getSelectionModel().select(0);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.rb = rb;
        this.userList.getItems().clear();
        this.userRole.getItems().clear();
        
        this.userRole.getItems().add(rb.getString("docent"));
        this.userRole.getItems().add(rb.getString("student"));
        
        this.usersListView.setOnKeyPressed((event)->this.removeFromUsersListView(event));
        
        try {
            List<User> userList = userService.getAll();
            userList.forEach((u) -> {
                this.userList.getItems().add(u.getEmail());
            });
        }
        catch(Exception e) {
            System.out.println("ERR "+e.getMessage());
        }
    }
    
    private void removeFromUsersListView(KeyEvent event) {
        if(event.getCode() == KeyCode.DELETE) {
            this.projectUsers.forEach((key, val) -> {
                if( (key+" ("+rb.getString("docent")+")").equals(this.usersListView.getSelectionModel().getSelectedItem()) ||
                    (key+" ("+rb.getString("student")+")").equals(this.usersListView.getSelectionModel().getSelectedItem()))
                    this.projectUsers.remove(key);
            });
        }
    }
}