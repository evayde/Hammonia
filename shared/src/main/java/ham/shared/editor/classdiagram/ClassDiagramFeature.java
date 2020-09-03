package ham.shared.editor.classdiagram;

/**
 *
 * @author Martin
 */
public abstract class ClassDiagramFeature {
    private String identifier;
    private String accessModifier;

    /**
     *
     * @return
     */
    public String getAccessModifier()
    {
        return accessModifier;
    }

    /**
     *
     * @param accessModifier
     */
    public void setAccessModifier(String accessModifier)
    {
        if(accessModifier.equals("+")||accessModifier.equals("-")||accessModifier.equals("#"))
            this.accessModifier = accessModifier;
        else
            this.accessModifier = "-";
    }
   
    /**
     *
     * @return
     */
    public String getIdentifier()
    {
        return identifier;
    }

    /**
     *
     * @param identifier
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }
    
    @Override
    public abstract String toString();
}
