package permlib.processor;

import java.util.HashSet;
import permlib.Permutation;
import permlib.property.PermProperty;
import permlib.property.Universal;

/**
 * A processor that collects all the permutations it receives into a set.
 * An optional {@link PermProperty property} limits the
 * collector to only include permutations that satisfy the property.
 * 
 * @author Michael Albert
 */
public class PermCollector implements PermProcessor {

    private HashSet<Permutation> collection = new HashSet<Permutation>();
    private PermProperty property;
    
    /** 
     * Constructor that produces a collector which collects all the
     * permutations it receives.
     * 
     */
    public PermCollector() {
        this(new Universal());
    };
    
    /**
     * Constructor that produces a collector which collects all the
     * permutations that satisfy the given property.
     * 
     * @param property the property
     */
    public PermCollector(PermProperty property) {
        this.property = property;
    }
    
    /**
     * Adds the permutation to the collection if it satisfies the property.
     * @param p the permutation
     */
    @Override
    public void process(Permutation p) {
        if (property.isSatisfiedBy(p)) collection.add(p);
    }

    /**
     * Empties the collection. Note that a new collection is created rather than
     * simply clearing the existing one. This is because external references to
     * the collection may exist created via getCollection().
     */
    @Override
    public void reset() {
        collection = new HashSet<Permutation>();
    }
    
    /**
     * Returns the collection.
     * 
     * @return the collection
     */
    public HashSet<Permutation> getCollection() {
        return collection;
    }

    @Override
    public String report() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
