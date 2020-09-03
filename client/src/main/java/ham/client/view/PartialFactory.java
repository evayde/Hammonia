package ham.client.view;

import javafx.scene.Node;

/**
 *
 * @author Martin
 */
public class PartialFactory {
    
    public PartialFactory() 
    {
        
    }
    
    public Node getPartial(String destiny, String name, String url)
    {
        Partial partial;
        
        if(destiny.equals("TAB")) 
        {
            partial = new TabPartial(name , url);
        }
        else if(destiny.equals("MODAL")) 
        {
            partial = new ModalPartial(name, url);
        }
        else 
        {
            partial = new SideViewPartial(name, url);
        }
        
        return partial.getNode();
    }
}
