package permlib.examples;

import java.util.Arrays;
import java.util.HashMap;
import permlib.PermUtilities;
import permlib.Permutation;
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
        int k = 5;
        int n = 12;
        for (Permutation p : sep.getPerms(k)) {
            counts.put(p, new Counter());
        }
        for (Permutation q : sep.getPerms(n)) {
            for (int[] c : new Combinations(n,k)) {
                counts.get(q.patternAt(c)).increment();
            }
        }
        for (Permutation p : counts.keySet()) {
            if (PermUtilities.isSymmetryRep(p)) {
                System.out.println(counts.get(p).value + " " + Arrays.toString(p.elements));
            }
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
}
