package ham.client.editor;

import com.google.gson.Gson;
import ham.client.Connector;
import ham.shared.editor.abstracts.UMLDiagramObject;
import ham.shared.editor.abstracts.UMLEdge;
import ham.shared.editor.abstracts.UMLNode;
import ham.client.projectManagement.ProjectService;
import ham.client.projectManagement.UMLDiagram;
import ham.client.userManagement.UserService;
import ham.shared.editor.Helper.Point;
import ham.shared.editor.Helper.Rectangle;
import ham.shared.editor.classdiagram.ClassDiagramClass;
import ham.shared.editor.classdiagram.ClassDiagramEdge;
import ham.shared.editor.classdiagram.ClassDiagramNote;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.prefs.Preferences;
import javafx.beans.property.SimpleBooleanProperty;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Martin
 */
public class EditorService {
    private static final UserService userService = UserService.getInstance();
    private static final ProjectService projectService = ProjectService.getInstance();
    private static final Gson GSON = new Gson();
    private static EditorService instance = null;
    private final Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
      
    private UMLDiagramObject selectedObject = null;
    private UMLNode creatingNode = null;
    private UMLEdge creatingEdge = null;
    
    public Integer reviseionBuffer = null;
    
    private String idBuffer;
    public boolean loadUI = false;
    private ArrayList<UMLDiagram> diagrams = new ArrayList<>();
    
    public SimpleBooleanProperty renderCall = new SimpleBooleanProperty(false);
    
    public SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
 
    private String diagramString;
    
    public double sizeNode;
    public double sizeEdge;
    public double fontSizeNode;
    public double fontSizeEdge;
    public String backgroundColorNode;
    public String backgroundColorEdge;
    public String borderColorNode;
    public String fontColorEdge;
    public String fontColorNode;
    
    
     
    public static EditorService getInstance(){
        if(instance==null)
            instance = new EditorService();
        return instance;
    }
    
    private EditorService() 
    {
        sizeNode=new Double(this.clientPref.get("sizeNode", "1")).doubleValue();
        sizeEdge=new Double(this.clientPref.get("sizeEdge", "1.5")).doubleValue();
        fontSizeNode=new Double(this.clientPref.get("fontSizeNode", "12")).doubleValue();
        fontSizeEdge=new Double(this.clientPref.get("fontSizeEdge", "12")).doubleValue();
        backgroundColorNode=this.clientPref.get("backgroundColorNode", "white");
        backgroundColorEdge=this.clientPref.get("backgroundColorEdge", "white");
        borderColorNode=this.clientPref.get("borderColorNode", "gray");
        fontColorEdge=this.clientPref.get("fontColorEdge", "lightgray");
        fontColorNode=this.clientPref.get("fontColorNode", "black");
    }
  
    public void addDiagram(UMLDiagram d)
    {
        for(UMLDiagram diagram: this.diagrams)
        {
            if(diagram.getId().toString().equals(d.getId().toString()))
            {
                this.diagrams.remove(diagram);
                this.diagrams.add(d);  
                return;
            }                                                         
        }
        this.diagrams.add(d);
    }
    
    public UMLDiagramObject getSelectedObject()
    {
        return selectedObject;
    }

    
    
    public void setSelectedObject(UMLDiagramObject selectedObject)
    {
      
        if(this.getSelectedObject()!=null)
        {
            this.getSelectedObject().setSelected(false);
            this.getSelectedObject().setUserLock(null);
        }
        
        if(selectedObject!=null)
        {
          
            
            if(selectedObject.getUserLock()==null||selectedObject.getUserLock().equals(userService.getCurrentUser().getId()))
            {
                 selectedObject.setSelected(true);
                 selectedObject.setUserLock(userService.getCurrentUser().getId());
            }
            else
            {
                selectedObject = null;
            }
        }
        
        this.selectedObject = selectedObject;
        
        
        if(this.loadUI)
        {
            this.loadUI=false;
            this.selected.setValue(!this.selected.getValue());
        }
        
         
    }

