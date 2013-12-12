package permlib.classes;

import java.util.Collection;
import java.util.Iterator;
import permlib.Permutation;
import permlib.processor.PermCollector;
import permlib.processor.PermProcessor;
import permlib.processor.RestrictedPermProcessor;
import permlib.property.HereditaryProperty;
import permlib.property.Intersection;
import permlib.property.PermProperty;

/**
 * Represents a permutation class as specified by a hereditary property. A
 * backing class is constructed by computing the basis up to a fixed length
 * and further processing is done by filtering the elements from the backing
 * class using the property.
 * 
 * @author Michael Albert
 */
public class HereditaryPermClass implements PermClassInterface {
    
    private PermutationClass basicClass;
    private HereditaryProperty property;
    private Collection<Permutation> basis;
    
    public HereditaryPermClass(HereditaryProperty property) {
        this.property = property;
        basis = property.getBasisTo(PermClassInterface.MAXIMUM_STORED_LENGTH);
        basicClass = new PermutationClass(basis);
    }
    
    @Override
    public PermClassInterface clone() {
        return new HereditaryPermClass(property);
    }
    
    @Override
    public void processPerms(int length, final PermProcessor proc) {
        basicClass.processPerms(length, new RestrictedPermProcessor(proc, property));
    }

    @Override
    public Collection<Permutation> getPerms(int length) {
        PermCollector collector = new PermCollector(property);
        basicClass.processPerms(length, collector);
        return collector.getCollection();       
    }

    @Override
    public Collection<Permutation> getPermsTo(int length) {
        PermCollector collector = new PermCollector(property);
        for(int i = 1; i <= length; i++) {
            basicClass.processPerms(i, collector);
        }
        return collector.getCollection();
    }

    @Override
    public boolean containsPermutation(Permutation q) {
        return property.isSatisfiedBy(q);
    }

    @Override
    public Iterator<Permutation> getIterator(int low, int high) {
        return basicClass.getRestrictedIterator(low, high, property);
    }

    @Override
    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {
        return basicClass.getRestrictedIterator(low, high, new Intersection(property, prop));
    }
}
