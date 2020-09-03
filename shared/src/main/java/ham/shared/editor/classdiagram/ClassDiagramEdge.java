package ham.shared.editor.classdiagram;

import ham.shared.editor.Helper.Vector;
import ham.shared.editor.abstracts.UMLEdge;
import ham.shared.editor.abstracts.UMLNode;
import static java.lang.Math.PI;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 *
 * @author Martin
 */
public class ClassDiagramEdge extends UMLEdge {

    private String sourceRole;
    private String targetRole;
    private String sourceMultiplicity;
    private String targetMultiplicity;
    private boolean sourceNavigable;
    private boolean targetNavigable;

    private boolean hiddenRole;
    private boolean hiddenMultiplicity;
    private boolean hiddenName;

    private double arrowHeigth;
    private double arrowWidth;

    
    
    private boolean dotted;

    private String typ;

    private Vector directionVec;

    public ClassDiagramEdge(ClassDiagramClass source, ClassDiagramClass target)
    {
        super(source, target);
        this.hiddenRole = true;
        this.hiddenMultiplicity = true;
        this.hiddenName = true;
        this.sourceNavigable = false;
        this.targetNavigable = false;
        this.dotted = false;
        
        this.sourceMultiplicity="1";
        this.targetMultiplicity="1";

        setBackgroundColor("white");
        setBorderColor("gray");
        setTextColor("lightgray");

        setFontFamily("Courier New");

        setLineWidth(1.5);

        this.setTyp("ASSOCIATION");
    }

    public ClassDiagramEdge()
    {
        this(null, null);
    }
 

    public String getTyp()
    {
        return typ;
    }

    public void setTyp(String typ)
    {
        if (typ.equals("AGGREGRATION") || typ.equals("ASSOCIATION") || typ.equals("COMPOSITION") || typ.equals("GENERALIZATION") || typ.equals("REALIZATION"))
        {
            this.typ = typ;
            if (!typ.equals("ASSOCIATION"))
            {
                setSourceNavigable(true);
                setTargetNavigable(false);
            }
            if (typ.equals("REALIZATION"))
            {
                this.setDotted(true);
            }
            else
            {
                this.setDotted(false);
            }
        }
        else
        {
            this.typ = "ASSOCIATION";
            this.setDotted(false);
        }
    }

    protected void setDotted(boolean b)
    {
        this.dotted = b;
    }

    public boolean isHiddenName()
    {
        return hiddenName;
    }

    public void setHiddenName(boolean hiddenName)
    {
        this.hiddenName = hiddenName;
    }

    public Vector getDirectionVec()
    {
        return directionVec;
    }

    public void setDirectionVec(Vector directionVec)
    {
        this.directionVec = directionVec;
    }

    public double getArrowHeigth()
    {
        return arrowHeigth;
    }

    public void setArrowHeigth(double arrowHeigth)
    {
        this.arrowHeigth = arrowHeigth;
    }

    public double getArrowWidth()
    {
        return arrowWidth;
    }

    public void setArrowWidth(double arrowWidth)
    {
        this.arrowWidth = arrowWidth;
    }

    public boolean isHiddenRole()
    {
        return hiddenRole;
    }

    public void setHiddenRole(boolean hiddenRole)
    {
        this.hiddenRole = hiddenRole;
    }

    public boolean isHiddenMultiplicity()
    {
        return hiddenMultiplicity;
    }

    public void setHiddenMultiplicity(boolean hiddenMultiplicity)
    {
        this.hiddenMultiplicity = hiddenMultiplicity;
    }

    public String getSourceRole()
    {
        return sourceRole;
    }

    public void setSourceRole(String sourceRole)
    {
        this.sourceRole = sourceRole;
    }

    public String getTargetRole()
    {
        return targetRole;
    }

    public void setTargetRole(String targetRole)
    {
        this.targetRole = targetRole;
    }

    public String getSourceMultiplicity()
    {
        return sourceMultiplicity;
    }

    public void setSourceMultiplicity(String sourceMultiplicity)
    {
        this.sourceMultiplicity = sourceMultiplicity;
    }

    public String getTargetMultiplicity()
    {
        return targetMultiplicity;
    }

    public void setTargetMultiplicity(String targetMultiplicity)
    {
        this.targetMultiplicity = targetMultiplicity;
    }

    public boolean isSourceNavigable()
    {
        return sourceNavigable;
    }

    public void setSourceNavigable(boolean sourceNavigable)
    {
        this.sourceNavigable = sourceNavigable;
    }

    public boolean isTargetNavigable()
    {
        return targetNavigable;
    }

    public void setTargetNavigable(boolean targetNavigable)
    {
        this.targetNavigable = targetNavigable;
    }

