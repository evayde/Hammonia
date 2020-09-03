package ham.shared.projectManagement;

import ham.shared.abstracts.Entity;
import java.util.ArrayList;
import org.bson.types.ObjectId;

/**
 *
 * @author Enrico Gruner
 */
public abstract class ProjectEntity extends Entity {
    private String name;
    private ObjectId creator;
    private ArrayList<ObjectId> students = new ArrayList<>();
    private ArrayList<ObjectId> docents = new ArrayList<>();
    private boolean isLearnproject; // defaults to false
    private ArrayList<ObjectId> files = new ArrayList<>();
    private ArrayList<ObjectId> diagrams = new ArrayList<>();

    public boolean isIsLearnproject()
    {
        return isLearnproject;
    }

    public void setIsLearnproject(boolean isLearnproject)
    {
        this.isLearnproject = isLearnproject;
    }

    public ArrayList<ObjectId> getDiagrams()
    {
        return diagrams;
    }

    public void setDiagrams(ArrayList<ObjectId> diagrams)
    {
        this.diagrams = diagrams;
    }
 
    public void addDiagram(ObjectId did) {
        this.diagrams.add(did);
    }
    
    public void removeDiagram(ObjectId did) {
        this.diagrams.remove(did);
    }
    
    public ArrayList<ObjectId> getFiles()
    {
        return files;
    }

    public void setFiles(ArrayList<ObjectId> file)
    {
        this.files = file;
    }
    
    public void addFile(ObjectId f) {
        this.files.add(f);
    }
    
    public void removeFile(ObjectId f) {
        this.files.remove(f);
    }
    /**
     *
     * @return User who created this project
     */
    public ObjectId getCreator()
    {
        return creator;
    }

    /**
     *
     * @param creator User who created this project
     */
    public void setCreator(ObjectId creator)
    {
        this.creator = creator;
    }
    
    /**
     * Returns true if User u created this project
     * 
     * @param u
     * @return
     */
    public boolean isCreator(ObjectId uid) {
        return this.creator.equals(uid);
    }

    /**
     * Returns project name
     * 
     * @return
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets project name (Unique)
     * 
     * @param name
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Get ArrayList of all participating students
     * 
     * @return
     */
    public ArrayList<ObjectId> getStudents()
    {
        return this.students;
    }

    /**
     * Set arraylist of participating students
     * 
     * @param students
     */
    public void setStudents(ArrayList<ObjectId> students)
    {
        this.students = students;
    }

    /**
     * Get ArrayList of all participating docents
     * 
     * @return
     */
    public ArrayList<ObjectId> getDocents()
    {
        return this.docents;
    }

    /**
     * set arraylist of participating docents
     * 
     * @param docents
     */
    public void setDocents(ArrayList<ObjectId> docents)
    {
        this.docents = docents;
    }
    
    /**
     * add a single student to the project
     * 
     * @param u
     * @return
     */
    public void addStudent(ObjectId u) {
        this.students.add(u);
    }
    
    /**
     * remove a single student from the project. Use setStudents to remove all
     * 
     * @param u
     * @return
     */
    public boolean removeStudent(ObjectId u) {
        if(this.students.contains(u)) {
            this.students.remove(u);
            return true;
        }
        return false;
    }
    
    /**
     * Returns true if User u is Student of this Project
     * 
     * @param u
     * @return
     */
    public boolean isStudent(ObjectId u) {
        return this.students.contains(u);
    }
    
    /**
     * Add a single docent to the project
     * 
     * @param u
     * @return
     */
    public void addDocent(ObjectId u) {
        this.docents.add(u);
    }
    
    /**
     * Remove a single docent from the project
     * 
     * @param u
     * @return
     */
    public boolean removeDocent(ObjectId u) {
        if(this.docents.contains(u)) {
            this.docents.remove(u);
            return true;
        }
        return false;
    }
    
    /**
     * Returns true if User u is docent
     * 
     * @param u
     * @return
     */
    public boolean isDocent(ObjectId u) {
        return this.docents.contains(u);
    }
}
