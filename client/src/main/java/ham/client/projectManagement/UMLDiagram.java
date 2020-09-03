package ham.client.projectManagement;

import org.bson.types.ObjectId;
/**
 *
 * @author Enrico Gruner
 */
public class UMLDiagram extends ham.shared.editor.abstracts.UMLDiagram {
    private ObjectId id;

    public ObjectId getId() {
        return this.id;
    }
}
