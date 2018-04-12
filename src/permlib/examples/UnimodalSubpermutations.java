/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.utilities.BTree;
import permlib.utilities.Combinations;
import permlib.utilities.Composition;

/**
 *
 * @author MichaelAlbert
 */
public class UnimodalSubpermutations {

    static final Random R = new Random();

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
        doAnneal(26);
    }

    public static void do321(int n) {
        int count = 0;
        long start = System.currentTimeMillis();
        while (true) {
            count++;
            if (count % 10 == 0) {
                System.out.println(count + " trials " + (System.currentTimeMillis() - start));
                // start = System.currentTimeMillis();
            }
            Permutation p = randomAv321(n);
            // System.out.println(p);
            ArrayList<Integer> r = subspectrum(p, n / 2);
            // System.out.println(r);
            if (!unimodal(r)) {
                System.out.println(p);
                System.out.println(r);
            }

        }
    }

    public static void doAll(int n) {
        int count = 0;
        long start = System.currentTimeMillis();
        while (true) {
            count++;
            if (count % 10 == 0) {
                System.out.println(count + " trials " + (System.currentTimeMillis() - start));
                // start = System.currentTimeMillis();
            }
            Permutation p = PermUtilities.randomPermutation(n);
            // System.out.println(p);
            ArrayList<Integer> r = subspectrum(p, n / 2);
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

    private static double penalty(ArrayList<Integer> r) {

        double global = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 1; i < r.size() - 1; i++) {
            double local = Math.max(r.get(i) - r.get(i - 1), r.get(i) - r.get(i + 1)) / ((double) r.get(i));
            global = Math.min(local, global);
            max = Math.max(max, r.get(i));
        }
        return global * 100000.0 / Math.pow(max, 0.5);
    }

    public static Permutation randomAv321(int n) {
        StringBuffer dp = BTree.dyckStringBuffer(RandomBTree.buildTree(n));
        return Av321FromDP(dp.toString());
    }

    private static Permutation Av321FromDP(String dp) {
        //System.out.println(dp);
        int n = dp.length();
        ArrayDeque<Integer> LRMaxValues = new ArrayDeque<Integer>();
        ArrayList<Integer> LRMaxPositions = new ArrayList<Integer>();
        int v = -1;
        int p = 0;
        boolean lastWasOne = false;
        for (char c : dp.toCharArray()) {
            if (c == '1') {
                v++;
                lastWasOne = true;
            } else {
                if (lastWasOne) {
                    LRMaxPositions.add(p);
                    LRMaxValues.add(v);
                    lastWasOne = false;
                }
                p++;
            }
        }
        ArrayDeque<Integer> nonMaxValues = new ArrayDeque<Integer>();
        for (int i = 0; i < n / 2; i++) {
            if (!LRMaxValues.contains(i)) {
                nonMaxValues.add(i);
            }
        }

        int[] elements = new int[n / 2];
        for (int i = 0; i < n / 2; i++) {
            if (!LRMaxPositions.contains(i)) {
                elements[i] = nonMaxValues.poll();
            } else {
                elements[i] = LRMaxValues.poll();
            }
        }

        //System.out.println(Arrays.toString(elements));
        return new Permutation(elements, true);

    }

    private static void doAnneal(int n) {
        long s = System.currentTimeMillis();
        System.out.println("Seed " + s);
        R.setSeed(s);
        int count = 0;
        long start = System.currentTimeMillis();
        double lastPenalty = Double.MAX_VALUE;
        double leastPenalty = Double.MAX_VALUE;
        Permutation p = PermUtilities.randomPermutation(n);
        System.out.println("Initial permutation: " + p);
        ArrayList<Integer> r = subspectrum(p, n / 2);
        lastPenalty = penalty(r);
        leastPenalty = penalty(r);

        while (true) {
            count++;
            if (count % 10 == 0) {
                System.out.println(count + " trials " + (System.currentTimeMillis() - start));
                // start = System.currentTimeMillis();
            }
            // System.out.println(p);
            Permutation q = neighbour(p);
            r = subspectrum(q, n / 2);
            double currentPenalty = penalty(r);
            //System.out.println("Current penalty " + currentPenalty + " for " + q);
            if (currentPenalty < lastPenalty) {
                if (currentPenalty < leastPenalty) {
                    System.out.println("New record " + r + " from " + q + " with penalty " + currentPenalty);
                    leastPenalty = currentPenalty;
                    p = q;
                }
                p = q;
                lastPenalty = currentPenalty;

            } else {
                if (acceptAnyway(currentPenalty - lastPenalty)) {
                    System.out.println("Accepting a non-improvement of " + (currentPenalty - lastPenalty));
                    p = q;
                    lastPenalty = currentPenalty;
                }

            }
            System.out.println("Current penalty " + lastPenalty + " for " + p);
        }
    }

    static double T = 1.0;

    private static boolean acceptAnyway(double d) {

        boolean accept = (R.nextDouble() < Math.exp(-d / T));
        if (accept) {
            T = T / 1.1;
        } else {
            T = T * 1.01;
        }
        return accept;
    }

    private static Permutation neighbour(Permutation p) {
//        int[] qe = Arrays.copyOf(p.elements, p.elements.length);
//        if (R.nextInt(2) == 0) {
//            for (int i = 0; i < qe.length; i++) {
//                qe[i] = 2 * qe[i] + 1;
//            }
//            int val = R.nextInt(2 * qe.length + 1);
//            int pos = R.nextInt(qe.length);
//            qe[pos] = val;
//            return new Permutation(qe);
//        }
//        int i = R.nextInt(qe.length);
//        int j = R.nextInt(qe.length);
//        int a = (i < j) ? i : j;
//        int b = i + j - a;
//        int v = qe[a];
//        for (i = a; i < b; i++) {
//            qe[i] = qe[i + 1];
//        }
//        qe[b] = v;
        return modifySubset(p);
    }
    
    private static Permutation modifySubset(Permutation p) {
        
        int subsetSize = 2;
        while (R.nextInt(3) > 0 && subsetSize <= p.length()) subsetSize++;
        int[] pos = Combinations.random(p.length(), subsetSize, R);
        Permutation q = PermUtilities.randomPermutation(subsetSize, R);
        int[] re = Arrays.copyOf(p.elements, p.elements.length);
        for(int i = 0; i < pos.length; i++) {
            re[pos[i]] = p.elements[pos[q.elements[i]]];
        }
        return new Permutation(re, true);
        
    }

}
