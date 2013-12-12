package permlib.classes;

import java.util.Collection;
import java.util.Iterator;
import permlib.Permutation;
import permlib.processor.PermProcessor;
import permlib.property.PermProperty;

/**
 * An interface for classes, i.e. collections of permutations. The intended
 * meaning of each of the methods is documented here.
 * 
 * @author Michael Albert
 */
public interface PermClassInterface {
    
    public static final int MAXIMUM_STORED_LENGTH = 8;
    
    /**
     * Process the permutations of a given length in the class using a processor.
     * 
     * @param length the length
     * @param proc the processor
     */
    public void processPerms(int length, PermProcessor proc);
    
    /**
     * Compute a collection of the permutations in the class of a given length.
     * 
     * @param length the length
     * @return the collection
     */
    public Collection<Permutation> getPerms(int length);
    
    /**
     * Compute a collection of the permutations in the class up to a given length.
     * 
     * @param length the length
     * @return the collection
     */
    public Collection<Permutation> getPermsTo(int length);
    
    /**
     * Determine if the class contains a permutation.
     * 
     * @param q the permutation
     * @return <code>true</code> if the class contains the permutation
     * 
     */
    public boolean containsPermutation(Permutation q);
    
    /**
     * Provide an iterator for the permutations in the class lying in a range
     * of lengths.
     * 
     * @param low the lower endpoint of the range
     * @param high the upper endpoint of the range
     * @return the iterator
     */
    public Iterator<Permutation> getIterator(int low, int high);
    
    /**
     * Provide an iterator for the permutations in the class lying in a range
     * of lengths, satisfying some additional property.
     * 
     * @param low the lower endpoint of the range
     * @param high the upper endpoint of the range
     * @param prop the property
     * @return the iterator
     */
    public Iterator<Permutation> getRestrictedIterator(int low, int high, 
            PermProperty prop);
}
