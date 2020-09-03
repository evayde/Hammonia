package ham.client;

import java.io.IOException;
import java.util.HashMap;
import java.util.prefs.Preferences;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Enrico Gruner
 */
public class Connector {
    private HashMap<String,String> hm = new HashMap<>();
    private int status = 0;
    private CloseableHttpClient httpclient = HttpClients.createDefault();
    private Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    
    private String getResponse(HttpRequestBase h) throws IOException {
        hm.forEach(
            (key,val)-> h.addHeader(key,val)
        );
        
       try (CloseableHttpResponse response = this.httpclient.execute(h)) {
            this.status = response.getStatusLine().getStatusCode();
            
            HttpEntity entity = response.getEntity();
            String userJSON = EntityUtils.toString(entity);
            
            response.close();
            
            return userJSON;
        } 
    }
    
    public void setHeader(HashMap<String,String> hm) {
        this.hm = hm;
    }
    
    public int getStatus() {
        return this.status;
    }
    
    public String post(String path) throws Exception {
        HttpPost httpPost = new HttpPost(this.clientPref.get("uri", "http://127.0.0.1")+":"+this.clientPref.get("port", "4567")+path);
        return this.getResponse(httpPost);
    }
    
    public String post(String path, String json) throws Exception {
        HttpPost httpPost = new HttpPost(this.clientPref.get("uri", "http://127.0.0.1")+":"+this.clientPref.get("port", "4567")+path);
        StringEntity requestEntity = new StringEntity(json,"UTF-8");
        httpPost.setEntity(requestEntity);
        return this.getResponse(httpPost);
    }
    
    public String get(String path) throws Exception {
        HttpGet httpGet = new HttpGet(this.clientPref.get("uri", "http://127.0.0.1")+":"+this.clientPref.get("port", "4567")+path);
        return this.getResponse(httpGet);
    }
    
    public String put(String path) throws Exception {
        HttpPut httpPut = new HttpPut(this.clientPref.get("uri", "http://127.0.0.1")+":"+this.clientPref.get("port", "4567")+path);
        return this.getResponse(httpPut);
    }
    
    public String put(String path, String json) throws Exception {
        HttpPut httpPut = new HttpPut(this.clientPref.get("uri", "http://127.0.0.1")+":"+this.clientPref.get("port", "4567")+path);
        StringEntity requestEntity = new StringEntity(json,"UTF-8");
        httpPut.setEntity(requestEntity);
        return this.getResponse(httpPut);
    }
}
