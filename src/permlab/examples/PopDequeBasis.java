package permlab.examples;

import permlib.property.PermProperty;
import permlib.property.Intersection;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.property.PlusCompletion;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 * Investigate the basis of the pop deque class on the assumption that the 
 * basis for its plus components is 4123, 1423, 3124, 1324
 * @author Michael Albert
 */
public class PopDequeBasis {

    public static void main(String[] args) {

        PermProperty componentBasis =
                new Intersection(
                PermUtilities.avoidanceTest("4123"),
        PermUtilities.avoidanceTest("1423"),
        PermUtilities.avoidanceTest("3124"),
        PermUtilities.avoidanceTest("1324"));

        HereditaryProperty popDequeSortable =
                HereditaryPropertyAdapter.forceHereditary(
                new PlusCompletion(componentBasis));
        
        for(Permutation p : popDequeSortable.getBasisTo(11)) {
            System.out.println(p);
        }
    }
}
