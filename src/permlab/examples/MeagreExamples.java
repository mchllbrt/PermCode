package permlab.examples;

import java.util.HashSet;
import permlib.classes.PermutationClass;
import permlib.processor.PermProcessor;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.SimplePermClass;
import permlib.classes.UniversalPermClass;
import permlib.property.Involves;

/**
 * Determine whether certain principal classes appear to be meagre.
 * @author Michael Albert
 */
public class MeagreExamples {

    public static void main(String[] args) {

        for (Permutation p : new UniversalPermClass(5)) {
            System.out.print(p + " ");
            SimplePermClass s = new SimplePermClass(p);
            PermutationClass c = new PermutationClass(p);
            int n = 7;
            HashSet<Involves> invTests = new HashSet<Involves>();
            for (Permutation q : c.getPerms(n)) {
                invTests.add(new Involves(q));
            }
            DetermineInvolvements d = new DetermineInvolvements(invTests);
            s.processWithTo((PermProcessor) d, 2 * n);
            System.out.println(d.numberRemainingBad());
        }

    }

    static class DetermineInvolvements implements PermProcessor {

        HashSet<Involves> badTests = new HashSet<Involves>();

        DetermineInvolvements(HashSet<Involves> tests) {
            badTests.addAll(tests);
        }

        @Override
        public void process(Permutation p) {
            HashSet<Involves> stillBad = new HashSet<Involves>();
            for (Involves i : badTests) {
                if (!i.isSatisfiedBy(p)) {
                    stillBad.add(i);
                }
            }
            badTests = stillBad;
        }

        public int numberRemainingBad() {
            return badTests.size();
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String report() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
