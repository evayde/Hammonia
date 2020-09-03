package ham.client.projectManagement;

import ham.client.WorkSpace;
import ham.shared.helper.BetterTreeItem;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

/**
 *
 * @author Enrico Gruner
 */
public class ProjectMdComponent implements Initializable {
    private final WorkSpace workSpace = WorkSpace.getInstance();
    private static final ProjectService projectService = ProjectService.getInstance();
    
    @FXML
    private Label projectName;
    
    @FXML
    private TextField fileName;
    
    @FXML
    private TextArea editor;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.projectName.textProperty().bind(projectService.bindSelectedName());
    }
    
    @FXML
    private void saveFileAction(ActionEvent event) {
        ProjectFile f = new ProjectFile();
        f.setContent(this.editor.getText());
        f.setName(this.fileName.getText());
        f.setType("md");
        
        projectService.addFileToProject(f, projectService.getSelected().getId());
        
        /**
        if(pf.getId() != null) {
            TreeView<String> projectTree = (TreeView<String>) this.workSpace.getElementById("projectTreeView");
            BetterTreeItem<String> item = new BetterTreeItem<>(f.getName());
            item.setItemId(pf.getId());
            
            System.out.println("FILE ID: "+ pf.getId());
            
            projectTree.getRoot().getChildren().forEach((pitem) -> {
                if( ( (BetterTreeItem) pitem ).getItemId()  == projectService.getSelected().getId()) {
                    pitem.getChildren().add(item);
                }
            });
            
            workSpace.addFiletab(projectService.getSelected(), f);
            workSpace.removeFromWorkSpace("ProjectNewMDView");
        }
        **/
    }
}
