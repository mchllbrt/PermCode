package permlib.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.utilities.Combinations;

/**
 * Compile evidenxe about equidistribution of statistixs in Sep
 *
 * @author Mixhael Albert
 */
public class SepStatistics {

    final static PermutationClass x = new PermutationClass(new Permutation("132"));
    
    public static void main(String[] args) {
//        for(int k = 4; k <= 10; k++) {
//            System.out.println(countXSym(k));
//        }
        doIt(6,10);
    }
    
    public static int countXSym(int k) {
        int c = 0;
        for (Permutation p : new Permutations(x, k)) {
            if (PermUtilities.isSymmetryRep(p)) c++;
        }
        return c;
    }

    public static void doIt(int k, int n) {
        HashMap<Permutation, OccurrenceCounts> counts = new HashMap<>();
        for (Permutation p : new Permutations(x, k)) {
            counts.put(p, new OccurrenceCounts());
        }
        for (Permutation q : new Permutations(x, n)) {
            HashMap<Permutation, Integer> localCounts = new HashMap<>();
            for (Permutation p : new Permutations(x, k)) {
                localCounts.put(p, 0);
            }
            for (int[] c : new Combinations(n, k)) {
                Permutation p = q.patternAt(c);
                localCounts.put(p, localCounts.get(p)+1);
            }
            for (Permutation p : new Permutations(x, k)) {
                counts.get(p).incrementCount(localCounts.get(p));
            }
        }
       HashMap<OccurrenceCounts, HashSet<Permutation>> eqClasses = new HashMap<>();

        for (Permutation p : counts.keySet()) {
                if (!eqClasses.containsKey(counts.get(p))) {
                    eqClasses.put(counts.get(p), new HashSet<Permutation>());
                }
                eqClasses.get(counts.get(p)).add(p);
        }
        System.out.println(eqClasses.size() + " distinct counts for " + k + " in " + n);
        
        for(OccurrenceCounts cs : eqClasses.keySet()) {
            for(Permutation p : eqClasses.get(cs)) {
//                if (PermUtilities.isSymmetryRep(p)) 
                    System.out.print("("+p+" ");
                    System.out.print(p.inverse() + ") ");
            }
            System.out.println();
        }
        

    }

    static class OccurrenceCounts {

        HashMap<Integer, Long> counts = new HashMap<>();

        OccurrenceCounts() {
        }

        ;
        
        void incrementCount(int i) {
            if (!counts.containsKey(i)) {
                counts.put(i, 0L);
            }
            counts.put(i, counts.get(i) + 1);
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 59 * hash + Objects.hashCode(this.counts);
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
            final OccurrenceCounts other = (OccurrenceCounts) obj;
            if (!Objects.equals(this.counts, other.counts)) {
                return false;
            }
            return true;
        }

    }

}
