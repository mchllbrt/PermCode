package permlab.examples;

import java.util.ArrayList;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.property.Griddable;
import permlib.property.PermProperty;
import permlib.property.PlusIndecomposable;

/**
 * Fiddle around with the negative staircase.
 * 
 * @author Michael Albert
 */
public class NegStaircase {
    
    public static void main(String[] args) {
        countPlusIndecomposables();
    }
    
    public static void countPlusIndecomposables() {
        PermProperty[][] pg = new PermProperty[2][3];
        pg[0][0] = PermProperty.DECREASING;
        pg[0][1] = PermProperty.DECREASING;
        pg[0][2] = PermProperty.EMPTY;
        pg[1][1] = PermProperty.DECREASING;
        pg[1][2] = PermProperty.DECREASING;
        pg[1][0] = PermProperty.EMPTY;
        Griddable g = new Griddable(pg);
        PermProperty pi = new PlusIndecomposable();
        ArrayList<Permutation> basis = new ArrayList<>();
        for(int n = 3; n <= 12; n++) {
            System.out.print(n + " ");
            int count = 0;
            for(Permutation p : (new PermutationClass(basis)).getPerms(n)) {
                if (!g.isSatisfiedBy(p)) {
                    basis.add(p);
                } else {
                    if (pi.isSatisfiedBy(p)) count++;
                }
            }
            System.out.println(count);
        }
    }

}
