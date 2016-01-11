/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import java.util.HashSet;
import permlib.Permutation;
import permlib.classes.SimplePermClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 *
 * @author MichaelAlbert
 */
public class BasisForSubsClosureAv321 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        HashSet<Permutation> basis = new HashSet<>();
        for(int n = 5; n <= 15; n++) {
            SimplePermClass c = new SimplePermClass(basis);
            HereditaryProperty a = AvoidanceTest.getTest(new Permutation("321"));
            for(Permutation p : c.getPerms(n)) {
                if (!a.isSatisfiedBy(p)) {
                    basis.add(p);
                    System.out.println(p);
                }
            }
            
        }
    }
    
}
