package ham.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import ham.client.view.MDWebViewPartial;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

/**
 *
 * @author Enrico Gruner
 */
public class WelcomeComponent implements Initializable {
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    
    @FXML
    AnchorPane welcomeView;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try {         
            MDWebViewPartial mdView = new MDWebViewPartial("/ham/client/WelcomeView.md", "uri");
            WebView wv = mdView.getWebView();
            welcomeView.getChildren().add(wv);
            
            welcomeView.widthProperty().addListener((obs,oldVal,newVal) -> {
                wv.setMinWidth(newVal.doubleValue());
            });
            welcomeView.heightProperty().addListener((obs,oldVal,newVal) -> {
                wv.setMinHeight(newVal.doubleValue());
            });
        }
        catch(Exception e) {
            System.out.println("ERR "+ e.getMessage());
        }
    }    
    
}
