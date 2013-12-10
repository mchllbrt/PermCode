/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import permlib.Permutation;

/**
 *
 * @author MichaelAlbert
 */
public class LCW {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Permutation p = new Permutation("5 8 4 6 1 3 2 9 7");
        ArrayList<Integer> pa = new ArrayList<Integer>();
        for (int i = 0; i < p.length(); i++) {
            pa.add(i);
        }
        HashSet<ArrayList<Integer>> rectangles = new HashSet<ArrayList<Integer>>();
        rectangles.add(pa);
        System.out.println(doCheck(p, rectangles, 2));
        HashSet<ArrayList<Integer>> splits = splits(p, 6, pa);
        for(ArrayList<Integer> a : splits) System.out.println(a);
        
//        final HashSet<Permutation> basis = new HashSet<Permutation>();
//        final int k = 2;
//        for (int n = 7; n <= 10; n++) {
//            PermClass c = new PermClass(basis);
//            c.processPerms(n,
//                    new PermProcessor() {
//                        @Override
//                        public void process(Permutation p) {
//                            ArrayList<Integer> pa = new ArrayList<Integer>();
//                            for (int i = 0; i < p.length(); i++) {
//                                pa.add(i);
//                            }
//                            HashSet<ArrayList<Integer>> rectangles = new HashSet<ArrayList<Integer>>();
//                            rectangles.add(pa);
//                            if (!doCheck(p, rectangles, k)) {
//                                System.out.println(p);
//                                basis.add(p);
//                            };
//                        }
//
//                        @Override
//                        public void reset() {
//                            throw new UnsupportedOperationException("Not supported yet.");
//                        }
//
//                        @Override
//                        public String report() {
//                            throw new UnsupportedOperationException("Not supported yet.");
//                        }
//                    });
//        }
    }

    public static HashSet<ArrayList<Integer>> splits(Permutation p, int index, Collection<ArrayList<Integer>> rectangles) {
        HashSet<ArrayList<Integer>> result = new HashSet<ArrayList<Integer>>();
        for (ArrayList<Integer> rectangle : rectangles) {
            result.addAll(splits(p, index, rectangle));
        }
        return result;
    }

    public static HashSet<ArrayList<Integer>> splits(Permutation p, int index, ArrayList<Integer> rectangle) {
        HashSet<ArrayList<Integer>> result = new HashSet<ArrayList<Integer>>();
        ArrayList<Integer> ne = new ArrayList<Integer>();
        ArrayList<Integer> nw = new ArrayList<Integer>();
        ArrayList<Integer> se = new ArrayList<Integer>();
        ArrayList<Integer> sw = new ArrayList<Integer>();
        for (int i : rectangle) {
            if (i < index && p.elements[i] > p.elements[index]) {
                nw.add(i);
            }
            if (i > index && p.elements[i] > p.elements[index]) {
                ne.add(i);
            }
            if (i < index && p.elements[i] < p.elements[index]) {
                sw.add(i);
            }
            if (i > index && p.elements[i] < p.elements[index]) {
                se.add(i);
            }
        }
        if (ne.size() > 0) {
            result.add(ne);
        }
        if (nw.size() > 0) {
            result.add(nw);
        }
        if (se.size() > 0) {
            result.add(se);
        }
        if (sw.size() > 0) {
            result.add(sw);
        }
        return result;
    }

    private static boolean doCheck(Permutation p, HashSet<ArrayList<Integer>> rectangles, int k) {
        if (rectangles.size() > k) {
            return false;
        }
        if (rectangles.size() == 0) {
            return true;
        }
        for (ArrayList<Integer> rectangle : rectangles) {
            for (int i : rectangle) {
                HashSet<ArrayList<Integer>> newRecs = splits(p, i, rectangles);
                if (doCheck(p, newRecs, k)) {
                    System.out.print(i + " ");
                    return true;
                }
            }
        }
        return false;
    }
}
