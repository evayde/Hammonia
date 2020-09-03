package ham.shared.editor.abstracts;

import ham.shared.editor.Helper.Point;
import ham.shared.editor.Helper.Rectangle;
import javafx.scene.canvas.GraphicsContext;

/**
 *
 * @author Martin
 */
public class UMLNode extends UMLDiagramObject {  
    private final double CONEC_POINTS = 0.01;

    private double minWidth = 10;
    private double minHeight = 10;
    private double width = 10;
    private double height = 10;
    
    /**
     *
     * @return
     */
    public double getMinWidth()
    {
        return minWidth;
    }

    /**
     *
     * @param minWidth
     */
    public void setMinWidth(double minWidth)
    {
        if (0 > minWidth)
        {
            throw new IllegalArgumentException("min width can only be a positive number");
        }
        this.minWidth = minWidth;
        if (minWidth > getWidth())
        {
            setWidth(minWidth);
        }
    }

    /**
     *
     * @return
     */
    public double getMinHeight()
    {
        return minHeight;
    }

    /**
     *
     * @param minHeight
     */
    public void setMinHeight(double minHeight)
    {
        if (0 > minHeight)
        {
            throw new IllegalArgumentException("min height can only be a positive number");
        }
        this.minHeight = minHeight;
        if (minHeight > getHeight())
        {
            setHeight(minHeight);
        }
    }

    /**
     *
     * @return
     */
    public double getWidth()
    {
        return width;
    }

    /**
     *
     * @param width
     */
    public void setWidth(double width)
    {
        if (0 > width)
        {
            throw new IllegalArgumentException("width can only be a positive number");
        }
        if (getMinWidth() > width)
        {
            throw new IllegalArgumentException("width can only be heigher than minWidth");
        }
        this.width = width;
    }

    /**
     *
     * @return
     */
    public double getHeight()
    {
        return height;
    }

