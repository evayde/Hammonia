package ham.shared.editor.abstracts;

import ham.shared.editor.Helper.Point;
import ham.shared.editor.Helper.Rectangle;
import ham.shared.editor.classdiagram.ClassDiagramClass;
import ham.shared.editor.classdiagram.ClassDiagramEdge;
import ham.shared.editor.classdiagram.ClassDiagramNote;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import javafx.scene.canvas.GraphicsContext;
import org.bson.types.ObjectId;

/**
 *
 * @author Martin
 */
public abstract class UMLDiagram extends UMLObject {
    private ArrayList<ClassDiagramClass> nodes = new ArrayList<>();
    private ArrayList<ClassDiagramEdge> edges = new ArrayList<>();
    private ArrayList<ClassDiagramNote> notes = new ArrayList<>();
     private ArrayList<Message> messgaes = new ArrayList<>();
     private ArrayList<String> history = new ArrayList<>();
    private Rectangle bounds;
    private ObjectId pid;
    private boolean multiEdge = false;
    private boolean exercise = false;
    private LocalDate deadLine = null;

    public LocalDate getDeadLine()
    {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine)
    {
        this.deadLine = deadLine;
    }
    
    
    
    
    public ObjectId getPid()
    {
        return pid;
    }

    public void setPid(ObjectId pid)
    {
        this.pid = pid;
    }
    
    public Rectangle getBounds()
    {
        return bounds;
    }

    public void setBounds(Rectangle bounds)
    {
        this.bounds = bounds;
    }

    public void setNodes(ArrayList<ClassDiagramClass> nodes)
    {
        this.nodes = nodes;
    }

    public void setEdges(ArrayList<ClassDiagramEdge> edges)
    {
        this.edges = edges;
    }

    public boolean isExercise()
    {
        return exercise;
    }

    public void setExercise(boolean exercise)
    {
        this.exercise = exercise;
    }
    
    
    /**
     *
     * @return
     */
    public ArrayList<UMLNode> getNodes()
    {
        ArrayList<UMLNode> tmp = new ArrayList<UMLNode>();
        tmp.addAll(this.nodes);
        tmp.addAll(this.notes);
        return tmp;
    }

    public ArrayList<String> getHistory()
    {
        return this.history;
    }
    
    public void addHistoy(String s)
    {
        this.history.add(s);
    }
    
    /**
     *
     * @return
     */
    public ArrayList<ClassDiagramEdge> getEdges()
    {
        return edges;
    }

    /**
     *
     * @param nodes
     */
    public void addNodes(UMLNode... nodes) {
        for(UMLNode n : nodes) 
            this.addNode(n);
    }
 
    public void addEdges(ClassDiagramEdge... edges) {
        for(ClassDiagramEdge e : edges) 
            this.addEdge(e);
    }
    
    public void removeNode(UMLNode node)
    {          
        ArrayList<UMLEdge> list = new ArrayList<>();
        
        this.getEdges().forEach((e) -> {
            if(e.isConnected(node))
                list.add(e);
        });
                     
        for(UMLEdge e : list)
        {
            removeEdge(e);
        }

        if(node.getClass()==ClassDiagramClass.class)
            this.nodes.remove(node);
        else if (node.getClass()==ClassDiagramNote.class)
            this.notes.remove(node);
    }

   
    public void addMessage(Message m)
    {
        this.messgaes.add(m);
    }
    
    public ArrayList<Message> getMessages()
    {
        return this.messgaes;
    }
    
    public void removeEdge(UMLEdge edge)
    {
        this.getEdges().remove(edge); 
    }
    
    /**
     *
     * @param node
     */
    private void addNode(UMLNode node)
    {
        node.setRevision(this.getRevision());
        if(node.getClass()== ClassDiagramClass.class)
            this.nodes.add((ClassDiagramClass)node);
        else if(node.getClass()== ClassDiagramNote.class)
            this.notes.add((ClassDiagramNote)node);
        this.incrementRevision();
    }
    

    /**
     *
     * @param edge
     */
    public boolean addEdge(ClassDiagramEdge edge)
    {
        edge.setRevision(this.getRevision());
        
        int count = 1;
        int n1=0;
        int n2=0;
        for(UMLEdge e : getEdges())
        {
            if((e.getSource().getRevision()==edge.getSource().getRevision()&&e.getTarget().getRevision()==edge.getTarget().getRevision())||(e.getSource().getRevision()==edge.getTarget().getRevision()&&e.getTarget().getRevision()==edge.getSource().getRevision()))
            {
                if(count==1)
                    n1=e.getCountNumber();
                if(count==2)
                    n2=e.getCountNumber();
                count++;
            }
               
        }
        
        if(count>3)
            return false;
        
        if(count>1&&!this.multiEdge)
            return false;
        
        System.out.println("n1:"+n1+"n2"+n2);
        
        int n3;
        if((n1==1&&n2==2)||(n1==2&&n2==1))
            n3=3;
        else if((n1==2&&n2==3)||(n1==3&&n2==2))
            n3=1;
        else if((n1==3&&n2==1)||(n1==1&&n2==3))
            n3=2;
        else if(n1==1&&n2==0)
            n3=2;
        else if(n1==2&&n2==0)
            n3=3;
        else if(n1==3&&n2==0)
            n3=1;
        else
            n3=1;
                    
                    
        
        edge.setCountNumber(n3);
        this.edges.add(edge);
        this.incrementRevision();
        return true;
    }
    
    public void zoom(double value)
    {
        edges.forEach((e) -> {
            e.setLineWidth(e.getLineWidth()*value);
            e.setFontSize(e.getFontSize()*value);
        });
         
        nodes.forEach((n) -> {
            n.setLineWidth(n.getLineWidth()*value);
            n.setFontSize(n.getFontSize()*value);
            n.setLocation(new Point(n.getLocation().getX()*value,n.getLocation().getY()*value));
        });
        
        notes.forEach((n) -> {
            n.setLineWidth(n.getLineWidth()*value);
            n.setFontSize(n.getFontSize()*value);
            n.setLocation(new Point(n.getLocation().getX()*value,n.getLocation().getY()*value));
        });
        
        this.setBounds(new Rectangle(0,0,this.getBounds().getWidth()*value,this.getBounds().getHeight()*value));
    }
    
    public UMLNode getUMLNode(Point point)
    {
        for(UMLNode node : getNodes())
        {
            if(node.contains(point))
                return node;
        }
        return null;
    }
    
    public UMLEdge getUMLEdge(Point point)
    {
        for(UMLEdge edge : getEdges())
        {
            if(edge.contains(point))
                return edge;
        }
        return null;
    }
    
    public UMLDiagramObject getUMLDiagramObject(Point point)
    {
        UMLDiagramObject element = getUMLNode(point);
        if(element == null)
            element = getUMLEdge(point);          
        return element;
    }
    
    /**
     *
     * @param graphics
     */
    public void draw(GraphicsContext graphics)
    {
        
        for(UMLEdge edge : getEdges())
        {
            UMLNode source = edge.getSource();
            UMLNode target = edge.getTarget();
            
            for(UMLNode node : getNodes())
            {
                if(source.getRevision().equals(node.getRevision()))
                {
                    edge.setSource(node);
                }
                else if(target.getRevision().equals(node.getRevision()))
                {
                    edge.setTarget(node);
                }
            }  
        }
        
        
        
        graphics.clearRect(0, 0, 5000, 5000);
        
        getEdges().forEach((edge) -> {
            edge.draw(graphics);
        });    
            
        getNodes().forEach((node) -> {
            node.draw(graphics);
        });
    }
}