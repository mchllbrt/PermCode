package permlib.utilities;

import java.util.Arrays;
import java.util.Random;

/**
 * Represents a composition of an integer <code>n</code> into <code>k</code>
 * parts (or eventually, any composition).
 * 
 * @author Michael Albert
 */
public class Composition {

    int n;
    int k;
    int[] parts;

    public Composition(int n, int k) {
        this.n = n;
        this.k = k;
        this.parts = new int[k];
        this.parts[0] = n;
    }

    public boolean hasNext() {
        return parts[k - 1] != n;
    }

    public int[] next() {
        if (!hasNext()) {
            return new int[0];
        }
        int i;
        for (i = k - 2; i >= 0 && parts[i] == 0; i--);
        parts[i]--;
        parts[i + 1]++;
        if (i+1 < k-1) {
            parts[i + 1] += parts[k - 1];
            parts[k - 1] = 0;
        }
        return parts;
    }
    
    public int[] getParts() {
        return parts;
    }
    
    
    
    
    
}
