package permlib.examples;

import java.util.Arrays;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.SimplePermClass;
import permlib.processor.PermProcessor;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 * Investigate statistics in simple permutations e.g., length of longest 
 * exceptional subpermutation, increasing oscillation etc.
 * 
 * @author Michae Albert
 */
public class SimpleStatistics {

     public static void main(String[] args) {
        for(int n = 4; n <= 12; n++) exceptionalStats(n);
    }
    
    public static void exceptionalStats(int n) {
        
        SimplePermClass s = new SimplePermClass();
        final HereditaryProperty[] avEx = new HereditaryProperty[n/2-1];
        final int[] avoidCounts = new int[avEx.length+1];
        for(int i = 0; i < avEx.length; i++) {
            avEx[i] = AvoidanceTest.getTest(PermUtilities.exceptionalSimples(2*i+4)[0]);
            //System.out.println(PermUtilities.exceptionalSimples(2*i+4)[0]);
        }
        
        PermProcessor proc = new PermProcessor() {

            @Override
            public void process(Permutation p) {
                int i = 0;
                for(i = 0; i < avEx.length && !avEx[i].isSatisfiedBy(p); i++) {
                    
                }
                avoidCounts[i]++;
            }

            @Override
            public void reset() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String report() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
            public int[] counts() {
                return avoidCounts;
            }
            
        };
        
        s.processPerms(n, proc);
        System.out.println(Arrays.toString(avoidCounts));
       
    }
    
    public static void skeletonAfterReversals(int length, int reversals) {
        
        
    }
}