    @Override
    public String getToolTip()
    {
        return "ClassDiagramEdge";
    }

    @Override
    public void draw(GraphicsContext graphics)
    {
        if (isHidden())
        {
            return;
        }

        if (isSelected())
        {
            graphics.setStroke(Color.web(this.getSelectedColor1()));
            graphics.setFill(Color.web(this.getSelectedColor1()));
        }
        else if(this.getUserLock()!=null)
        {
            graphics.setStroke(Color.web(this.getSelectedColor2()));
            graphics.setFill(Color.web(this.getSelectedColor2()));
        }
        else
        {
            graphics.setStroke(Color.web(getBackgroundColor()));
            graphics.setFill(Color.web(getBackgroundColor()));
        }

        graphics.setFont(Font.font(getFontFamily(), getFontSize()));
        graphics.setLineWidth(getLineWidth());
        setSourcePoint(getSource().getConnectionPoint(getTarget(),this.getCountNumber()));
        setTargetPoint(getTarget().getConnectionPoint(getSource(),this.getCountNumber()));

        if ((getTargetPoint().getX() == 0 && getTargetPoint().getY() == 0) || (getSourcePoint().getX() == 0 && getSourcePoint().getY() == 0))
        {
            return;
        }

        Vector vec = (new Vector(getTargetPoint().getX() - getSourcePoint().getX(), getTargetPoint().getY() - getSourcePoint().getY()));
        setDirectionVec(new Vector(vec.normalize().x(), vec.normalize().y()));

        Vector vec2 = new Vector(getDirectionVec().rotate(PI * 0.5).x(), getDirectionVec().rotate(PI * 0.5).y());

        if (typ.equals("AGGREGRATION"))
        {
            setArrowHeigth(getLineWidth() * 12);
            setArrowWidth(getLineWidth() * 4);

            graphics.strokePolygon(new double[]
            {
                getSourcePoint().getX() + getDirectionVec().x() * getLineWidth(), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth() / 2) + (vec2.x() * -getArrowWidth())), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth())), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth() / 2) + (vec2.x() * getArrowWidth()))
            },
                    new double[]
                    {
                        getSourcePoint().getY() + getDirectionVec().y() * getLineWidth(), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth() / 2) + (vec2.y() * -getArrowWidth())), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth())), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth() / 2) + (vec2.y() * getArrowWidth()))
                    }, 4);
        }
        else if (typ.equals("ASSOCIATION"))
        {
            setArrowHeigth(getLineWidth() * 6);
            setArrowWidth(getLineWidth() * 2.5);

            if (isSourceNavigable() == true)
            {
                graphics.fillPolygon(new double[]
                {
                    getSourcePoint().getX(), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth()) + (vec2.x() * -getArrowWidth())), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth()) + (vec2.x() * getArrowWidth()))
                },
                        new double[]
                        {
                            getSourcePoint().getY(), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth()) + (vec2.y() * -getArrowWidth())), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth()) + (vec2.y() * getArrowWidth()))
                        }, 3);
            }
            if (isTargetNavigable() == true)
            {
                graphics.fillPolygon(new double[]
                {
                    getTargetPoint().getX(), getTargetPoint().getX() + (-(getDirectionVec().x() * getArrowHeigth()) + (vec2.x() * -getArrowWidth())), getTargetPoint().getX() + (-(getDirectionVec().x() * getArrowHeigth()) + (vec2.x() * getArrowWidth()))
                },
                        new double[]
                        {
                            getTargetPoint().getY(), getTargetPoint().getY() + (-(getDirectionVec().y() * getArrowHeigth()) + (vec2.y() * -getArrowWidth())), getTargetPoint().getY() + (-(getDirectionVec().y() * getArrowHeigth()) + (vec2.y() * getArrowWidth()))
                        }, 3);
            }
        }
        else if (typ.equals("COMPOSITION"))
        {
            setArrowHeigth(getLineWidth() * 12);
            setArrowWidth(getLineWidth() * 4);

            graphics.fillPolygon(new double[]
            {
                getSourcePoint().getX(), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth() / 2) + (vec2.x() * -getArrowWidth())), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth())), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth() / 2) + (vec2.x() * getArrowWidth()))
            },
                    new double[]
                    {
                        getSourcePoint().getY(), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth() / 2) + (vec2.y() * -getArrowWidth())), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth())), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth() / 2) + (vec2.y() * getArrowWidth()))
                    }, 4);
        }
        else if (typ.equals("GENERALIZATION"))
        {
            setArrowHeigth(getLineWidth() * 8);
            setArrowWidth(getLineWidth() * 4);

            graphics.strokePolygon(new double[]
            {
                getSourcePoint().getX() + getDirectionVec().x() * getLineWidth(), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth()) + (vec2.x() * -getArrowWidth())), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth()) + (vec2.x() * getArrowWidth()))
            },
                    new double[]
                    {
                        getSourcePoint().getY() + getDirectionVec().y() * getLineWidth(), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth()) + (vec2.y() * -getArrowWidth())), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth()) + (vec2.y() * getArrowWidth()))
                    }, 3);

        }
        else if (typ.equals("REALIZATION"))
        {
            setArrowHeigth(getLineWidth() * 6);
            setArrowWidth(getLineWidth() * 2.5);

            if (isSourceNavigable() == true)
            {            
                graphics.strokeLine(getSourcePoint().getX(), getSourcePoint().getY(), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth()) + (vec2.x() * -getArrowWidth())), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth()) + (vec2.y() * -getArrowWidth())));
                graphics.strokeLine(getSourcePoint().getX(), getSourcePoint().getY(), getSourcePoint().getX() + ((getDirectionVec().x() * getArrowHeigth()) + (vec2.x() * getArrowWidth())), getSourcePoint().getY() + ((getDirectionVec().y() * getArrowHeigth()) + (vec2.y() * getArrowWidth())));
            }
        }

        if (this.dotted)
        {
            double dotLength = 7;
            double vecLength = Math.sqrt(Math.pow(vec.x(), 2) + Math.pow(vec.y(), 2));
            for (double i = 0; i < vecLength; i = i + dotLength * 3)
            {
                graphics.strokeLine(getSourcePoint().getX() + i * getDirectionVec().x(), getSourcePoint().getY() + i * getDirectionVec().y(), getSourcePoint().getX() + i * getDirectionVec().x() + dotLength * getDirectionVec().x(), getSourcePoint().getY() + i * getDirectionVec().y() + dotLength * getDirectionVec().y());
            }
        }
        else
        {
            if (isSourceNavigable() && isTargetNavigable())
            {
                graphics.strokeLine(getSourcePoint().getX() + getDirectionVec().x() * getArrowHeigth(), getSourcePoint().getY() + getDirectionVec().y() * getArrowHeigth(), getSourcePoint().getX() + vec.x() - getDirectionVec().x() * getArrowHeigth(), getSourcePoint().getY() + vec.y() - getDirectionVec().y() * getArrowHeigth());
            }
            else if (isSourceNavigable())
            {
                graphics.strokeLine(getSourcePoint().getX() + getDirectionVec().x() * getArrowHeigth(), getSourcePoint().getY() + getDirectionVec().y() * getArrowHeigth(), getSourcePoint().getX() + vec.x(), getSourcePoint().getY() + vec.y());
            }
            else if (isTargetNavigable())
            {
                graphics.strokeLine(getSourcePoint().getX(), getSourcePoint().getY(), getSourcePoint().getX() + vec.x() - getDirectionVec().x() * getArrowHeigth(), getSourcePoint().getY() + vec.y() - getDirectionVec().y() * getArrowHeigth());
            }
            else
            {
                graphics.strokeLine(getSourcePoint().getX(), getSourcePoint().getY(), getSourcePoint().getX() + vec.x(), getSourcePoint().getY() + vec.y());
            }

        }

        graphics.setFill(Color.web(getTextColor()));
        if (!isHiddenName())
        {
            graphics.fillText(getName(), (getSourcePoint().getX() + vec.x() * 0.5) - (getName().length() * (getLetterWidth() / 2)), (getSourcePoint().getY() + vec.y() * 0.5) - (getLetterHeight() / 2));
        }

        if (!isHiddenMultiplicity())
        {
            graphics.fillText(getSourceMultiplicity(), (getSourcePoint().getX() + vec.x() * 0.2) + 10, (getSourcePoint().getY() + vec.y() * 0.2) - (getLetterHeight() / 2));
            graphics.fillText(getTargetMultiplicity(), (getSourcePoint().getX() + vec.x() * 0.8) + 10, (getSourcePoint().getY() + vec.y() * 0.8) - (getLetterHeight() / 2));
        }

        if (!isHiddenRole())
        {
            graphics.fillText(getSourceRole(), (getSourcePoint().getX() + vec.x() * 0.2) - (getSourceRole().length() * (getLetterWidth() / 2)), (getSourcePoint().getY() + vec.y() * 0.2) - (getLetterHeight() * 2));
            graphics.fillText(getTargetRole(), (getSourcePoint().getX() + vec.x() * 0.8) - (getTargetRole().length() * (getLetterWidth() / 2)), (getSourcePoint().getY() + vec.y() * 0.8) - (getLetterHeight() * 2));
        }

    }
}
