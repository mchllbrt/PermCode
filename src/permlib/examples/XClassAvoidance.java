/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Symmetry;
import permlib.utilities.Partitions;

/**
 * Check for WE in the X-class using recurrences
 *
 * @author Michael Albert
 */
public class XClassAvoidance {

    static HashMap<Permutation, ArrayList<Long>> spectra = new HashMap<>();
    static HashMap<Permutation, ArrayList<Long>> newSpectra = new HashMap<>();

    static int generationCount = 0;

    public static void main(String[] args) {
        //foo();
        generateSpectra(5, 6);
        showSpectra(5);
    }

    static void doGeneration(boolean addnew) {
        for (Permutation p : spectra.keySet()) {
            if (addnew && p.length() == generationCount) {
                for (Symmetry s : Symmetry.values()) {
                    Permutation ps = s.on(p);
                    Permutation q = PermUtilities.sum(Permutation.ONE, ps);
                    if (PermUtilities.isSymmetryRep(q)) {
                        initialise(q);
                    }
                }
            }
            update(p);
        }
        for (Permutation q : newSpectra.keySet()) {
            spectra.put(q, newSpectra.get(q));
        }
        newSpectra.clear();
        generationCount++;

    }

    private static void initialise(Permutation q) {
        ArrayList<Long> qs = new ArrayList<>();
        for (int i = 0; i < q.length(); i++) {
            qs.add(0L);
        }
        qs.add(1L);
        newSpectra.put(q, qs);
    }

    private static void update(Permutation p) {
        if (p.length() <= 1) {
            long result = 0L;
            result += 4 * spectra.get(p).get(generationCount);
            result -= 2 * spectra.get(p).get(generationCount - 1);
            spectra.get(p).add(result);
            return;
        }
        if (p.at(p.length() - 1) != p.length() - 1) {
            Permutation q = PermUtilities.symmetryRep(p.delete(0));
            long result = 0L;
            result += spectra.get(q).get(generationCount);
            result += 3 * spectra.get(p).get(generationCount);
            result -= spectra.get(q).get(generationCount - 1);
            result -= spectra.get(p).get(generationCount - 1);
            spectra.get(p).add(result);
        } else {
            Permutation ql = PermUtilities.symmetryRep(p.delete(0));
            Permutation qr = PermUtilities.symmetryRep(p.delete(p.length() - 1));
            Permutation qlr = PermUtilities.symmetryRep(p.delete(p.length() - 1).delete(0));
            long result = 0L;
            result += spectra.get(ql).get(generationCount);
            result += 2 * spectra.get(p).get(generationCount);
            result += spectra.get(qr).get(generationCount);
            result -= spectra.get(qlr).get(generationCount - 1);
            result -= spectra.get(p).get(generationCount - 1);
            spectra.get(p).add(result);
        }
    }

    private static void collectSpectra(int i) {
        HashMap<ArrayList<Long>, HashSet<Permutation>> classes = new HashMap<>();
        for (Permutation p : spectra.keySet()) {
            if (p.length() == i) {
                ArrayList<Long> sp = spectra.get(p);
                if (!classes.containsKey(sp)) {
                    classes.put(sp, new HashSet<Permutation>());
                }
                classes.get(sp).add(p);
            }
        }
        System.out.println(i + " " + classes.size());
    }

    private static void foo() {
        for (int n = 3; n <= 12; n++) {
            int result = 0;
            for (ArrayList<Integer> c : new Compositions(n).get()) {
                if (c.get(0) < 2) {
                    continue;
                }
                int cr = 1;
                for (int i = 1; i < c.size(); i++) {
                    if (c.get(i) >= 2) {
                        cr *= c.get(i) / 2;
                    }
                }
                result += cr;
            }
            System.out.println(n + " " + result);
        }
    }

    private static void showSpectra(int i) {
        HashMap<ArrayList<Long>, HashSet<Permutation>> classes = new HashMap<>();
        for (Permutation p : spectra.keySet()) {
            if (p.length() == i) {
                ArrayList<Long> sp = spectra.get(p);
                if (!classes.containsKey(sp)) {
                    classes.put(sp, new HashSet<Permutation>());
                }
                classes.get(sp).add(p);
            }
        }
        for (ArrayList<Long> spec : classes.keySet()) {
            System.out.print(spec + " ");
            for (Permutation p : classes.get(spec)) {
                System.out.print(p + " ");
            }
            System.out.println();
        }
    }

    static class Compositions {

        ArrayList<ArrayList<Integer>> cs = new ArrayList<>();

        public Compositions(int n) {
            if (n == 0) {
                cs.add(new ArrayList<Integer>());
                return;
            }
            for (int k = 1; k <= n; k++) {
                for (ArrayList<Integer> pre : (new Compositions(n - k).get())) {
                    ArrayList<Integer> c = new ArrayList<>(pre);
                    c.add(k);
                    cs.add(c);
                }
            }
        }

        public ArrayList<ArrayList<Integer>> get() {
            return cs;
        }
    }

    static void generateSpectra(int n, int extraTerms) {
        initialise(new Permutation());
        initialise(Permutation.ONE);
        initialise(new Permutation("12"));
        for (Permutation q : newSpectra.keySet()) {
            spectra.put(q, newSpectra.get(q));
        }
        newSpectra.clear();
        spectra.get(Permutation.ONE).add(2L);
        spectra.get(new Permutation()).add(1L);
        spectra.get(new Permutation()).add(2L);
        generationCount = 2;
        for (int i = 0; i < n; i++) {
            doGeneration(true);
        }
        for (int i = 0; i < extraTerms; i++) {
            doGeneration(false);
        }
//        for (int i = 3; i <= n; i++) {
//            collectSpectra(i);
//        }
    }

}
