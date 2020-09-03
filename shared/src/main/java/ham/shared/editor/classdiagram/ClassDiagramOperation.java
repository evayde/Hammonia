package ham.shared.editor.classdiagram;

import java.util.ArrayList;

/**
 *
 * @author Martin
 */
public class ClassDiagramOperation extends ClassDiagramFeature{
    private String returnType;
    private ArrayList<String> parameterList = new ArrayList<String>();
    private boolean abstractMethod;

    public ClassDiagramOperation(String accessModifier, String identifier , ArrayList<String> parameterList, String returnType, boolean abstractMethod)
    {
        this.setReturnType(returnType);
        this.setAccessModifier(accessModifier);
        this.setIdentifier(identifier);
        this.setParameterList(parameterList);
        this.setAbstractMethod(abstractMethod);
    }
    
    public ClassDiagramOperation()
    {
        
    }
    /**
     *
     * @return
     */
    public boolean isAbstractMethod()
    {
        return abstractMethod;
    }

    /**
     *
     * @param abstractMethod
     */
    public void setAbstractMethod(boolean abstractMethod)
    {
        this.abstractMethod = abstractMethod;
    }

    /**
     *
     * @return
     */
    public String getReturnType()
    {
        return returnType;
    }

    /**
     *
     * @param returnType
     */
    public void setReturnType(String returnType)
    {
        this.returnType = returnType;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getParameterList()
    {
        return parameterList;
    }

    /**
     *
     * @param parameterList
     */
    public void setParameterList(ArrayList<String> parameterList)
    {
        this.parameterList = parameterList;
    }
    
    @Override
    public String toString()
    {
        String tmp = "";
        int count = 0;
        
        for(String parameter : parameterList)
        {
            count++;
            tmp = tmp + parameter; 
            if(parameterList.size()>count)
                tmp = tmp +","; 
        }
        
        
        return getAccessModifier()+" "+getIdentifier()+"("+tmp+") : "+getReturnType();
    }
  
}
