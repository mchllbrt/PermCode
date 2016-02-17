package permlib.examples;

import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.classes.SimplePermClass;
import permlib.processor.PermCollector;
import permlib.property.AvoidanceTest;
import permlib.property.Complement;
import permlib.property.HereditaryMerge;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.property.Intersection;

/**
 * Does the merge of Av(1342) intersect Sep with itself contain Av(1342)
 *
 * @author Michael Albert
 */
public class MergeofSepAv1342 {

    public static void main(String[] args) {

        PermutationClass theClass = new PermutationClass("1342");
        SimplePermClass s = new SimplePermClass("1342");
        HereditaryProperty a = AvoidanceTest.getTest("2413");
        HereditaryProperty b = AvoidanceTest.getTest("3142");
        HereditaryProperty c = AvoidanceTest.getTest("1342");
        HereditaryProperty d = HereditaryPropertyAdapter.forceHereditary(new Intersection(a, b, c));
        HereditaryProperty m = new HereditaryMerge(d, d);
        PermCollector proc = new PermCollector(new Complement(m));
        for (int n = 6; n <= 20; n++) {
            System.out.println("Testing " + n);
            proc.reset();
            s.processPerms(n, proc);
            if (!proc.getCollection().isEmpty()) {
                for (Permutation p : proc.getCollection()) {
                    System.out.println(p);
                }
            }
        }

    }

}