    public UMLDiagram getDiagram()
    {
        for(UMLDiagram diagram: this.diagrams)
        {
            if(diagram.getId().toString().equals(this.idBuffer))
                return diagram;
        }
      
        return null;
    }
    
    public void updateDiagram(UMLDiagram d)
    {     
         for(UMLDiagram diagram: this.diagrams)
        {
            if(diagram.getId().toString().equals(d.getId().toString()))
            {
                if(this.getSelectedObject()!=null&&this.getDiagramID().equals(diagram.getId().toString()))
                     this.reviseionBuffer = this.selectedObject.getRevision();
                this.diagrams.remove(diagram);
                this.diagrams.add(d);  
                if(d.getId().toString().equals(this.idBuffer))
                      renderCall.setValue(!renderCall.getValue());
                return;
            }                                                         
        }
    }

    public void setDiagramID(String id)
    {
        if(!id.equals(this.idBuffer))
            this.setSelectedObject(null);
        this.idBuffer = id;
        renderCall.setValue(!renderCall.getValue());
    }
    
    public String getDiagramID()
    {
        return this.idBuffer;
    }

    public UMLNode getCreatingNode()
    {
        return creatingNode;
    }

    public void setCreatingNode(UMLNode creatingNode)
    {
        this.creatingNode = creatingNode;
    }

    public UMLEdge getCreatingEdge()
    {
        return creatingEdge;
    }

    public void setCreatingEdge(UMLEdge creatingEdge)
    {
        this.creatingEdge = creatingEdge;
    }


    public String getDiagramString()
    {
        return diagramString;
    }

    public void setDiagramString(String diagramString)
    {
        this.diagramString = diagramString;
    }
    
    public void createNode(Point point)
    {
        this.getCreatingNode().setFontSize(this.fontSizeNode);
        this.getCreatingNode().setLineWidth(this.sizeNode);
        this.getCreatingNode().setBackgroundColor(this.backgroundColorNode);
        this.getCreatingNode().setBorderColor(this.borderColorNode);
        this.getCreatingNode().setTextColor(this.fontColorNode);
        this.getCreatingNode().setLocation(point);
        this.getDiagram().addNodes((UMLNode)this.getCreatingNode());
        this.setSelectedObject(this.getCreatingNode());
        this.selected.setValue(!this.selected.getValue());
        
        
        if(getCreatingNode().getClass()==ClassDiagramClass.class)
            this.getDiagram().addHistoy(userService.getCurrentUser().getName()+": neue Klasse erstellt");
        else
            this.getDiagram().addHistoy(userService.getCurrentUser().getName()+": neue Notiz erstellt");
        
        this.setCreatingNode(null);
        this.saveDiagram();
    }
    
    public void createEdge(UMLNode source, UMLNode target)
    {
        this.getCreatingEdge().setFontSize(this.fontSizeEdge);
        this.getCreatingEdge().setLineWidth(this.sizeEdge);
        this.getCreatingEdge().setBackgroundColor(this.backgroundColorEdge);
        this.getCreatingEdge().setTextColor(this.fontColorEdge);
        this.getCreatingEdge().setSource(source);
        this.getCreatingEdge().setTarget(target);
        this.selected.setValue(!this.selected.getValue());
        if(this.getDiagram().addEdge((ClassDiagramEdge)getCreatingEdge()))
             this.setSelectedObject(this.getCreatingEdge());
        
        
        
        this.getDiagram().addHistoy(userService.getCurrentUser().getName()+": neue Relation erstellt ("+source.getName()+","+target.getName()+")");
        
        this.setCreatingEdge(null);
        this.saveDiagram();

    }
    
