package permlib.examples;

import java.util.ArrayList;
import java.util.HashMap;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Look at the container machine where the contents are 231 avoiding. I want to
 * know if the number of paths to 312 and to 321 (for instance) of each length
 * are the same.
 *
 * @author Michael Albert
 */
public class Av4231Machine {

    static PermutationClass av231 = new PermutationClass(new Permutation("231"));
    static HashMap<Permutation, Integer> popStateCounts = new HashMap<>();
    static HashMap<Permutation, Integer> noPopStateCounts = new HashMap<>();

    public static void main(String[] args) {
        noPopStateCounts.put(new Permutation(0), 1);
        for(int n = 1; n < 20; n++) {
            HashMap<Permutation, Integer> nextPopStateCounts = new HashMap<>();
            HashMap<Permutation, Integer> nextNoPopStateCounts = new HashMap<>();
            for(Permutation p : noPopStateCounts.keySet()) {
                for(Permutation q : children(p)) {
                    if (q.at(0) == q.length()-1) {
                        increment(nextPopStateCounts,q,noPopStateCounts.get(p));
                    } else {
                         increment(nextNoPopStateCounts,q,noPopStateCounts.get(p));
                    }
                }
            }
            for(Permutation p : popStateCounts.keySet()) {
                for(Permutation q : children(p)) {
                    if (q.at(0) == q.length()-1) {
                        increment(nextPopStateCounts,q,popStateCounts.get(p));
                    } else {
                         increment(nextNoPopStateCounts,q,popStateCounts.get(p));
                    }
                }
                if (p.length() > 0) {
                    increment(nextPopStateCounts, PermUtilities.delete(p, 0), popStateCounts.get(p));
                }
            }
            popStateCounts = nextPopStateCounts;
            noPopStateCounts = nextNoPopStateCounts;
            for(Permutation p : new Permutations(av231,3)) {
                System.out.print(p + " ");
                if (popStateCounts.containsKey(p)) {
                    System.out.print(popStateCounts.get(p));
                } else {
                    System.out.print(0);
                }
                System.out.print(" ");
                if (noPopStateCounts.containsKey(p)) {
                    System.out.print(noPopStateCounts.get(p));
                } else {
                    System.out.print(0);
                }
               System.out.println(); 
            }
            System.out.println();
        }
    }
    
   
    
    static void increment(HashMap<Permutation, Integer> states, Permutation p, int v) {
        if (!states.containsKey(p)) {
            states.put(p,0);
        }
        states.put(p, states.get(p)+v);
    }
    
    static ArrayList<Permutation> children(Permutation p) {
        ArrayList<Permutation> result = new ArrayList<Permutation>();
        int max = Integer.MIN_VALUE;
        for(int i = 0; i < p.length(); i++) {
            if (p.at(i) > max) {
                result.add(PermUtilities.insert(p, i, p.length()));
                max = p.at(i);
            }
        }
        result.add(PermUtilities.insert(p, p.length(), p.length()));
        return result;
    }

}
