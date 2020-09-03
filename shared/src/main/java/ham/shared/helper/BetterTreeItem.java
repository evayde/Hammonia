package ham.shared.helper;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import org.bson.types.ObjectId;

/**
 *
 * @author Enrico
 */
public class BetterTreeItem<T> extends TreeItem {
    private ObjectId itemId;
    private String itemType;
    
    public ObjectId getItemId()
    {
        return itemId;
    }

    public void setItemId(ObjectId itemId)
    {
        this.itemId = itemId;
    }

    public String getItemType()
    {
        return itemType;
    }

    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }
    
    public BetterTreeItem() {
        super(null);
    }

    public BetterTreeItem(final T value) {
        super(value, (Node)null);
    }
    
    public BetterTreeItem(final T value, final Node graphic) {
        super(value, graphic);
    }
}