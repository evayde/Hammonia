package ham.client.projectManagement;

import ham.shared.projectManagement.ProjectFileEntity;
import org.bson.types.ObjectId;

/**
 *
 * @author Enrico Gruner
 */
public class ProjectFile extends ProjectFileEntity {
    private ObjectId id;

    public ObjectId getId()
    {
        return id;
    }
    
}
