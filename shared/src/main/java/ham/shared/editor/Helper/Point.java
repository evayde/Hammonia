package ham.shared.editor.Helper;

import javafx.geometry.Point2D;

/**
 * Point class to allow mongodb serialization of JavaFX Point2D.
 * 
 * @author Enrico Gruner
 */
public class Point extends Point2D {
    
    public Point() {
        super(0,0);
    }
    
    public Point(double x, double y) {
        super(x,y);
    }
}