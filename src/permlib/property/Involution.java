package permlib.property;

import permlib.Permutation;
import permlib.utilities.InvolutionUtilities;

/**
 * The property of being an involution.
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
