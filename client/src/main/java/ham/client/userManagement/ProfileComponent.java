package ham.client.userManagement;

import com.google.gson.Gson;
import ham.client.WorkSpace;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 *
 * @author Enrico Gruner
 */
public class ProfileComponent implements Initializable {
    private final static WorkSpace workSpace = WorkSpace.getInstance();
    private final static UserService userService = UserService.getInstance();
    private final static Gson GSON = new Gson();
    
    private SimpleStringProperty name = new SimpleStringProperty();
    
    @FXML
    private TextField userName;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.name.set(userService.getCurrentUser().getName());
        userName.textProperty().bindBidirectional(this.name);
    }
    
    @FXML
    private void editUserAction() throws Exception 
    {
        userService.editUser(this.userName.getText());
    }
}
