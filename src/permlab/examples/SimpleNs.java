/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import permlib.property.Griddable;
import permlib.property.PermProperty;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.Permutation;

/**
 * What is the apparent basis of the class of N-shaped simple permutations?
 * @author MichaelAlbert
 */
public class SimpleNs {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Griddable g = new Griddable(
                new PermProperty[][] {
                    {PermProperty.INCREASING, PermProperty.DECREASING, PermProperty.INCREASING}
                });
        HereditaryProperty n = HereditaryPropertyAdapter.forceHereditary(g);
        for(Permutation p : n.getBasisTo(8)) System.out.println(p);
    }
}
