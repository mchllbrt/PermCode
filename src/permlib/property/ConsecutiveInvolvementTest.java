package permlib.property;

import permlib.Permutation;

/**
 * Used to create tests for the involvement of permutations in consecutive sets of positions.
 * 
 * @author Michael Albert
 */
public class ConsecutiveInvolvementTest implements PermProperty {

    private int[] qInv;

    /**
     * Used to create tests for the involvement of permutations in consecutive
     * sets of positions.
     *
     * @author Michael Albert
     */
    public ConsecutiveInvolvementTest(Permutation p) {
        qInv = p.inverse().elements;
    }

    /**
     * Determines whether a permutation has this property.
     *
     * @param p the permutation
     * @return <code>true</code> if <code>p</code> involves the permutation used
     * to construct this property.
     */
    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p.elements);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        for (int i = 0; i <= values.length - qInv.length; i++) {
            int k = 1;
            while (k < qInv.length && values[i + qInv[k]] > values[i + qInv[k - 1]]) {
                k++;
            }
            if (k == qInv.length) {
                return true;
            }
        }

        return false;
    }
}
