package permlib.examples;

import java.util.ArrayList;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 * How long is the shortest separable permutation containing all separables of
 * length k?
 *
 * @author Michael Albert
 */
public class UniversalSeparable {

    public static void main(String[] args) {

        PermutationClass sep = new PermutationClass("2413", "3142");
        PermutationClass s = new PermutationClass("231");
        for (int k = 1; k <= 10; k++) {
            System.out.println("Searching for " + k + "-universal permutation.");
            ArrayList<HereditaryProperty> a = new ArrayList<>();
            for (Permutation p : s.getPerms(k)) {
                a.add(AvoidanceTest.getTest(p));
            }
            int n = 2*k-1; // Lower bound from the increasing and decreasing permutation
            boolean found = false;
            while (!found) {
                System.out.println("Trying length: " + n);
                for (Permutation p : new Permutations(s, n)) {
                    boolean containsAll = true;
                    for (HereditaryProperty t : a) {
                        containsAll = !t.isSatisfiedBy(p);
                        if (!containsAll) {
                            break;
                        }
                    }
                    if (containsAll) {
                        found = true;
                        System.out.println(p);
                        // break;
                    }
                }
                n++;
            }
            System.out.println();
        }
    }

}
