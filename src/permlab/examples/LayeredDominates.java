package permlab.examples;

import java.util.HashMap;
import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 * Check that for every permutation, p, there is a layered permutation of the
 * same length which dominates all the layered perms that p dominates.
 *
 * @author Michael Albert
 */
public class LayeredDominates {

    static PermutationClass layered = new PermutationClass("231", "312");
    static HashMap<Permutation, HereditaryProperty> avoidance = new HashMap<>();

    public static void main(String[] args) {

         generateAvoidanceTests(9);
         for(Permutation p : new Permutations(4)) {
             System.out.print(p + " : ");
             for(Permutation q : maximalLayeredDominatedBy(p)) {
                 System.out.print(q + " ");
             }
             System.out.println();
         }
    }

    private static HashSet<HashSet<Permutation>> maximalsFromLayered(int n) {
        System.out.println("Generating maximal layered for " + n);
        HashSet<HashSet<Permutation>> result = new HashSet<>();
        for (Permutation p : layered.getPerms(n)) {
            HashSet<Permutation> pDominates = layeredDominatedBy(p);
            boolean addP = true;
            HashSet<Permutation> toRemove = null;
            for (HashSet<Permutation> a : result) {
                if (a.containsAll(pDominates)) {
                    addP = false;
                    break;
                }
                if (pDominates.containsAll(a)) {
                    toRemove = a;
                    break;
                }
            }
            if (toRemove != null) {
                result.remove(toRemove);
            }
            if (addP) {
                result.add(pDominates);
            }
        }

//        for(Permutation p : layered.getPerms(n)) {
//            if (result.contains(layeredDominatedBy(p))) {
//                System.out.println(p);
//            }
//            
//        }
//        System.out.println();
        return result;
    }

    private static HashSet<Permutation> layeredDominatedBy(Permutation p) {
        HashSet<Permutation> result = new HashSet<>();
        for (Permutation q : layered.getPermsTo(p.length() - 1)) {
            if (!(avoidance.get(q).isSatisfiedBy(p))) {
                result.add(q);
            }
        }
        return result;
    }

    private static void generateAvoidanceTests(int i) {
        for (Permutation q : layered.getPermsTo(i)) {
            avoidance.put(q, AvoidanceTest.getTest(q));
        }
    }

    private static HashSet<Permutation> maximalLayeredDominatedBy(Permutation p) {
        HashSet<Permutation> all = layeredDominatedBy(p);
        HashSet<Permutation> result = new HashSet<>();
        result.addAll(all);
        for (Permutation q : all) {
            result.removeAll(layeredDominatedBy(q));
        }
        return result;
    }

    public static void basicTest() {

        for (int n = 4; n <= 10; n++) {
            // First generate the maximal dominated sets by layered perms.
            System.out.println("Testing " + n);
            HashSet<HashSet<Permutation>> maximals = maximalsFromLayered(n);
            for (Permutation p : new Permutations(n)) {
                if (layered.containsPermutation(p)) {
                    continue;
                }
                HashSet<Permutation> pDominated = layeredDominatedBy(p);
                boolean pGood = false;
                for (HashSet<Permutation> a : maximals) {
                    pGood = a.containsAll(pDominated);
                    if (pGood) {
                        break;
                    }
                }
                if (!pGood) {
                    System.out.println("Bad " + p);
                }
            }
            System.out.println();
        }
    }

}
