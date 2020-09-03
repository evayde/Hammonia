package ham.shared.editor.abstracts;

import ham.shared.editor.Helper.Point;
import javafx.scene.canvas.GraphicsContext;
import org.bson.types.ObjectId;

/**
 *
 * @author Martin
 */
public abstract class UMLDiagramObject extends UMLObject {
    private String backgroundColor = "yellow";
    private String borderColor = "red";
    private String textColor = "red";
    private String fontFamily = "Courier New";
    
    private String selectedColor1="orange";
    private String selectedColor2="red";
    
    private ObjectId userLock = null;
    
    private double fontSize = 12;
    private double letterWidth = 7.3;
    private double letterHeight = 9.8;
    private boolean hidden = false;
    private double lineWidth = 1;
    private boolean selected = false;

    public ObjectId getUserLock()
    {
        return userLock;
    }

    public void setUserLock(ObjectId userLock)
    {
        this.userLock = userLock;
    }
   
    
    public String getSelectedColor1()
    {
        return selectedColor1;
    }

    public void setSelectedColor1(String selectedColor1)
    {
        this.selectedColor1 = selectedColor1;
    }

    public String getSelectedColor2()
    {
        return selectedColor2;
    }

    public void setSelectedColor2(String selectedColor2)
    {
        this.selectedColor2 = selectedColor2;
    }

    
    
    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

    public double getLetterWidth()
    {
        return letterWidth;
    }

    public void setLetterWidth(double letterWidth)
    {
        this.letterWidth = letterWidth;
    }

    public double getLetterHeight()
    {
        return letterHeight;
    }

    public void setLetterHeight(double letterHeight)
    {
        this.letterHeight = letterHeight;
    }

    public double getLineWidth()
    {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth)
    {
        this.lineWidth = lineWidth;
    }
    
    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden(boolean hidden)
    {
        this.hidden = hidden;
    }
    
    public String getFontFamily()
    {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily)
    {
        this.fontFamily = fontFamily;
    }

    public double getFontSize()
    {
        return fontSize;
    }

    public void setFontSize(double fontSize)
    {
        double tmp = fontSize/this.fontSize;
        setLetterWidth(getLetterWidth()*tmp);
        setLetterHeight(getLetterHeight()*tmp);
        this.fontSize = fontSize;      
    }
    
    /**
     *
     * @return
     */
    public String getBackgroundColor()
    {
        return this.backgroundColor;
    }

    /**
     *
     * @param backgroundColor
     */
    public void setBackgroundColor(String backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    /**
     *
     * @return
     */
    public String getBorderColor()
    {
        return borderColor;
    }

    /**
     *
     * @param borderColor
     */
    public void setBorderColor(String borderColor)
    {
        this.borderColor = borderColor;
    }

    /**
     *
     * @return
     */
    public String getTextColor()
    {
        return textColor;
    }

    /**
     *
     * @param textColor
     */
    public void setTextColor(String textColor)
    {
        this.textColor = textColor;
    }
    
    /**
     *
     * @return
     */
    public String getToolTip()
    {
        return "DiagramObject";
    }
    
    /**
     *
     * @param aPoint
     * @return
     */
    public abstract boolean contains(Point aPoint);
    
    /**
     *
     * @param graphics
     */
    @Override
    public abstract void draw(GraphicsContext graphics);
}
