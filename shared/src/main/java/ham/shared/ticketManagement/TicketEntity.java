package ham.shared.ticketManagement;

import ham.shared.abstracts.Entity;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author Enrico
 */
public abstract class TicketEntity extends Entity {
    private String name;
    private String content;
    
    private Date date;
    
    private ObjectId issuer;
    private ObjectId project;
    private ObjectId diagram;

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public ObjectId getDiagram()
    {
        return diagram;
    }

    public void setDiagram(ObjectId diagram)
    {
        this.diagram = diagram;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public ObjectId getIssuer()
    {
        return issuer;
    }

    public void setIssuer(ObjectId issuer)
    {
        this.issuer = issuer;
    }

    public ObjectId getProject()
    {
        return project;
    }

    public void setProject(ObjectId project)
    {
        this.project = project;
    }
    
    
}
