package permlib.examples;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;
import permlib.property.AvoidanceTest;
import permlib.utilities.Partitions;

/**
 * Working in the plus closure of V look for non-trivial Wilf-equivalences
 * knowing a lot of rules already.
 *
 * @author Michael Albert
 */
public class PlusClosureOfVWilfCollapse {

    public static void main(String[] args) {
        PermutationClass c = new PermutationClass("231", "4132");
//        showSpectrum(c, new Permutation("6 5 4 3 2 1 10 9 8 7"), 10, 14);
//        showSpectrum(c, new Permutation("5 4 3 2 1 10 9 8 7 6"), 10, 14);
        for(int n = 12; n <= 12; n++) {
            showSpectra(c,representatives(n),n,n+4);
            System.out.println();
        }
    }

    public static ArrayList<Permutation> representatives(int n) {
        ArrayList<Permutation> result = new ArrayList<>();
        result.add(new Permutation(n));
        // k is the total size of the decreasing parts
        for (int k = 2; k <= n; k++) {
            for (ArrayList<Integer> layerSizes : new Partitions(k)) {
                if (layerSizes.get(layerSizes.size() - 1) == 1) {
                    continue;
                }
                if (n > k) {
                    for (ArrayList<Integer> incSizes : new Partitions(n - k)) {
                        if (incSizes.size() <= layerSizes.size() + 1) {
                            if (incSizes.size() == 1 || incSizes.get(0) > incSizes.get(1)) {
                                result.add(buildPermI(layerSizes, incSizes, n));
                            }
                        }
                        if (incSizes.size() <= layerSizes.size()) {
                            result.add(buildPermD(layerSizes, incSizes, n));
                        }
                    }
                } else {
                    result.add(buildPermD(layerSizes, new ArrayList<Integer>(), n));
                }
            }

        }

        return result;
    }

    private static Permutation buildPermI(ArrayList<Integer> layerSizes, ArrayList<Integer> incSizes, int n) {
        Permutation result = new Permutation();
        for (int index = 0; index < Math.max(layerSizes.size(), incSizes.size()); index++) {
            if (index < incSizes.size()) {
                result = PermUtilities.sum(result, PermUtilities.increasingPermutation(incSizes.get(index)));
            }
            if (index < layerSizes.size()) {
                result = PermUtilities.sum(result, PermUtilities.decreasingPermutation(layerSizes.get(index)));
            }
        }
        return result;
    }

    private static Permutation buildPermD(ArrayList<Integer> layerSizes, ArrayList<Integer> incSizes, int n) {
        Permutation result = new Permutation();
        for (int index = 0; index < Math.max(layerSizes.size(), incSizes.size()); index++) {
            if (index < layerSizes.size()) {
                result = PermUtilities.sum(result, PermUtilities.decreasingPermutation(layerSizes.get(index)));
            }
            if (index < incSizes.size()) {
                result = PermUtilities.sum(result, PermUtilities.increasingPermutation(incSizes.get(index)));
            }
        }
        return result;
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

    private static void showSpectra(PermutationClass c, ArrayList<Permutation> reps, int i, int high) {
        HashMap<ArrayList<Long>, HashSet<Permutation>> map = new HashMap<>();
        for (Permutation p : reps) {
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
