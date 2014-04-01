package permlib.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 * Used to create avoidance tests based on given permutations. Note, several
 * special case avoidance tests are implemented.
 *
 * @author Michael Albert
 */
public class AvoidanceTest implements HereditaryProperty {

    /**
     * An enum containing implementations for special case avoidance tests.
     */
    private enum SpecialCaseAvoidanceTest implements HereditaryProperty {

        A12 {
            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(new Permutation("12"));
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                for (int i = 1; i < values.length; i++) {
                    if (values[i] > values[i - 1]) {
                        return false;
                    }
                }
                return true;
            }
        },
        A21 {
            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(new Permutation("21"));
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                for (int i = 1; i < values.length; i++) {
                    if (values[i] < values[i - 1]) {
                        return false;
                    }
                }
                return true;
            }
        },
        A123 {
            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(new Permutation("123"));
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                int a = Integer.MAX_VALUE;
                int b = Integer.MAX_VALUE;
                for (int i = 0; i < values.length; i++) {
                    if (values[i] < a) {
                        a = values[i];
                    } else if (values[i] < b) {
                        b = values[i];
                    } else {
                        return false;
                    }
                }
                return true;
            }
        },
        A132 {
            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(new Permutation("132"));
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                return A312.isSatisfiedBy(PermUtilities.complement(values));
            }
        },
        A213 {
            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(new Permutation("213"));
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                return A312.isSatisfiedBy(PermUtilities.reverse(values));
            }
        },
        A312 {
            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(new Permutation("312"));
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return A231.isSatisfiedBy(PermUtilities.inverse(p));
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                return isSatisfiedBy(new Permutation(values));
            }
        },
        A231 {
            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(new Permutation("231"));
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                int[] stack = new int[values.length];
                int stackHead = -1;
                int lastOut = -1;
                for (int i = 0; i < values.length; i++) {
                    if (stackHead >= 0 && values[i] > stack[stackHead]) {
                        while (stackHead >= 0 && stack[stackHead] >= lastOut && values[i] > stack[stackHead]) {
                            lastOut = stack[stackHead--];
                        }
                        if (stackHead >= 0 && values[i] > stack[stackHead]) {
                            return false;
                        }
                    }
                    stack[++stackHead] = values[i];
                }
                return (stackHead < 0 || stack[stackHead] > lastOut);
            }
        },
        A321 {
            @Override
            public Collection<Permutation> getBasis() {
                HashSet<Permutation> result = new HashSet<Permutation>();
                result.add(new Permutation("321"));
                return result;
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                return A123.isSatisfiedBy(PermUtilities.reverse(values));
            }
        },
        A2143 {
            public static final int MAX_SIZE = 20;
            int[] largeValues = new int[MAX_SIZE];
            int[] smallValues = new int[MAX_SIZE];
            int largeStackTop = -1;
            int smallStackTop = -1;

            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(new Permutation("2143"));
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return isSatisfiedBy(p.elements);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {

                if (values.length < 4) return true;
                
                // Protection resize, don't want to do often
                if (values.length > largeValues.length) {
                    largeValues = new int[(values.length * 2) + 1];
                    smallValues = new int[largeValues.length];                    
                }
                
                // First compute from right to left "max 3 of a 43 to the right of here"
                int[] max3s = new int[values.length];
                int t = values.length - 1;
                largeStackTop = -1;
                do {
                    largeValues[++largeStackTop] = values[t];
                    t--;
                } while (t > 0 && values[t] < values[t + 1]);

                if (t == 0) {
                    return true; // The permutation is increasing
                }
                int max3 = Integer.MIN_VALUE;
                while (largeStackTop >= 0 && largeValues[largeStackTop] < values[t]) {
                    max3 = largeValues[largeStackTop--];
                }
                largeValues[++largeStackTop] = values[t];
                max3s[t] = max3;
                t--;
                while (t > 0) { // No point for smaller t, no room for 21
                    if (values[t] > max3) { // We might do better
                        while (largeStackTop >= 0 && largeValues[largeStackTop] < values[t]) {
                            max3 = largeValues[largeStackTop--];
                        }
                        largeValues[++largeStackTop] = values[t];
                    }
                    max3s[t] = max3;
                    t--;
                }
                // Now we need to work from left to right, looking for the "minimum 2 of a 21 to now"
                // If this is ever < max3 at this point, then we have a 2143
                smallStackTop = -1;
                t = 0;
                // The perm is not increasing so this won't overrun
                int small2 = Integer.MAX_VALUE;
                do {
                    smallValues[++smallStackTop] = values[t];
                    t++;
                } while (values[t - 1] < values[t]);
                while (smallStackTop >= 0 && smallValues[smallStackTop] > values[t]) {
                    small2 = smallValues[smallStackTop--];
                }
                smallValues[++smallStackTop] = values[t];
                t++;
                while (t <= values.length - 2) {
                    if (small2 < max3s[t]) {
                        return false;
                    }
                    if (values[t] < small2) { // We might do better
                        while (smallStackTop >= 0 && smallValues[smallStackTop] > values[t]) {
                            small2 = smallValues[smallStackTop--];
                        }
                        smallValues[++smallStackTop] = values[t];
                    }
                    t++;
                }
                return true;
            }
        };
    }
    
    private Involves inv;
    private Collection<Permutation> basis = new ArrayList<Permutation>();

    /**
     * The property of avoiding a single permutation.
     *
     * @param p the permutation
     */
    private AvoidanceTest(Permutation p) {
        this.inv = new Involves(p);
        basis.add(p);
    }

    /**
     * Determines whether a permutation has this property.
     *
     * @param q the permutation
     * @return <code>true</code> if <code>q</code> avoids the permutation used
     * to construct this property
     */
    @Override
    public boolean isSatisfiedBy(Permutation q) {
        return !inv.isSatisfiedBy(q);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        return !inv.isSatisfiedBy(values);
    }

    /**
     * Returns the basis of this property, i.e. the permutation that is avoided.
     *
     * @return the basis of this property
     */
    @Override
    public Collection<Permutation> getBasis() {
        return basis;
    }

    @Override
    public Collection<Permutation> getBasisTo(int n) {
        return basis;
    }

    /**
     * Used to create an avoidance test based on the input permutation. This is
     * the method that all avoidance test creations will go through. Note, this
     * method allows the option to check whether there are any applicable
     * special case tests implemented in the SpecialCaseAvoidanceTest enum.
     *
     * @param p the permutation for the test to avoid.
     * @param useSpecialTests whether to check for a special case test.
     * @return an avoidance test based on the input permutation.
     */
    public static HereditaryProperty getTest(Permutation p, boolean useSpecialTests) {
        if (useSpecialTests) {
            for (SpecialCaseAvoidanceTest test : SpecialCaseAvoidanceTest.values()) {
                if (test.getBasis().contains(p)) {
                    return test;
                }
            }
            if (PermProperty.INCREASING.isSatisfiedBy(p)) {
                return new AvoidsIncreasing(p.length());
            }
            if (PermProperty.DECREASING.isSatisfiedBy(p)) {
                return new AvoidsDecreasing(p.length());
            }
        }
        return new AvoidanceTest(p);
    }

    public static HereditaryProperty getTest(Permutation p) {
        return getTest(p, true);
    }

    public static HereditaryProperty getTest(String str) {
        return getTest(new Permutation(str));
    }
}