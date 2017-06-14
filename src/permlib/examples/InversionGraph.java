package permlib.examples;

import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;

/**
 * Some investigations on inversion graphs (mainly looking for small diameter)
 * @author Michael Albert
 */
public class InversionGraph {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        doExamples(6);
    }
    
    private static boolean isInversion(Permutation p, int i, int j) {
        return (i < j) ? p.elements[i] > p.elements[j] : p.elements[i] < p.elements[j];
    }
    
    private static boolean haveNeighbour(Permutation p, int i, int j) {
        if (isInversion(p,i,j)) return true;
        for(int k = 0; k < p.elements.length; k++) {
            if (isInversion(p,i,k) && isInversion(p,j,k)) return true;
        }
        return false;
    }
    
    private static boolean diameterTwo(Permutation p) {
        for(int i = 0; i < p.elements.length; i++) {
            for(int j = i+1; j < p.elements.length; j++) {
                if (!haveNeighbour(p,i,j)) return false;
            }
        }
        return true;
    }

    private static void doExamples(int i) {
       for(Permutation p : new Permutations(i)) {
           if (!PermUtilities.MINUSINDECOMPOSABLE.isSatisfiedBy(p)) continue;
           if (diameterTwo(p)) System.out.println(p);
       }
    }
    
}
