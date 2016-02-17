package permlib.examples;

import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 * Just a small example of an avoidance timing template in the current system.
 *
 * @author Michael Albert
 */
public class Av321AvoidanceTiming {

    public static void main(String[] args) {

        PermutationClass a321 = new PermutationClass("321");
        for (int i = 0; i < 3; i++) {
            for (Permutation p : new Permutations(5)) {
                System.out.print(p + " ");
                HereditaryProperty ap = AvoidanceTest.getTest(p);
                long count = 0;
                long start = System.currentTimeMillis();
                for (Permutation q : new Permutations(10)) {
                    if (ap.isSatisfiedBy(q)) {
                        count++;
                    }
                }
                System.out.print(count + " ");
                System.out.println(System.currentTimeMillis() - start);
            }
            System.out.println();
        }
    }

}
