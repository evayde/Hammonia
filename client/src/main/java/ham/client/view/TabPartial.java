package ham.client.view;

import com.google.gson.Gson;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;

/**
 *
 * @author Martin
 */
public class TabPartial implements Partial {
    private Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    private static final Gson GSON = new Gson();
    private String name;
    private String url;

    public TabPartial(String name, String url)
    {
        this.name = name;
        this.url = url;
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
            node = getAnchorPane("/ham/client/view/BasicTabView.fxml");
            node.setId(name);
            ScrollPane scrollPane = this.getScrollPaneSideView((AnchorPane)node);
            
            scrollPane.setContent(getAnchorPane(this.url));
            AnchorPane pane2 = (AnchorPane)scrollPane.getContent();
            
            scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
                    pane2.setMinWidth(newVal.doubleValue()-15.0);
                });
            scrollPane.heightProperty().addListener( (obs,oldVal,newVal) -> {
                pane2.setMinHeight(newVal.doubleValue()-20.0);
            });
        } catch (Exception e)
        {
            System.out.println("ERROR: "+e.getMessage());
        }
        return node;
    }

    private Node getAnchorPane(String url) throws Exception
    {
        ResourceBundle rb = ResourceBundle.getBundle("ham/client/i18n/MainView", GSON.fromJson(this.clientPref.get("locale", GSON.toJson(new Locale("de","DE"))), Locale.class));
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url), rb);
        return loader.load();
    }
    
   private ScrollPane getScrollPaneSideView(AnchorPane pane)
   {
       return (ScrollPane)pane.getChildren().get(0);
   }
}
