package ham.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author Enrico Gruner
 */
public class BasicModalComponent implements Initializable {
    @FXML
    private Button modalCloseButton;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    @FXML
    private void closeModalAction(ActionEvent event) 
    {
        ((Stage) modalCloseButton.getScene().getWindow()).close();
    }

}
