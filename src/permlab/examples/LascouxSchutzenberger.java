/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import permlib.PermStatistics;
import permlib.classes.MultiPermutationClass;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.Permutations;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 *
 * @author MichaelAlbert
 */
public class LascouxSchutzenberger {

    static HereditaryProperty A2143 = AvoidanceTest.getTest("2143");
    
    static String[][] s = {{"2143"},
        {"21543",
            "32154",
            "315264",
            "215364",
            "214365",
            "426153",
            "231564",
            "241365",
            "312645",
            "314265",
            "241635",
            "214635",
            "5472163",
            "4265173",
            "5173264",
            "5276143",
            "5271436",
            "2547163",
            "51763284",
            "25476183",
            "61837254",
            "65821437",
            "54726183",
            "64821537",
            "26587143",
            "65872143",
            "51736284",
            "26581437",
            "26481537",
            "61873254",
            "64872153",
            "61832547",
            "54762183",
            "26487153",
            "65827143"},
        {"316254",
            "214365",
            "326154",
            "426153",
            "231654",
            "432165",
            "325164",
            "312654",
            "241653",
            "321654",
            "421653",
            "421635",
            "321645",
            "216543",
            "321564",
            "5314276",
            "3162475",
            "3416275",
            "2514736",
            "3165274",
            "3514276",
            "3521476",
            "2541376",
            "4253176",
            "2165374",
            "2513746",
            "5472163",
            "2316475",
            "4163275",
            "2415736",
            "2315746",
            "5241376",
            "4153276",
            "2413675",
            "2164735",
            "2164753",
            "3126475",
            "3512746",
            "4251376",
            "4265173",
            "3152746",
            "2157436",
            "5173264",
            "2413756",
            "2157364",
            "5276143",
            "5271436",
            "2416375",
            "2157463",
            "2517436",
            "3146275",
            "3142756",
            "2547163",
            "2163754",
            "4315276",
            "2416735",
            "3524176",
            "3142675",
            "2147635",
            "3162745",
            "3164275",
            "2541736",
            "2175364",
            "3125746",
            "2174635",
            "51763284",
            "21468357",
            "23416785",
            "25476183",
            "61837254",
            "65821437",
            "54726183",
            "64821537",
            "26587143",
            "65872143",
            "51736284",
            "41526387",
            "26581437",
            "34127856",
            "41238567",
            "26481537",
            "61873254",
            "41627385",
            "64872153",
            "24618357",
            "21637485",
            "24613587",
            "61832547",
            "54762183",
            "26487153",
            "65827143"}};

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Permutation p = new Permutation("2436517");
//        RS rs = rs(p);
////        System.out.println(rs.r + " " + rs.s);
//        for (Permutation q : children(p)) {
//            System.out.println(q);
//        }
        
//        for(Permutation p : new Permutations(4)) {
//            System.out.println(p + " " + vex(p));
//        } 
          System.out.println(vex(new Permutation("321465")));
          basisTo(8);
    }

    public static RS rs(Permutation p) {
        RS result = new RS();
        int r = p.elements.length - 2;
        while (r >= 0 && p.elements[r] < p.elements[r + 1]) {
            r--;
        }
        result.r = r;
        int s = p.elements.length - 1;
        while (p.elements[s] > p.elements[r]) {
            s--;
        }
        result.s = s;
        return result;
    }

    static class RS {

        int r, s;
    }

    public static ArrayDeque<Permutation> children(Permutation p) {
        ArrayDeque<Permutation> result = new ArrayDeque<Permutation>();
        if (A2143.isSatisfiedBy(p)) {
            return result;
        }
        int pInv = PermStatistics.inversions(p);
//        System.out.println(pInv);
        RS rs = rs(p);
        for (int j = 0; j < p.length(); j++) {
            if (j != rs.r) {
                Permutation q = p.clone();
                int v = q.elements[j];
                q.elements[j] = q.elements[rs.s];
                q.elements[rs.s] = q.elements[rs.r];
                q.elements[rs.r] = v;
                // System.out.println(q + " " + q.inversions());
                if (PermStatistics.inversions(q) == pInv) {
                    result.add(q);
                }
            }
        }
        if (result.isEmpty()) {
            return children(PermUtilities.sum(Permutation.ONE, p));
        }
        return result;
    }
    
    public static int vex(Permutation p) {
        int result = 0;
        for(Permutation q : children(p)) result += vex(q);
        return (result == 0) ? 1 : result;
    }
    
    public static boolean vexBound(Permutation p, int k) {
        ArrayDeque<Permutation> q = new ArrayDeque<Permutation>();
        q.add(p);
        int leaves = 0;
        while (!q.isEmpty()) {
            Collection<Permutation> children = children(q.poll());
            if (children.isEmpty()) {
                leaves++;
            } else {
                q.addAll(children);
            }
            if (q.size() + leaves > k) return false;
            
        }
        return q.size() + leaves <= k;
    }
    
    public static void basisTo(int m) {
        HereditaryProperty h = new HereditaryProperty() {
            @Override
            public Collection<Permutation> getBasis() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return vexBound(p,4);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        PermutationClass v1 = new PermutationClass("2143");
        PermutationClass v2 = new PermutationClass(s[1]);
        PermutationClass v3 = new PermutationClass(s[2]);       
        HashSet<Permutation> basis = new HashSet<Permutation>();
        MultiPermutationClass c = new MultiPermutationClass(basis);
        for (int n = 4; n <= 16; n++) {
            System.out.println("Length " + n);
            for (Permutation p : new Permutations(c, n)) {
//                System.out.print(p);
                if (v1.containsPermutation(p) || v2.containsPermutation(p) || v3.containsPermutation(p)) {
                    // System.out.println(" in vex");
                    continue;
                }
//                System.out.println(" " + onePerm(p));
//                System.out.println("Testing " + p);
                if (!h.isSatisfiedBy(p)) {
                    basis.add(p);
                    System.out.println(p);
                }
            }
            c = new MultiPermutationClass(basis);
        }
    }
}
