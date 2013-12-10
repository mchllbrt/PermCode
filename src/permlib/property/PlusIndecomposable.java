package permlib.property;

import permlib.Permutation;

/**
 * The property of being plus indecomposable. That is, not having a proper
 * initial segment by position which is also a proper initial segment by value.
 * 
 * @author Michael Albert
 */
public final class PlusIndecomposable implements PermProperty {
    
    /**
     * Determines whether a permutation is plus indecomposable.
     * @param p the permutation
     * @return <code>true</code> if the permutation is plus indecomposable
     */
    @Override
    public final boolean isSatisfiedBy(Permutation p) {
        return safeIsSatisfiedBy(p.elements);
    }
    
    /**
     * Determines whether the pattern of an array of values is plus
     * indecomposable.
     * 
     * @param values the values
     * @return <code>true</code> if the pattern of <code>values</code> is
     * plus indecomposable
     */
    @Override
    public final boolean isSatisfiedBy(int[] values) {
        return isSatisfiedBy(new Permutation(values));
    }
    
    @Override
    public String toString() {
        return "+Ind";
    }

    private boolean safeIsSatisfiedBy(int[] values) {
        if (values.length == 0) {
            return true;
        }
        int high = Integer.MIN_VALUE;
        for (int index = 0; index < values.length; index++) {
            high = (values[index] > high ? values[index] : high);
            if (high == index && index < values.length-1) {
                return false;
            }
        }
        return true;
    }
}
