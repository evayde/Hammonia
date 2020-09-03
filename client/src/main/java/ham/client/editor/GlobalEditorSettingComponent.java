package ham.client.editor;

import ham.shared.editor.abstracts.UMLDiagramObject;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class GlobalEditorSettingComponent implements Initializable {
    private static final EditorService editorService = EditorService.getInstance();
    private final Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    
    @FXML
    private ColorPicker backgroundColorPicker;

    @FXML
    private TextField sizeTextField2;

    @FXML
    private TextField fontSizeTextField;

    @FXML
    private ColorPicker textColorPicker2;

    @FXML
    private TextField sizeTextField;

    @FXML
    private TextField fontSizeTextField2;

    @FXML
    private ColorPicker backgroundColorPicker2;

    @FXML
    private ColorPicker borderColorPicker;

    @FXML
    private ColorPicker textColorPicker;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
       loadUI();
    }    
    
    

    @FXML
    void setGlobalSettingAction(ActionEvent event) {
        this.clientPref.put("backgroundColorNode", backgroundColorPicker.getValue().toString());
        this.clientPref.put("fontColorNode", textColorPicker.getValue().toString());
        this.clientPref.put("borderColorNode", borderColorPicker.getValue().toString());
        this.clientPref.put("backgroundColorEdge", backgroundColorPicker2.getValue().toString());
        this.clientPref.put("fontColorEdge", textColorPicker2.getValue().toString());
        
        
        editorService.backgroundColorNode=backgroundColorPicker.getValue().toString();
        editorService.fontColorNode=textColorPicker.getValue().toString();
        editorService.borderColorNode=borderColorPicker.getValue().toString();
        
        editorService.backgroundColorEdge=backgroundColorPicker2.getValue().toString();
        editorService.fontColorEdge=textColorPicker2.getValue().toString();
        
         try{ 
             editorService.sizeNode=(new Double(sizeTextField.getText())).doubleValue();
             this.clientPref.put("sizeNode", sizeTextField.getText());
         }catch(Exception e){ }
         try{ 
             editorService.fontSizeNode=(new Double(fontSizeTextField.getText())).doubleValue();
             this.clientPref.put("fontSizeNode", fontSizeTextField.getText());
         }catch(Exception e){ }
         
         try{ 
             editorService.sizeEdge=(new Double(sizeTextField2.getText())).doubleValue();
             this.clientPref.put("sizeEdge", sizeTextField2.getText());
         }catch(Exception e){ }
         try{
             editorService.fontSizeEdge=(new Double(fontSizeTextField2.getText())).doubleValue();
             this.clientPref.put("fontSizeEdge", fontSizeTextField2.getText());
         }catch(Exception e){ }
             
         loadUI();
    }
    
    private void loadUI()
    {
         sizeTextField.setText((new Double(editorService.sizeNode)).toString());
        fontSizeTextField.setText((new Double(editorService.fontSizeNode)).toString());
        backgroundColorPicker.setValue(Color.web(editorService.backgroundColorNode));
        textColorPicker.setValue(Color.web(editorService.fontColorNode));
        borderColorPicker.setValue(Color.web(editorService.borderColorNode));
        
        sizeTextField2.setText((new Double(editorService.sizeEdge)).toString());
        fontSizeTextField2.setText((new Double(editorService.fontSizeEdge)).toString());
        backgroundColorPicker2.setValue(Color.web(editorService.backgroundColorEdge));
        textColorPicker2.setValue(Color.web(editorService.fontColorEdge));
    }
    
}
