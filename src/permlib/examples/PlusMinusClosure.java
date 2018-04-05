package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 *
 * @author Michael Albert
 */
public class PlusMinusClosure {

    private final HashSet<Permutation> basis = new HashSet<>();
    private PermutationClass c;
    private PermutationClass pc;
    int basisKnownTo = 1;

    public PlusMinusClosure(PermutationClass c) {
        this.c = c;
        this.pc = new PermutationClass(basis);
    }

    public HashSet<Permutation> generateBasis(int n) {
        for (int i = basisKnownTo + 1; i <= n; i++) {
            for (Permutation p : new Permutations(pc, i)) {
                if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p) && PermUtilities.MINUSINDECOMPOSABLE.isSatisfiedBy(p) && !c.containsPermutation(p)) // System.out.println("Using " + q);
                {
                    basis.add(p);
                }
            }
            pc = new PermutationClass(basis);
        }
        basisKnownTo  = n;
        return basis ;
    }
    


public HashSet<Permutation> getBasis() {
        return basis;
    }

    public static void main(String[] args) {
        HashSet<Permutation> basis = new HashSet<Permutation>();
        for(Permutation p : new Permutations(5)) basis.add(p);
        basis.remove(new Permutation("41253"));
        PermutationClass c = new PermutationClass(basis);
        PlusMinusClosure pm = new PlusMinusClosure(c);
        HashSet<Permutation> b = pm.generateBasis(10);
        PermutationClass theClass = new PermutationClass(b);
        PrincipalSpectra specC = new PrincipalSpectra(theClass);
        System.out.println(specC.getSpectrum(new Permutation("21543867"), 12));
        System.out.println(specC.getSpectrum(new Permutation("31265487"), 12));
//        for(int k = 3; k<= 6; k++) {
//            HashMap<ArrayList<Long>, HashSet<Permutation>> specs = specC.getSpectra(k, k+4, false);
//            for(ArrayList<Long> spec : specs.keySet()) {
//                System.out.print(spec + " ");
//                for(Permutation p : specs.get(spec)) System.out.print(p + " ");
//                System.out.println();
//            }
//        }
    }

}
