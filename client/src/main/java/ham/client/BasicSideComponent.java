package ham.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;

/**
 * Providing the default template for Sidecomponents.
 *
 * @author Martin
 */
public class BasicSideComponent implements Initializable {
    private static final WorkSpace workSpace = WorkSpace.getInstance();
 
    @FXML
    private AnchorPane basicSideViewPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}
    
    public void closeSideView()
    {
        ((SplitPane)basicSideViewPane.getParent().getParent()).getItems().remove(this.basicSideViewPane);
    }
    
    @FXML
    private void closeAction(ActionEvent event) 
    {
       this.closeSideView();
    }  
    
    @FXML
    private void upAction(ActionEvent event) 
    {
       SplitPane root = ((SplitPane)basicSideViewPane.getParent().getParent());
       int oldIndex = root.getItems().indexOf(this.basicSideViewPane);
       
       if(oldIndex > 0) 
       {
         root.getItems().set(oldIndex, root.getItems().get(oldIndex-1));
         root.getItems().set(oldIndex-1, this.basicSideViewPane);
       }
    } 
    
    @FXML
    private void downAction(ActionEvent event) 
    {
       SplitPane root = ((SplitPane)basicSideViewPane.getParent().getParent());
       int oldIndex = root.getItems().indexOf(this.basicSideViewPane);
       
       if(oldIndex < root.getItems().size()-1) 
       {
         Node tmpNode = root.getItems().get(oldIndex+1);
         root.getItems().set(oldIndex+1, this.basicSideViewPane);
         root.getItems().set(oldIndex, tmpNode);
       }
    } 
}
