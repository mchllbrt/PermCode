package permlib.utilities;

import java.util.Arrays;
import java.util.Iterator;

/**
 * A class that provides an iterator for combinations of
 * <code>k</code> elements from
 * <code>[0...(n-1)]</code>.
 *
 * @author M Albert, M Belton
 */
public class Combinations implements Iterable<int[]> {

    public static int[] complement(int n, int[] c) {
       int[] result = new int[n - c.length];
       int cIndex = 0;
       int rIndex = 0;
       for(int i = 0; i < n; i++) {
           if (cIndex < c.length && c[cIndex] == i) {
               cIndex++;
           } else {
               result[rIndex++] = i;
           }
       }
       return result;
    }

    private int n;
    private int k;

    /**
     * A constructor that creates a set of combinations by specifying the length
     * and upper bound.
     *
     * @param n The upper bound of the combinations (exclusive).
     * @param k The length of the combinations.
     */
    public Combinations(int n, int k) {
        this.n = n;
        this.k = k;
    }

    /**
     * Defines an iterator as an anonymous class that iterates through all
     * possible combinations in the created set.
     *
     * @return an iterator of type combination.
     */
    @Override
    public Iterator<int[]> iterator() {
        return new Iterator<int[]>() {

            int[] c = null;

            /**
             * Returns true if there is a next combination in the set.
             */
            @Override
            public boolean hasNext() {
                return k <= n && (c == null || (k > 0 && c[0] < n - k));
            }

            /**
             * Returns the next combination in the set.
             */
            @Override
            public int[] next() {
                if (c == null) {
                    createFirstCombination();
                } else {
                    update();
                }
                return c;
            }

            /**
             * An operation to remove a combination from the set. This is not
             * supported in this context.
             */
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove is not supported.");
            }

            private void createFirstCombination() {
                c = new int[k];
                for (int i = 0; i < k; i++) {
                    c[i] = i;
                }
            }

            private void update() {
                int i = 1;
                while (i <= k && c[k - i] == n - i) {
                    i++;
                }
                if (i > k) {
                    return;
                }
                c[k - i]++;
                for (int j = k - i + 1; j < k; j++) {
                    c[j] = c[j - 1] + 1;
                }
            }
        }; // End of anonymous class
    }

    public static void main(String[] args) {
        Combinations cs = new Combinations(6, 0);
        for (int[] c : cs) {
            System.out.println(Arrays.toString(c));
        }
    }

    /**
     * Updates
     * <code>c</code> to be the next combination of the same length on [0,n).
     *
     * @param c the combination to update
     * @param n the upper bound (exclusive) for the combination
     * @return
     * <code>true</code> if
     * <code>c</code> was not the lex-last combination.
     *
     */
    public static boolean nextCombination(int[] c, int n) {

        int m = n - 1;
        int index = c.length - 1;
        while (index >= 0 && c[index] == m) {
            m--;
            index--;
        }

        if (index < 0) {
            return false;
        }

        c[index]++;
        for (int i = index + 1; i < c.length; i++) {
            c[i] = c[i - 1] + 1;
        }

        return true;
    }

    /**
     * Returns the first combination of the given length. That is, the numbers
     * <code>0</code> through
     * <code>length-1</code>.
     *
     * @param length the length
     * @return the integer array
     * <code>{0, 1, ..., length-1}</code>
     */
    public static int[] firstCombination(int length) {
        int[] result = new int[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;
    }
} // End of Combinations class.