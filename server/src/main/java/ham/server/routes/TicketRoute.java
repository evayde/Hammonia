package ham.server.routes;

import com.google.gson.Gson;
import ham.server.projectManagement.ProjectService;
import ham.server.ticketManagement.Ticket;
import ham.server.ticketManagement.TicketService;
import ham.server.userManagement.UserService;
import java.util.HashMap;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 *
 * @author Enrico
 */
public class TicketRoute {
    final static Gson GSON = new Gson();
    final static UserService userService = UserService.getInstance();
    final static ProjectService projectService = ProjectService.getInstance();
    final static TicketService ticketService = TicketService.getInstance();
    
    final static Logger ticketLogger = LoggerFactory.getLogger(TicketRoute.class);
    
    public static Route create = (Request req, Response res) -> 
    {
        Ticket t = (Ticket) new Ticket().fromJSON(req.body());
        HashMap<String,String> result = ticketService.createTicket(t);
 
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");  
    };
    
    public static Route getProjectTickets = (Request req, Response res) ->
    {
        JSONObject obj = new JSONObject(req.body());
        ObjectId pid = GSON.fromJson( obj.getString("pid") , ObjectId.class);
        
        HashMap<String, String> result = ticketService.getProjectTickets(pid);
        
        res.status(Integer.parseInt(result.get("status")));
        return result.get("payload");
    };
}