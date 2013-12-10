package permlib.property;

import permlib.Permutation;

/**
 * This class represents an involvement test for a given permutation. That is,
 * it represents the complement of the principal class defined by its
 * constructor's parameter.
 *
 * The involvement test is a basic depth first right to left scan. Each element
 * is known to lie between two succeeding ones (or above all such, or below all
 * such). The indices of these elements are stored as two arrays created when
 * the test is constructed. This test is primarily intended for use when
 * constructing classes. In that context when an involvement occurs it is known
 * that it will include some final segment of the involving permutation, and so
 * methods are included to check for this possibility. See also {
 *
 * @see Involves Involves}
 *
 * @author Michael Albert
 */
public class InvolvesFromRight implements PermProperty {

    private Permutation p;
    private int[] upperBound;
    private int[] lowerBound;
    private static final int NO_BOUND = -1;
    int[] indices; // Used globally to prevent recreation on each test

    /**
     * This constructor sets up the representation of a permutation as two
     * arrays which keep track of upper and lower limits of each entry in terms
     * of the succeeding elements. The given permutation represents the pattern
     * that we wish to check involvement of in other permutations.
     *
     * @param p the given permutation
     */
    public InvolvesFromRight(Permutation p) {
        this.p = p;
        indices = new int[p.length()];
        upperBound = new int[p.length()];
        lowerBound = new int[p.length()];
        for (int i = 0; i < p.length(); i++) {
            upperBound[i] = lowerBound[i] = NO_BOUND;
            int minAbove = Integer.MAX_VALUE;
            int maxBelow = Integer.MIN_VALUE;
            for (int j = i + 1; j < p.length(); j++) {
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
    }

    /**
     * The property of involving a single permutation, with a rightward biased
     * testing method.
     *
     * @param s a string representing the permutation
     */
    public InvolvesFromRight(String s) {
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
        for (indices[p.length() - 1] = values.length - 1; indices[p.length() - 1] >= 0; indices[p.length() - 1]--) {
            if (check(values, p.length() - 2)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks the involvement of the underlying permutation,
     * <code>p</code> in the permutation
     * <code>q</code>, where that involvement must include the final
     * <code>includingFinal</code> elements of
     * <code>q</code>.
     *
     * @param q the permutation to check
     * @param includingFinal the number of final elements that must be included
     *
     * @return
     * <code>true</code> if
     * <code>q</code> involves
     * <code>p</code>
     */
    public boolean isSatisfiedBy(Permutation q, int includingFinal) {
        return isSatisfiedBy(q.elements, includingFinal);
    }

    /**
     * Checks the involvement of the underlying permutation,
     * <code>p</code> in the array
     * <code>values</code>, where that involvement must include the final
     * <code>includingFinal</code> elements of
     * <code>values</code>.
     *
     * @param values the array
     * @param includingFinal the number of final elements that must be included
     *
     * @return
     * <code>true</code> if
     * <code>values</code> involves
     * <code>p</code>
     */
    public boolean isSatisfiedBy(int[] values, int includingFinal) {
        indices = new int[p.length()];
        for (int i = 1; i <= includingFinal; i++) {
            indices[p.length() - i] = values.length - i;
        }
        if (!checkFinalPattern(values, includingFinal)) {
            return false;
        }
        return check(values, p.length() - includingFinal - 1);
    }

    private boolean checkFinalPattern(int[] q, int includingFinal) {
        for (int i = 1; i < includingFinal; i++) {
            if (!fits(q, p.length() - i - 1)) {
                return false;
            }
        }
        return true;
    }

    private boolean check(int[] q, int next) {
        if (next < 0) {
            return true;
        }
        for (indices[next] = indices[next + 1] - 1; indices[next] >= 0; indices[next]--) {
            if (fits(q, next) && check(q, next - 1)) {
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
     * @param p The given permutation to be tested.
     *
     * @return An integer array containing the indices of where the pattern
     * occurs in
     * <code>q</code>.
     */
    public int[] involvedWhere(Permutation p) {
        if (isSatisfiedBy(p)) {
            return indices;
        }
        return new int[0];
    }

    @Override
    public String toString() {
        return "Involves(" + p + ")";
    }
}