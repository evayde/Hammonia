package ham.shared.abstracts;


import com.google.gson.Gson;

/**
 *
 * @author Enrico Gruner
 */
public abstract class Entity {
    final static Gson GSON = new Gson();
    
    @Override
    public String toString() {
        return GSON.toJson(this);
    }
    
    public Entity fromJSON(String json) {
        return GSON.fromJson(json, this.getClass());
    }
}
