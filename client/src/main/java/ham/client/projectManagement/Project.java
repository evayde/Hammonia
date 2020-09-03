package ham.client.projectManagement;

import ham.shared.projectManagement.ProjectEntity;
import org.bson.types.ObjectId;

/**
 *
 * @author Enrico Gruner
 */
public class Project extends ProjectEntity {
    private ObjectId id;
    
    public ObjectId getId() {
        return this.id;
    }
}
