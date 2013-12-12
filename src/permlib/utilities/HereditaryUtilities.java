package permlib.utilities;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import permlib.classes.PermutationClass;
import permlib.Permutation;
import permlib.processor.PermProcessor;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 * Static utility functions associated with hereditary properties
 *
 * @author MichaelAlbert
 */
public class HereditaryUtilities {

    public static final HereditaryProperty EMPTY = new HereditaryProperty() {

        @Override
        public Collection<Permutation> getBasis() {
            return Arrays.asList(Permutation.ONE);
        }

        @Override
        public Collection<Permutation> getBasisTo(int n) {
            return getBasis();
        }

        @Override
        public boolean isSatisfiedBy(Permutation p) {
            return false;
        }

        @Override
        public boolean isSatisfiedBy(int[] values) {
            return false;
        }
        
    };
    
    public static HereditaryProperty merge(final HereditaryProperty a, final HereditaryProperty b) {
        return new HereditaryProperty() {
            @Override
            public Collection<Permutation> getBasis() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                int n = values.length;
                for (int k = n; k >= 0; k--) {
                    for (int[] aIndices : new Combinations(n, k)) {
                        int[] aValues = new int[k];
                        for (int i = 0; i < k; i++) {
                            aValues[i] = values[aIndices[i]];
                        }
                        if (a.isSatisfiedBy(aValues)) {
                            int[] bIndices = Combinations.complement(n, aIndices);
                            int[] bValues = new int[n - k];
                            for (int i = 0; i < n - k; i++) {
                                bValues[i] = values[bIndices[i]];
                            }
                            if (b.isSatisfiedBy(bValues)) {
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        };
    }

    public static HereditaryProperty merge2(final HereditaryProperty a, final HereditaryProperty b) {
        return new HereditaryProperty() {
            @Override
            public Collection<Permutation> getBasis() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                int n = values.length;
                if (n > 31) {
                    throw new UnsupportedOperationException("Not supported on sequences of length 32 or greater");
                }
                if (!a.isSatisfiedBy(new int[]{0})) {
                    return b.isSatisfiedBy(values);
                }
                if (!b.isSatisfiedBy(new int[]{0})) {
                    return a.isSatisfiedBy(values);
                }
                HashSet<Integer> thisLevel = new HashSet<Integer>();
                for (int i = 0; i < values.length; i++) {
                    thisLevel.add(1 << i);
                }
                int k = 2;
                while (!thisLevel.isEmpty() && k < values.length) {
                    for(int[] c : new Combinations(values.length, k)) {
                       HashSet<Integer> nextLevel = new HashSet<Integer>();
                       int cCode = testCombination(c, thisLevel);
                       if (cCode > 0) nextLevel.add(cCode);
                    }
                }
                return false;
            }

            private int testCombination(int[] c, HashSet<Integer> thisLevel) {
                int cCode = 0;
                for(int i : c) cCode += (1 << i);
                
                
                return cCode;
            }
        };
    }

    public static void main(String[] args) {
        HereditaryProperty a = AvoidanceTest.getTest("21");
        HereditaryProperty b = AvoidanceTest.getTest("231");
        final HereditaryProperty m = merge(a, b);

        final HashSet<Permutation> basis = new HashSet<Permutation>();
        long now = System.currentTimeMillis();
        for (int n = 3; n < 11; n++) {
            System.out.println("n = " + n);
            PermutationClass c = new PermutationClass(basis);
            c.processPerms(n, new PermProcessor() {
                @Override
                public void process(Permutation p) {
                    if (!m.isSatisfiedBy(p)) {
                        System.out.println(p);
                        basis.add(p);
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
            });
        }
        System.out.println(System.currentTimeMillis() - now);
    }
}
