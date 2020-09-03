
package ham.shared.editor.classdiagram;

import ham.shared.editor.abstracts.UMLNode;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 *
 * @author Martin
 */
public class ClassDiagramNote extends UMLNode{
    private final double PADDINGX = 10;
    private final double PADDINGY = 5;
    
    private ArrayList<String> lines = new ArrayList<>();
    
    private double gap;
    
    public ClassDiagramNote(){      
        setName("Note");

        this.gap = 4;
        setLineWidth(1);

        setBackgroundColor("white");
        setBorderColor("gray");
        setTextColor("black");

        setFontSize(12);
        setFontFamily("Courier New");
        
        this.calculateBound();
    
    }

    public ArrayList<String> getLines()
    {
        return lines;
    }

    public void setLines(ArrayList<String> lines)
    {
        this.lines = lines;
        calculateBound();
    }
    
    
    
    @Override
    public void setFontSize(double size)
    {
        super.setFontSize(size);
        calculateBound();   
    }
    
    @Override
    public String getToolTip()
    {
        return "Note";
    }
    
     public void calculateBound()
    {
        
        setMinHeight(PADDINGY*3 + (getLineWidth()*2) +(lines.size()*(getLetterHeight()+gap)));
        setHeight(PADDINGY*3 + (getLineWidth()*2)  +(lines.size()*(getLetterHeight()+gap)));
        
        int maxSize=0;
        for(String strg : lines)
        {
            if(strg.length()>maxSize)
                maxSize=strg.length();
        }
               
        setMinWidth(maxSize * getLetterWidth() + PADDINGX * 2);
        setWidth(maxSize * getLetterWidth() + PADDINGX * 2);
    }
     
     
     @Override
    public void draw(GraphicsContext graphics)
    {
        if (isHidden())
            return;
        
        double x = getLocation().getX();
        double y = getLocation().getY();
        double width = getWidth();
        double height = getHeight();
        double space = getLetterHeight() + gap;

        double count = 1;

        //Setup 
        if(isSelected()==true)
            graphics.setStroke(Color.web(this.getSelectedColor1()));
        else if(this.getUserLock()!=null)
            graphics.setStroke(Color.web(this.getSelectedColor2()));
        else
            graphics.setStroke(Color.web(getBorderColor()));
        
        graphics.setFill(Color.web(getBackgroundColor()));
        graphics.setLineWidth(getLineWidth());

        
        graphics.fillPolygon(new double[]
            {
                x,x,x+width,x+width,x+(width*0.9)
            },
                    new double[]
                    {
                        y,y+height,y+height,y+0.1*height,y
                    }, 5);
        
        
        
          graphics.strokePolygon(new double[]
            {
                x,x,x+width,x+width,x+(width*0.9)
            },
                    new double[]
                    {
                        y,y+height,y+height,y+0.1*height,y
                    }, 5);
        
        //Setup 
        graphics.setFill(Color.web(getTextColor()));
        graphics.setFont(Font.font(getFontFamily(), getFontSize()));

        //Lines
        for (String line : lines)
        {
            graphics.fillText(line, x + PADDINGX, y + PADDINGY + count * space);
            count++;
        }  
    }
    
}
