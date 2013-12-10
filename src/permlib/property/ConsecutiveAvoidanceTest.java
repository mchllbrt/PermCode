package permlib.property;

import java.util.ArrayList;
import java.util.Collection;
import permlib.Permutation;

/**
 * Used to create tests for the avoidance of permutations in consecutive sets of positions.
 * 
 * @author Michael Albert
 */
public class ConsecutiveAvoidanceTest implements PermProperty {
    
    private PermProperty iTest;
    
    /**
     * Constructs an avoidance test for a given permutation.
     * 
     * @param a the permutation whose avoidance is to be tested
     */
    public ConsecutiveAvoidanceTest(Permutation a) {
       this.iTest = new ConsecutiveInvolvementTest(a);
    }
    
    public ConsecutiveAvoidanceTest(Collection<Permutation> as) {
        ArrayList<PermProperty> iTests = new ArrayList<PermProperty>();
        for(Permutation a : as) {
            iTests.add(new ConsecutiveInvolvementTest(a));
        }
        this.iTest = new Union(iTests);
    }

    /**
     * Determines whether a permutation has this property.
     * @param p the permutation
     * @return <code>true</code> if <code>p</code> avoids the permutation used
     * to construct this property.
     */
    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return !iTest.isSatisfiedBy(p);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        return !iTest.isSatisfiedBy(values);
    }

}
