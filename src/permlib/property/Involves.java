package permlib.property;

import permlib.Permutation;

/**
 * This class represents an involvement test for a given permutation. That is,
 * it represents the complement of the principal class defined by its
 * constructor's parameter.
 *
 * The involvement test is a basic depth first left to right scan. Each
 * successive element is known to lie between two preceding ones (or above all
 * such, or below all such). The indices of these elements are stored as two
 * arrays created when the test is constructed.
 *
 * @author Michael Albert
 */
public class Involves implements PermProperty {

    private Permutation p;
    private int[] upperBound;
    private int[] lowerBound;
    private static final int NO_BOUND = -1;
    private int[] indices; // Used globally to avoid recreation on each test.

    /**
     * This constructor sets up the representation of a permutation as two
     * arrays which keep track of upper and lower limits of each entry in terms
     * of the preceding elements scanning left to right. The given permutation
     * represents the pattern that we wish to check involvement of in other
     * permutations.
     *
     * @param p the given permutation
     */
    public Involves(Permutation p) {
        this.p = p;
        upperBound = new int[p.elements.length];
        lowerBound = new int[p.elements.length];
        for (int i = 0; i < p.elements.length; i++) {
            upperBound[i] = lowerBound[i] = NO_BOUND;
            int minAbove = Integer.MAX_VALUE;
            int maxBelow = Integer.MIN_VALUE;
            for (int j = 0; j < i; j++) {
                if (p.elements[j] < p.elements[i]) {
                    if (p.elements[j] > maxBelow) {
                        maxBelow = p.elements[j];
                        lowerBound[i] = j;
                    }
                } else {
                    if (p.elements[j] < minAbove) {
                        minAbove = p.elements[j];
                        upperBound[i] = j;
                    }
                }
            }
        }
        indices = new int[p.length()];
    }

    /**
     * The property of involving a single permutation.
     *
     * @param s a string representing the permutation
     */
    public Involves(String s) {
        this(new Permutation(s));
    }

    /**
     * Checks the involvement of the underlying permutation,
     * <code>p</code> in the permutation
     * <code>q</code>.
     *
     * @param q the permutation to check.
     *
     * @return
     * <code>true</code> if
     * <code>q</code> involves
     * <code>p</code>
     */
    @Override
    public boolean isSatisfiedBy(Permutation q) {
        return isSatisfiedBy(q.elements);
    }

    /**
     * Checks the involvement of the underlying permutation,
     * <code>p</code> in the array
     * <code>values</code>.
     *
     * @param values the array of values to check.
     *
     * @return
     * <code>true</code> if
     * <code>values</code> involves
     * <code>p</code>
     */
    @Override
    public boolean isSatisfiedBy(int[] values) {
        if (p.length() <= 1 && p.length() <= values.length) {
            return true;
        }
        for (indices[0] = 0; indices[0] < values.length; indices[0]++) {
            if (check(values, 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean check(int[] q, int next) {
        if (next == indices.length) {
            return true;
        }
        for (indices[next] = indices[next - 1] + 1; indices[next] < q.length; indices[next]++) {
            if (fits(q, next) && check(q, next + 1)) {
                return true;
            }
        }
        return false;
    }

    private boolean fits(int[] q, int next) {
        return (lowerBound[next] == NO_BOUND || q[indices[next]] > q[indices[lowerBound[next]]])
                && (upperBound[next] == NO_BOUND || q[indices[next]] < q[indices[upperBound[next]]]);
    }

    /**
     * Determines where the underlying permutation is involved in a given
     * permutation. An integer array containing the indices where the pattern
     * occurs in the given permutation is returned, or if the pattern does not
     * occur then an empty integer array is returned.
     *
     * @param q The given permutation to be tested.
     *
     * @return An integer array containing the indices of where the pattern
     * occurs in
     * <code>q</code>.
     */
    public int[] involvedWhere(Permutation q) {
        if (isSatisfiedBy(q)) {
            return indices;
        }
        return new int[0];
    }

    @Override
    public String toString() {
        return "Involves(" + p + ")";
    }
}