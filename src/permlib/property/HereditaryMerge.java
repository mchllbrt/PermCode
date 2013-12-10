package permlib.property;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import permlib.classes.PermutationClass;
import permlib.Permutation;
import permlib.processor.PermProcessor;
import permlib.utilities.HereditaryUtilities;

/**
 * A property of permutations that can be split into two sub-permutations of
 * which one satisfies one given hereditary property, and the other another.
 * Thus, the whole permutation satisfies a merge of two hereditary properties.
 * 
 * @author MichaelAlbert
 */
public class HereditaryMerge implements HereditaryProperty {

    private HereditaryProperty a;
    private HereditaryProperty b;

    public HereditaryMerge(HereditaryProperty... props) {
        if (props.length == 0) {
            a = HereditaryUtilities.EMPTY;
            b = HereditaryUtilities.EMPTY;
        }
        if (props.length == 1) {
            a = props[0];
            b = HereditaryUtilities.EMPTY;
        }
        if (props.length == 2) {
            a = props[0];
            b = props[1];
        }
        if (props.length > 2) {
            a = props[0];
            b = new HereditaryMerge(Arrays.copyOfRange(props, 1, props.length));
        }
    }

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
        return isSatisfiedBy2(values);
    }

    public boolean isSatisfiedBy1(int[] values) {
        if (values.length > 31) {
            throw new UnsupportedOperationException("Not supported on sequences of length 32 or more yet.");
        }
        if (!a.isSatisfiedBy(Permutation.ONE)) {
            return b.isSatisfiedBy(values);
        }
        if (!b.isSatisfiedBy(Permutation.ONE)) {
            return a.isSatisfiedBy(values);
        }
        HashSet<Integer> thisLevel = new HashSet<Integer>();
        HashSet<Integer> nextLevel = new HashSet<Integer>();
        for (int i = 0; i < values.length; i++) {
            thisLevel.add(1 << i);
        }
        int level = 2;
        while (level < values.length && !thisLevel.isEmpty()) {
            nextLevel = nextLevel(thisLevel, level, values);
            if (hasMaximalWitness(thisLevel, nextLevel, level, values)) {
                return true;
            }
            thisLevel = nextLevel;
            nextLevel = new HashSet<Integer>();
            level++;
        }
        return !thisLevel.isEmpty();
    }

    private HashSet<Integer> nextLevel(HashSet<Integer> thisLevel, int level, int[] values) {
        HashSet<Integer> result = new HashSet<Integer>();
        for (int c : thisLevel) {
            for (int m = values.length - 1; (c >> m) == 0; m--) {
                int code = c | (1 << m);
                if (allDeletions(code, thisLevel) && a.isSatisfiedBy(codeToArray(code, level, values))) {
                    result.add(code);
                }
            }
        }
        return result;
    }

    private boolean hasMaximalWitness(HashSet<Integer> thisLevel, HashSet<Integer> nextLevel, int level, int[] values) {
        for (int c : thisLevel) {
            if (noExtension(c, nextLevel, values.length)) {
                int[] complement = codeToArray((1 << values.length) - c - 1, values.length - level + 1, values);
                if (b.isSatisfiedBy(complement)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int[] codeToArray(int code, int level, int[] values) {
        int[] result = new int[level];
        int codeIndex = 0;
        int arrayIndex = 0;
        while (arrayIndex < result.length) {
            if (((code >> codeIndex) & 1) != 0) {
                result[arrayIndex] = values[codeIndex];
                arrayIndex++;
            }
            codeIndex++;
        }
        return result;
    }

    private static int[] codeToArray(int code, int[] values) {
        int level = 0;
        for (int i = 0; i < values.length; i++) {
            if (((code >> i) & 1) != 0) {
                level++;
            }
        }
        return codeToArray(code, level, values);
    }

    private boolean noExtension(int c, HashSet<Integer> nextLevel, int length) {
        for (int m = 0; m < length; m++) {
            if (nextLevel.contains(c | (1 << m))) {
                return false;
            }
        }
        return true;
    }

    public boolean isSatisfiedBy2(int[] values) {
        if (a.isSatisfiedBy(values)) {
            return true;
        }

        HashSet<Integer> candidates = new HashSet<Integer>();
        for (int i = 0; i < values.length; i++) {
            candidates.add((1 << values.length) - (1 << i) - 1);
        }
        HashSet<Integer> maxAs = new HashSet<Integer>();
        HashSet<Integer> failures = new HashSet<Integer>();
        while (!candidates.isEmpty()) {
            failures.clear();
            for (int c : candidates) {
                if (a.isSatisfiedBy(codeToArray(c, values))) {
                    if (b.isSatisfiedBy(codeToArray((1 << values.length) - 1 - c, values))) {
                        return true;
                    } else {
                        maxAs.add(c);
                    }
                } else {
                    failures.add(c);
                }
            }
            candidates.clear();
            for (int c : failures) {
                for (int i = 0; i < values.length; i++) {
                    if ((c | (1 << i)) == c) {
                        int d = c & (~(1 << i));
                        boolean dCandidate = true;
                        for (int ma : maxAs) {
                            dCandidate = !((d & ma) == d);
                            if (!dCandidate) {
                                break;
                            }
                        }
                        if (dCandidate) {
                            candidates.add(d);
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean allDeletions(int code, HashSet<Integer> thisLevel) {
        for (int m = 0; (code >> m) != 0; m++) {
            if (((code >> m) & 1) != 0) {
                if (!thisLevel.contains(code - (1 << m))) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {

        HereditaryProperty a = AvoidanceTest.getTest("21");
        final HereditaryProperty b = AvoidanceTest.getTest("231");
        final HereditaryProperty m = new HereditaryMerge(a, b);
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
