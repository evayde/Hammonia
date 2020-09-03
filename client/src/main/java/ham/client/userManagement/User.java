package ham.client.userManagement;

import ham.shared.userManagement.UserEntity;
import org.bson.types.ObjectId;

/**
 *
 * @author Martin
 */
public class User extends UserEntity{
    private ObjectId id;

    public ObjectId getId()
    {
        return id;
    }
}
