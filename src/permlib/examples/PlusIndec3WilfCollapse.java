package permlib.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Experiments on Wilf-collapse in non plus-closed subclasses of +(321, 312,
 * 231).
 *
 * @author Michael Albert
 */
public class PlusIndec3WilfCollapse {

    static PermutationClass c;
    static final HashSet<Permutation> bc = new HashSet<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        bc.add(new Permutation("231"));
        bc.add(new Permutation("312"));
        bc.add(new Permutation("321"));
        c = new PermutationClass(bc);
        for (Permutation p : new Permutations(4,6)) {
            if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p) && c.containsPermutation(p)) {
                bc.add(p);
                c = new PermutationClass(bc);
                System.out.println(p);
            }
            // c = new PermutationClass(bc);
        }

        for (Permutation p : new Permutations(c, 6)) {
            System.out.println("Extra basis element: " + sumC(p));
            HashSet<Permutation> bp = new HashSet<>(bc);
            bp.add(p);
            PrincipalSpectra ps = new PrincipalSpectra(new PermutationClass(bp));
            for (int k = 1; k <= 5; k++) {
                HashMap<ArrayList<Long>, HashSet<Permutation>> specs = ps.getSpectra(k + 1, k + 5, false);
                for (ArrayList<Long> sp : specs.keySet()) {
                    if (specs.get(sp).size() > 1) {
                        System.out.print(sp + " ");
                        for (Permutation q : specs.get(sp)) {
                            System.out.print(sumC(q) + " ");
                        }
                        System.out.println();

                    }
                }

            }
            System.out.println();
        }
    }

    

    private static String sumC(Permutation p) {
        StringBuilder result = new StringBuilder();
        Permutation[] c = PermUtilities.sumComponents(p);
        for (Permutation q : c) {
            result.append('(');
            result.append(q);
            result.append(')');
        }
        return result.toString();
    }
}
