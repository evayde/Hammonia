package ham.server.userManagement;

import ham.shared.userManagement.UserEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

/**
 * The User entity.
 * 
 * @author Enrico Gruner
 * @version 0.15
 */

@Entity("users")
public class User extends UserEntity {
    @Id
    private ObjectId id; // Assigned by MongoDb

    public ObjectId getId()
    {
        return id;
    }
}