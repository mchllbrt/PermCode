/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import static permlib.PermUtilities.skewSum;
import static permlib.PermUtilities.sum;
import permlib.Permutation;
import static permlib.Permutation.ONE;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;
import permlib.processor.PermProcessor;

/**
 *
 * @author MichaelAlbert
 */
public class Av231PlusStuff {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println(tikzMatchingForPerm(new int[] {3,1,0,2,7,4,6,5}));
//        generateClasses(10, false);
//        int[] counts = new int[11];
//        for (Permutation p : reps) {
////            if (p.length() == 10) {
////                System.out.println(Arrays.toString(p.elements));
////            }
//            counts[p.length()]++;
//            if (p.length() == 8) System.out.println(p);
//        }
//        for(int i =1; i < counts.length; i++) System.out.println(counts[i]);
        
//        int nMax = 12;
//        for (int n = 3; n <= nMax; n++) {
//            generateClasses(n, false);
//            int m1 = 0;
//            int m2 = 0;
//            int m3 = 0;
//            for (Permutation p : reps) {
//                if (p.length() == n) {
//                    int m = countLRMax(p);
//                    if (m == 1) {
//                        m1++;
//                    } else if (m == 2) {
//                        m2++;
//                    } else {
//                        m3++;
//                    };
//                }
//            }
//            System.out.println(n + ": " + m1 + " " + m2 + " " + m3);
//        }
//        
//        for (Permutation p : reps) {
//            if (p.length() == 12 && countLRMax(p) == 2) System.out.println(p);
//        }
        //System.out.println(Arrays.toString(p.elements));
        // checkSpectra(n);
        // fullSpectra(n, 15, true);
//        do321();
    }

    public static void doCounts(Permutation p, int low, int high) {
        PermutationClass c = new PermutationClass(new Permutation("231"), p);
        PermCounter ct = new PermCounter();
        for (int n = low; n < high; n++) {
            c.processPerms(n, ct);
            System.out.print(ct.getCount() + " ");
            ct.reset();
        }
        System.out.println();
    }

    public static Permutation sum3(Permutation a, Permutation b, Permutation c) {
        return PermUtilities.sum(PermUtilities.sum(a, b), c);
    }

    public static void doAll(Permutation a, Permutation b, Permutation c, int low, int high) {
        doCounts(sum3(a, b, c), low, high);
        doCounts(sum3(a, c, b), low, high);
        doCounts(sum3(b, a, c), low, high);
        doCounts(sum3(b, c, a), low, high);
        doCounts(sum3(c, a, b), low, high);
        doCounts(sum3(c, b, a), low, high);

    }

    public static void doX(int n) {
        PermutationClass c = new PermutationClass("231");
        for (Permutation q : new Permutations(c, n)) {
            Permutation a = q.insert(0, 0);
            Permutation b = q.insert(q.length(), q.length());
            PermutationClass ca = new PermutationClass(new Permutation("231"), a);
            PermutationClass cb = new PermutationClass(new Permutation("231"), b);
            for (int m = n + 2; m < n + 8; m++) {
                PermCounter ct = new PermCounter();
                ca.processPerms(m, ct);
                long cta = ct.getCount();
                ct.reset();
                cb.processPerms(m, ct);
                long ctb = ct.getCount();
                ct.reset();
                if (cta != ctb) {
                    System.out.println(a + " " + b);
                    break;
                }
            }
        }
        System.out.println("Done");
    }

    public static void doWilf(int nMin, int nMax, boolean verbose, int checkTotal) {
        PermutationClass c = new PermutationClass("231");
        HashMap<Spectrum, HashSet<Permutation>> wilfClasses = new HashMap<Spectrum, HashSet<Permutation>>();
        for (int n = nMin; n <= nMax; n++) {
            for (Permutation q : new Permutations(c, n)) {
                long[] qC = new long[checkTotal];
                PermutationClass d = new PermutationClass(new Permutation("231"), q);
                for (int m = n + 1; m <= n + checkTotal; m++) {
                    PermCounter counter = new PermCounter();
                    d.processPerms(m, counter);
                    qC[m - n - 1] = counter.getCount();
                }
                // System.out.println(q + " " + Arrays.toString(qC));
                Spectrum qs = new Spectrum(qC);
                if (wilfClasses.containsKey(qs)) {
                    wilfClasses.get(qs).add(q);
                } else {
                    HashSet<Permutation> aSet = new HashSet<Permutation>();
                    aSet.add(q);
                    wilfClasses.put(qs, aSet);
                }
            }
            System.out.println("Length " + n + " has " + wilfClasses.keySet().size() + " classes");
            if (verbose) {
                System.out.println();
                for (Spectrum s : wilfClasses.keySet()) {
                    System.out.println(Arrays.toString(s.counts) + " " + (wilfClasses.get(s).size()));
                    System.out.println();
                    for (Permutation q : wilfClasses.get(s)) {
                        System.out.println(tikzMatchingForPerm(q.elements));
                        System.out.println();
                    }
                    System.out.println();
                }
            }
            wilfClasses.clear();

        }

    }

    public static HashSet<HashSet<Permutation>> buildClasses(int n) {
        if (n == 1) {
            HashSet<HashSet<Permutation>> result = new HashSet<HashSet<Permutation>>();
            HashSet<Permutation> c = new HashSet<Permutation>();
            c.add(new Permutation("1"));
            result.add(c);
            return result;
        }
        return buildClasses(n, buildClasses(n - 1));
    }

    private static HashSet<HashSet<Permutation>> buildClasses(int n, HashSet<HashSet<Permutation>> parentClasses) {
        HashSet<HashSet<Permutation>> result = new HashSet<HashSet<Permutation>>();
        HashSet<Permutation> ps = new HashSet<Permutation>();
        for (HashSet<Permutation> parentClass : parentClasses) {
            HashSet<Permutation> childClass = new HashSet<Permutation>();
            for (Permutation p : parentClass) {
                childClass.addAll(children(p));
            }
            result.add(childClass);
            ps.addAll(childClass);
        }
        PermutationClass c = new PermutationClass(new Permutation("231"));
        for (Permutation q : new Permutations(c, n)) {
            if (!ps.contains(q)) {
                HashSet<Permutation> eqClass = equivClass(q);
                result.add(eqClass);
                ps.addAll(eqClass);
                System.out.println(q);
            }
        }
        return result;
    }

    public static HashSet<Permutation> children(Permutation p) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
            result.add(sum(p, new Permutation("1")));
            result.add(sum(new Permutation("1"), p));
        }
        result.add(skewSum(new Permutation("1"), p));
        return result;
    }

    private static HashSet<Permutation> equivClass(Permutation q) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        Permutation[] scq = sumComponents(q);
        for (Permutation r : new Permutations(scq.length)) {
            Permutation s = new Permutation();
            for (int i = 0; i < scq.length; i++) {
                s = sum(s, scq[r.elements[i]]);
            }
            result.add(s);
        }
        return result;
    }

    private static Permutation[] sumComponents(Permutation q) {
        int[] e = q.elements;
        int low = 0;
        int m = e[0];
        ArrayList<Permutation> comps = new ArrayList<Permutation>();
        int compCount = 1;
        for (int i = 1; i < e.length; i++) {
            if (e[i] > m) {
                compCount++;
                m = e[i];
                comps.add(q.segment(low, i));
                low = i;
            }
        }
        comps.add(q.segment(low, q.length()));
        Permutation[] result = new Permutation[comps.size()];
        comps.toArray(result);
        return result;
    }

    private static boolean isDecreasing(Permutation p) {
        for (int i = 1; i < p.length(); i++) {
            if (p.elements[i] > p.elements[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static void checkSpectra(int i) {
        PermutationClass c = new PermutationClass("231");
        PermCounter counter = new PermCounter();
        c.processPerms(i, counter);
        long l = counter.getCount() - 1;
        HashMap<Permutation, ArrayList<Long>> spectra = new HashMap<>();
        HashSet<Permutation> block = new HashSet<Permutation>();
        for (Permutation p : eqClasses.keySet()) {
            if (p.length() == i) {
                block.add(p);
                spectra.put(p, new ArrayList<Long>());
                spectra.get(p).add(l);
            }
        }
        splitBlock(block, i + 1, spectra);
        for (Permutation p : block) {
            System.out.println(p + " " + spectra.get(p));
        }
    }

    public static void splitBlock(HashSet<Permutation> block, int m, HashMap<Permutation, ArrayList<Long>> spectra) {
        if (block.size() <= 1) {
            return;
        }
//        if (m > 16) {
//            System.out.println("Giving up");
//            return;
//        }
        System.out.println("Splitting at length " + m + " with block size " + block.size());
        if (block.size() <= 4) {
            for (Permutation p : block) {
                System.out.print(p + " ");
            }
            System.out.println();
        }
        Permutation bca = new Permutation("231");
        HashMap<Long, HashSet<Permutation>> blocks = new HashMap<>();
        PermCounter counter = new PermCounter();
        for (Permutation p : block) {
            PermutationClass d = new PermutationClass(bca, p);
            d.processPerms(m, counter);
            if (blocks.containsKey(counter.getCount())) {
                HashSet<Permutation> b = blocks.get(counter.getCount());
                b.add(p);
            } else {
                HashSet<Permutation> b = new HashSet<>();
                b.add(p);
                blocks.put(counter.getCount(), b);
            }
            spectra.get(p).add(counter.getCount());
            counter.reset();
        }
        for (long l : blocks.keySet()) {
            splitBlock(blocks.get(l), m + 1, spectra);
        }

    }

    private static void fullSpectra(int n, final int maxLength, boolean exact) {
        Permutation bca = new Permutation("231");
        PermProcessor c = new PermProcessor() {
            long[] counts = new long[maxLength + 1];

            @Override
            public void process(Permutation p) {
                if (p.length() <= maxLength) {
                    counts[p.length()]++;
                }
            }

            @Override
            public void reset() {
                for (int i = 0; i < counts.length; i++) {
                    counts[i] = 0;
                }
            }

            @Override
            public String report() {
                StringBuilder result = new StringBuilder();
                for (long c : counts) {
                    result.append(c);
                    result.append(' ');
                }
                result.deleteCharAt(result.length() - 1);
                return result.toString();
            }

        };
        for (Permutation p : reps) {
            if (!exact || p.length() == n) {
                PermutationClass d = new PermutationClass(bca, p);
                c.reset();
                d.processPerms(0, maxLength, c);
                System.out.println(p + " " + c.report());
            }
        }
    }

    private static void do321() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static class Spectrum {

        long[] counts;

        public Spectrum(long[] counts) {
            this.counts = Arrays.copyOf(counts, counts.length);
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 71 * hash + Arrays.hashCode(this.counts);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Spectrum other = (Spectrum) obj;
            if (!Arrays.equals(this.counts, other.counts)) {
                return false;
            }
            return true;
        }

    }

    public static String tikzMatchingForPerm(int[] p) {
        StringBuilder result = new StringBuilder();
        result.append("\\begin{tikzpicture}\n");
        result.append("\\draw (-0.5,0) -- (" + (2 * p.length - 0.5) + ",0);\n");
        result.append("\\foreach \\x in {0,1,...," + (2 * p.length - 1) + "} \n");
        result.append("  \\fill (\\x, 0) circle (0.05);\n");
        addArcs(result, p, 0, 0, p.length);
        result.append("\\end{tikzpicture}");
        return result.toString();
    }

    private static void addArcs(StringBuilder result, int[] p, int fromPoint, int fromIndex, int toIndex) {
        int arcLength = 1;
        int i;
        for (i = fromIndex + 1; i < toIndex && p[i] < p[fromIndex]; i++) {
            arcLength += 2;
        }
        result.append("\\draw (" + fromPoint + ",0)  arc[radius = " + arcLength + "/sqrt(2), start angle=135, end angle=45];\n");
        if (i > fromIndex + 1) {
            addArcs(result, p, fromPoint + 1, fromIndex + 1, i);
        }
        if (i < toIndex) {
            addArcs(result, p, fromPoint + arcLength + 1, i, toIndex);
        }
    }

    public static int countLRMax(Permutation p) {
        int result = 1;
        int m = p.elements[0];
        for (int i = 1; i < p.elements.length; i++) {
            if (p.elements[i] > m) {
                result++;
                m = p.elements[i];
            }
        }
        return result;
    }

    public static HashMap<Permutation, Permutation> normalForms = new HashMap<>();
    public static HashMap<Permutation, HashSet<Permutation>> eqClasses = new HashMap<>();
    public static Permutation[] reps;

    public static void generateClasses(int n, boolean showClasses) {
        normalForms.put(ONE, ONE);
        normalForms.put(new Permutation(), new Permutation());
        PermutationClass c = new PermutationClass("231");
        // System.out.println(transform(new Permutation("132")));
        int classes = 0;
        for (int k = 2; k <= n; k++) {
            for (Permutation p : new Permutations(c, k)) {
                transform(p);
            }
            // System.out.println(k + " " + (eqClasses.keySet().size() - classes));
            classes = eqClasses.keySet().size();
            if (showClasses) {
                for (Permutation p : eqClasses.keySet()) {
                    if (p.length() == k) {
                        for (Permutation q : eqClasses.get(p)) {
                            System.out.println(Arrays.toString(q.elements));
                        }
                        System.out.println();
                        System.out.println("-----------------------");
                    }
                }
                System.out.println();
            }
        }
        reps = new Permutation[eqClasses.keySet().size()];
        eqClasses.keySet().toArray(reps);
        Arrays.sort(reps);
//        for(Permutation p : reps) {
//            // System.out.print(p + " : ");
//            PermutationClass d = new PermutationClass(new Permutation("231"), p);
//            PermCounter counter = new PermCounter();
//            for(int m = p.length() + 1; m < p.length() + 7; m++) {
//                d.processPerms(m, counter);
//                System.out.print(counter.getCount() + " ");
//                counter.reset();
//            }
//            System.out.println(": " + p);
//        }
    }

    public static Permutation transform(Permutation p) {
        // System.out.print("Transforming " + p + " to ");
        Permutation result = normalForms.get(p);
        if (result != null) {
            // System.out.println(result);
            return result;
        }
        Permutation[] sc = sumComponents(p);
        if (sc.length == 1) {
            // System.out.println("A");
            // n - transform under arch
            result = skewSum(Permutation.ONE, transform(p.segment(1, p.length())));
        } else if (sc.length == 2) {
            Permutation[] nsc = new Permutation[2];
            nsc[0] = transform(sc[0]);
            nsc[1] = transform(sc[1]);
            Arrays.sort(nsc);
            if (nsc[0].length() == 1 || (nsc[0].length() > 1 && nsc[0].elements[0] - 1 == nsc[0].elements[1])) {
                nsc[0] = nsc[0].segment(1, nsc[0].length());
                result = skewSum(ONE, transform(sum(nsc[0], nsc[1])));
            } else if (nsc[1].length() == 1 || (nsc[1].length() > 1 && nsc[1].elements[0] - 1 == nsc[1].elements[1])) {
                nsc[1] = nsc[1].segment(1, nsc[1].length());
                result = skewSum(ONE, transform(sum(nsc[0], nsc[1])));
            } else {
                result = sum(nsc[0], nsc[1]);
            }
//        } else if (sc.length == 2 && isDecreasing(sc[1])) {
//            result = skewSum(sc[1], transform(sc[0]));
//        } else if (sc.length == 2 && (p.elements[1] == p.elements[0] - 1 || sc[0].length() == 1)) {
//            int maxPos = 1;
//            while (p.elements[maxPos] != p.length() - 1) {
//                maxPos++;
//            }
//            result = transform(sum(
//                    p.segment(1, maxPos),
//                    skewSum(
//                            ONE,
//                            transform(p.segment(maxPos, p.length()))
//                    )
//            ));
        } else {
            Permutation[] nfc = new Permutation[sc.length];
            for (int i = 0; i < nfc.length; i++) {
                nfc[i] = transform(sc[i]);
            }
            Arrays.sort(nfc);
            result = nfc[0];
            for (int i = 1; i < nfc.length; i++) {
                result = sum(result, nfc[i]);
            }

        }
        // System.out.println(result + " adding form");
        normalForms.put(p, result);
        if (eqClasses.containsKey(result)) {
            HashSet<Permutation> eqClass = eqClasses.get(result);
            eqClass.add(p);
        } else {
            HashSet<Permutation> eqClass = new HashSet<Permutation>();
            eqClass.add(p);
            eqClasses.put(result, eqClass);
        }
        return result;
    }

}


// \draw (0,0)  arc[radius = 3/sqrt(2), start angle=135, end angle=45];
