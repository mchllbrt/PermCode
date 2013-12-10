package permlib.property;

import java.util.Collection;
import java.util.HashSet;
import permlib.Permutation;
import permlib.classes.UniversalPermClass;

/**
 * This class represents the property of having length bounded by <code>lengthBound</code>.
 * 
 * @author Michael Albert
 */
public final class BoundedLength implements HereditaryProperty{
    
    private int lengthBound;
    
    /**
     * Constructs the property of being of bounded length.
     * 
     * @param lengthBound the length bound (inclusive).
     */
    public BoundedLength(int lengthBound) {
        this.lengthBound = lengthBound;
    }

    @Override
    public final boolean isSatisfiedBy(Permutation p) {
        return p.length() <= lengthBound;
    }

    @Override
    public final boolean isSatisfiedBy(int[] values) {
        return values.length <= lengthBound;
    }

    @Override
    public final Collection<Permutation> getBasis() {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for(Permutation p : new UniversalPermClass(lengthBound+1)) result.add(p);
        return result;
    }

    @Override
    public final Collection<Permutation> getBasisTo(int n) {
        if (n <= lengthBound) return new HashSet<Permutation>();
        return getBasis();
    }
    
}
