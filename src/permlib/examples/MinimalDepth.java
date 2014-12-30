package permlib.examples;

import java.util.Collection;
import java.util.HashSet;
import static permlib.PermStatistics.*;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Investigate A Woo's question from PP2014 about permutations of minimal depth.
 *
 *
 * @author Michael Albert
 */
public class MinimalDepth {

    public static void main(String[] args) {

        PermutationClass c = new PermutationClass(new Permutation("4231"));
        Collection<Permutation> basis = new HashSet<>();
        basis.add(new Permutation("4231"));
        for (int n = 4; n <= 16; n++) {
            PermutationClass d = new PermutationClass(basis);
            System.out.println("Testing " + n);
            for (Permutation p : new Permutations(c, n)) {
                // System.out.println(p + " " + hasMinimalDepth(p));
                if (hasMinimalDepth(p)) {
                    if (!d.containsPermutation(p)) {
                        System.out.println("Counterexample of min depth" + p);
                        return;
                    }
                } else {
                    if (d.containsPermutation(p)) {
                        System.out.println("Basis element: " + p);
                        basis.add(p);
                    }
                }

            }
        }

    }

    public static boolean hasMinimalDepth(Permutation p) {
        return 2 * depth(p) - inversions(p) + cycles(p) - p.length() == 0;
    }
}
