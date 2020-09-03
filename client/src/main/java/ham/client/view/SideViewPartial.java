package ham.client.view;

import com.google.gson.Gson;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

/**
 *
 * @author Martin
 */
public class SideViewPartial implements Partial {
    private Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    private static final Gson GSON = new Gson();
    private String name;
    private String url;
    private ResourceBundle rb;

    public SideViewPartial(String name, String url)
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
            node = getAnchorPane("/ham/client/view/BasicSideView.fxml");
            node.setId(name);
            ScrollPane scrollPane = getScrollPaneSideView((AnchorPane)node);
            scrollPane.setContent(getAnchorPane(this.url));
            AnchorPane pane2 = (AnchorPane)scrollPane.getContent();
            
            scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
                    pane2.setMinWidth(newVal.doubleValue()-15.0);
                });
            scrollPane.heightProperty().addListener((obs,oldVal,newVal) -> {
                pane2.setMinHeight(newVal.doubleValue()-15.0);
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
        this.rb = rb;
        FXMLLoader loader = new FXMLLoader(getClass().getResource(url), rb);
        return loader.load();     
    }
    
    
   private ScrollPane getScrollPaneSideView(AnchorPane pane)
   {
       GridPane gridPane = (GridPane)pane.getChildren().get(0);
       AnchorPane ap = (AnchorPane)gridPane.getChildren().get(0);
       Label l = (Label)ap.getChildren().get(0);
       l.setText(this.rb.getString(this.name+".title"));
       return (ScrollPane)gridPane.getChildren().get(1);
   }
}
