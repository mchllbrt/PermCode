package permlib.examples;

import java.util.Arrays;
import java.util.HashMap;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;
import permlib.processor.PermProcessor;
import permlib.property.PermProperty;
import permlib.utilities.IntPair;

/**
 * Look at subclasses of Av231 avoiding another permutation as an interval.
 *
 * @author Michael Albert
 */
public class PermIntervals {

    public static void main(String[] args) {

        // 142365 and 213456
        doJointPair(new Permutation("142365"), new Permutation("213456"), 8);
        // doWilfImpliesJoint(5,7);

    }

    public static void doPerm(Permutation p, int k) {
        PermutationClass c = new PermutationClass("231");
        System.out.print(p + " ");
        NoIntervalProperty nop = new NoIntervalProperty(p);
        PermCounter counter = new PermCounter(nop);
        for (int n = p.length() + 1; n <= p.length() + k; n++) {
            counter.reset();
            for (Permutation q : new Permutations(c, n)) {
                counter.process(q);
            }
            System.out.print(counter.getCount() + " ");
        }
        System.out.println();
    }
    
    public static long[] permSpec(Permutation p, int k) {
        long[] result = new long[k];
        PermutationClass c = new PermutationClass("231");
        NoIntervalProperty nop = new NoIntervalProperty(p);
        PermCounter counter = new PermCounter(nop);
        for (int n = p.length() + 1; n <= p.length() + k; n++) {
            counter.reset();
            for (Permutation q : new Permutations(c, n)) {
                counter.process(q);
            }
            result[n-p.length()-1] = counter.getCount();
        }
        return result;
    }

    public static HashMap<Permutation, long[]> permSpecs(int n, int k) {
        HashMap<Permutation, long[]>  result = new HashMap<>();
        PermutationClass c = new PermutationClass("231");
        for(Permutation p : new Permutations(c,n)) {
            result.put(p, permSpec(p,k));
        }
        return result;
    }
    
    public static void doWilfImpliesJoint(int n, int k) {
        HashMap<Permutation, long[]>  specs = permSpecs(n,k);
        for(Permutation p : specs.keySet()) {
            for(Permutation q : specs.keySet()) {
                if (!p.equals(q) && Arrays.equals(specs.get(p), specs.get(q))) {
                    doJointPair(p,q,k);
                }
            }
        }
        
    }
    public static void doPair(Permutation p, Permutation q, int k) {
        PermutationClass c = new PermutationClass("231");
        NoIntervalProperty nop = new NoIntervalProperty(p);
        NoIntervalProperty noq = new NoIntervalProperty(q);
        PermCounter counterp = new PermCounter(nop);
        PermCounter counterq = new PermCounter(noq);
        for (int n = p.length() + 1; n <= p.length() + k; n++) {
            System.out.print(n + ": ");
            counterp.reset();
            counterq.reset();
            for (Permutation per : new Permutations(c, n)) {
                counterp.process(per);
                counterq.process(per);
            }
            System.out.println(counterp.getCount() + " " + counterq.getCount());
        }
    }

    public static void doJointPair(Permutation p, Permutation q, int k) {
        PermutationClass c = new PermutationClass("231");
        IntervalCounter pInt = new IntervalCounter(p);
        IntervalCounter qInt = new IntervalCounter(q);
        for (int n = p.length() + 1; n <= p.length() + k; n++) {
            // System.out.println("In length " + n);
            HashMap<IntPair, Integer> counts = new HashMap<>();
            for (Permutation r : new Permutations(c, n)) {
                pInt.reset();
                qInt.reset();
                IntPair pair = new IntPair(pInt.getCount(r), qInt.getCount(r));
                if (!counts.containsKey(pair)) {
                    counts.put(pair, 0);
                    counts.put(new IntPair(qInt.getCount(r), pInt.getCount(r)), 0);
                }
                counts.put(pair, counts.get(pair) + 1);
            }
            for (IntPair pair : counts.keySet()) {
                // System.out.println(pair + " " + counts.get(pair) + " " + counts.get(pair.reverse()));
                if (!counts.get(pair).equals(counts.get(pair.reverse()))) {
                    System.out.println(p + " " + q + " " + n);
                    return;
                }
            }
        }
    }

