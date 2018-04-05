package permlib.examples;

import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.SimplePermClass;
import permlib.property.HereditaryProperty;

/**
 * Conjecture: There are 2(n-4)! permutations that are simple and have the
 * property that for all k <= n-4 when we delete the top k elements we get a
 * simple. @author MichaelAlbert
 */
public class MaxDeletableSimples {

    public static void main(String[] args) {
        
        findClass(4);
//        
//        SimplePermClass s = new SimplePermClass();
//        PermCounter c = new PermCounter(new PermProperty() {
//
//            @Override
//            public boolean isSatisfiedBy(Permutation p) {
//                return isDeletable(p);
//            }
//
//            @Override
//            public boolean isSatisfiedBy(int[] values) {
//                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//        }) ;
//        for(int n = 4; n <= 10; n++) {
//            s.processPerms(n, c);
//            System.out.println(c.getCount());
//            c.reset();
//        }
         
    }

    public static boolean isDeletable(Permutation p) {
        if (p.length() < 4) {
            return true;
        }
        if (!PermUtilities.isSimple(p)) {
            return false;
        }
        return isDeletable(p.delete(0));
    }
    
    public static void findClass(int n) {
        SimplePermClass s = new SimplePermClass("2413","4123");
        HashSet<Permutation> simps = s.getPerms(2*n+2);
        for(Permutation p : new Permutations(4)) {
            boolean hasP = false;
            HereditaryProperty ap = PermUtilities.avoidanceTest(p);
            for(Permutation si : simps) {
                if (!ap.isSatisfiedBy(si)) {
                    hasP = true; break;
                }
            }
            if (!hasP) System.out.println(p);
        }
               
    }

}