    public void openDiagram() throws JSONException, IOException
    { 
        
     /* Gson GSON = new Gson();
        JSONObject obj = new JSONObject(this.diagramString);
        
         
        UMLDiagramEntity tmpDiagram = new ClassDiagram();
        tmpDiagram.setId(GSON.fromJson(obj.getString("id"), Id.class));
        tmpDiagram.setBounds(GSON.fromJson(obj.getString("bounds"), Rectangle2D.class));
        tmpDiagram.setName(GSON.fromJson(obj.getString("name"), String.class));
        tmpDiagram.setRevision(GSON.fromJson(obj.getString("revision"), Integer.class));
        tmpDiagram.setLocation(GSON.fromJson(obj.getString("location"), Point2D.class));
        
             
        List<String> test = GSON.fromJson(obj.getString("nodes"), List.class);
       
       // File file = new File("bla.txt");
//	FileUtils.writeStringToFile(file, GSON.toJson(test));
        
    
       
      test.forEach((node)->{
            tmpDiagram.addNodes(GSON.fromJson(node,ClassDiagramClass.class));
      });

   /*     test.forEach((node)-> {
             //mpDiagram.addNodes((ClassDiagramClass)node);
             //tmpDiagram.addNodes(GSON.fromJson(node,ClassDiagramClass.class));
             System.out.println("test");
        } );
        
           
        
        System.out.println(test);
        System.out.println(test.size());
        
        
        this.setDiagram(tmpDiagram);
        
       // Entity test = this.getDiagram().fromJSON(this.diagramString);
      // JSONObject obj = new JSONObject(this.diagramString);
      // System.out.println(obj.get("name"));
        //
        //UMLDiagram test = (ClassDiagram)GSON.fromJson(this.diagramString, UMLDiagramEntity.class);
        //this.setDiagram((UMLDiagramEntity)this.getDiagram().fromJSON(this.diagramString));*/
        
    }  

    public UMLDiagram addDiagramToProject(UMLDiagram d, ObjectId pid)
    {
        try{
            HashMap<String, String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());
            
            Connector con = new Connector();
            con.setHeader(hm);
            
            JSONObject obj = new JSONObject();
            obj.put("pid", GSON.toJson(pid));
            obj.put("diagram", d.toString());
            
            String responseString = con.put("/project/addDiagram", obj.toString());
            if(con.getStatus() == 200) {
                UMLDiagram diagram = ((UMLDiagram) new UMLDiagram().fromJSON(responseString));
                diagram.setBounds(new Rectangle(0,0,2000,1500));
                return diagram;
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            System.out.println("ERROR: "+ e.getMessage());
            return null;
        }
    }
    
    public UMLDiagram saveDiagram()
    {
       try {
            if(this.getSelectedObject()!=null)
            {
                this.reviseionBuffer = this.selectedObject.getRevision();
                this.getSelectedObject().setSelected(false);
            }
           
            HashMap<String, String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());
            
            Connector con = new Connector();
            con.setHeader(hm);
            
            System.out.println("ZU SPEI. DIAGRAMM"+this.getDiagram().toString());
            
            JSONObject obj = new JSONObject();
           // obj.put("pid", GSON.toJson(projectService.getSelected().getId()));
            obj.put("pid", GSON.toJson(projectService.getSelected().getId()));
            obj.put("diagram", this.getDiagram().toString());
  
            
            String responseString = con.put("/project/saveDiagram", obj.toString());
            System.out.println(responseString);
            
            this.setSelectedObject(null);
            
            if(con.getStatus() == 200) {    
                System.out.println("SPEICHERE DIAGRAMM "+responseString);
                return ((UMLDiagram) new UMLDiagram().fromJSON(responseString));
            }
            else {
                return null;
            }
        }
        catch(Exception e) {
            System.out.println("ERROR: "+e.getMessage());
            return null;
        }
    }
    
    public UMLDiagram getDiagramById(ObjectId id) {
        try {
            HashMap<String,String> hm = new HashMap<>();
            hm.put("Authorization-Token", userService.getCurrentUser().getToken());

            Connector con = new Connector();
            con.setHeader(hm);

            JSONObject obj = new JSONObject();
            obj.put("did", GSON.toJson(id));
            
            String responseString = con.post("/project/getDiagramById", obj.toString());
            if(con.getStatus() == 200)
                return ((UMLDiagram)new UMLDiagram().fromJSON(responseString));
            else
                return null;
        }
        catch(Exception e) {
            return null;
        }
    }
}
