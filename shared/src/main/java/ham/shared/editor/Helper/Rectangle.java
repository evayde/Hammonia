package ham.shared.editor.Helper;

import javafx.geometry.Rectangle2D;

/**
 *
 * @author Enrico Gruner
 */
public class Rectangle extends Rectangle2D {

    public Rectangle(double minX, double minY, double width, double height) {
        super(minX, minY, width, height);
    }
    
    public Rectangle() {
        super(0,0,0,0);
    }
    
    public boolean contains(Point p) {
        if (p == null) return false;
        return super.contains(p.getX(), p.getY());
    }
}
