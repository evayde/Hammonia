package ham.shared.editor.classdiagram;

/**
 *
 * @author Martin
 */
public class ClassDiagramProperty extends ClassDiagramFeature {
    private String type;
    
    /**
     *
     */
    public ClassDiagramProperty() {}
    
    /**
     *
     * @param accessModifier
     * @param type
     * @param identifier
     */
    public ClassDiagramProperty(String accessModifier, String type, String identifier) {
        this.setProperty(accessModifier, type, identifier);
    }

    /**
     *
     * @return
     */
    public String getType()
    {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type)
    {
        this.type = type;
    }
    
    private void setProperty(String accessModifier, String identifier,String type) {
        this.setType(type);
        this.setAccessModifier(accessModifier);
        this.setIdentifier(identifier);
    }
    
    @Override
    public String toString()
    {
        return getAccessModifier()+" "+getIdentifier()+" : "+getType();
    }
}
