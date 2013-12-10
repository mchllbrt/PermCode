package permlib.property;

import permlib.PermUtilities;
import permlib.Permutation;

/**
 * The property of being simple. 
 * 
 * @author Michael Albert
 */
public class Simple implements PermProperty {
    
    private int[] mins = new int[PermUtilities.MAX_PERM_SIZE];
    private int[] maxs = new int[PermUtilities.MAX_PERM_SIZE];
    
    public Simple() {}

    /**
     * Determines whether a permutation is simple.
     * 
     * @param p the permutation
     * @return <code>true</code> if the permutation is simple
     */
    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p.elements);
    }
    
    @Override
    public String toString() {
        return "Simple";
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        System.arraycopy(values, 0, mins, 0, values.length);
        System.arraycopy(values, 0, maxs, 0, values.length);
        for(int i = 1; i < values.length-1; i++) {
            for(int j = values.length-1; j >= i; j--) {
                mins[j] = Math.min(mins[j-1], values[j]);
                maxs[j] = Math.max(maxs[j-1], values[j]);
                if (maxs[j] - mins[j] == i) return false;
            }    
        }
        return true;
    }
}
