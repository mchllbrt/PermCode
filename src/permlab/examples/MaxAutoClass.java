package permlab.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Compute the maximum class extending R whose automorphism group is equal to
 * Aut(R)
 *
 * @author MichaelAlbert
 */
public class MaxAutoClass {

    public static void main(String[] args) {
        doSpecificGroup();
    }

    public static void doAll() {
        HashSet<PartialIsomorphism> autR = new HashSet<PartialIsomorphism>();
        for (PartialIsomorphismSpecification aut : PartialIsomorphismSpecification.getAllSpecifications()) {
            autR.add(new PartialIsomorphism(aut.specification));
        }
        doGroup(autR);
    }

    public static String report(HashMap<Permutation, Permutation> iso) {
        StringBuilder result = new StringBuilder();
        result.append(iso.get(new Permutation("12")));
        result.append(" ");
        result.append(iso.get(new Permutation("2413")));
        result.append(" [");
        result.append(iso.get(new Permutation("132")));
        result.append(" ");
        result.append(iso.get(new Permutation("213")));
        result.append(" ");
        result.append(iso.get(new Permutation("231")));
        result.append(" ");
        result.append(iso.get(new Permutation("312")));
        result.append("]");
        return result.toString();
    }

    public static void doSpecificGroup() {
//        Permutation[] nme = new Permutation[4];
//        nme[0] = new Permutation("132");
//        nme[1] = new Permutation("213");
//        nme[2] = new Permutation("231");
//        nme[3] = new Permutation("312");
//
//        Permutation[] nmg = new Permutation[4];
//        nmg[0] = new Permutation("312");
//        nmg[1] = new Permutation("213");
//        nmg[2] = new Permutation("132");
//        nmg[3] = new Permutation("231");
//
//        Permutation[] nmh = new Permutation[4];
//        nmh[0] = new Permutation("231");
//        nmh[1] = new Permutation("213");
//        nmh[2] = new Permutation("312");
//        nmh[3] = new Permutation("132");

        Permutation[] nme = new Permutation[4];
        nme[0] = new Permutation("132");
        nme[1] = new Permutation("213");
        nme[2] = new Permutation("231");
        nme[3] = new Permutation("312");

        Permutation[] nmg = new Permutation[4];
        nmg[0] = new Permutation("213");
        nmg[1] = new Permutation("132");
        nmg[2] = new Permutation("231");
        nmg[3] = new Permutation("312");

        Permutation[] nmh = new Permutation[4];
        nmh[0] = new Permutation("132");
        nmh[1] = new Permutation("213");
        nmh[2] = new Permutation("312");
        nmh[3] = new Permutation("231");

        Permutation[] nmgh = new Permutation[4];
        nmgh[0] = new Permutation("213");
        nmgh[1] = new Permutation("132");
        nmgh[2] = new Permutation("312");
        nmgh[3] = new Permutation("231");


        HashSet<PartialIsomorphism> autR = new HashSet<PartialIsomorphism>();
        autR.add(new PartialIsomorphism((new PartialIsomorphismSpecification(true, true, nme).specification)));
        autR.add(new PartialIsomorphism((new PartialIsomorphismSpecification(false, true, nmg).specification)));
        autR.add(new PartialIsomorphism((new PartialIsomorphismSpecification(false, true, nmh).specification)));
        autR.add(new PartialIsomorphism((new PartialIsomorphismSpecification(true, true, nmgh).specification)));

        doGroup(autR);
    }

    private static HashSet<Permutation> shadow(Permutation q) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for (int i = 0; i < q.length(); i++) {
            result.add(PermUtilities.delete(q, i));
        }
        return result;
    }

    private static void doGroup(HashSet<PartialIsomorphism> autR) {
        HashSet<Permutation> basis = new HashSet<Permutation>();
        for (int n = 4; n <= 8; n++) {
//            System.out.println("n = " + n);
//            System.out.println("Basis size = " + basis.size());

            HashMap<HashSet<Permutation>, Permutation> shadowI = new HashMap<HashSet<Permutation>, Permutation>();
            HashMap<Permutation, HashSet<Permutation>> shadows = new HashMap<Permutation, HashSet<Permutation>>();

            PermutationClass c = new PermutationClass(basis);
            for (Permutation p : new Permutations(c, n)) {
                shadowI.put(shadow(p), p);
                shadows.put(p, shadow(p));
            }
            for (Permutation p : new Permutations(c, n)) {
//            System.out.println("P = " + p);
                boolean goodP = true;
                HashMap<PartialIsomorphism, Permutation> pi = new HashMap<PartialIsomorphism, Permutation>();
                for (PartialIsomorphism g : autR) {
//                System.out.println(" g = " + g);
                    HashSet<Permutation> pIs = new HashSet<Permutation>();
                    for (Permutation sp : shadows.get(p)) {
                        // System.out.println("  Adding " + g.iso.get(sp) + " to image shadow");
                        pIs.add(g.iso.get(sp));
                    }
                    if (shadowI.containsKey(pIs)) {
                        pi.put(g, shadowI.get(pIs));
                    } else {
                        goodP = false;
                        break;
                    }
                }
                if (goodP) {
                    for (PartialIsomorphism g : autR) {
                        g.addPair(p, pi.get(g));
                    }
                } else {
                    System.out.println(p);
                    basis.add(p);
                }
            }
        }
        System.out.println();

        PermutationClass c = new PermutationClass(basis);
        Permutation[] c5 = c.getPerms(5).toArray(new Permutation[0]);
        Arrays.sort(c5);
        // for(Permutation p : c5) System.out.println(p);

        for (PartialIsomorphism g : autR) {
            System.out.println(report(g.iso));
            for (Permutation p : c5) {
                System.out.println(p + " " + g.iso.get(p));
            }
            System.out.println();
        }
    }
}
