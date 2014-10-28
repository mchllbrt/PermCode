package permlib.examples;

import java.util.HashSet;
import static permlib.PermUtilities.LDSLength;
import static permlib.PermUtilities.LISLength;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.utilities.IntPair;

/**
 * In a class avoiding both layered and antilayered patterns, the length of the
 * LIS or LDS grows linearly. What is the constant?
 *
 * @author Michael Albert
 */
public class PermErdosHajnal {

    public static void main(String[] args) {

        funny();
        
    }
    
    public static void original() {
        Permutation p = new Permutation("312");
        Permutation q = new Permutation("231");
        HashSet<IntPair> pairs = new HashSet<>();
        int n = 12;
        for (Permutation r : new Permutations(new PermutationClass(p), n)) {
            pairs.add(new IntPair(LISLength(r), LDSLength(r)));
        }
        for (IntPair pair : pairs) {
            System.out.println(pair.getFirst() + ", " + pair.getSecond());
        }
    }
    
    public static void funny() {
        
        HashSet<Permutation> basis = new HashSet<>();
        for(Permutation p : new Permutations(8)) {
            if (LISLength(p)  + LDSLength(p) == 6) basis.add(p);
        } 
        HashSet<IntPair> pairs = new HashSet<>();
        for (Permutation r : new Permutations(new PermutationClass(basis), 12)) {
            pairs.add(new IntPair(LISLength(r), LDSLength(r)));
        }
        for (IntPair pair : pairs) {
            System.out.println(pair.getFirst() + ", " + pair.getSecond());
        }
    }
    
    public static void sn(int n) {
        
         HashSet<IntPair> pairs = new HashSet<>();
       for(Permutation p : new Permutations(n)) {
            pairs.add(new IntPair(LISLength(p), LDSLength(p)));
        } 
        
        for (IntPair pair : pairs) {
            System.out.println(pair.getFirst() + ", " + pair.getSecond());
        }
    }

}
