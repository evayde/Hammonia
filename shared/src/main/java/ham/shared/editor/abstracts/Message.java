
package ham.shared.editor.abstracts;

import org.bson.types.ObjectId;

/**
 *
 * @author Martin
 */
public class Message {
    private String name;
    private String content;
    private String author;
    
    private ObjectId authorID;
    
    public Message()
    {
        
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

    public String getAuthor()
    {
        return author;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public ObjectId getAuthorID()
    {
        return authorID;
    }

    public void setAuthorID(ObjectId authorID)
    {
        this.authorID = authorID;
    }
    
    
}
