/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.Symmetry;
import permlib.property.ConsecutiveAvoidanceTest;
import permlib.property.PermProperty;


/**
 * What is the largest class containing the ladder in which every automorphism
 * of the ladder fixing 12 and 21 can be extended to the rest of the class so
 * that it fixes all other elements. Basically this means that we cannot contain
 * any permutation that covers one, but not both elements of the ladder.
 *
 * @author Michael Albert
 */
public class LadderPlusIdentity {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashSet<Permutation> basis = new HashSet<Permutation>();
        basis.add(new Permutation("231"));
        basis.add(new Permutation("213"));
        for (int n = 4; n < 16; n++) {
            int count = 0;
//            Permutation ab = ab(n - 1);
//            Permutation ba = ba(n - 1);
            PermProperty inClass = new ConsecutiveAvoidanceTest(basis);
            for (Permutation p : new Permutations(n)) {
                if (inClass.isSatisfiedBy(p)) {
//                    Permutation q = PermUtilities.delete(p, 0);
//                    boolean abIn = q.equals(ab);
//                    boolean baIn = q.equals(ba);
//                    q = PermUtilities.delete(p, p.length() - 1);
//                    abIn |= q.equals(ab);
//                    baIn |= q.equals(ba);
//                    if (abIn ^ baIn) {
//                        System.out.println(p);
//                        basis.add(p);
//                    } else {
                    count++;
//                    }
                }
            }
            System.out.println(n + "\t" + count);
        }
    }

    private static Permutation ab(int n) {
        int[] values = new int[n];
        values[0] = (n - 1) / 2;
        int a = values[0] + 1;
        int b = values[0] - 1;
        for (int i = 1; i < n; i++) {
            if (i % 2 == 1) {
                values[i] = a;
                a++;
            } else {
                values[i] = b;
                b--;
            }
        }
        return new Permutation(values);
    }

    private static Permutation ba(int n) {
        return Symmetry.C.on(ab(n));
    }
}
