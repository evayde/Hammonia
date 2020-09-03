package ham.client.userManagement;

import com.google.gson.Gson;
import ham.client.WorkSpace;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 *
 * @author Enrico Gruner
 */
public class AdminComponent implements Initializable {
    private final static WorkSpace workSpace = WorkSpace.getInstance();
    private final static UserService userService = UserService.getInstance();
    private final static Gson GSON = new Gson();
    
    // Register User
    @FXML
    private TextField userName;
    
    @FXML
    private TextField userEmail;
    
    @FXML
    private PasswordField userPassword;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
    }
    
    @FXML
    private void registerUserAction(ActionEvent event) {
        User u = new User();
        u.setEmail(this.userEmail.getText());
        u.setName(this.userName.getText());
        u.setPassword(this.userPassword.getText());
        
        userService.registerUser(u);
    }
}
