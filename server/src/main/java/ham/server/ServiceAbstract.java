package ham.server;

import java.util.HashMap;

/**
 * Providing response interface
 * 
 * @author Enrico Gruner
 * @version 0.3
 */
public abstract class ServiceAbstract {
    protected HashMap<String, String> result = new HashMap<>();
    
    protected HashMap<String, String> getResult() {
        return this.result;
    }
    
    protected void setResult(String code, String payload) {
        this.result.put("status", code);
        this.result.put("payload", payload);
    }
}
