package permlib.property;

import java.util.ArrayList;
import java.util.Collection;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 * A special case of an avoidance test, specifically for avoiding monotone
 * increasing permutations of a given length.
 * @author Michael Albert
 */
public final class AvoidsIncreasing implements HereditaryProperty {

    private int length;
    
    /**
     * Specifies the length of monotone increasing permutation to test 
     * avoidance of.
     * 
     * @param length the length of the monotone increasing permutation to avoid.
     */
    public AvoidsIncreasing(int length) {
        this.length = length;
    }

    /**
     * Determines whether or not an array of integers has an increasing (or
     * rather non-decreasing) sequence of the given length.
     *
     * @param values the array of integers
     * @return <code>true</code> if the array does not contain a non-decreasing
     * sequence of the length.
     */
    @Override
    public final boolean isSatisfiedBy(int[] values) {
        int[] maxSequenceBottoms = new int[length];
        if (values.length >= length) {
            for (int i = 0; i < length; i++) {
                maxSequenceBottoms[i] = Integer.MAX_VALUE;
            }
            for (int v : values) {
                int i = 0;
                while (i < length && v >= maxSequenceBottoms[i]) {
                    i++;
                }
                if (i == length - 1) {
                    return false;
                }
                maxSequenceBottoms[i] = v;
            }
        }
        return true;
    }
    
    @Override
    public final boolean isSatisfiedBy(Permutation p) {
        return this.isSatisfiedBy(p.elements);
    }

    @Override
    public final Collection<Permutation> getBasis() {
        ArrayList<Permutation> basis = new ArrayList<Permutation>();
        basis.add(PermUtilities.increasingPermutation(length));
        return basis;
    }

    @Override
    public final Collection<Permutation> getBasisTo(int n) {
        return getBasis();
    }
}
