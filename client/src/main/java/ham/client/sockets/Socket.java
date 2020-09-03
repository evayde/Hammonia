package ham.client.sockets;

import ham.client.userManagement.UserService;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.engineio.client.Transport;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 *
 * @author Enrico Gruner
 */
public class Socket {
    private static final UserService userService = UserService.getInstance();
    private io.socket.client.Socket socket;
    private Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    
    public Socket() {
        
        try {
            IO.Options opt = new IO.Options();
            opt.forceNew = true; // if relogin
            this.socket = IO.socket(this.clientPref.get("uri", "http://127.0.0.1")+":"+ (Integer.parseInt(this.clientPref.get("port", "4567"))+1) , opt);
            
            this.socket.io()
            .on(Manager.EVENT_TRANSPORT, (Object ...args) -> {
                Transport t = (Transport)args[0];
                t.on(Transport.EVENT_REQUEST_HEADERS, (Object ...headerArgs) -> {
                    @SuppressWarnings("unchecked")
                    Map<String,List<String>> headers = (Map<String,List<String>>) headerArgs[0];
                    headers.put("AUTHORIZATION-TOKEN", Arrays.asList(userService.getCurrentUser().getToken()));
                });
            });

            this.registerSockets();            
            this.socket.connect();  
        }
        catch(Exception e) {
            System.out.println(e.getMessage());
        }  
    }
    
    private void registerSockets() {
        ProjectSocket.getInstance().registerListeners(this.socket);
    }
    
    public void disconnect() {
        this.socket.disconnect();
    }
    
}