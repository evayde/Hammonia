package ham.client.editor;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class HistoryComponent implements Initializable {
     private static final EditorService editorService = EditorService.getInstance();
     
    @FXML
    private VBox historyVBox;

    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
         editorService.renderCall.addListener((obs, oldSelection, newSelection)->{
            loadUI();    
        });
         
         loadUI();
    }    
    
    private void loadUI()
    {
        if (editorService.getDiagram() == null)
        {
            return;
        }

        historyVBox.getChildren().clear();

        ArrayList<String> tmp = editorService.getDiagram().getHistory();

        for (int i = tmp.size() - 1; i >= 0; i--)
        {
            Label label = new Label();
            label.setStyle("-fx-padding: 5;");
            label.setText(tmp.get(i));
            historyVBox.getChildren().add(label);
            historyVBox.getChildren().add(new Separator());
        }
    }
    
}
