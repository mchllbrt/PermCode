package permlib.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.Symmetry;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;
import permlib.property.HereditaryProperty;

/**
 * Looking for equivalences in Av(231, p, q)
 *
 * @author Michael Albert
 */
public class PairsInAv231 {

    public static void main(String[] args) {
        doWilf(3,4, true, 10);
        // doSpecial();
    }

    public static void doSpecial() {

        PermutationClass c = new PermutationClass("231", "321");
        long[] cCounts = new long[8];
        for (int n = 4; n < 12; n++) {
            PermCounter counter = new PermCounter();
            c.processPerms(n, counter);
            cCounts[n - 4] = counter.getCount();
        }
        System.out.println(Arrays.toString(cCounts));
        c = new PermutationClass("231", "123");
        for (int n = 4; n < 12; n++) {
            PermCounter counter = new PermCounter();
            c.processPerms(n, counter);
            cCounts[n - 4] = counter.getCount();
        }
        System.out.println(Arrays.toString(cCounts));
        c = new PermutationClass("231", "321");
        for (Permutation q : new Permutations(c, 4)) {
            PermutationClass d = new PermutationClass("231", "321", q.toString());
            for (int n = 4; n < 12; n++) {
                PermCounter counter = new PermCounter();
                d.processPerms(n, counter);
                cCounts[n - 4] = counter.getCount();
            }
            System.out.println(q + " " + Arrays.toString(cCounts));
        }
    }

    public static void doWilf(int n, boolean verbose, int checkTotal) {
        PermutationClass c = new PermutationClass("231");
        HashSet<PermPair> pairs = new HashSet<>();
        for (Permutation a : new Permutations(c, n)) {
            for (Permutation b : new Permutations(c, n)) {
                if (!a.equals(b)) {
                    pairs.add(new PermPair(a, b));
                }
            }
        }
        HashSet<PermPair> pairReps = new HashSet<>();
        for (PermPair pair : pairs) {
            PermPair np = new PermPair(Symmetry.IRC.on(pair.a), Symmetry.IRC.on(pair.b));
            if (!pairReps.contains(np) && !pairReps.contains(pair)) {
                pairReps.add(pair);
            }
        }
        System.out.println("Length " + n + " has " + pairReps.size() + " symmetry orbits.");
        HashMap<Spectrum, HashSet<PermPair>> wilfClasses = new HashMap<>();
        for (PermPair pair : pairs) {
            long[] qC = new long[checkTotal];
            PermutationClass d = new PermutationClass(new Permutation("231"), pair.a, pair.b);
            for (int m = n + 1; m <= n + checkTotal; m++) {
                PermCounter counter = new PermCounter();
                d.processPerms(m, counter);
                qC[m - n - 1] = counter.getCount();
            }
            // System.out.println(q + " " + Arrays.toString(qC));
            Spectrum qs = new Spectrum(qC);
            if (wilfClasses.containsKey(qs)) {
                wilfClasses.get(qs).add(pair);
            } else {
                HashSet<PermPair> aSet = new HashSet<>();
                aSet.add(pair);
                wilfClasses.put(qs, aSet);
            }
        }
        System.out.println("Length " + n + " has " + wilfClasses.keySet().size() + " classes");
        if (verbose) {
            System.out.println();
            for (Spectrum s : wilfClasses.keySet()) {
                System.out.println(Arrays.toString(s.counts) + " " + (wilfClasses.get(s).size()));
                for (PermPair q : wilfClasses.get(s)) {
                    System.out.println(q.a + " " + q.b);
                    // System.out.println();
                }
                System.out.println();
            }
        }
        wilfClasses.clear();
    }

    public static void doWilf(int n, int m, boolean verbose, int checkTotal) {
        PermutationClass c = new PermutationClass("231");
        HashSet<PermPair> pairs = new HashSet<>();
        for (Permutation a : new Permutations(c, n)) {
            HereditaryProperty aa = PermUtilities.avoidanceTest(a);
            for (Permutation b : new Permutations(c, m)) {
                if (aa.isSatisfiedBy(b)) {
                    pairs.add(new PermPair(a, b));
                }
            }
        }
        HashSet<PermPair> pairReps = new HashSet<>();
        for (PermPair pair : pairs) {
            PermPair np = new PermPair(Symmetry.IRC.on(pair.a), Symmetry.IRC.on(pair.b));
            if (!pairReps.contains(np) && !pairReps.contains(pair)) {
                pairReps.add(pair);
            }
        }
        System.out.println("Length " + n + " " + m + " has " + pairReps.size() + " symmetry orbits.");
        HashMap<Spectrum, HashSet<PermPair>> wilfClasses = new HashMap<>();
        for (PermPair pair : pairs) {
            long[] qC = new long[checkTotal];
            PermutationClass d = new PermutationClass(new Permutation("231"), pair.a, pair.b);
            for (int l = n + 1; l <= n + checkTotal; l++) {
                PermCounter counter = new PermCounter();
                d.processPerms(l, counter);
                qC[l - n - 1] = counter.getCount();
            }
            // System.out.println(q + " " + Arrays.toString(qC));
            Spectrum qs = new Spectrum(qC);
            if (wilfClasses.containsKey(qs)) {
                wilfClasses.get(qs).add(pair);
            } else {
                HashSet<PermPair> aSet = new HashSet<>();
                aSet.add(pair);
                wilfClasses.put(qs, aSet);
            }
        }
        System.out.println("Length " + n + " " + m + " has " + wilfClasses.keySet().size() + " classes");
        if (verbose) {
            System.out.println();
            for (Spectrum s : wilfClasses.keySet()) {
                System.out.println(Arrays.toString(s.counts) + " " + (wilfClasses.get(s).size()));
                for (PermPair q : wilfClasses.get(s)) {
                    System.out.println(q.a + " " + q.b);
                    // System.out.println();
                }
                System.out.println();
            }
        }
        wilfClasses.clear();
    }

    static class PermPair implements Comparable {

        Permutation a;
        Permutation b;

        PermPair(Permutation p, Permutation q) {
            if (p.compareTo(q) < 0) {
                a = p;
                b = q;
            } else {
                a = q;
                b = p;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 11 * hash + Objects.hashCode(this.a);
            hash = 11 * hash + Objects.hashCode(this.b);
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
            final PermPair other = (PermPair) obj;
            if (!Objects.equals(this.a, other.a)) {
                return false;
            }
            if (!Objects.equals(this.b, other.b)) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(Object o) {
            final PermPair other = (PermPair) o;
            int c = this.a.compareTo(other.a);
            return (c != 0) ? c : this.b.compareTo(other.b);
        }

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
