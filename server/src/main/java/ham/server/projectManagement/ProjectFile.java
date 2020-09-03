package ham.server.projectManagement;

import ham.shared.projectManagement.ProjectFileEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author Enrico Gruner
 */
@Entity("projectfile")
public class ProjectFile extends ProjectFileEntity {
    @Id
    private ObjectId id;

    public ObjectId getId()
    {
        return id;
    }
}
