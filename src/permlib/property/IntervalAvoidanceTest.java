package permlib.property;

import permlib.Permutation;

/**
 * Determines whether or not a permutation avoids another as an interval.
 * 
 * @author Michael Albert
 */
public class IntervalAvoidanceTest implements PermProperty {
    
    public int[] pattern;
    
    public IntervalAvoidanceTest(Permutation pattern) {
        this.pattern = pattern.elements;
    }

    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p.elements);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        for(int i = 0; i <= values.length - pattern.length; i++) {
            int offset = values[i] - pattern[i];
            if (offset < 0) continue;
            int j = 1;
            while (j < pattern.length && values[j] == pattern[j] + offset) j++;
            if (j == pattern.length) return false;
        }
        return true;
    }
    
}
