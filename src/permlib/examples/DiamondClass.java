package permlib.examples;

import permlib.Permutation;
import permlib.property.PermProperty;

/**
 * In the diamond class what determines whether point locations are uniquely
 * determined?
 * 
 * @author Michael Albert
 */
public class DiamondClass {
    
    static PermProperty I  = new PermProperty() {

        @Override
        public boolean isSatisfiedBy(Permutation p) {
            return isSatisfiedBy(p.elements);
        }

        @Override
        public boolean isSatisfiedBy(int[] values) {
            for(int i = 1; i < values.length; i++) {
                if (values[i] < values[i-1]) return false;
            }
            return true;
        }
        
    };
    
    static PermProperty D  = new PermProperty() {

        @Override
        public boolean isSatisfiedBy(Permutation p) {
            return isSatisfiedBy(p.elements);
        }

        @Override
        public boolean isSatisfiedBy(int[] values) {
            for(int i = 1; i < values.length; i++) {
                if (values[i] >= values[i-1]) return false;
            }
            return true;
        }
        
    };
    

}
