package permlib.property;

import java.util.ArrayList;
import java.util.Collection;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 * A special case of an avoidance test, specifically for avoiding monotone
 * decreasing permutations of a given length.
 * 
 * @author Michael Albert
 */
public final class AvoidsDecreasing implements HereditaryProperty {

    private final int length;

    /**
     * Specifies the length of monotone decreasing permutation to test 
     * avoidance of.
     * 
     * @param length the length of the monotone decreasing permutation to avoid.
     */
    public AvoidsDecreasing(int length) {
        this.length = length;
    }

    @Override
    public final boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p.elements);
    }

    /**
     * Determines whether or not an array of integers has a decreasing (or
     * rather non-increasing) sequence of the given length.
     *
     * @param values the array of integers
     * @return <code>true</code> if the array does not contain a non-increasing
     * sequence of the length.
     */
    public final boolean isSatisfiedBy(int[] values) {
/* minSequenceTops was converted from a data field to a local variable in order
 * to enforce immutability. If this turns out to be problematic from a
 * performance standpoint then an alternative is (in this special case) to
 * add a method isSatisfiedBy(int[] values, int[] mst) - if the basic method
 * is called then it simply creates mst and the behaviour is as it is now. 
 * But a calling thread, knowing it will make many calls could instead create
 * mst for once and for all and pass its copy in. Simple experiments suggest
 * about a 10% performance hit for the local variable method against the data
 * field one.
 */
        int[] minSequenceTops = new int[length];
        if (values.length >= length) {
            for (int i = 0; i < length; i++) {
                minSequenceTops[i] = Integer.MIN_VALUE;
            }
            for (int v : values) {
                int i = 0;
                while (i < length && v < minSequenceTops[i]) {
                    i++;
                }
                if (i == length - 1) {
                    return false;
                }
                minSequenceTops[i] = v;
            }
        }
        return true;
    }

    @Override
    public final Collection<Permutation> getBasis() {
        ArrayList<Permutation> basis = new ArrayList<Permutation>();
        basis.add(PermUtilities.decreasingPermutation(length));
        return basis;
    }

    @Override
    public final Collection<Permutation> getBasisTo(int n) {
        return getBasis();
    }
}