    public static void doClass(PermutationClass c, int k, int levels) {
        for (Permutation p : new Permutations(c, k)) {
            System.out.print(p + " ");
            NoIntervalProperty nop = new NoIntervalProperty(p);
            PermCounter counter = new PermCounter(nop);
            for (int n = k + 1; n <= k + levels; n++) {
                counter.reset();
                for (Permutation q : new Permutations(c, n)) {
                    counter.process(q);
                }
                System.out.print(counter.getCount() + " ");
            }
            System.out.println();
        }
    }

    private static void doCounts(int k) {
        PermutationClass c = new PermutationClass("231");
        for (Permutation p : new Permutations(c, k)) {
            System.out.println(p);
            IntervalCounter counter = new IntervalCounter(p);
            for (int n = k + 1; n <= k + 5; n++) {
                int[] counts = new int[n - k + 2];
                for (Permutation q : new Permutations(c, n)) {
                    counts[counter.getCount(q)]++;
                }
                System.out.println(Arrays.toString(counts));
            }
            System.out.println();
        }
    }

    private static void doCounts(Permutation p, int k) {
        PermutationClass c = new PermutationClass("231");
        System.out.println(p);
        IntervalCounter counter = new IntervalCounter(p);
        for (int n = p.length()+1; n <= p.length() + k; n++) {
            int[] counts = new int[n - p.length() + 2];
            for (Permutation q : new Permutations(c, n)) {
                counts[counter.getCount(q)]++;
            }
            System.out.println(Arrays.toString(counts));
        }
        System.out.println();
    }

    private static void doCounts(int k, int n) {
        PermutationClass c = new PermutationClass("231");
        HashMap<Permutation, int[]> counts = new HashMap<>();
        for (Permutation p : new Permutations(c, k)) {
            // System.out.println(p);
            IntervalCounter counter = new IntervalCounter(p);
            int[] pCounts = new int[n - k + 2];
            for (Permutation q : new Permutations(c, n)) {
                pCounts[counter.getCount(q)]++;
            }
            counts.put(p, pCounts);
        }
        for (Permutation p : counts.keySet()) {
            System.out.println(p + " " + Arrays.toString(counts.get(p)));
        }
        for (Permutation p : counts.keySet()) {
            int[] pc = counts.get(p);
            for (Permutation q : counts.keySet()) {
                int[] pq = counts.get(q);
                if (!p.equals(q) && pc[0] == pq[0] && !Arrays.equals(pc, pq)) {
                    System.out.println("Counterexample: " + p + " " + q);
                }
            }
        }
    }

    static class NoIntervalProperty implements PermProperty {

        int[] patValues;

        public NoIntervalProperty(Permutation pat) {
            this.patValues = pat.elements;
        }

        @Override
        public boolean isSatisfiedBy(Permutation p) {
            return isSatisfiedBy(p.elements);
        }

        @Override
        public boolean isSatisfiedBy(int[] values) {
            for (int i = 0; i <= values.length - patValues.length; i++) {
                int offset = values[i] - patValues[0];
                int j = 1;
                while (j < patValues.length && patValues[j] + offset == values[i + j]) {
                    j++;
                }
                if (j == patValues.length) {
                    return false;
                }
            }
            return true;
        }

    }

    static class IntervalCounter implements PermProcessor {

        int[] patValues;
        int count;

        public IntervalCounter(Permutation pat) {
            this.patValues = pat.elements;
        }

        public boolean process(int[] values) {
            reset();
            for (int i = 0; i <= values.length - patValues.length; i++) {
                int offset = values[i] - patValues[0];
                int j = 1;
                while (j < patValues.length && patValues[j] + offset == values[i + j]) {
                    j++;
                }
                if (j == patValues.length) {
                    count++;
                }
            }
            return true;
        }

        @Override
        public void process(Permutation p) {
            process(p.elements);
        }

        @Override
        public void reset() {
            count = 0;
        }

        public int getCount(Permutation p) {
            process(p);
            return count;
        }

        @Override
        public String report() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
