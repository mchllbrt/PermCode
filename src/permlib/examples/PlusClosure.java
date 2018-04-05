package permlib.examples;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Try to generate the basis for a plus closure.
 *
 * @author Michael Albert
 */
public class PlusClosure {

    private final HashSet<Permutation> basis = new HashSet<>();
    private PermutationClass c;
    private PermutationClass pc;
    int basisKnownTo = 1;

    public PlusClosure(PermutationClass c) {
        this.c = c;
        this.pc = new PermutationClass(basis);
    }

    public HashSet<Permutation> generateBasis(int n) {
        for (int i = basisKnownTo + 1; i <= n; i++) {
            for (Permutation p : new Permutations(pc, i)) {
                // System.out.println("Trying " + p);
                for (Permutation q : PermUtilities.sumComponents(p)) {
                    // System.out.println("Using " + q);
                    if (!c.containsPermutation(q)) {
                        basis.add(p);
                        // System.out.println(p);
                        break;
                    }
                }
            }
            pc = new PermutationClass(basis);
        }
        basisKnownTo = n;
        return basis;
    }

    public HashSet<Permutation> getBasis() {
        return basis;
    }

    public static void main(String[] args) {
        PermutationClass c = new PermutationClass("123");
        PlusClosure p = new PlusClosure(c);
        HashSet<Permutation> b = p.generateBasis(10);
        Permutation[] ba = new Permutation[b.size()];
        b.toArray(ba);
        Arrays.sort(ba);
        for(Permutation q : ba) System.out.println(q);
    }

}
