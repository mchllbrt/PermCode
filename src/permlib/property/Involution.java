package permlib.property;

import permlib.Permutation;
import permlib.utilities.InvolutionUtilities;

/**
 * A class containing methods for representing an involution property.
 * 
 * @author Michael Albert
 */
public class Involution implements PermProperty {

    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return InvolutionUtilities.isInvolution(p);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        return InvolutionUtilities.isInvolution(new Permutation(values));
    }
    
    @Override
    public String toString() {
        return "Involution";
    }
}
