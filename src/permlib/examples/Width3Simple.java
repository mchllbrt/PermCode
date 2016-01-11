package permlib.examples;

import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.SimplePermClass;
import permlib.property.PermProperty;

/**
 * Construct a simple known to have placement width >= 3
 * @author Michael Albert
 */
public class Width3Simple {
    
    public static void main(String[] args) {
        
        PermProperty p = new PermProperty() {

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                int n = values.length;
                if (values[1] == n-1 || values[1] == 0 || values[n-2] == n-1 || values[n-2] == 0) return false;
                if (values[0] == 1 || values[0] == n-2 || values[n-1] == 1 || values[n-1] == n-2 ) return false;
                int d = values[0] - values[n-1];
                if (d*d == 1) return false;
                for(int i = 0; i < values.length-1; i++) {
                    if ((values[i] == 0 || values[i] == n-1) && (values[i+1] == 0 || values[i+1] == n-1)) return false;
                }
                return true;
            }
            
           
            
        
        }; 
        
        HashSet<Permutation> basis = new HashSet<Permutation>();
        
        for(int n = 6; n <= 12; n++) {
            int count = 0;
            for(Permutation q : (new SimplePermClass(basis)).getPerms(n)) {
                if (p.isSatisfiedBy(q)) {
                    //System.out.println(q);
                    basis.add(q); count++;
                }
            }
            System.out.println(n + " " + count);
        }
        
        
    }
    
}
