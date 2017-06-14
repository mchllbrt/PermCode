/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import permlib.Permutation;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.property.Intersection;
import permlib.property.PermProperty;
import permlib.property.Union;

/**
 *
 * @author MichaelAlbert
 */
public class MRConjecture0 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        PermProperty a312 = AvoidanceTest.getTest("312");
        PermProperty a231 = AvoidanceTest.getTest("231");
        PermProperty a123 = AvoidanceTest.getTest("123");
        PermProperty a132 = AvoidanceTest.getTest("132");
        PermProperty a213 = AvoidanceTest.getTest("213");
        PermProperty a321 = AvoidanceTest.getTest("321");
        
        PermProperty c = new Union(a123, a132, a213);
        PermProperty d = new Intersection(c, a321, a231, a312);
        HereditaryProperty h = HereditaryPropertyAdapter.forceHereditary(d);
        for(Permutation p : h.getBasisTo(10)) System.out.println(p);
    }
    
}
