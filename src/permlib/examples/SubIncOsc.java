/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.PermProperty;

/**
 * Stuff about the class Sub(2 4 1 6 3 8 5 10 7 ...)
 *
 * @author Michael Albert
 */
public class SubIncOsc {

    static final boolean UP = true;
    static final boolean DOWN = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashSet<Permutation> basis = new HashSet<>();
        basis.add(new Permutation("321"));
        PermutationClass c = new PermutationClass(basis);
        for (int n = 4; n <= 8; n++) {
            for (Permutation p : new Permutations(c, n)) {
                if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
                    if (!p.equals(incosc(p.length(), UP)) && !p.equals(incosc(p.length(), DOWN))) {
                        System.out.println(p);
                        basis.add(p);
                    }
                }
            }
            c = new PermutationClass(basis);
        }
//        System.out.println();
//        PrincipalSpectra p = new PrincipalSpectra(new PermutationClass(basis));
//        for (int n = 2; n <= 10; n++) {
//            HashMap<ArrayList<Long>, HashSet<Permutation>> r = p.getSpectra(n, n + 5, false);
//            for (ArrayList<Long> s : r.keySet()) {
//                System.out.print(s + " ");
//                for (Permutation q : r.get(s)) {
//                    System.out.print(sumC(q) + " ");
//                }
//                System.out.println();
//            }
//            System.out.println();
//        }
//        [2757, 6041, 13188, 28683, 62149] 
//        (312)(2413)(312) (312)(312)(2413) (312)(231)(2413) 
//        (2413)(312)(231) (312)(3142)(312) (2413)(231)(231) 
//        (231)(312)(3142) (3142)(312)(312) (231)(3142)(231) 
//        (231)(2413)(231) (231)(231)(3142) (3142)(231)(312)
//        
//        Permutation p312 = new Permutation("312");
//        Permutation p231 = new Permutation("231");
//        Permutation p2413 = new Permutation("2413");
//        Permutation p3142 = new Permutation("3142");
//        
//        Permutation q = sum(p312, p2413, p312);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p312, p312, p2413);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p312, p231, p2413);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p2413, p312, p231);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p312, p3142, p312);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p2413, p231, p231);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p231, p312, p3142);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p3142, p312, p312);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p231, p3142, p231);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p231, p2413, p231);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p231, p231, p3142);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        q = sum(p3142, p231, p312);
//        System.out.println(p.getSpectrum(q,19) + " " + sumC(q));
//        
    }

    public static Permutation incosc(int n, boolean type) {
        if (n == 1) {
            return new Permutation(1);
        }
        if (n == 2) {
            return new Permutation("21");
        }
        if (n == 3 && type == UP) {
            return new Permutation("231");
        }
        if (n == 3 && type == DOWN) {
            return new Permutation("312");
        }

        int[] v = new int[n];

        if (type == UP) {
            v[0] = 2;
            v[1] = 4;
            v[2] = 1;
        } else {
            v[0] = 4;
            v[1] = 1;
            v[2] = 6;
        }

        for (int i = 3; i < n - 1; i++) {
            if (type == UP) {
                v[i] = v[i - 1] + 5;
            } else {
                v[i] = v[i - 1] - 3;
            }
            type = !type;
        }

        if (type == DOWN) {
            v[n - 1] = v[n - 2] - 3;
        } else {
            v[n - 1] = v[n - 2] + 2;
        }

        // System.out.println(Arrays.toString(v));
        return new Permutation(v);
    }

    private static String sumC(Permutation p) {
        StringBuilder result = new StringBuilder();
        Permutation[] c = PermUtilities.sumComponents(p);
        for (Permutation q : c) {
            result.append('(');
            result.append(q);
            result.append(')');
        }
        return result.toString();
    }
    
    public static Permutation sum(Permutation... ps) {
        int n = 0;
        for(Permutation p : ps) n += p.length();
        int[] elements = new int[n];
        int offset = 0;
        int i = 0;
        for(Permutation p : ps) {
            for(int k = 0; k < p.length(); k++) {
                elements[i++] = p.elements[k]+offset;
            }
            offset += p.length();
        }
        return new Permutation(elements, true);
    }

}
