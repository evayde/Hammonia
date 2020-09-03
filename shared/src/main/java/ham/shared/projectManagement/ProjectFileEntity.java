package ham.shared.projectManagement;

import ham.shared.abstracts.Entity;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 *
 * @author Enrico
 */
public class ProjectFileEntity extends Entity{
    private String name;
    private String content;
    private String type;
    private Date time;
    private ObjectId pid;

    public ObjectId getPid()
    {
        return pid;
    }

    public void setPid(ObjectId pid)
    {
        this.pid = pid;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Date getTime()
    {
        return time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }
}
