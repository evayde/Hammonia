package ham.server.projectManagement;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author Enrico Gruner
 */
@Entity("umldiagram")
public class UMLDiagram extends ham.shared.editor.abstracts.UMLDiagram {
    @Id
    private ObjectId id;

    public ObjectId getId() {
        return this.id;
    }
    
    
}
