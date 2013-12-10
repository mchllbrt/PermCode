package permlab.examples;

import permlib.processor.PermCounter;
import permlib.classes.PermutationClass;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.property.PermProperty;
import static permlib.PermUtilities.sum;
import static permlib.PermUtilities.skewSum;
import permlib.property.HereditaryProperty;

/**
 *
 * @author Michael Albert
 */
public class PeculiarBijections {

    /**
     * @param args the command line arguments
     */
    static PermProperty av231 = PermUtilities.avoidanceTest("231");
    static Permutation one = new Permutation("1");

    public static void main(String[] args) {
//        Permutation p = new Permutation("531264");
//        System.out.println("Id:  " + p);
//        System.out.println("R:   " + p.reverse());
//        System.out.println("C:   " + p.complement());
//        System.out.println("RC:  " + p.complement().reverse());
//        Permutation q = p.inverse();
//        System.out.println("I:   " + q);
//        System.out.println("RI:  " + q.reverse());
//        System.out.println("CI:  " + q.complement());
//        System.out.println("RCI: " + q.complement().reverse());

        Permutation p231 = new Permutation("231");
        Permutation p132 = new Permutation("132");

        PermutationClass a231 = new PermutationClass(p231);
        PermutationClass a132 = new PermutationClass(p132);
        
        for(int j = 3; j <= 8; j++) {
            for(Permutation p : a231.getPerms(j)) {
                HereditaryProperty ap = PermUtilities.avoidanceTest(p);
                HereditaryProperty app = PermUtilities.avoidanceTest(P(p));
                boolean good = true;
                for(Permutation q : a231.getPerms(10)) {
                    Permutation pq = P(q);
                    good = ap.isSatisfiedBy(q) && app.isSatisfiedBy(pq) ||
                           !ap.isSatisfiedBy(q) && !app.isSatisfiedBy(pq);
                    if (!good) break;
                }
                if (good) System.out.println(p + "  " + P(p));
            }
        }

//        for (int j = 8; j <= 8; j++) {
//            for (Permutation p : a231.getPerms(j)) {
//                PermCounter cCounter = new PermCounter(new Avoids(p));
//                PermCounter dCounter = new PermCounter(new Avoids(P(p)));
//                if(!P(p).equals(p.reverse())) {
//                    int i = j+1;
//                int maxLength = 12;
//                while (i <= maxLength) {
//                    a231.processPerms(i, cCounter);
//                    a132.processPerms(i, dCounter);
//                    if (cCounter.getCount() != dCounter.getCount()) {
//                        break;
//                    }
//                    i++;
//                }
//                if (i > maxLength) {
//                    System.out.println(p + "   " + P(p));
//                }
//                }
//            }
//        }
    }

    public static Permutation P(Permutation q) {
        if (!av231.isSatisfiedBy(q)) {
            return null;
        }
        if (q.length() <= 1) {
            return q;
        }
        int i = 0;
        while (q.elements[i] != q.length() - 1) {
            i++;
        }
        Permutation a = q.segment(0, i);
        Permutation b = q.segment(i + 1, q.length());
        return skewSum(sum(P(a), one), P(b));
    }
}
