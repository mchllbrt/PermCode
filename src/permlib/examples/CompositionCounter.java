package permlib.examples;

import java.util.ArrayDeque;
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

    public CompositionCounter(int part, CompositionCounter tailCounter) {
        this.c = new ArrayList<Integer>();
        c.add(part);
        c.addAll(tailCounter.c);
        maxPart = Math.max(part, tailCounter.maxPart);
        sum = part + tailCounter.sum;
        counts = new long[sum + 1][maxPart + 1];
        spectrum = new long[sum + 1];
        spectrum[0] = 1;
        counts[0][0] = 1;
        computeCountsFrom(tailCounter);

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

    public boolean unimodal() {
        return CompositionCounter.this.unimodal(spectrum);
    }

    public static boolean unimodal(long[] s) {
        int i = 1;
        while (i < s.length && s[i] >= s[i - 1]) {
            i++;
        }
        while (i < s.length && s[i] <= s[i - 1]) {
            i++;
        }
        return i == s.length;
    }
    
    public static boolean logConcave(long[] s) {
        for(int i = 2; i < s.length-1; i++) {
            if (s[i]*s[i] - s[i-1]*s[i+1] < 0) return false;
        }
        return true;
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

    static ArrayDeque<Integer> composition = new ArrayDeque<>();
    static long count = 0;

    public static void DFSCompositions(int n, CompositionCounter tailCounter, int maxParts) {

        if (n == 0 || tailCounter.c.size() >= maxParts) {
            return;
        }
        for (int part = n; part > 0; part--) {
            CompositionCounter newCounter = new CompositionCounter(part, tailCounter);
            count++;
            if (count % 1000000 == 0) {
                System.out.println("Computed " + (count / 1000000) + "M");
            }
            if (!logConcave(newCounter.spectrum)) {
                System.out.println(newCounter);
            }
            DFSCompositions(n - part, newCounter, maxParts);
        }

    }
    
    public static void PartBoundedDFSCompositions(int n, CompositionCounter tailCounter, int maxPart) {

        if (n == 0) {
            return;
        }
        for (int part = Math.min(maxPart,n); part > 0; part--) {
            CompositionCounter newCounter = new CompositionCounter(part, tailCounter);
            count++;
            if (count % 1000000 == 0) {
                System.out.println("Computed " + (count / 1000000) + "M");
            }
            if (!unimodal(newCounter.spectrum)) {
                System.out.println(newCounter);
            }
            PartBoundedDFSCompositions(n - part, newCounter, maxPart);
        }

    }

    public static void DFSCompositions(int n, CompositionCounter tailCounter) {
        DFSCompositions(n, tailCounter, n);
    }

    public static void main(String[] args) {
        ArrayList<Integer> c = new ArrayList<>();
        c.add(2);
        c.add(3);
        c.add(1);
        c.add(4);
        CompositionCounter cc = new CompositionCounter(c);
        System.out.println(cc.qPolynomial());
    }

    private void computeCountsFrom(CompositionCounter tailCounter) {

        for (int k = 1; k <= sum; k++) {
            for (int f = 1; f <= Math.min(k, maxPart); f++) {
                if (c.get(0) >= f) {
                    counts[k][f] = tailCounter.getCount(k - f);
                } else {
                    counts[k][f] = tailCounter.getCount(k, f);
                }
                spectrum[k] += counts[k][f];
            }
        }
    }
    
    public String qPolynomial() {
        StringBuilder result = new StringBuilder();
        result.append(" 1 + ");
        for(int i = 1; i < spectrum.length; i++) {
            result.append(spectrum[i]);
            result.append("*q^");
            result.append(i);
            if (i < spectrum.length-1) result.append(" + ");
        }
        result.append(";");
        return result.toString();
    }
    
    private static ArrayList<Integer> getComposition(int n, int index) {
        if (n == 0) {
            return new ArrayList<Integer>();
        }
        ArrayList<Integer> result = getComposition(n - 1, index / 2);
        if (index % 2 == 0) {
            result.add(1);
        } else {
            result.set(result.size() - 1, result.get(result.size() - 1) + 1);
        }
        return result;
    }

}

//        for (int n = 26; n <= 30; n++) {
//            for (int i = 0; i < (2 << (n - 1)); i++) {
//                if (i % (2 << 20) == 0) System.out.println(i/(2 << 20));
//                CompositionCounter c = makeCounter(n, i);
//                if (!c.unimodal()) {
//                    System.out.println(c);
//                }
//            }
//            System.out.println("Done " + n);
//        }
