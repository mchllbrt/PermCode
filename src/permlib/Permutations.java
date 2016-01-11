package permlib;

import permlib.classes.PermClassInterface;
import permlib.classes.UniversalPermClass;
import java.util.Iterator;
import permlib.property.PermProperty;

/**
 * This class represents abstractly the collection of all permutations of some
 * given lengths, possibly lying in a given class, and/or with certain properties.
 * The main 
 * reason for doing this is to provide an
 * iterator over all permutations of those lengths so that code such as: <p>
 * <code> for(Permutation p : new Permutations(3,10)) {...}</code> <p> can be
 * used.
 *
 * @author Michael Albert
 */
public class Permutations implements Iterable<Permutation> {

    private PermClassInterface c;
    private int low;
    private int high;
    private PermProperty prop;
   
     /**
     * Create new collection of permutations.
     * 
     * @param c permutation class
     * @param low lower length bound
     * @param high upper length bound
     */
    public Permutations(PermClassInterface c, int low, int high) {
        this(c, low, high, null);
    }
    
     /**
     * Create new collection of permutations.
     * 
     * @param c permutation class
     * @param length length of permutation
     * @param prop property of to impose on class
     */
    public Permutations(PermClassInterface c, int length, PermProperty prop) {
        this(c, length, length, prop);
    }
    
     /**
     * Create new collection of permutations.
     * 
     * @param c permutation class
     * @param length length of permutation
     */
    public Permutations(PermClassInterface c, int length) {
        this(c, length, length);
    }
    
     /**
     * Create new collection of permutations.
     * 
     * @param low lower length bound
     * @param high upper length bound
     * @param prop property of to impose on class
     */
    public Permutations(int low, int high, PermProperty prop) {
        this(new UniversalPermClass(high), low, high, prop);
    }
    
     /**
     * Create new collection of permutations.
     * 
     * @param low lower length bound
     * @param high upper length bound
     */
    public Permutations(int low, int high) {
        this(new UniversalPermClass(high), low, high);
    }
    
     /**
     * Create new collection of permutations.
     * 
     * @param length length of permutation
     * @param prop property of to impose on class
     */
    public Permutations(int length, PermProperty prop) {
        this(new UniversalPermClass(length), length, length, prop);
    }
    
     /**
     * Create new collection of permutations.
     * 
     * @param length length of permutation
     */
    public Permutations(int length) {
        this(new UniversalPermClass(length), length);
    }

    /**
     * Create new collection of permutations.
     * 
     * @param c permutation class
     * @param low lower length bound
     * @param high upper length bound
     * @param prop property of to impose on class
     */
    public Permutations(PermClassInterface c, int low, int high, PermProperty prop) {
        this.c = c;
        this.low = low;
        this.high =  high;
        this.prop = prop;
    }
    
    /**
     * Returns the iterator for this class.
     * 
     * @return the iterator for this class
     */
    @Override
    public Iterator<Permutation> iterator() {
        if(prop == null) {
            return c.getIterator(low, high);
        }
        return c.getRestrictedIterator(low, high, prop);
    } //End of Iterator
}
