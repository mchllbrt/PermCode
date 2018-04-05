package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;
import permlib.property.AvoidanceTest;

/**
 * Random investigations of Wilf-collapse.
 *
 * @author Michael Albert
 */
public class WilfCollapse {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        PermutationClass c = new PermutationClass("231", "4132");
//        showSpectrum(c, new Permutation("132457689"), 10,15);
//        showSpectrum(c, new Permutation("132456879"), 10,15);
//        showSpectrum(c, new Permutation("123546798"), 10,15);
//        showSpectrum(c, new Permutation("132465789"), 10,15);

        PermutationClass c = new PermutationClass("231", "4132");
        for (int n = 2; n <= 8; n++) {
            showSpectra(c, new PermutationClass("312", "231"), n, 2 * n);
            System.out.println();
        }

    }

    /**
     * Sum closure of Sub(4132).
     */
    static PermutationClass buildClass(int n) {
        HashSet<Permutation> inds = new HashSet<>();
        inds.add(new Permutation("1"));
        inds.add(new Permutation("21"));
        inds.add(new Permutation("321"));
        inds.add(new Permutation("312"));
        inds.add(new Permutation("4132"));
        HashSet<Permutation> result = new HashSet<>();
        result.add(new Permutation("231"));
        PermutationClass c = new PermutationClass(result);
        for (int m = 4; m <= n; m++) {
            for (Permutation p : new Permutations(c, m)) {
                if (!inds.contains(p) && PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
                    // System.out.println(p);
                    result.add(p);
                }
            }
            c = new PermutationClass(result);
        }
        return new PermutationClass(result);
    }

    static PermutationClass buildClass312(int n) {
        HashSet<Permutation> inds = new HashSet<>();
        inds.add(new Permutation("1"));
        inds.add(new Permutation("21"));
        inds.add(new Permutation("312"));
        HashSet<Permutation> result = new HashSet<>();
        result.add(new Permutation("231"));
        result.add(new Permutation("321"));
        PermutationClass c = new PermutationClass(result);
        for (int m = 4; m <= n; m++) {
            for (Permutation p : new Permutations(c, m)) {
                if (!inds.contains(p) && PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
                    System.out.println(p);
                    result.add(p);
                }
            }
            c = new PermutationClass(result);
        }
        return new PermutationClass(result);
    }

    static PermutationClass buildClass123(int n) {
        PermutationClass a123 = new PermutationClass("123");
        HashSet<Permutation> result = new HashSet<>();
        PermutationClass c = new PermutationClass(result);
        for (int m = 2; m <= n; m++) {
            for (Permutation p : new Permutations(c, m)) {
                if (!a123.containsPermutation(p) && PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
                    // System.out.println(p);
                    result.add(p);
                }
            }
            c = new PermutationClass(result);
        }
        for (Permutation p : result) {
            System.out.print(p + " ");
        }
        System.out.println();
        return new PermutationClass(result);
    }

    static PermutationClass bc(int n) {
        PermutationClass c = new PermutationClass("132", "213", "123", "231");
        HashSet<Permutation> basis = new HashSet<>();
        PermutationClass result = new PermutationClass(basis);
        for (int m = 4; m <= n; m++) {
            for (Permutation p : new Permutations(result, m)) {
                if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p) && !c.containsPermutation(p)) {
                    // System.out.println(p);
                    basis.add(p);
                }
            }
            result = new PermutationClass(basis);
        }

        return result;
    }

    private static void showSpectrum(PermutationClass c, Permutation p, int i, int high) {
        PermCounter counter = new PermCounter(AvoidanceTest.getTest(p));
        ArrayList<Long> spec = new ArrayList<>();
        for (int n = i + 1; n <= high; n++) {
            counter.reset();
            c.processPerms(n, counter);
            // System.out.print(counter.getCount() + " ");
            spec.add(counter.getCount());
        }
        System.out.println(spec + " " + wfv(p));
    }

    private static void showSpectra(PermutationClass c, int i, int high) {
        HashMap<ArrayList<Long>, HashSet<Permutation>> map = new HashMap<>();
        for (Permutation p : new Permutations(c, i)) {
            // System.out.print(p + ": ");
            PermCounter counter = new PermCounter(AvoidanceTest.getTest(p));
            ArrayList<Long> spec = new ArrayList<>();
            for (int n = i + 1; n <= high; n++) {
                counter.reset();
                c.processPerms(n, counter);
                // System.out.print(counter.getCount() + " ");
                spec.add(counter.getCount());
            }
            if (!map.containsKey(spec)) {
                map.put(spec, new HashSet<Permutation>());
            }
            map.get(spec).add(p);
        }
        for (ArrayList<Long> spec : map.keySet()) {
            System.out.print(spec + " ");
            for (Permutation p : map.get(spec)) {
                //System.out.print(wordForm(p) + " ");
                //System.out.print(wf(p) + " ");
                // System.out.print(wf312(p) + " ");
                System.out.print(wfv(p) + " ");
            }
            System.out.println();
        }
    }

    private static Permutation[] sumComponents(Permutation q) {
        int[] e = q.elements;
        int low = 0;
        int m = e[0];
        ArrayList<Permutation> comps = new ArrayList<Permutation>();
        int compCount = 1;
        for (int i = 1; i < e.length; i++) {
            if (e[i] > m) {
                if (m == i - 1) {
                    compCount++;
                    comps.add(q.segment(low, i));
                    low = i;
                }
                m = e[i];
                //comps.add(q.segment(low, i));
                // low = i;
            }
        }
        comps.add(q.segment(low, q.length()));
        Permutation[] result = new Permutation[comps.size()];
        comps.toArray(result);
        return result;

    }

    private static String wordForm(Permutation p) {
        Permutation[] c = sumComponents(p);
        StringBuilder result = new StringBuilder();
        HashMap<Permutation, Character> letters = new HashMap<>();
        letters.put(new Permutation("1"), 'a');
        letters.put(new Permutation("21"), 'b');
        letters.put(new Permutation("321"), 'c');
        letters.put(new Permutation("312"), 'd');
        letters.put(new Permutation("4132"), 'e');
        for (Permutation q : c) {
            result.append(letters.get(q));
        }

        return result.toString();
    }

    private static String wf(Permutation p) {
        StringBuilder result = new StringBuilder();
        Permutation[] c = sumComponents(p);
        for (Permutation q : c) {
            if (q.length() == 1) {
                result.append('a');
            } else if (q.at(q.length() - 1) == 0) {
                result.append("d_" + q.length());
            } else {
                result.append("c_" + q.length());
            }
        }
        return result.toString();
    }

    private static String wf312(Permutation p) {
        StringBuilder result = new StringBuilder();
        Permutation[] c = sumComponents(p);
        for (Permutation q : c) {
            if (q.length() == 1) {
                result.append('a');
            } else if (q.length() == 2) {
                result.append("b");
            } else {
                result.append("c");
            }
        }
        return result.toString();
    }

    private static String wfv(Permutation p) {
        StringBuilder result = new StringBuilder();
        Permutation[] c = sumComponents(p);
        for (Permutation q : c) {
            result.append('(');
            result.append(q);
            result.append(')');
        }
        return result.toString();
    }

    private static void showSpectra(PermutationClass c, PermutationClass pc, int i, int high) {
        HashMap<ArrayList<Long>, HashSet<Permutation>> map = new HashMap<>();
        for (Permutation p : new Permutations(pc, i)) {
            // System.out.print(p + ": ");
            PermCounter counter = new PermCounter(AvoidanceTest.getTest(p));
            ArrayList<Long> spec = new ArrayList<>();
            for (int n = i + 1; n <= high; n++) {
                counter.reset();
                c.processPerms(n, counter);
                // System.out.print(counter.getCount() + " ");
                spec.add(counter.getCount());
            }
            if (!map.containsKey(spec)) {
                map.put(spec, new HashSet<Permutation>());
            }
            map.get(spec).add(p);
        }
        for (ArrayList<Long> spec : map.keySet()) {
            System.out.print(spec + " ");
            for (Permutation p : map.get(spec)) {
                //System.out.print(wordForm(p) + " ");
                //System.out.print(wf(p) + " ");
                // System.out.print(wf312(p) + " ");
                System.out.print(wfv(p) + " ");
            }
            System.out.println();
        }
    }
}
