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
public class ClassDiagramClass extends UMLNode {
    private final double PADDINGX = 10;
    private final double PADDINGY = 5;
  
    private ArrayList<ClassDiagramOperation> classOperations = new ArrayList<>();
    private ArrayList<ClassDiagramProperty> classProperties = new ArrayList<>();
    private String typ;
    
    private double gap;


    public ClassDiagramClass()
    {
        setTyp("NORMAL");
        setName("Class");

        this.gap = 4;
        setLineWidth(1);

        setBackgroundColor("white");
        setBorderColor("gray");
        setTextColor("black");

        setFontSize(12);
        setFontFamily("Courier New");     
    }
    
    @Override
    public void setFontSize(double size)
    {
        super.setFontSize(size);
        calculateBound();   
    }
    
    @Override
    public void setLineWidth(double lineWidth)
    {
        super.setLineWidth(lineWidth);
        calculateBound();
    }
    public void calculateBound()
    {
        int help = 1;
        if(getTyp().equals("INTERFACE"))
            help++;
        setMinHeight(PADDINGY + (getLineWidth()*2) + 3.7 * (gap + getLetterHeight())+((classOperations.size()+classProperties.size()+help)*(getLetterHeight()+gap)));
        setHeight(PADDINGY + (getLineWidth()*2) + 3.7 * (gap + getLetterHeight())+((classOperations.size()+classProperties.size()+help)*(getLetterHeight()+gap)));
        
        int maxSize=0;
        for(ClassDiagramOperation op : classOperations)
        {
            if(op.toString().length()>maxSize)
                maxSize=op.toString().length();
        }
        for(ClassDiagramProperty pro : classProperties)
        {
            if(pro.toString().length()>maxSize)
                maxSize=pro.toString().length();
        }
        if(getName().length()>maxSize)
            maxSize=getName().length();
        if(getTyp().equals("INTERFACE")&&maxSize<13)
            maxSize=13;
        
        setMinWidth(maxSize * getLetterWidth() + PADDINGX * 2);
        setWidth(maxSize * getLetterWidth() + PADDINGX * 2);
    }

    public String getTyp()
    {
        return typ;
    }

    public void setTyp(String typ)
    {
        if (typ.equals("NORMAL") || typ.equals("INTERFACE") || typ.equals("ABSTRACT"))
        {
            //Mehrfach setType(INTERFACE) abfangen, wechesl auf normal wieder verkleinern
          /*  if (typ.equals("INTERFACE"))
            {
                editBound("<<interface>>");
            }*/
            this.typ = typ;
          
        }
        else
        {
            this.typ = "NORMAL";
        }
        calculateBound();
    }

    /**
     *
     * @return
     */
    public ArrayList<ClassDiagramOperation> getClassOperations()
    {
        return classOperations;
    }

    /*
     *
     * @return
     */
    public ArrayList<ClassDiagramProperty> getClassProperties()
    {
        return classProperties;
    }

    /**
     *
     * @param classDiagramProperties
     */
    public void addClassProperties(ClassDiagramProperty... classDiagramProperties)
    {
        for (ClassDiagramProperty prop : classDiagramProperties)
        {
            this.addClassProperty(prop);
        }
    }

    /**
     *
     * @param classDiagramProperties
     */
    public void addClassOperations(ClassDiagramOperation... classDiagramOperation)
    {
        for (ClassDiagramOperation prop : classDiagramOperation)
        {
            this.addClassOperation(prop);
        }
    }

    /**
     *
     * @param classDiagramOperation
     */
    private void addClassOperation(ClassDiagramOperation classDiagramOperation)
    {
        for (ClassDiagramOperation operation : getClassOperations())
        {
            if (operation.getIdentifier().equals(classDiagramOperation.getIdentifier()) && operation.getParameterList().equals(classDiagramOperation.getParameterList()))
            {
                return;
            }
        }
        this.classOperations.add(classDiagramOperation);
        calculateBound();
    }

    /**
     *
     * @param classDiagramProperty
     */
    private void addClassProperty(ClassDiagramProperty classDiagramProperty)
    {
        for (ClassDiagramProperty property : getClassProperties())
        {
            if (property.getIdentifier().equals(classDiagramProperty.getIdentifier()))
            {
                return;
            }
        }
        this.classProperties.add(classDiagramProperty);
        calculateBound();
    }

    @Override
    public String getToolTip()
    {
        return "Class";
    }

    @Override
    public void setName(String name)
    {
        super.setName(name);
        calculateBound();
    }

    private void editBound(String text)
    {
        if (getMinWidth() < text.length() * getLetterWidth() + PADDINGX * 2)
        {
            setMinWidth(text.length() * getLetterWidth() + PADDINGX * 2);
        }
        setMinHeight(getMinHeight() + getLetterHeight() + gap);

    }

    /**
     *
     * @param graphics
     */
    @Override
    public void draw(GraphicsContext graphics)
    {
        if (isHidden())
        {
            return;
        }

        double x = getLocation().getX();
        double y = getLocation().getY();
        double width = getWidth();
        double height = getHeight();
        double space = getLetterHeight() + gap;
        double seperator_gap = (getLineWidth() / 3);

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

        //Background
        graphics.fillRect(x, y, width, height);

        //Border
        graphics.strokeRect(x, y, width, height);

        //Setup 
        graphics.setFill(Color.web(getTextColor()));
        graphics.setFont(Font.font(getFontFamily(), getFontSize()));

        //Interface text
        if (getTyp().equals("INTERFACE"))
        {
            graphics.fillText("<<interface>>", x + PADDINGX, y + PADDINGY + count * space);
            count++;
        }

        //Headline
        if (getTyp().equals("ABSTRACT") || getTyp().equals("INTERFACE"))
            graphics.setFont(Font.font(getFontFamily(), FontWeight.BOLD, FontPosture.ITALIC, getFontSize()));
        else
            graphics.setFont(Font.font(getFontFamily(), FontWeight.BOLD, getFontSize()));
        
        graphics.fillText(getName(), x + PADDINGX, y + PADDINGY + count * space);   
        graphics.setFont(Font.font(getFontFamily(), getFontSize()));

        //First Seperator
        count = count + 0.7;
        graphics.strokeRect(x+1, y + PADDINGY + count * space + seperator_gap * 2, width-2, getLineWidth() / 3);
        count = count + 0.5;
        count++;

        //ClassProperties
        for (ClassDiagramProperty property : getClassProperties())
        {
            graphics.fillText(property.toString(), x + PADDINGX, y + PADDINGY + count * space + seperator_gap * 2);
            count++;
        }

        //Secound Seperator
        graphics.strokeRect(x+1, y + PADDINGY + count * space + seperator_gap * 3, width-2, getLineWidth() / 3);
        count = count + 1.2;

        //ClassOperations
        for (ClassDiagramOperation operation : getClassOperations())
        {
            if (operation.isAbstractMethod() == true)
            {
                graphics.setFont(Font.font(getFontFamily(), FontPosture.ITALIC, getFontSize()));
            }
            graphics.fillText(operation.toString(), x + PADDINGX, y + PADDINGY + count * space + seperator_gap * 4);
            if (operation.isAbstractMethod() == true)
            {
                graphics.setFont(Font.font(getFontFamily(), getFontSize()));
            }
            count++;
        }
    }
}
