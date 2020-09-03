package ham.client;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author Enrico Gruner
 */
public class MainApp extends Application {
    @Override
    public void start(Stage stage) 
    {
        try 
        {
            WorkSpace.getInstance().setUp(stage);
        }
        catch(Exception e) 
        {
            System.out.println("ERROR starting the server "+e.getMessage());
        }
    }
    
    @Override
    public void stop() 
    {
        WorkSpace.getInstance().tearDown();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
