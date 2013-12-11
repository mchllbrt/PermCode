package permlib.property;

import java.util.Collection;
import java.util.HashSet;
import permlib.classes.PermutationClass;
import permlib.Permutation;
import permlib.processor.PermCollector;

/**
 * Represents the class of permutations having no more inversions than a given
 * bound.
 * 
 * @author Michael Albert
 */
public final class BoundedInversions implements HereditaryProperty {
    
    private int bound;
    
    /**
     * Constructor sets the bound.
     * 
     * @param bound the bound for the number of inversions (inclusive).
     */
    public BoundedInversions(int bound) {
        this.bound = bound;
    }

    @Override
    public final boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p.elements);
    }

    @Override
    public final boolean isSatisfiedBy(int[] values) {
        int inversions = 0;
        for(int i = 0; i < values.length; i++) {
            for(int j = i+1; j < values.length; j++) {
                if (values[i] > values[j]) inversions++;
                if (inversions > bound) return false;
            }
        }
        return true;
    }

    @Override
    public Collection<Permutation> getBasis() {
        return getBasisTo(2*bound+2);
    }

    @Override
    public Collection<Permutation> getBasisTo(int n) {
        PermProperty bad = new Complement(this);
        HashSet<Permutation> result = new HashSet<Permutation>();
        int k = 0;
        while (k*(k-1)/2 <= bound) k++;
        PermutationClass c = new PermutationClass(result);
        PermCollector collector = new PermCollector(bad);
        for(int j = k; j <= n; j++) {
            c.processPerms(j, collector);
            result.addAll(collector.getCollection());
            collector.reset();
            c = new PermutationClass(result);
        }
        return result;
    }
}
