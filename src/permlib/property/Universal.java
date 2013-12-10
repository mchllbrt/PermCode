package permlib.property;

import java.util.ArrayList;
import java.util.Collection;
import permlib.Permutation;

/**
 * The universal property satisfied by all permutations.
 * 
 * @author Michael Albert
 */
public class Universal implements HereditaryProperty {

    /**
     * This property is always true
     * @param p the permutation to be tested
     * @return <code>true</code>
     */
    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return true;
    }
    
    /**
     * This property is always true
     * 
     * @param values the array to be tested
     * @return <code>true</code>
     */
    @Override
    public boolean isSatisfiedBy(int[] values) {
        return true;
    }

    @Override
    public String toString() {
        return "Universal";
    }

    @Override
    public Collection<Permutation> getBasis() {
        return new ArrayList<Permutation>();
    }

    @Override
    public Collection<Permutation> getBasisTo(int n) {
        return getBasis();
    }
}
