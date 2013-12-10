package permlib.classes;

import java.util.Collection;
import java.util.Iterator;
import permlib.Permutation;
import permlib.property.PermProperty;

/**
 * An interface for classes, i.e. collections of permutations.
 * 
 * @author Michael Albert
 */
public interface PermClassInterface {
    
    public static final int MAXIMUM_STORED_LENGTH = 8;
    
    public void processPerms(int length, permlib.processor.PermProcessor proc);
    public Collection<Permutation> getPerms(int length);
    public Collection<Permutation> getPermsTo(int length);
    public boolean containsPermutation(Permutation q);
    public Iterator<Permutation> getIterator(int low, int high);
    public Iterator<Permutation> getRestrictedIterator(int low, int high, 
            PermProperty prop);
}
