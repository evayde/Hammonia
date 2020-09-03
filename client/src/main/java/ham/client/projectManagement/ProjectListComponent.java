package ham.client.projectManagement;

import ham.client.WorkSpace;
import ham.client.editor.EditorService;
import ham.shared.helper.BetterTreeItem;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeView;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class ProjectListComponent implements Initializable {
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final ProjectService projectService = ProjectService.getInstance();
    private static final EditorService editorService = EditorService.getInstance();
    private boolean firstOpen = false;

    @FXML
    private TreeView<String> projectTreeView;
    private BetterTreeItem<String> selected = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {        
        // TODO: Make it less ugly. 
        projectTreeView.getSelectionModel().selectedItemProperty().addListener((obs,oldVal,newVal) -> {
            if(projectTreeView.getSelectionModel().getSelectedItem().getParent() != null) {
                if(projectTreeView.getSelectionModel().getSelectedItem().getParent().getValue().equals(rb.getString("projects"))) {
                    if(selected != null && selected != projectTreeView.getSelectionModel().getSelectedItem()) {
                        this.selected = (BetterTreeItem) projectTreeView.getSelectionModel().getSelectedItem();
                        this.selected.setValue(newVal.getValue());
                    }
                    else {
                        if(selected == null) {
                            this.selected = (BetterTreeItem) projectTreeView.getSelectionModel().getSelectedItem();
                            this.selected.setValue(this.selected.getValue());
                        }
                    }
                }
                else {
                    this.selected = (BetterTreeItem) projectTreeView.getSelectionModel().getSelectedItem();
                    if(this.selected.getItemType().equals("diagram")) {
                        System.out.println(this.selected.getItemId());                    
                        UMLDiagram d = editorService.getDiagramById(this.selected.getItemId());
                        projectService.setSelected(projectService.getProjectById(d.getPid()));
                        editorService.addDiagram(d);
                        editorService.setDiagramID(d.getId().toString());
                        workSpace.addToWorkSpace("TAB", this.selected.getValue().toString(), "/ham/client/editor/DiagramView.fxml");
                        if(!firstOpen)
                        {
                            workSpace.addToWorkSpace("LEFT", "ToolBoxView", "/ham/client/editor/ToolBoxView.fxml");
                            workSpace.addToWorkSpace("RIGHT", "PropertyView", "/ham/client/editor/PropertyView.fxml");
                            firstOpen=true;
                        }
                    }
                }
            }
            
            if(this.selected.getItemId() != null && this.selected.getItemType().equals("project")) {
                projectService.setSelected(projectService.getProjectById(this.selected.getItemId()));
            }
        });
        
        // TODO: Refactor
        BetterTreeItem<String> root = new BetterTreeItem<>(rb.getString("projects"));
        root.setExpanded(true);
        projectTreeView.setRoot(root);
        projectService.getAllFromCurrentUser().forEach((p) -> {
            BetterTreeItem<String> item = new BetterTreeItem<>(p.getName());
            item.setItemId(p.getId());
            item.setItemType("project");
            
            p.getFiles().forEach((f) -> {
                BetterTreeItem<String> fileItem = new BetterTreeItem<>(projectService.getFileById(f).getName());
                fileItem.setItemId(f);
                fileItem.setItemType("md");
                item.getChildren().add(fileItem);
            });
            
            p.getDiagrams().forEach((d) -> {
                BetterTreeItem<String> diagramItem = new BetterTreeItem<>(editorService.getDiagramById(d).getName());
                diagramItem.setItemId(d);
                diagramItem.setItemType("diagram");
                item.getChildren().add(diagramItem);
            });
            
            item.setExpanded(true);
            root.getChildren().add(item);
        });
    }
    
}
