package permlab.examples;

import java.util.ArrayList;
import java.util.List;
import permlib.classes.InvolutionPermClass;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Symmetry;
import permlib.processor.PermProcessor;
import permlib.property.PermProperty;

/**
 * Moths are (apparently) permutations whose principal classes are not simply
 * dominated.
 * 
 * @author Michael Albert
 */
public class Moths {

    final static InvolutionPermClass invs = new InvolutionPermClass();
    final static PermProcessor findExtensions = new PermProcessor() {

        @Override
        public void process(Permutation pp) {
            final Permutation p = Symmetry.R.on(pp);
            if (!PermUtilities.isSymmetryRep(p) || !PermUtilities.isSimple(p)) {
                return;
            }
            System.out.println("Processing " + p);
            PermProcessor qProc = new PermProcessor() {

                @Override
                public void process(Permutation qq) {
                    Permutation q = Symmetry.R.on(qq);
                    List<Integer> cp = candidatePositions(q);
                    if (cp.size() == 1) {

                        for (int i : cp) {
                            PermProperty avp = PermUtilities.avoidanceTest(p);
                            if (avp.isSatisfiedBy(q)) {
                                if (!hasCuttingExtension(q, i, avp)) {
                                    System.out.println(p + " | " + q);
                                }
                            }
                        }
                    }
                }

                @Override
                public void reset() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String report() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            };
            invs.processPerms(14,qProc);
        }

        private boolean isGoodExtension(Permutation q, int i, int v, PermProperty prop) {
            if ((i == 0 || i == q.length()) && (v == 0 || v == q.length())) {
                return false;
            }
            if (i < q.length() && (v == q.elements[i] + 1 || v == q.elements[i])) {
                return false;
            }
            if (i > 0 && (v == q.elements[i - 1] + 1 || v == q.elements[i - 1])) {
                return false;
            }
            return prop.isSatisfiedBy(PermUtilities.insert(q, i, v));
        }

        private boolean hasCuttingExtension(Permutation q, int i, PermProperty prop) {
            int a = q.elements[i];
            int b = q.elements[i + 1];
            int low = (a < b) ? a : b;
            int high = (a < b) ? b : a;
            if (high - low > 1) {
                return true;
            }
            for (int v = 0; v <= low - 1; v++) {
                if (isGoodExtension(q, i + 1, v, prop)) {
                    return true;
                }
            }
            for (int v = high + 2; v <= q.length(); v++) {
                if (isGoodExtension(q, i + 1, v, prop)) {
                    return true;
                }
            }
            for (int j = 0; j <= i - 1; j++) {
                if (isGoodExtension(q, j, high, prop)) {
                    return true;
                }
            }
            for (int j = i + 3; j <= q.length(); j++) {
                if (isGoodExtension(q, j, high, prop)) {
                    return true;
                }
            }
            return false;
        }

        private List<Integer> candidatePositions(Permutation q) {
            List<Integer> result = new ArrayList<Integer>();
            for (int i = 0; i < q.length() - 1; i++) {
                int d = q.elements[i + 1] - q.elements[i];
                if (d == 1 || d == -1) {
                    result.add(i);
                }
            }
            return result;
        }

        @Override
        public void reset() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String report() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    };

    public static void main(String[] args) {

        invs.processPerms(10, findExtensions);
    }
}
