/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 *
 * @author MichaelAlbert
 */
public class UnimodalSubpermutations {

    public static ArrayList<Integer> subspectrum(Permutation p, int low) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);
        HashSet<Permutation> parents = new HashSet<>();
        parents.add(p);
        for (int k = p.length() - 1; k >= low; k--) {
            // System.out.print(k + "   ");
            HashSet<Permutation> children = new HashSet<>();
            for (Permutation q : parents) {
                for (int i = 0; i < q.length(); i++) {
                    children.add(PermUtilities.delete(q, i));
                }
            }
            result.add(children.size());
            // System.out.println(children.size());
            parents = children;
        }
        return result;

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int count = 0;
        long start = System.currentTimeMillis();
            while (true) {
            count++;
            if (count % 10 == 0) {
                System.out.println(count + " trials " + (System.currentTimeMillis() - start));
                start = System.currentTimeMillis();
            }
            Permutation p = PermUtilities.randomPermutation(26);
            // System.out.println(p);
            ArrayList<Integer> r = subspectrum(p, 13);
            // System.out.println(r);
            if (!unimodal(r)) {
                System.out.println(p);
                System.out.println(r);
            }

        }
    }

    private static boolean unimodal(ArrayList<Integer> r) {
        int c = Integer.MIN_VALUE;
        int i = 0;
        while (i < r.size() && c <= r.get(i)) {
            c = r.get(i);
            i++;
        }
        while (i < r.size() && c >= r.get(i)) {
            c = r.get(i);
            i++;
        }
        return i == r.size();

    }

}
