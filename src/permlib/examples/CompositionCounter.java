package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * Find the subcomposition spectrum of a composition
 *
 * @author Michael Albert
 */
public class CompositionCounter {
    
    static HashMap<ArrayList<Integer>, CompositionCounter> computed = new HashMap<>();
    
    static{
        for (int n = 1; n <= 20; n++) {
            for (int i = 0; i < (2 << (n - 1)); i++) {
                CompositionCounter c = makeCounter(n, i);
                computed.put(c.c, c);
            }
            System.out.println("Init " + n);
        }
        
    }

    ArrayList<Integer> c;
    long[][] counts;
    long[] spectrum;
    int maxPart;
    int sum;

    public CompositionCounter(ArrayList<Integer> c) {
        this.c = c;
        computeMaxAndSum();
        counts = new long[sum + 1][maxPart + 1];
        spectrum = new long[sum + 1];
        spectrum[0] = 1;
        counts[0][0] = 1;
        if (c.size() > 0) {
            computeCounts();
        }
    }

    public static CompositionCounter makeCounter(int sum, int index) {
        ArrayList<Integer> c = new ArrayList<>();
        int part = 1;
        while (sum > 0) {
            if (index % 2 == 0) {
                c.add(part);
                part = 1;
            } else {
                part++;
            }
            sum--;
            index /= 2;
        }
        return new CompositionCounter(c);
    }

    public long getCount(int total, int firstPart) {
        if (total > sum || firstPart > maxPart) {
            return 0;
        }
        return counts[total][firstPart];
    }

    public long getCount(int total) {
        if (total == sum) {
            return 1;
        }
        if (total > sum) {
            return 0;
        }
        return spectrum[total];
    }

    public String toString() {
        return "Comp: " + c + " Spec: " + Arrays.toString(spectrum);
    }

    public boolean uniModal() {
        int i = 1;
        while (i <= sum && spectrum[i] >= spectrum[i - 1]) {
            i++;
        }
        while (i <= sum && spectrum[i] <= spectrum[i - 1]) {
            i++;
        }
        return i > sum;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.c);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CompositionCounter other = (CompositionCounter) obj;
        if (!Objects.equals(this.c, other.c)) {
            return false;
        }
        return true;
    }

    private void computeMaxAndSum() {
        maxPart = 0;
        sum = 0;
        for (int i : c) {
            maxPart = (i > maxPart) ? i : maxPart;
            sum += i;
        }
    }

    private void computeCounts() {
        ArrayList<Integer> c1 = new ArrayList<>(c);
        c1.remove(0);
        CompositionCounter cc1;
        if (computed.containsKey(c1)) {
            cc1 = computed.get(c1);
        } else {
            cc1 = new CompositionCounter(c1);
        }
        for (int k = 1; k <= sum; k++) {
            for (int f = 1; f <= Math.min(k, maxPart); f++) {
                if (c.get(0) >= f) {
                    counts[k][f] = cc1.getCount(k - f);
                } else {
                    counts[k][f] = cc1.getCount(k, f);
                }
                spectrum[k] += counts[k][f];
            }
        }
    }

    public static void main(String[] args) {
        for (int n = 26; n <= 30; n++) {
            for (int i = 0; i < (2 << (n - 1)); i++) {
                if (i % (2 << 20) == 0) System.out.println(i/(2 << 20));
                CompositionCounter c = makeCounter(n, i);
                if (!c.uniModal()) {
                    System.out.println(c);
                }
            }
            System.out.println("Done " + n);
        }

    }
}
