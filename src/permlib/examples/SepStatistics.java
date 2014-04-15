package permlib.examples;

import java.util.ArrayList;
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
 * Compile evidence about equidistribution of statistics in Sep
 *
 * @author Michael Albert
 * @author Jay Pantone
 */
public class SepStatistics {

    /*
        For each class added to classesToCheck, the popularity of all
        permutations of length k in the class is computed among permutations
        length n, starting with n = k+1 and incrementing n until one of the
        following occurs: unique distribution is found (no non-trivial
        equidistributions) or until the same number of equidistribution classes
        is found twice in a row or until maxLengthN is hit. Note that if the
        same number of classes is found twice in a row and there is a
        non-trivial equidistribution, this class should be checked to further
        lengths.
    */
    
    public static void main(String[] args) {
        int k = 6;
        int maxLengthN = 11;
        
        ArrayList<PermutationClass> classesToCheck = new ArrayList<>();
        
        classesToCheck.add(new PermutationClass(new Permutation("2413"), new Permutation("3142")));
        classesToCheck.add(new PermutationClass(new Permutation("1324"), new Permutation("4231")));
        classesToCheck.add(new PermutationClass(new Permutation("1432"), new Permutation("2341"), new Permutation("4123"), new Permutation("3214")));
        
        for (Permutation p : new Permutations(3,5)) {
            if (PermUtilities.isSymmetryRep(p)) {
                classesToCheck.add(new PermutationClass(p));
            }
        }
        
        for (PermutationClass C : classesToCheck) {
            System.out.println("\nChecking class: " + C);
            int n = k+1;
            HashMap distClasses = findDistClasses(k, n, C);
            int last = 0;
            int numDistinct = distClasses.keySet().size();
            System.out.println("\t\t" + numDistinct + " distinct classes of length " + n + ".");
            while (last != numDistinct && n < maxLengthN && hasNontrivialDistEq(distClasses)) {    
                n++;
                last = numDistinct;
                distClasses = findDistClasses(k, n, C);
                numDistinct = distClasses.keySet().size();
                System.out.println("\t\t" + numDistinct + " distinct classes of length " + n + ".");
            }
            if (hasNontrivialDistEq(distClasses)) {
                if (last != numDistinct) {
                    System.out.println("\t <!> maxN not high enough. <!>");
                }
                System.out.println("\t NOT Uniquely Distributed");
                printClasses(distClasses, false, "\t\t");
            } else {
                System.out.println("\t Uniquely Distributed");
            }   
        }
    }
    
    public static boolean hasNontrivialDistEq(HashMap<OccurrenceCounts, HashSet<Permutation>> distClasses) {
        for(OccurrenceCounts cs : distClasses.keySet()) {
            HashSet<Permutation> symReps = new HashSet<>();
            HashSet<Permutation> permsToPrint = new HashSet<>();
            for(Permutation p : distClasses.get(cs)) {
                Permutation r = PermUtilities.symmetryRep(p);
                if (!symReps.contains(r)){
                    permsToPrint.add(p);
                    symReps.add(r);
                }
            }
            if (permsToPrint.size() > 1) {
                return true;
            }
        }
        return false;
    }
    
    public static HashMap<OccurrenceCounts, HashSet<Permutation>> findDistClasses(int k, int n, PermutationClass C) {
     
        HashMap<Permutation, OccurrenceCounts> counts = new HashMap<>();
        for (Permutation p : new Permutations(C, k)) {
            counts.put(p, new OccurrenceCounts());
        }
        for (Permutation q : new Permutations(C, n)) {
            HashMap<Permutation, Integer> localCounts = new HashMap<>();
            for (Permutation p : new Permutations(C, k)) {
                localCounts.put(p, 0);
            }
            for (int[] c : new Combinations(n, k)) {
                Permutation p = q.patternAt(c);
                localCounts.put(p, localCounts.get(p)+1);
            }
            for (Permutation p : new Permutations(C, k)) {
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
        
        return eqClasses;
    }
    
    public static void printClasses(HashMap<OccurrenceCounts, HashSet<Permutation>> distClasses) {
        printClasses(distClasses, false, "");
    }
    
    public static void printClasses(HashMap<OccurrenceCounts, HashSet<Permutation>> distClasses, String preface) {
        printClasses(distClasses, false, preface);
    }
    
    public static void printClasses(HashMap<OccurrenceCounts, HashSet<Permutation>> distClasses, boolean printTrivial, String preface) {
        
//        System.out.println(distClasses.size() + " distinct counts.");
        
        for(OccurrenceCounts cs : distClasses.keySet()) {
            HashSet<Permutation> symReps = new HashSet<>();
            HashSet<Permutation> permsToPrint = new HashSet<>();
            for(Permutation p : distClasses.get(cs)) {
                Permutation r = PermUtilities.symmetryRep(p);
                if (!symReps.contains(r)){
                    permsToPrint.add(p);
                    symReps.add(r);
                }
            }
            if (printTrivial || permsToPrint.size() > 1) {
                System.out.print(preface);
                for (Permutation p : permsToPrint) {
                    System.out.print(p + " ");
                }
                System.out.println();
            }
        }
        
    }
    
    static class OccurrenceCounts {

        HashMap<Integer, Long> counts = new HashMap<>();

        OccurrenceCounts() {
        }
        
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
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            final OccurrenceCounts other = (OccurrenceCounts) obj;
            return Objects.equals(this.counts, other.counts);
        }
    }

}
