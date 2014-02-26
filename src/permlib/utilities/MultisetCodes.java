package permlib.utilities;

import java.util.Arrays;
import java.util.Iterator;

/**
 * A class that provides an iterator for multisets of <code>k</code>
 * elements from <code>[0...(n-1)]</code>.
 * 
 * @author Michael Albert
 */
public class MultisetCodes implements Iterable<int[]> {

    int n;
    int k;
    Iterator<int[]> cs;

    public MultisetCodes(int n, int k) {
        this.n = n;
        this.k = k;
        this.cs = new Combinations(n + k - 1, k).iterator(); 
    }

    @Override
    public Iterator<int[]> iterator() {
        return new Iterator<int[]>() {

            int[] result = new int[k];

            /**
             * Returns true if there is a next multiset in the collection.
             */
            @Override
            public boolean hasNext() {
                return cs.hasNext();
            }

            /**
             * Returns the next multiset in the collection.
             */
            @Override
            public int[] next() {
                int[] c = cs.next();
                if (k > 0) {
                    result[0] = c[0];
                    for (int i = 1; i < result.length; i++) {
                        result[i] = result[i - 1] + c[i] - c[i - 1] - 1;
                    }
                    return result;
                }
                return c;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove is not supported.");
            }
        }; // End of anonymous class


    }
    
    public static void main(String[] args) {
        MultisetCodes ms = new MultisetCodes(4,3);
        for(int[] m : ms) System.out.println(Arrays.toString(m));
    }
}
