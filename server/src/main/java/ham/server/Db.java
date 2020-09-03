package ham.server;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 *
 * @author Enrico Gruner
 * @version 0.1
 */
public class Db {
    private final Morphia morphia = new Morphia();
    private final Datastore ds;
    
    /**
     * Connect to the datastore
     */
    public Db() {
        this.ds = this.morphia.createDatastore(new MongoClient(), "hammonia");
        this.ds.ensureIndexes();
    }
    
    /**
     * 
     * @return return the datastore
     */
    public Datastore getDatastore() {
        return this.ds;
    }
}
