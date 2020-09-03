package ham.client.projectManagement;

import ham.client.WorkSpace;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author Enrico Gruner
 */
public class ExerciseComponent implements Initializable {
    private final WorkSpace workSpace = WorkSpace.getInstance();
    private static final ProjectService projectService = ProjectService.getInstance();
   
    @FXML
    private VBox exerciseList;
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        List<Project> projects = projectService.getLearningProjects();
        
        projects.forEach(p -> {
            Label l = new Label();
            l.setText(p.getName());
            l.setOnMouseClicked((e)-> {
                System.out.println("PID " + p.getId());
            });

            exerciseList.getChildren().add(l);        
        });        
    }
}
