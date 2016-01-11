/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import permlib.Permutation;
import permlib.Permutations;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryMerge;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.property.Intersection;
import permlib.property.MinusIrreducible;
import permlib.property.PermProperty;
import permlib.property.PlusIrreducible;

/**
 *
 * @author MichaelAlbert
 */
public class BasisOfSepMergeSep {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HereditaryProperty a = AvoidanceTest.getTest("2413");
        HereditaryProperty b = AvoidanceTest.getTest("3142");
        HereditaryProperty d = HereditaryPropertyAdapter.forceHereditary(new Intersection(a, b));
        HereditaryProperty m = new HereditaryMerge(d, d);
        PermProperty irr = new Intersection(new PlusIrreducible(), new MinusIrreducible());
        for (Permutation p : new Permutations(11)) {
            if (irr.isSatisfiedBy(p) && !m.isSatisfiedBy(p)) {
                System.out.println(p);
            }
        }
    }

}
