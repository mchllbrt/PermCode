package permlab.examples;

import permlib.property.Griddable;
import permlib.property.PermProperty;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.Permutation;

/**
 *
 * @author MichaelAlbert
 */
public class GridExamples {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Griddable g = new Griddable(
                new PermProperty[][] {
                    {PermProperty.INCREASING, PermProperty.EMPTY}, 
                    {PermProperty.EMPTY, PermProperty.DECREASING},
                    {PermProperty.EMPTY, PermProperty.INCREASING},
                    {PermProperty.DECREASING, PermProperty.EMPTY}
                });
        HereditaryProperty n = HereditaryPropertyAdapter.forceHereditary(g);
        for(Permutation p : n.getBasisTo(8)) System.out.println(p);
    }
}
