package permlib.examples;

import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 *
 * @author Michael Albert
 */
public class FixedInAv4231And4321 {

    public static void main(String[] args) {
        PermutationClass c = new PermutationClass("4321","4231");
        for(int n = 4; n < 9; n++) {
           int count = 0;
           int total = 0;
           for(Permutation p : new Permutations(c,n)) {
               total++;
               if (p.inverse().equals(p) && p.reverse().complement().equals(p)) {
                   System.out.println(p);
                   count++;
               }
           }
           System.out.println(total + " " + count);
           System.out.println();
        }
    }
}
    

