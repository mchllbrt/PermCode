/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.Arrays;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.MultiPermutationClass;
import permlib.classes.PermutationClass;
import permlib.property.Involves;

/**
 * Is the bound given in the IMO problem 2017 tight?
 *
 * @author Michael Albert
 */
public class IMO5 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n = 4;
        // for(Permutation p : basis(n)) System.out.println(p);
        MultiPermutationClass c = new MultiPermutationClass(basis(n));
        Permutation p = new Permutation("1 5 9 13 17 2 6 10 14 18 3 7 11 15 19 4 8 12 16");
        System.out.println(c.containsPermutation(p));
        System.out.println(p);
        for(Permutation q : basis(n)) {
            Involves i = new Involves(q);
            if (i.isSatisfiedBy(p)) {
                System.out.print(q + " ");
                System.out.println(Arrays.toString(i.involvedWhere(p)));
            }
        }
        // generateExamples(c, 16, 18);
//        int count = 0;
//        long trials = 0;
//        HashSet<Permutation> egs = new HashSet<>();
//        for (Permutation p : new Permutations(c, 18, 20)) {
//            System.out.println(p);
//        }
//        long t = System.currentTimeMillis();
//        while (count < 30) {
//            Permutation p = PermUtilities.randomPermutation(n*(n+1)-2);
//            if (c.containsPermutation(p)) {
//                System.out.println(p);
//                count++;
//                egs.add(p);
//            }
//            trials++;
//            if (trials % 1000000 == 0) {
//                System.out.println("Trials " + trials / 1000000 + "M");
//                if (egs.size() > 0) {
//                    for (Permutation q : egs) System.out.println(q);
//                }
//            }
//        }
//        System.out.println(System.currentTimeMillis()-t);
//        AnimatedPermFrame f = new AnimatedPermFrame(egs);
//        f.setVisible(true);

        //  for(Permutation p : c.getPerms(6)) System.out.println(p);
//        PermCounter proc = new PermCounter();
//        for(int m = 2*n+1; true; m++) {
//            c.processPerms(m, proc);
//            System.out.println(m + " " + proc.getCount());
//            if (proc.getCount() == 0) break;
//            proc.reset();
//        }
//        AnimatedPermFrame f = new AnimatedPermFrame(c.getPerms(11));
//        f.setVisible(true);
        //for (Permutation p : c.getPerms(11)) System.out.println(p);
//        Permutation p = tiltedSquare(n);
//        System.out.println(p);
//        System.out.println(c.containsPermutation(p));
    }

    public static HashSet<Permutation> basis(int n) {
        HashSet<Permutation> result = new HashSet<>();
        for (Permutation p : new Permutations(n)) {
            result.addAll(inflations(p));
        }
        return result;
    }

    public static HashSet<Permutation> inflations(Permutation p) {
        HashSet<Permutation> result = new HashSet<>();
        for (int k = 0; k < (1 << p.length()); k++) {
//            Permutation q = inflateMonotone(p, sizes(p.length(), k));
//            System.out.println(q);
            result.add(inflateMonotone(p, sizes(p.length(), k)));
        }
//        System.out.println();
        return result;
    }

    public static Permutation inflateMonotone(Permutation p, int[] sizes) {
        int sum = 0;
        for (int s : sizes) {
            sum += (s > 0) ? s : -s;
        }
        int[] vLow = new int[p.length()];
        for (int i = 0; i < p.length(); i++) {
            for (int j = i + 1; j < p.length(); j++) {
                if (p.elements[i] < p.elements[j]) {
                    vLow[j] += (sizes[i] > 0) ? sizes[i] : -sizes[i];
                } else {
                    vLow[i] += (sizes[j] > 0) ? sizes[j] : -sizes[j];
                }
            }
        }
        int[] result = new int[sum];
        int ri = 0;
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] > 0) {
                int vL = vLow[i];
                for (int j = 0; j < sizes[i]; j++) {
                    result[ri++] = vL++;
                }
            } else {
                int vH = vLow[i] - sizes[i] - 1;
                for (int j = 0; j < -sizes[i]; j++) {
                    result[ri++] = vH--;
                }
            }
        }
        return new Permutation(result);
    }

    public static int[] sizes(int n, int code) {
        int[] result = new int[n];
        for (int k = 0; k < n; k++) {
            result[k] = ((code >> k & 1) != 0) ? -2 : 2;
        }
        // System.out.println(Arrays.toString(result));
        return result;
    }

    public static Permutation tiltedSquare(int n) {
        int[] result = new int[n * n];
        int i = 0;
        for (int col = 0; col < n; col++) {
            for (int row = 0; row < n; row++) {
                result[i] = col + n * row;
                i++;
            }
        }
        return new Permutation(result);
    }

    public static void generateExamples(PermutationClass c, int n, int printAtLength) {
        long trials = 0;
        while (true) {
            Permutation p = PermUtilities.randomPermutation(n);
            if (c.containsPermutation(p)) {
                if (p.length() >= printAtLength) {
                    System.out.println(p);
                }
                generateExtension(c,p, printAtLength);
            }
            trials++;
            if (trials % 1000000 == 0) {
                System.out.println("T: " + (trials / 1000000) + "M");
            }
        }

    }

    public static boolean generateExtension(PermutationClass c, Permutation p, int printAtLength) {
        boolean result = false;
        for (Permutation q : PermUtilities.onePointExtensions(p)) {
            if (c.containsPermutation(q)) {
                if (q.length() >= printAtLength) System.out.println("(" + q.length() + ") " + q);
                generateExtension(c, q, printAtLength);
                result = true;
            }
        }
        return result;
    }
}
