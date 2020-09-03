package ham.shared.userManagement;

import ham.shared.abstracts.Entity;
import java.util.ArrayList;
import org.bson.types.ObjectId;

/**
 *
 * @author Enrico Gruner
 */
public abstract class UserEntity extends Entity {
    private String name;
    private String email;
    private String password;
    private boolean admin = false;
    private ArrayList<ObjectId> projects = new ArrayList<>();
    private String token;
    
    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    /**
     *
     * @return
     */
    public ArrayList<ObjectId> getProjects()
    {
        return projects;
    }

    /**
     *
     * @param projects
     */
    public void setProjects(ArrayList<ObjectId> projects)
    {
        this.projects = projects;
    }
    
    /**
     *
     * @param pname
     * @param role
     * @return
     */
    public boolean addProject(ObjectId pid) {
        if(this.projects.contains(pid))
            return false;
        
        this.projects.add(pid);
        return true;
    }
    
    /**
     *
     * @param pname
     * @return
     */
    public boolean removeProject(ObjectId pid) {
        if(!this.projects.contains(pid))
            return false;
        
        this.projects.remove(pid);
        return true;
    }

    /**
     *
     * @return true if User is admin
     */
    public boolean isAdmin()
    {
        return admin;
    }

    /**
     * Set Admin Status of User. True means User is admin.
     * 
     * @param admin
     */
    public void setAdmin(boolean admin)
    {
        this.admin = admin;
    }

    /**
     *
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public String getEmail()
    {
        return email;
    }

    /**
     * 
     * @param email
     */
    public void setEmail(String email)
    {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getPassword()
    {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }
}
