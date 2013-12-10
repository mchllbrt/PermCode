/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.property.PermProperty;
import permlib.property.PlusIndecomposable;
import permlib.utilities.Combinations;

/**
 *
 * @author MichaelAlbert
 */
public class VInceSumIndecomposable {

    static PermProperty si = new PlusIndecomposable();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        HashSet<Permutation> sis = new HashSet<Permutation>();
        sis.add(new Permutation("321"));
        sis.add(new Permutation("312"));
        sis.add(new Permutation("231"));
        sis.add(new Permutation("21"));
        sis.add(new Permutation("1"));
        sis.add(new Permutation());
        Permutation[] si4 = sumIndecomposables(4);
        for(int[] c : new Combinations(si4.length, 2)) {
            
        }
   }

    public static int[] siSubSpec(Permutation p) {
        return siSpectrum(PermUtilities.subpermutations(p), p.length());
    }

    static Permutation[] sumIndecomposables(int n) {
        ArrayList<Permutation> sis = new ArrayList<Permutation>();
        for(Permutation p : new Permutations(n)) {
            if (si.isSatisfiedBy(p)) sis.add(p);
        }
        return sis.toArray(new Permutation[0]);
    }
    
    public static boolean badArray(int[] s) {
        double sum = 0.0;
        double xi = 1 / 2.30522;
        double x = 1.0;
        for (int i = 1; i < s.length; i++) {
            x *= xi;
            sum += s[i] * x;
        }
        return sum > 1.0;
    }
    
    public static boolean badPerm(Permutation p) {
        return badArray(siSubSpec(p));
    }
    
    public static int[] siSpectrum(Collection<Permutation> ps, int maxLength) {
        int[] result = new int[maxLength+1];
        for(Permutation p : ps) {
            if (si.isSatisfiedBy(p)) result[p.length()]++;
        }
        return result;
    }
    
    public static int[] siSpectrum(Collection<Permutation> ps) {
        int n = 0;
        for(Permutation p : ps) {
            n = (n > p.length()) ? n : p.length();
        }
        return siSpectrum(ps, n);
    }
    
    public boolean allowed(Permutation p, Collection<Permutation> smallerSIs) {
        for(Permutation q : PermUtilities.subpermutations(p)) {
            if (si.isSatisfiedBy(q) && !smallerSIs.contains(q)) return false;
        }
        return true;
    }
    
}
