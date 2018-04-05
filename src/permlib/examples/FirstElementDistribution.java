package permlib.examples;

import java.util.Arrays;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.processor.PermProcessor;

/**
 * An example to show how the 'processPerms' method is used. Reports on the
 * distribution of first elements in a given permutation class.
 * 
 * @author Michael Albert
 */
public class FirstElementDistribution {

    // An anonymously defined PermProcessor to collect the counts
    
    static long[] counts;
    
    static final PermProcessor firsElementCounter = new PermProcessor() {
        
        @Override
        public void process(Permutation p) {
            counts[p.at(0)]++;
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String report() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    };
    
    public static void main(String[] args) {
        PermutationClass c = new PermutationClass("321");
        for(int n = 1; n <= 10; n++) {
            counts = new long[n];
            c.processPerms(n, firsElementCounter);
            System.out.println(n + " " + Arrays.toString(counts));
        }
    }
    
}
