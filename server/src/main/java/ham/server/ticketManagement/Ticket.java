package ham.server.ticketManagement;

import ham.shared.ticketManagement.TicketEntity;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author Enrico
 */
@Entity("ticket")
public class Ticket extends TicketEntity {
    @Id
    private ObjectId id; // Assigned by MongoDb

    public ObjectId getId()
    {
        return id;
    }
}
