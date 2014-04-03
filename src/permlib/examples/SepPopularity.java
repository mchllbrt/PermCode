package permlib.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.utilities.Combinations;

/**
 * Look for examples of equipopularity in Sep
 *
 * @author Michael Albert
 */
public class SepPopularity {

    static Permutation p2413 = new Permutation("2413");
    static Permutation p3142 = new Permutation("3142");
    static PermutationClass sep = new PermutationClass(p2413, p3142);
    static int maxLength = 8;

    public static void main(String[] args) {
        HashMap<Permutation, Counter> counts = new HashMap<Permutation, Counter>();
        int k = 10;
        int n = 13;
        for (Permutation p : new Permutations(sep, k)) {
            counts.put(p, new Counter());
        }
        for (Permutation q : new Permutations(sep, n)) {
            for (int[] c : new Combinations(n,k)) {
                counts.get(q.patternAt(c)).increment();
            }
        }
        HashSet<Long> cs = new HashSet<>();
        
        for (Permutation p : counts.keySet()) {
            if (PermUtilities.isSymmetryRep(p)) {
                System.out.println(counts.get(p).value + " " + Arrays.toString(p.elements));
                cs.add(counts.get(p).value);
            }
        }
        System.out.println(cs.size() + "distinct counts for " + k + " in " + n);
        
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
}
