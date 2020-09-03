package ham.client.userManagement;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ham.client.WorkSpace;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class LoginComponent implements Initializable {
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final UserService userService = UserService.getInstance();
    private final Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    
    private SimpleStringProperty errorText = new SimpleStringProperty("");

    @FXML
    private Label error;
    
    @FXML
    private TextField urlTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordPasswordField;
    
    @FXML
    private CheckBox sslCheckBox;
    
    @FXML
    private TextField port;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        urlTextField.setText(this.clientPref.get("uri", "http://127.0.0.1"));
        emailTextField.setText(this.clientPref.get("email", "test@abc.de"));
        passwordPasswordField.setText("123456");
        sslCheckBox.setSelected(this.clientPref.get("ssl", "false").equals("true"));
        port.setText(this.clientPref.get("port", "4567"));
        
        error.textProperty().bindBidirectional(errorText);
    }    
    
    @FXML
    void loginAction(ActionEvent event) throws Exception{
        this.clientPref.put("ssl",sslCheckBox.isSelected()?"True":"False" );
        this.clientPref.put("uri", urlTextField.getText());
        this.clientPref.put("email", emailTextField.getText());
        this.clientPref.put("port", port.getText());
        
        try {
            if(userService.loginUser(emailTextField.getText(), passwordPasswordField.getText())) {
                workSpace.removeFromWorkSpace("LoginView");
                
                workSpace.addToWorkSpace("LEFT", "ConnectionView", "/ham/client/userManagement/ConnectionView.fxml");
                workSpace.setUpWorkspace();
                
                this.errorText.set("");
            }
            else {
                this.errorText.set("Login fehlgeschlagen!");
            }
        }
        catch(Exception e) {
            workSpace.setDebugMessage(e.getMessage());
        }
    }
}