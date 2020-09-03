package ham.shared.editor.abstracts;

import ham.shared.editor.Helper.Point;
import ham.shared.editor.Helper.Rectangle;
import ham.shared.editor.classdiagram.ClassDiagramClass;
import javafx.scene.canvas.GraphicsContext;
import math.geom2d.Vector2D;

/**
 *
 * @author Martin
 */
public class UMLEdge extends UMLDiagramObject {
    private UMLNode source;
    private UMLNode target;
    private int countNumber;
    private Point sourcePoint;
    private Point targetPoint;
    
    public UMLEdge(UMLNode source, UMLNode target)
    {
        this.source = source;
        this.target = target;
        this.countNumber=1;
    }
    
     public UMLEdge()
    {
        this(null,null);
    }

    public int getCountNumber()
    {
        return countNumber;
    }

    public void setCountNumber(int countNumber)
    {
        this.countNumber = countNumber;
    }

     
     
    public Point getSourcePoint()
    {
        return sourcePoint;
    }

    public void setSourcePoint(Point sourcePoint)
    {
        this.sourcePoint = sourcePoint;
    }

    public Point getTargetPoint()
    {
        return targetPoint;
    }

    public void setTargetPoint(Point targetPoint)
    {
        this.targetPoint = targetPoint;
    }
    

    public UMLNode getSource()
    {
        return source;
    }

    public void setSource(UMLNode source)
    {
        this.source = source;
    }

    public UMLNode getTarget()
    {
        return target;
    }

    public void setTarget(UMLNode target)
    {
        this.target = target;
    }

    public boolean isConnected(UMLNode node)
    {
        return (source.equals(node)||target.equals(node));
    }
    
    @Override
    public String getToolTip()
    {
        return "UMLEdge";
    }
    
    @Override
    public boolean contains(Point point)
    {
        Vector2D vec = (new Vector2D(getTargetPoint().getX()-getSourcePoint().getX(),getTargetPoint().getY()-getSourcePoint().getY())); 
        Rectangle rect = new Rectangle((getSourcePoint().getX()+(vec.x()*0.5))-50,(getSourcePoint().getY()+(vec.y()*0.5))-50,100,100);
        Rectangle rect2 = new Rectangle(getSourcePoint().getX()-10,getSourcePoint().getY()-10,20,20);
        Rectangle rect3 = new Rectangle(getTargetPoint().getX()-10,getTargetPoint().getY()-10,20,20);
        
        if(rect.contains(point)||rect2.contains(point)||rect3.contains(point))
            return true;
        else
            return false;
    }

    @Override
    public void draw(GraphicsContext graphics) {}
}
