package permlib.examples;

import permlib.Permutation;
import permlib.property.AvoidsDecreasing;
import permlib.property.AvoidsIncreasing;
import permlib.property.Griddable;
import permlib.property.PermProperty;

/**
 *
 * @author MichaelAlbert
 */
public class DSM {

    final static PermProperty ad = new AvoidsDecreasing(3);
    final static PermProperty ai = new AvoidsIncreasing(3);
    final static Griddable g = new Griddable(new PermProperty[][]{{ad, ai}, {ai, ad}});

    public static void main(String[] args) {
        Permutation p = new Permutation("321987456");
        System.out.println(g.isSatisfiedBy(p, true));
        
    }
}
