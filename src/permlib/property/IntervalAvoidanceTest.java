package permlib.property;

import java.util.Arrays;
import permlib.Permutation;
import permlib.Permutations;

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
            int offset = values[i] - pattern[0];
            int j = 1;
            while (j < pattern.length && values[i+j] == pattern[j] + offset) j++;
            if (j == pattern.length) {
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] args) {
        
        Permutation p = new Permutation("132");
        PermProperty ap = new IntervalAvoidanceTest(p);
        for(Permutation q : new Permutations(3)) {
            if (!ap.isSatisfiedBy(q)) System.out.println(q);
        }
        for(Permutation q : new Permutations(4)) {
            if (!ap.isSatisfiedBy(q)) System.out.println(q);
        }
    }
    
}
