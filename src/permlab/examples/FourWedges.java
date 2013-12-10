/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import permlib.property.PermProperty;
import permlib.property.Intersection;
import permlib.property.HereditaryProperty;
import permlib.property.Union;
import permlib.property.HereditaryPropertyAdapter;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 * What is the basis of the class which is the union of the four wedges?
 * @author Michael Albert
 */
public class FourWedges {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        PermProperty left =
                new Intersection(
                PermUtilities.avoidanceTest("132"),
                PermUtilities.avoidanceTest("312"));
        
        PermProperty right =
                new Intersection(
                PermUtilities.avoidanceTest("213"),
                PermUtilities.avoidanceTest("231"));
        
        PermProperty up =
                new Intersection(
                PermUtilities.avoidanceTest("213"),
                PermUtilities.avoidanceTest("312"));
        
        PermProperty down =
                new Intersection(
                PermUtilities.avoidanceTest("132"),
                PermUtilities.avoidanceTest("231"));
        
        HereditaryProperty fourWedge =
                HereditaryPropertyAdapter.forceHereditary(
                new Union(left, right, up, down)
                );
        
        for(Permutation p : fourWedge.getBasisTo(8))
            System.out.println(p);
    }
}
