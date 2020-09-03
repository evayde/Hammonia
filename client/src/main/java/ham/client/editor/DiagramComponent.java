package ham.client.editor;

import ham.client.WorkSpace;
import ham.client.userManagement.UserService;
import ham.shared.editor.abstracts.UMLDiagramObject;
import ham.shared.editor.abstracts.UMLEdge;
import ham.shared.editor.abstracts.UMLNode;
import ham.shared.editor.Helper.Point;
import ham.shared.editor.classdiagram.ClassDiagramClass;
import ham.shared.editor.classdiagram.ClassDiagramEdge;
import ham.shared.editor.classdiagram.ClassDiagramNote;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class DiagramComponent implements Initializable {
    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final EditorService editorService = EditorService.getInstance();
   private static final UserService userService = UserService.getInstance();
         
    private GraphicsContext gc; 
    private UMLNode nodeBuffer = null;
    private UMLEdge checkEdge = null;
    private double zoomSspeed = 1.1;
    private String diagramID;
    
    private boolean keyUP = true;
    
    @FXML
    private Canvas drawCanvas;
    
    @FXML
    private AnchorPane canvasAnchorPane; 
        
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        this.diagramID=editorService.getDiagramID();
        canvasAnchorPane.setId(this.diagramID);
        
        editorService.renderCall.addListener((obs, oldSelection, newSelection)->{
            
            if(editorService.getDiagramID().equals(diagramID))
           {
               if(editorService.reviseionBuffer!=null)
               {
                    for(UMLNode node:editorService.getDiagram().getNodes())
                    {
                        if(editorService.reviseionBuffer.equals(node.getRevision()))
                            editorService.setSelectedObject(node);
                
                    }
                    for(ClassDiagramEdge edge:editorService.getDiagram().getEdges())
                    {
                          if(editorService.reviseionBuffer.equals(edge.getRevision()))
                            editorService.setSelectedObject(edge);
                    } 
               }
                editorService.reviseionBuffer=null;
                render();
           }
        
        });
        
        try{
          gc = drawCanvas.getGraphicsContext2D();
          drawCanvas.setWidth(editorService.getDiagram().getBounds().getWidth());
          drawCanvas.setHeight(editorService.getDiagram().getBounds().getHeight());
          drawCanvas.addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> drawCanvas.requestFocus());
          render();
        }
        catch(Exception e) {
            System.out.println("ERROR: "+ e.getMessage());
        }
    }  
    
    @FXML
    void keyReleasedAction(KeyEvent event) {
        this.keyUP = true;
    }
    
    @FXML
    private void keyPressedAction(KeyEvent event) {
                    
        if(!this.keyUP)
            return;
        
        switch (event.getCode())
        {
            case PLUS:
                if(editorService.getSelectedObject()!=null)
                {
                    if(editorService.getSelectedObject().getClass()==ClassDiagramClass.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Klasse) vergrößert");
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramEdge.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Relation) vergrößert"); 
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramNote.class)
                         editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Notiz) vergrößert");
                    
                    editorService.getSelectedObject().setLineWidth(editorService.getSelectedObject().getLineWidth()*zoomSspeed);
                    editorService.getSelectedObject().setFontSize(editorService.getSelectedObject().getFontSize()*zoomSspeed);  
                   // ClassDiagramClass t = (ClassDiagramClass) editorService.getSelectedObject();
                  //  t.addClassProperties(new ClassDiagramProperty("+","Name","String"),new ClassDiagramProperty("+","Nachname","String"));   
                    editorService.selected.setValue(!editorService.selected.getValue());
                }
                else
                {
                    editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": alle Elemente vergrößert");
                    editorService.getDiagram().zoom(zoomSspeed);
                }
                this.keyUP=false;
                render();
                break;
            case MINUS:
                 if(editorService.getSelectedObject()!=null)
                 {
                    if(editorService.getSelectedObject().getClass()==ClassDiagramClass.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Klasse) verkleinert");
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramEdge.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Relation) verkleinert"); 
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramNote.class)
                         editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Notiz) verkleinert");
                    
                    editorService.getSelectedObject().setLineWidth(editorService.getSelectedObject().getLineWidth()*(1/zoomSspeed));
                    editorService.getSelectedObject().setFontSize(editorService.getSelectedObject().getFontSize()*(1/zoomSspeed));
                    editorService.selected.setValue(!editorService.selected.getValue());
                 }
                 else
                 {
                     editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": alle Elemente verkleinert");
                     editorService.getDiagram().zoom(1/zoomSspeed);
                 }
                 this.keyUP=false;
                 render();
                break;
            case DELETE:
                if(editorService.getSelectedObject()!=null)
                {
                          if(editorService.getSelectedObject().getClass()==ClassDiagramClass.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Klasse) gelöscht");
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramEdge.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Relation) gelöscht"); 
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramNote.class)
                         editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Notiz) gelöscht");
                          
                    if(editorService.getSelectedObject().getClass().getSuperclass()== UMLNode.class)
                    {
                        editorService.getDiagram().removeNode((UMLNode)editorService.getSelectedObject());
                        editorService.setSelectedObject(null);
                    }
                    else
                    {                   
                        editorService.getDiagram().removeEdge((UMLEdge)editorService.getSelectedObject());
                        editorService.setSelectedObject(null);
                    }
                    editorService.selected.setValue(!editorService.selected.getValue());
                    this.keyUP=false;
                    render();
                }

                break;
            default:
                break;
        }
        editorService.saveDiagram();
        
    }
    
    @FXML
    private void mouseClickAction(MouseEvent event) {
            
        if(event.getButton()==MouseButton.PRIMARY)
        {
            if(editorService.getCreatingNode()!=null)
            {
                editorService.createNode(new Point(event.getX(),event.getY()));
                //render();
            }
            else if(editorService.getCreatingEdge()!=null)
            {
                if(checkEdge==null)
                    checkEdge=editorService.getCreatingEdge();
                
                if(editorService.getCreatingEdge()!=checkEdge)
                {
                    this.nodeBuffer=null;
                    checkEdge=editorService.getCreatingEdge();
                }
                            
                UMLDiagramObject object = editorService.getDiagram().getUMLDiagramObject(new Point(event.getX(),event.getY()));
                if(object!=null&&object.getClass().getSuperclass()== UMLNode.class)
                {
                    if(this.nodeBuffer==null)
                        this.nodeBuffer=(UMLNode)object;
                    else
                    {
                        editorService.createEdge(nodeBuffer,(UMLNode)object);
                        this.nodeBuffer=null;
                        this.checkEdge=null;
                       // render();
                    }
                }
            }
            else
            {
                if(editorService.getSelectedObject()==null) 
                {
                    UMLDiagramObject object = editorService.getDiagram().getUMLDiagramObject(new Point(event.getX(),event.getY()));
                    if(object!=null)
                    {
                        editorService.setSelectedObject(object);
                        
                              if(editorService.getSelectedObject().getClass()==ClassDiagramClass.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Klasse) gesperrt");
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramEdge.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Relation) gesperrt"); 
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramNote.class)
                         editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Notiz) gesperrt");
                              
                        
                        editorService.selected.setValue(!editorService.selected.getValue());
                        editorService.saveDiagram();
                       // render();
                    }
                }
                else
                {
                    if(editorService.getSelectedObject().getClass().getSuperclass()== UMLNode.class)
                    {
                              if(editorService.getSelectedObject().getClass()==ClassDiagramClass.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Klasse) verschoben");
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramNote.class)
                         editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Notiz) verschoben");
                              
                        editorService.getSelectedObject().setLocation(new Point(event.getX(),event.getY()));
                        editorService.selected.setValue(!editorService.selected.getValue());
                        editorService.saveDiagram();
                       // render();
                    }
                }
            }
        }
        else if(event.getButton()==MouseButton.SECONDARY)
        {
            if(editorService.getSelectedObject()!=null)
            {
                        if(editorService.getSelectedObject().getClass()==ClassDiagramClass.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Klasse) freigegeben");
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramEdge.class)
                        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Relation) freigegeben"); 
                    else if(editorService.getSelectedObject().getClass()==ClassDiagramNote.class)
                         editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": "+editorService.getSelectedObject().getName()+" (Notiz) freigegeben");
                        
                 editorService.setSelectedObject(null);
                 editorService.selected.setValue(!editorService.selected.getValue());
                 editorService.saveDiagram();
                // render();
            }
            if(editorService.getCreatingNode()!=null)
            {
                editorService.setCreatingNode(null);
            }
            if(editorService.getCreatingEdge()!=null)
            {
                editorService.setCreatingEdge(null);
                this.nodeBuffer=null;
                this.checkEdge=null;
            }
        }
       // editorService.saveDiagram();
          render();
    }
    
    @FXML
    private void mouseMoveAction(MouseEvent event) {
        //workSpace.setDebugMessage("x: "+event.getX()+"  y: "+event.getY());
       
    }
    

    
    private void render()
    {
        drawCanvas.setWidth(editorService.getDiagram().getBounds().getWidth());
        drawCanvas.setHeight(editorService.getDiagram().getBounds().getHeight());
        
        System.out.println("DIAGRAMM LOADED "+editorService.getDiagram().toString());
        
        editorService.getDiagram().draw(gc); 
    }    
}
