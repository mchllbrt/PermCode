/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.HashSet;
import permlib.classes.InvolutionPermClass;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.processor.PermCollector;
import permlib.processor.PermProcessor;
import permlib.processor.RestrictedPermProcessor;
import permlib.property.PermProperty;

/**
 *
 * @author MichaelAlbert
 */
public class InvolutionSchmerlTrotter {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int n = 9;
        InvolutionPermClass i = new InvolutionPermClass();
        PermCollector c = new PermCollector();
        PermProcessor s = new RestrictedPermProcessor(c, PermUtilities.SIMPLE);
        HashSet<Permutation>[] simpleInvolutions;
        simpleInvolutions = (HashSet<Permutation>[]) new HashSet[n+1];
        for(int k = 1; k <= n; k++) {
            // System.out.println(k);
            i.processPerms(k, s);
            simpleInvolutions[k] = c.getCollection();
            c.reset();           
        }
        System.out.println("Generation complete");
//        for(int k = 6; k <= n; k++) {
//            System.out.print(k + " ");
//            HashSet<Permutation> sk = (HashSet<Permutation>) simpleInvolutions[k].clone();
//            for(int m = k-1; m >= 1; m--) {
//                // System.out.println("** " + m + " " + sk.size());
//                for (Permutation p : simpleInvolutions[m]) {
//                    // System.out.println(p + " ");
//                    PermProperty ap = PermUtilities.avoidanceTest(p);
//                    HashSet<Permutation> tsk = new HashSet<Permutation>();
//                    for(Permutation q : sk) {
//                        if (ap.isSatisfiedBy(q)) tsk.add(q);
//                    }
//                    sk = tsk;
//                    // System.out.println(tsk.size());
//                    if (sk.isEmpty()) {
//                        System.out.println(m);
//                        break;
//                    }
//                }
//                if (sk.isEmpty()) break;
//            }
//        }
          for (Permutation p : simpleInvolutions[7])
              System.out.println(p);
    }
    
    
}
