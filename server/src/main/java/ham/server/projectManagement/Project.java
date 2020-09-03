package ham.server.projectManagement;

import ham.shared.projectManagement.ProjectEntity;
import java.util.ArrayList;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

/**
 * Project Entity
 * 
 * @author Enrico Gruner
 * @version 0.15
 */
@Entity("projects")
public class Project extends ProjectEntity {
    @Id
    private ObjectId id;
   
    public ObjectId getId() {
        return this.id;
    }
}