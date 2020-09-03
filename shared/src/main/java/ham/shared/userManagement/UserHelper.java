package ham.shared.userManagement;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;
import java.util.UUID;

/**
 *
 * @author Enrico Gruner
 */
public class UserHelper {
    public static String getToken() {
        return UUID.randomUUID().toString();
    }
    
    public static String[] credentialsFromBase64(String authentication) {
        String credentials = StringUtils.newStringUtf8(Base64.decodeBase64(authentication.replace("Basic ", "")));
        return credentials.split(":");
    }
    
    public static String credentialsToBase64(String email, String password) {
        String credentialsString = email+":"+password;
        byte[] encodedCredentials = Base64.encodeBase64(credentialsString.getBytes());
        
        return new String(encodedCredentials);
    }
}