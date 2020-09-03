package ham.shared.editor.abstracts;

import ham.shared.abstracts.Entity;
import ham.shared.editor.Helper.Point;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Martin
 */
public abstract class UMLObject extends Entity {
    private String name;
    private Integer revision;
    private Point location;

    /**
     *
     */
    public UMLObject()
    {
        this.name = "Unnamed";
        this.revision = 0;
        this.location = new Point(0, 0);
    }

    /**
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public Integer getRevision()
    {
        return revision;
    }

    /**
     *
     * @param revision
     */
    public void setRevision(Integer revision)
    {
        if(null == revision)
        {
            throw new NullPointerException("newRevisionNumber can't be null");
        }
        if(0 > revision)
        {
            throw new IllegalArgumentException("newRevisionNumber can't be negative number");
        }
        this.revision = revision;
    }

    /**
     *
     * @return
     */
    public Point getLocation()
    {
        return location;
    }

    /**
     *
     * @param location
     */
    public void setLocation(Point location)
    {
         if (null == location) {
            throw new NullPointerException("Location can't be null");
        }
        this.location = location;
    }

    /**
     *
     */
    public void incrementRevision()
    {
        ++this.revision;
    }
    
    /**
     *
     * @param graphics
     */
    public abstract void draw(GraphicsContext graphics);
}
