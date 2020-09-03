package ham.client.userManagement;

import com.google.gson.Gson;
import ham.client.WorkSpace;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.beans.property.SimpleStringProperty;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class ConnectionComponent implements Initializable {
    private final static WorkSpace workSpace = WorkSpace.getInstance();
    private final static UserService userService = UserService.getInstance();
    private final static Gson GSON = new Gson();
    
    private SimpleStringProperty name = new SimpleStringProperty();
    
    @FXML
    private Label emailLabel;

    @FXML
    private Label nameLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        emailLabel.setText(userService.getCurrentUser().getEmail());
        //nameLabel.setText(SessionHelper.activeUser.getName());
        
        this.name.set(userService.getCurrentUser().getName());
        nameLabel.textProperty().bindBidirectional(this.name);
    }    
    
    @FXML
    private void logoutAction(ActionEvent event) throws Exception {
        workSpace.getSocket().disconnect();
        workSpace.removeFromWorkSpace("ConnectionView");
        workSpace.addToWorkSpace("LEFT", "LoginView", "/ham/client/userManagement/LoginView.fxml");
       
        userService.logoutUser();
    } 
}
