package permlib.property;

import permlib.PermUtilities;
import permlib.Permutation;

/**
 * The plus completion of a property. 
 * 
 * Strictly speaking a new property obtained by insisting that each of the plus
 * components of a permutation satisfies the original property.
 * 
 * @author Michael Albert
 */
public final class PlusCompletion implements PermProperty {
    
    final PermProperty prop;
    
    public PlusCompletion(PermProperty prop) {
        this.prop = prop;
    }

    /**
     * Returns <code>true</code> if every plus component of the permutation
     * satisfies the property.
     * 
     * @param p the permutation
     * @return <code>true</code> if every plus component of the permutation
     * satisfies the property
     */
    @Override
    public final boolean isSatisfiedBy(Permutation p) {
        Permutation[] plusComponents = PermUtilities.plusComponents(p);
        for(Permutation component : plusComponents) {
            if (!prop.isSatisfiedBy(component)) return false;
        }
        return true;
    }

    /**
     * Returns <code>true</code> if every plus component of the pattern of
     * the values satisfies the property.
     * 
     * @param values the values
     * @return <code>true</code> if every plus component of the pattern of 
     * <code>values</code> satisfies the property
     */
    @Override
    public final boolean isSatisfiedBy(int[] values) {
        return isSatisfiedBy(new Permutation(values));
    }
    
}
