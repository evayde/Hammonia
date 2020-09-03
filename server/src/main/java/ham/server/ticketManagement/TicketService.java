package ham.server.ticketManagement;

import com.google.gson.Gson;
import ham.server.Db;
import ham.server.ServiceAbstract;
import ham.server.userManagement.UserService;
import ham.shared.interfaces.Service;
import java.util.HashMap;
import java.util.List;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Enrico
 */
public class TicketService extends ServiceAbstract implements Service {
    private final Logger ticketServiceLogger = LoggerFactory.getLogger(TicketService.class);
    private static TicketService instance = null;
    final static Gson GSON = new Gson();
    private final Db db = new Db();
    
    private TicketService() {}
    
    public static TicketService getInstance() {
        if(instance == null)
            instance = new TicketService();
        return instance;
    }
    
    public HashMap<String,String> createTicket(Ticket t) {
        try {
            if(this.db.getDatastore().get(t.getProject()) == null) {
                this.setResult("404", "Project not found");
                return this.result;
            }
            
            this.db.getDatastore().save(t);
            this.setResult("200", this.db.getDatastore().get(t).toString());
        }
        catch(Exception e) {
            ticketServiceLogger.error("Could not create ticket");
            this.setResult("500", GSON.toJson(new Error("Could not create ticket")));
        }
        return this.result;
    }
    
    public HashMap<String,String> getProjectTickets(ObjectId p) {
        try {
            List<Ticket> tickets = this.db.getDatastore().find(Ticket.class).filter("project ==", p).asList();
            this.setResult("200", GSON.toJson(tickets));
        }
        catch(Exception e) {
            ticketServiceLogger.error("Couldnt retrieve tickets");
            this.setResult("500", GSON.toJson(new Error("Couldnt retrieve tickets.")));
        }
        return this.result;
    }
}