package permlib.property;

import permlib.Permutation;

/**
 * The property of being plus irreducible. That is, not having a consecutive 
 * pair of elements that are consecutive by value in order.
 * 
 * @author Michael Albert
 */
public class PlusIrreducible implements PermProperty {

    /**
     * Determines whether a permutation is plus irreducible.
     * @param p the permutation
     * @return <code>true</code> if the permutation is plus irreducible.
     */
    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p.elements);
    }
    
    @Override
    public boolean isSatisfiedBy(int[] values) {
        for(int i = 1; i < values.length; i++) {
            if (values[i] - values[i-1] == 1) return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "+Irr";
    }
}
