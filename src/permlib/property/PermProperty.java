package permlib.property;

import permlib.PermUtilities;
import permlib.Permutation;

/**
 * A property of permutations, or equivalently
 * a subset of the set of all permutations. Implementing classes should be 
 * immutable whenever this is feasible as such properties may well be used in
 * a multi-threaded context, e.g. the GUI.
 * 
 * @author Michael Albert
 */
public interface PermProperty {
    
    public static final PermProperty INCREASING = PermUtilities.avoidanceTest("21");
     
    public static final PermProperty DECREASING = PermUtilities.avoidanceTest("12");
    
    public static final PermProperty SIMPLE = new Simple();
    
    public static final PermProperty EMPTY = new PermProperty() {

        @Override
        public boolean isSatisfiedBy(Permutation p) {
            return p.length() == 0;
        }
        
        @Override
        public boolean isSatisfiedBy(int[] values) {
            return values.length == 0;
        }
    };
    
    /**
     * Determines whether this property is satisfied by a permutation.
     * @param p the permutation
     * @return Returns <code>true</code> if the permutation satisfies the property.
     */
    public boolean isSatisfiedBy(Permutation p);
    
    /**
     * Determines whether this property is satisfied by the pattern of an array
     * of values.
     * @param values the values
     * @return returns <code>true</code> if the pattern of the values satisfies
     * the property.
     */
    public boolean isSatisfiedBy(int[] values);

}
