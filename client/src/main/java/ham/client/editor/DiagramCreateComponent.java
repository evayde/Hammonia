package ham.client.editor;

import ham.client.WorkSpace;
import ham.client.projectManagement.Project;
import ham.client.projectManagement.ProjectService;
import ham.client.projectManagement.UMLDiagram;
import ham.client.userManagement.UserService;
import ham.shared.editor.Helper.Rectangle;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 *
 * @author Enrico Gruner
 */
public class DiagramCreateComponent implements Initializable {
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final ProjectService projectService = ProjectService.getInstance();
    private static final EditorService editorService = EditorService.getInstance();
    private static final UserService userService = UserService.getInstance();
    
    @FXML
    private TextField diagramName;
    
    @FXML
    private Label projectName;
    
    
    @FXML
    private DatePicker dateDatePicker;
    
    @FXML
    private CheckBox createTaskCheckBox;
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        createTaskCheckBox.setSelected(false);
        
        if(projectService.getSelected()==null)
            createTaskCheckBox.setDisable(false);
        else
        {        
            if(projectService.getSelected().getDocents().contains(userService.getCurrentUser().getId())||
                    projectService.getSelected().getCreator().equals(userService.getCurrentUser().getId())||
                    userService.getCurrentUser().isAdmin())
            {
                createTaskCheckBox.setDisable(false);
                dateDatePicker.setDisable(false);
            }
            else
            {
                createTaskCheckBox.setDisable(true);
                dateDatePicker.setDisable(true);
            }
        }
        
        
        this.projectName.textProperty().bind(projectService.bindSelectedName());
    }
    
    @FXML
    private void saveDiagramAction(ActionEvent event) {
        if(diagramName.getText().isEmpty())
            return;
        
        Project p = projectService.getSelected();
                
        if(p != null) {
            UMLDiagram diagram = new UMLDiagram();
            diagram.setName(diagramName.getText());
            diagram.setBounds(new Rectangle(0,0,2000,1500));
            
            if(createTaskCheckBox.isSelected())
                diagram.setExercise(true);
            
            diagram.setDeadLine(dateDatePicker.getValue());
            
            UMLDiagram saved = editorService.addDiagramToProject(diagram,p.getId());
            
            editorService.setDiagramID(saved.getId().toString());
            editorService.addDiagram(saved);
            // TODO: Refactor
            workSpace.addToWorkSpace("TAB", diagram.getName(), "/ham/client/editor/DiagramView.fxml");
            workSpace.addToWorkSpace("LEFT", "ToolBoxView", "/ham/client/editor/ToolBoxView.fxml");
            workSpace.removeFromWorkSpace("NewDiagramView");
            // TODO: Start Socket?
        }
    }
    
}
