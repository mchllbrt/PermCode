package permlib.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermClassInterface;
import permlib.classes.PermutationClass;
import permlib.utilities.Combinations;

/**
 * Look for examples of equipopularity in Sep
 *
 * @author Michael Albert
 */
public class SepPopularity {

    static final Permutation p2413 = new Permutation("2413");
    static final Permutation p3142 = new Permutation("3142");
    static final PermutationClass c = new PermutationClass(new Permutation("231"));
    static int maxLength = 8;

    static final Permutation p25314 = new Permutation("25314");
    static final Permutation p246135 = new Permutation("246135");
    static final Permutation p362514 = new Permutation("362514");
    static final PermutationClass x = new PermutationClass(new Permutation("1324"), new Permutation("4231"));
    
    public static void main(String[] args) {
        doIt3(7, 7, 12,
                new PermutationClass(
                        new Permutation("1324"), new Permutation("4231")
                )
        );
    }

    public static void doIt(int k, int n) {
        HashMap<Permutation, Counter> counts = new HashMap<Permutation, Counter>();
        for (Permutation p : new Permutations(c, k)) {
            counts.put(p, new Counter());
        }
        for (Permutation q : new Permutations(c, n)) {
            for (int[] c : new Combinations(n, k)) {
                counts.get(q.patternAt(c)).increment();
            }
        }
        HashSet<Long> cs = new HashSet<>();

        for (Permutation p : counts.keySet()) {

            System.out.println(counts.get(p).value + " " + Arrays.toString(p.elements));
            cs.add(counts.get(p).value);

        }
        System.out.println(cs.size() + " distinct counts for " + k + " in " + n);

    }

    public static void doIt2(int k, int n, PermClassInterface cl) {
        HashMap<Permutation, Counters> counts = new HashMap<Permutation, Counters>();
        for (Permutation p : new Permutations(cl, k)) {
            counts.put(p, new Counters(k + 1, n));
        }
        for (int m = k + 1; m < n; m++) {
            for (Permutation q : new Permutations(cl, m)) {
                for (int[] c : new Combinations(m, k)) {
                    counts.get(q.patternAt(c)).increment(m);
                }
            }
        }

        HashMap<Counters, HashSet<Permutation>> blocks = new HashMap<>();

        for (Permutation p : counts.keySet()) {
            if (!blocks.containsKey(counts.get(p))) {
                blocks.put(counts.get(p), new HashSet<Permutation>());
            }
            blocks.get(counts.get(p)).add(p);
        }
        System.out.println(blocks.size() + " distinct counts for " + k + " in " + n);

        for (Counters c : blocks.keySet()) {
            System.out.println(c);
            for (Permutation p : blocks.get(c)) {
                System.out.println("  " + Arrays.toString(p.elements));
            }
        }

    }

    public static void doIt3(int low, int high, int n, PermClassInterface cl) {
        for (int k = low; k <= high; k++) {
            doIt2(k, n, cl);
        }
    }

    static class Counter {

        long value = 0;

        void increment() {
            value++;
        }

        long value() {
            return value;
        }
    }

    static class Counters {

        long[] counts;
        int low;

        public Counters(int low, int high) {
            this.low = low;
            counts = new long[high - low];
        }

        void increment(int k) {
            counts[k - low]++;
        }

        long[] value() {
            return counts;
        }

        public String toString() {
            return Arrays.toString(counts);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 23 * hash + Arrays.hashCode(this.counts);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Counters other = (Counters) obj;
            if (!Arrays.equals(this.counts, other.counts)) {
                return false;
            }
            return true;
        }

    }
}
