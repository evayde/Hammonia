package ham.client.view;

import com.google.gson.Gson;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Martin
 */
public class ModalPartial implements Partial{
    private String name;
    private String url;
    private Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    private static final Gson GSON = new Gson();
    private ResourceBundle rb = null;
    
    public ModalPartial(String name, String url)
    {
        this.name = name;
        this.url = url;
        this.rb = ResourceBundle.getBundle("ham/client/i18n/MainView", GSON.fromJson(this.clientPref.get("locale", GSON.toJson(new Locale("de","DE"))), Locale.class));
    }
    
    public Node getNode()
    {
        return createNode();
    }
    
    private Node createNode()
    {
        Node node = null;
        try
        {
            node = getAnchorPane("/ham/client/view/BasicModalView.fxml");
            node.setId(name);
            
     
            VBox vbox = (VBox) ((GridPane) node).getChildren().get(0);
            Label l = (Label)vbox.getChildren().get(0);
            l.setText(this.rb.getString(this.name));

        } catch (Exception e)
        {
            System.out.println("ERROR: "+e.getMessage());
        }
        return node;
    }
    
    private Node getAnchorPane(String url) throws Exception
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url),this.rb);
        return loader.load();     
    }
}
