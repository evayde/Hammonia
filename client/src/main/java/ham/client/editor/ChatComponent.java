package ham.client.editor;

import com.google.gson.Gson;
import ham.client.WorkSpace;
import ham.client.userManagement.UserService;
import ham.shared.editor.abstracts.Message;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Martin
 */
public class ChatComponent implements Initializable {

    private static final WorkSpace workSpace = WorkSpace.getInstance();
    private static final EditorService editorService = EditorService.getInstance();
    private Preferences clientPref = Preferences.userNodeForPackage(ham.client.MainApp.class);
    private static final UserService userService = UserService.getInstance();
    private static final Gson GSON = new Gson();
    ResourceBundle rb;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        editorService.renderCall.addListener((obs, oldSelection, newSelection) ->
        {
            loadUI();
        });

        this.rb = ResourceBundle.getBundle("ham/client/i18n/MainView", GSON.fromJson(this.clientPref.get("locale", GSON.toJson(new Locale("de", "DE"))), Locale.class));
        loadUI();
    }

    @FXML
    private TextField headLineTextField;

    @FXML
    private TextArea contenTextField;

    @FXML
    private VBox ticketVBox;

    @FXML
    void createAction(ActionEvent event)
    {
        if (contenTextField.getText().equals("") || headLineTextField.getText().equals(""))
        {
            return;
        }

        Message m = new Message();
        m.setAuthor(userService.getCurrentUser().getName());
        m.setContent(contenTextField.getText());
        m.setName(headLineTextField.getText());
        m.setAuthorID(userService.getCurrentUser().getId());

        editorService.getDiagram().addMessage(m);
        
        editorService.getDiagram().addHistoy(userService.getCurrentUser().getName()+": neue Nachricht verfasst");
        
        editorService.saveDiagram();
    }

    private void loadUI()
    {
        if (editorService.getDiagram() == null)
        {
            return;
        }

        ticketVBox.getChildren().clear();

        ArrayList<Message> tmp = editorService.getDiagram().getMessages();

        for (int i = tmp.size() - 1; i >= 0; i--)
        {
            // System.out.println(i);
            Label label = new Label();
            label.setText(rb.getString("author") + ":\n" + tmp.get(i).getAuthor() + "\n--------------------\n" + rb.getString("headline") + ":\n" + tmp.get(i).getName() + "\n--------------------\n" + rb.getString("content") + ":\n" + tmp.get(i).getContent());
            ticketVBox.getChildren().add(label);
            ticketVBox.getChildren().add(new Separator());
        }
    }

}
