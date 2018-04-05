package permlib.examples;

import java.util.HashMap;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.utilities.Combinations;

/**
 * Which patterns occur more frequently than others? Say p dominates q if in S_n
 * the number of permutations containing more copies of p than q is larger than the
 * number of permutations containing more copies of q than p.
 * 
 * @author Michael Albert
 */
public class DominatingPatterns {

    static HashMap<Permutation, HashMap<Permutation, Integer>> census = new HashMap<>();
    static PermutationClass c = new PermutationClass();
    
    public static void main(String[] args) {
        int n = 10; int k = 4;
        c = new PermutationClass("231", "312");
        buildCensus(n,k);
        for(Permutation p : new Permutations(c,k)) {
            //if (PermUtilities.isSymmetryRep(p)) {
                System.out.print(p + " > ");
                for(Permutation q : new Permutations(c,k)) {
                    int diff = dominates(p,q);
                    if (diff > 0) System.out.print(q + "(" + diff + ") ");
                }
                System.out.println();
            //}
        }
    }

    private static void buildCensus(int n, int k) {
    
        for(Permutation q : new Permutations(c,n)) {
            census.put(q, new HashMap<Permutation, Integer>());
            for(Permutation p : new Permutations(c,k)) {
                census.get(q).put(p, 0);
            }
            for(int[] c : new Combinations(n,k)) {
                Permutation p = q.patternAt(c);
                census.get(q).put(p, census.get(q).get(p)+1);
            }
        }       
        
    }

    private static int dominates(Permutation p, Permutation q) {
        int wins = 0;
        for(Permutation t : census.keySet()) {
            int diff = census.get(t).get(p) - census.get(t).get(q);
            if (diff > 0) wins++;
            if (diff < 0) wins--;
        }
        return wins;
    }
    
}
