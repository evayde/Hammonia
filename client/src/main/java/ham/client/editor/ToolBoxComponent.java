package ham.client.editor;

import ham.client.WorkSpace;
import ham.shared.editor.classdiagram.ClassDiagramClass;
import ham.shared.editor.Helper.Point;
import ham.shared.editor.classdiagram.ClassDiagramEdge;
import ham.shared.editor.classdiagram.ClassDiagramNote;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author Martin
 */
public class ToolBoxComponent implements Initializable {
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final EditorService editorService = EditorService.getInstance();
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
    }    
    
    @FXML
    private void newClassAction(ActionEvent event) 
    {    
        ClassDiagramClass class1 = new ClassDiagramClass();
        class1.setName("Class");
        class1.setLocation(new Point(0,0));
        editorService.setCreatingEdge(null);
        editorService.setCreatingNode(class1);
    }
    
    @FXML
    void newNoteAction(ActionEvent event) {
        ClassDiagramNote note = new ClassDiagramNote();
        note.setName("Note");
        note.setLocation(new Point(0,0));
        editorService.setCreatingEdge(null);
        editorService.setCreatingNode(note);
    }

    @FXML
    private void newAssociationAction(ActionEvent event) {
        ClassDiagramEdge edge = new ClassDiagramEdge();    
        edge.setTyp("ASSOCIATION");
        edge.setSourceRole("SourceRole");
        edge.setTargetRole("TargetRole");
        editorService.setCreatingEdge(edge);
        editorService.setCreatingNode(null);
    }

    @FXML
    private void newGeneralizationAction(ActionEvent event) {
        ClassDiagramEdge edge = new ClassDiagramEdge();
        edge.setTyp("GENERALIZATION");
        edge.setHiddenName(false);
        edge.setName("Genralization");
        edge.setSourceRole("SourceRole");
        edge.setTargetRole("TargetRole");
        editorService.setCreatingEdge(edge);
        editorService.setCreatingNode(null);
    }

    @FXML
    private void newRealizationAction(ActionEvent event) {
        ClassDiagramEdge edge = new ClassDiagramEdge();
        edge.setTyp("REALIZATION");
        edge.setHiddenName(false);
        edge.setName("Realization");
        edge.setSourceRole("SourceRole");
        edge.setTargetRole("TargetRole");
        editorService.setCreatingEdge(edge);
        editorService.setCreatingNode(null);
    }

    @FXML
    private void newCompositionAction(ActionEvent event) {
        ClassDiagramEdge edge = new ClassDiagramEdge();
        edge.setTyp("COMPOSITION");
        edge.setSourceRole("SourceRole");
        edge.setTargetRole("TargetRole");
        edge.setSourceMultiplicity("1");
        editorService.setCreatingEdge(edge);
        editorService.setCreatingNode(null);
    }

    @FXML
    private void newAggregrationAction(ActionEvent event) {
        ClassDiagramEdge edge = new ClassDiagramEdge();
        edge.setTyp("AGGREGRATION");
        edge.setSourceRole("SourceRole");
        edge.setTargetRole("TargetRole");
        edge.setSourceMultiplicity("0..1");
        editorService.setCreatingEdge(edge);
        editorService.setCreatingNode(null);
    }
}
