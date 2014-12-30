package permlib.examples;

import java.util.Arrays;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;
import permlib.processor.PermProcessor;
import permlib.property.PermProperty;

/**
 * Look at subclasses of Av231 avoiding another permutation as an interval.
 *
 * @author Michael Albert
 */
public class PermIntervals {

    public static void main(String[] args) {

        PermutationClass c = new PermutationClass(new Permutation("231"));
        // doClass(c, 3, 6);
        // doPair(new Permutation("3 2 1"), new Permutation("3 1 2"), 10);
       doCounts(3);
       NoIntervalProperty nop = new NoIntervalProperty(new Permutation("321"));
       for(Permutation p : new Permutations(new PermutationClass("231"),5)) {
           if (!nop.isSatisfiedBy(p)) System.out.println(p);
       }
        
        

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