    /**
     *
     * @param height
     */
    public void setHeight(double height)
    {
        if (0 > height)
        {
            throw new IllegalArgumentException("height can only be a positive number");
        }
        if (getMinHeight() > height)
        {
            throw new IllegalArgumentException("height can only be heigher than minHeight");
        }
        this.height = height;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getToolTip()
    {
        return "UMLNode";
    }
    
    /**
     *
     * @param point
     * @return
     */
    @Override
     public boolean contains(Point point)
    {
        return getBounds().contains(point);
    }

    /**
     *
     * @return
     */
    public final Rectangle getBounds()
    {
        return new Rectangle(getLocation().getX(), getLocation().getY(), getWidth(), getHeight());
    }
    
    public final Point getCenterPoint()
    {
        return new Point(getLocation().getX()+getWidth()/2,getLocation().getY()+getHeight()/2);
    }
    
    /**
     *
     * @return
     */
    public final Rectangle getMinimalBounds()
    {
        return new Rectangle(getLocation().getX(), getLocation().getY(), getMinWidth(), getMinHeight());
    }
    
    public final Point getConnectionPoint(UMLNode target,int edgeCount)
    {
        int helper = 25;
        edgeCount--;
        edgeCount=edgeCount*helper;
        //Rechts
        if(this.getLocation().getX()+this.getWidth()<target.getLocation().getX()+5)
        {
                //Nebeneinander
                 for(double i = 0;i<0.5;i=i+CONEC_POINTS)
                {
                    double count = 0.5+i;
                    if(this.getLocation().getY()+this.getHeight()*count>target.getLocation().getY()&&this.getLocation().getY()+this.getHeight()*count<target.getLocation().getY()+target.getHeight())
                    {
                        return new Point(this.getLocation().getX()+this.getWidth(),this.getLocation().getY()+this.getHeight()*count-(this.getHeight()*CONEC_POINTS*edgeCount));
                    }
                    count = 0.5-i;
                    if(this.getLocation().getY()+this.getHeight()*count>target.getLocation().getY()&&this.getLocation().getY()+this.getHeight()*count<target.getLocation().getY()+target.getHeight())
                    {
                        return new Point(this.getLocation().getX()+this.getWidth(),this.getLocation().getY()+this.getHeight()*count+(this.getHeight()*CONEC_POINTS*edgeCount));
                    }
                        
                    
                }             
                //rechtsoben
                if(this.getLocation().getY()>target.getLocation().getY()+target.getHeight())
                {        
                    //mehr Rechts
                    if(target.getLocation().getX()-(this.getLocation().getX()+this.getWidth())>this.getLocation().getY()-(target.getLocation().getY()+target.getHeight()))
                    {
                        
                        return new Point(this.getLocation().getX()+this.getWidth(),this.getLocation().getY()+this.getHeight()*CONEC_POINTS+(this.getHeight()*CONEC_POINTS*edgeCount));
                    }
                    //mehr Oben
                    else
                    {//edgeCount = helper*2-edgeCount;
                        return new Point(this.getLocation().getX()+this.getWidth()-CONEC_POINTS*this.getWidth()-(CONEC_POINTS*this.getWidth()*edgeCount),this.getLocation().getY());
                    }
                }
                //rechtsunten
                else if(this.getLocation().getY()+this.getHeight()<target.getLocation().getY())
                {
                    //mehr Rechts
                    if(target.getLocation().getX()-(this.getLocation().getX()+this.getWidth())>target.getLocation().getY()-(this.getLocation().getY()+this.getHeight()))
                    {//edgeCount = helper*2-edgeCount;
                        return new Point(this.getLocation().getX()+this.getWidth(),this.getLocation().getY()+this.getHeight()-this.getHeight()*CONEC_POINTS-(this.getHeight()*CONEC_POINTS*edgeCount));
                    }
                    //mehr unten
                    else
                        return new Point(this.getLocation().getX()+this.getWidth()-CONEC_POINTS*this.getWidth()-(CONEC_POINTS*this.getWidth()*edgeCount),this.getLocation().getY()+this.getHeight());
                }    
      }
      //Links
      else if(this.getLocation().getX()+5>target.getLocation().getX()+target.getWidth())
      {   
                //Nebeneinander
                for(double i = 0;i<0.5;i=i+CONEC_POINTS)
                {
                    double count = 0.5+i;
                    if(this.getLocation().getY()+this.getHeight()*count>target.getLocation().getY()&&this.getLocation().getY()+this.getHeight()*count<target.getLocation().getY()+target.getHeight())
                    {
                        //if(i!=0)
                            //edgeCount = helper*2-edgeCount;
                        return new Point(this.getLocation().getX(),this.getLocation().getY()+this.getHeight()*count-(this.getHeight()*CONEC_POINTS*edgeCount));
                    }
                    count = 0.5-i;
                    if(this.getLocation().getY()+this.getHeight()*count>target.getLocation().getY()&&this.getLocation().getY()+this.getHeight()*count<target.getLocation().getY()+target.getHeight())
                    {
                        //edgeCount = helper*2-edgeCount;
                        return new Point(this.getLocation().getX(),this.getLocation().getY()+this.getHeight()*count+(this.getHeight()*CONEC_POINTS*edgeCount));
                    }
                    
                }
                //linksoben
                if(this.getLocation().getY()>target.getLocation().getY()+target.getHeight())
                {
                    //mehr links
                     if(this.getLocation().getX()-(target.getLocation().getX()+target.getWidth())>this.getLocation().getY()-(target.getLocation().getY()+target.getHeight()))
                          return new Point(this.getLocation().getX(),this.getLocation().getY()+this.getHeight()*CONEC_POINTS+(this.getHeight()*CONEC_POINTS*edgeCount));
                    //mehr Oben
                    else
                     {//edgeCount = helper*2-edgeCount;
                        return new Point(this.getLocation().getX()+CONEC_POINTS*this.getWidth()+(CONEC_POINTS*this.getWidth()*edgeCount),this.getLocation().getY());
                     }
                }
                //linksunten
                else if(this.getLocation().getY()+this.getHeight()<target.getLocation().getY())
                {
                    //mehr links
                    if(this.getLocation().getX()-(target.getLocation().getX()+target.getWidth())>target.getLocation().getY()-(this.getLocation().getY()+this.getHeight()))
                    {
                        //edgeCount = helper*2-edgeCount;
                        return new Point(this.getLocation().getX(),this.getLocation().getY()+this.getHeight()-this.getHeight()*CONEC_POINTS-(this.getHeight()*CONEC_POINTS*edgeCount));
                    }
                    //mehr unten
                    else
                        return new Point(this.getLocation().getX()+CONEC_POINTS*this.getWidth()+(CONEC_POINTS*this.getWidth()*edgeCount),this.getLocation().getY()+this.getHeight());
                }         
      }
      //oben
      else if(this.getLocation().getY()>target.getLocation().getY()+target.getHeight())
      {
           for(double i = 0;i<0.5;i=i+CONEC_POINTS)
           {
              double count = 0.5+i;
              if(this.getLocation().getX()+this.getWidth()*count>target.getLocation().getX()&&this.getLocation().getX()+this.getWidth()*count<target.getLocation().getX()+target.getWidth())
              {
                  if(i==0)
                      i=CONEC_POINTS;
                  else if(i==1)
                      i=1-CONEC_POINTS;
                  
                   
                  return new Point(this.getLocation().getX()+this.getWidth()*count-(this.getWidth()*CONEC_POINTS*edgeCount),this.getLocation().getY());
              }
               count = 0.5-i;
              if(this.getLocation().getX()+this.getWidth()*count>target.getLocation().getX()&&this.getLocation().getX()+this.getWidth()*count<target.getLocation().getX()+target.getWidth())
              {
                  if(i==0)
                      i=CONEC_POINTS;
                  else if(i==1)
                      i=1-CONEC_POINTS;
                  
                  return new Point(this.getLocation().getX()+this.getWidth()*count+(this.getWidth()*CONEC_POINTS*edgeCount),this.getLocation().getY());
              }
          } 
      }
      //unten
      else if(this.getLocation().getY()+this.getHeight()<target.getLocation().getY())
      {
          for(double i = 0;i<0.5;i=i+CONEC_POINTS)
           {
              double count = 0.5+i;
              if(this.getLocation().getX()+this.getWidth()*count>target.getLocation().getX()&&this.getLocation().getX()+this.getWidth()*count<target.getLocation().getX()+target.getWidth())
              {
                
                  if(i==0)
                      i=CONEC_POINTS;
                  else if(i==1)
                      i=1-CONEC_POINTS;
                  
                  return new Point(this.getLocation().getX()+this.getWidth()*count-(this.getWidth()*CONEC_POINTS*edgeCount),this.getLocation().getY()+this.getHeight());
              }
              count = 0.5-i;
              if(this.getLocation().getX()+this.getWidth()*count>target.getLocation().getX()&&this.getLocation().getX()+this.getWidth()*count<target.getLocation().getX()+target.getWidth())
              {
                  if(i==0)
                      i=CONEC_POINTS;
                  else if(i==1)
                      i=1-CONEC_POINTS;
                  
                  return new Point(this.getLocation().getX()+this.getWidth()*count+(this.getWidth()*CONEC_POINTS*edgeCount),this.getLocation().getY()+this.getHeight());
              }
          }
      }    
      
      return new Point(0,0);
    }

    @Override
    public void draw(GraphicsContext graphics)
    {
        
    }
}
