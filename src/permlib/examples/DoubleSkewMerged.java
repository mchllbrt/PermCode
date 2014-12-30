package permlib.examples;

import java.util.ArrayList;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.AvoidsDecreasing;
import permlib.property.AvoidsIncreasing;
import permlib.property.Griddable;
import permlib.property.PermProperty;

/**
 *
 * @author MichaelAlbert
 */
public class DoubleSkewMerged {

    final static PermProperty ad = new AvoidsDecreasing(3);
    final static PermProperty ai = new AvoidsIncreasing(3);
    final static PermProperty g = new Griddable(new PermProperty[][]{{ad, ai}, {ai, ad}});

    public static void main(String[] args) {
        ArrayList<Permutation> basis = new ArrayList<>();
        PermutationClass c = new PermutationClass(basis);
        for (int n = 4; n <= 12; n++) {
            System.out.println("Trying " + n);
            for (Permutation p : new Permutations(c, n)) {
                if (!g.isSatisfiedBy(p)) {
                    System.out.println(p);
                    basis.add(p);
                }
                c = new PermutationClass(basis);
            }
        }
    }

}
