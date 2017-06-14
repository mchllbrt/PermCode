package permlib.property;

import permlib.Permutation;
import permlib.Permutations;

/**
 * The property of being a Dumont permutation of the first type (i.e., even
 * values are descent tops, odd are ascent bottoms or final). Note that due to
 * differences between 0-based and 1-based indexing, internal meanings of even
 * and odd may differ.
 * 
 * @author Michael Albert
 */
public class DumontFirst implements PermProperty {

    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p.elements);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        // Even values start descents
       // Odd values start ascents (or are final)
       if (values[values.length-1] % 2 != 0) return false;
       for(int i = 0; i < values.length-1; i++) {
           if (values[i] % 2 == 1) {
               if (values[i+1] > values[i]) return false;
           } else {
               if (values[i+1] < values[i]) return false;
           }
       }
       return true;
    }
    
    // Basic test 
    public static void main(String[] args) {
        DumontFirst dum = new DumontFirst();
        for(Permutation p : new Permutations(4)) {
            System.out.println(p + " " + dum.isSatisfiedBy(p) );
        } 
        
    }

}
