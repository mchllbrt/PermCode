package permlab.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import static permlab.examples.Av231PlusStuff.tikzMatchingForPerm;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;

/**
 *
 * @author MichaelAlbert
 */
public class Av321Wilf {

    public static void main(String[] args) {

        doWilf(6,6, true, 9);
        // doDiff(8, 9, 14);
    }

    public static void doCounts(Permutation p, int low, int high) {
        PermutationClass c = new PermutationClass(new Permutation("321"), p);
        PermCounter ct = new PermCounter();
        for (int n = low; n < high; n++) {
            c.processPerms(n, ct);
            System.out.print(ct.getCount() + " ");
            ct.reset();
        }
        System.out.println();
    }

    public static void doWilf(int nMin, int nMax, boolean verbose, int checkTotal) {
        PermutationClass c = new PermutationClass("321");
        HashMap<Spectrum, HashSet<Permutation>> wilfClasses = new HashMap<>();
        for (int n = nMin; n <= nMax; n++) {
            for (Permutation q : new Permutations(c, n)) {
                long[] qC = new long[checkTotal];
                PermutationClass d = new PermutationClass(new Permutation("321"), q);
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
                    HashSet<Permutation> aSet = new HashSet<>();
                    aSet.add(q);
                    wilfClasses.put(qs, aSet);
                }
            }
            System.out.println("Length " + n + " has " + wilfClasses.keySet().size() + " classes");
            if (verbose) {
                System.out.println();
                for (Spectrum s : wilfClasses.keySet()) {
                    System.out.println(Arrays.toString(s.counts) + " " + (wilfClasses.get(s).size()));
                    for (Permutation q : wilfClasses.get(s)) {
                        System.out.println(q);
                        // System.out.println();
                    }
                    System.out.println();
                }
            }
            wilfClasses.clear();
        }

    }
    
    public static void doDiff(int n, int low, int high) {
        for(int a = 1; a <= n/2; a++) {
                doCounts(diff(a, n-a), low, high);
        }
    }
    
    static Permutation diff(int a, int b) {
            return PermUtilities.skewSum(new Permutation(a), new Permutation(b));
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
}
