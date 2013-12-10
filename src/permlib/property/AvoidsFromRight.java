package permlib.property;

import java.util.ArrayList;
import java.util.Collection;
import permlib.Permutation;

/**
 * This class represents an avoidance test for a single permutation. That is, it
 * represents the principal class defined by its constructor's parameter. The
 * testing algorithm used scans from the right and can be made to assume that a
 * certain number of elements must be included. See also {
 *
 * @see InvolvesFromRight InvolvesFromRight}.
 *
 * @author Michael Albert
 */
public final class AvoidsFromRight extends HereditaryPropertyAdapter {

    private InvolvesFromRight inv;
    private Permutation p;

    /**
     * The property of avoiding a single permutation.
     *
     * @param p the permutation
     */
    public AvoidsFromRight(Permutation p) {
        this.p = p;
        this.inv = new InvolvesFromRight(p);
    }

    public AvoidsFromRight(String s) {
        this(new Permutation(s));
    }

    @Override
    public final boolean isSatisfiedBy(Permutation q) {
        return !inv.isSatisfiedBy(q);
    }

    /**
     * Determines whether
     * <code>q</code> avoids the underlying permutation
     * <code>p</code> over all sets of indices that include the
     * <code>includingFinal</code> final ones.
     *
     * @param q the permutation in which to check avoidance
     * @param includingFinal the number of indices to include
     * @return <code>true</code> if <code>p</code> is not appropriately involved
     * in <code>q</code>
     */
    public final boolean isSatisfiedBy(Permutation q, int includingFinal) {
        return !inv.isSatisfiedBy(q, includingFinal);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        return !inv.isSatisfiedBy(values);
    }

    /**
     * Returns the basis of this property, i.e. the permutation that is avoided.
     *
     * @return the permutation that is avoided
     */
    @Override
    public Collection<Permutation> getBasis() {
        ArrayList<Permutation> basis = new ArrayList<Permutation>();
        basis.add(p);
        return basis;
    }

    @Override
    public String toString() {
        return "Avoids(" + p + ")";
    }
}