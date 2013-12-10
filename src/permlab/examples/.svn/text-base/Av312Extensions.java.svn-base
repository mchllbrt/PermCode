package permlab.examples;

import java.util.Arrays;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 * Extending a 312 avoider on the right and then removing sufficiently many
 * small values to re-establish 312 avoidance describes the evolution of the
 * maximal 312 avoiding top values of a 4231 avoiding permutation. Perhaps this
 * can be used to find/bound growth rates?
 *
 * @author Michael Albert
 */
public class Av312Extensions {

    public static void main(String[] args) {

//        int maxLength = 0;
//        HashMap<Permutation, Long> currentGeneration = new HashMap<Permutation, Long>();
//        currentGeneration.put(new Permutation(1), 1L);
//        for (int time = 0; time < 12; time++) {
//            HashMap<Permutation, Long> nextGeneration = new HashMap<Permutation, Long>();
//            for (Permutation p : currentGeneration.keySet()) {
//                long count = currentGeneration.get(p);
//                // System.out.println(p + " " + count);
//                for (int v = 0; v <= p.length(); v++) {
//                    Permutation q = doExtension(p, v);
//                    if (nextGeneration.containsKey(q)) {
//                        nextGeneration.put(q, nextGeneration.get(q) + count);
//                    } else {
//                        nextGeneration.put(q, count);
//                    }
//                }
//
//            }
//            currentGeneration = nextGeneration;
//            System.out.println("----------------");
//            System.out.println("Length summary");
////            long[] counts = new long[time+3];
//            for (Permutation p : currentGeneration.keySet()) {
//                if (p.length() == 3) {
//                    System.out.println(p + " " + currentGeneration.get(p));
//                }
//            }
//            // for(int i = 2; i < counts.length; i++) System.out.println(i + " " + counts[i]);
//            System.out.println("----------------");
//        }

        // Try a series of walks, weighting each by the mean branching factor.
        Permutation p = new Permutation(1);
        int trials = 100;
        int trialLength = 10000;
        double[] weights = new double[trials];
        for (int trial = 0; trial < trials; trial++) {
            for (int time = 0; time < trialLength; time++) {
                weights[trial] += Math.log(p.length() + 1);
                p = doExtension(p, (int) (Math.random() * (p.length() + 1)));
            }
            // weights[trial] = Math.exp(weights[trial]);
        }
        double minWeight = trialLength * trialLength;
        for (int i = 0; i < weights.length; i++) {
            minWeight = (minWeight < weights[i]) ? minWeight : weights[i];
        }
        double sum = 0;
        for (int i = 0; i < weights.length; i++) {
            weights[i] -= minWeight;
            sum += Math.exp(weights[i]);
        }
        System.out.println(Arrays.toString(weights));
        sum /= weights.length;
        System.out.println(sum);
        System.out.println(Math.exp((minWeight + Math.log(sum))/trialLength));


//        PermProperty i312 = new Involves("312");
//        for(Permutation p : new Permutations(1,5)) {
//        if (i312.isSatisfiedBy(p)) continue;
//        StringBuilder out = new StringBuilder();
//        out.append(p + " --> ");
//        Permutation[] exts = new Permutation[p.length()+1];
//        for(int v = 0; v <= p.length(); v++) {
//            exts[v] = doExtension(p,v);
//        }
//        Arrays.sort(exts);
//        for(Permutation q : exts) out.append(q + " + ");
//        out.delete(out.length()-2, out.length());
//        System.out.println(out);
//        }
//            // System.out.println(p);
//            if (time % 10000 == 0) {
//                maxLength = (maxLength >= p.length()) ? maxLength : p.length();
//                counts[p.length()]++;
//            }
//            
        //       }
        //       for(int i = 2; i <= maxLength; i++) System.out.println((i + "     ").substring(0,5) + counts[i]);

    }

    private static Permutation doExtension(Permutation p, int v) {
        // Add v at the end of p, find the maximum 1 in a 312 that
        // creates and delete it and all smaller elements. To find that
        // 1, scan left to right for the first element >= v. Now scan for
        // the next element < v -- all other 1's are smaller else 312.
        // System.out.println("Appending " + (v+1) + " to " + p);
        int i = 0;
        while (i < p.length() && p.elements[i] < v) {
            i++;
        }
        while (i < p.length() && p.elements[i] >= v) {
            i++;
        }
        int lowValue = (i < p.length()) ? p.elements[i] : -1;
        // System.out.println("Low value = " + (lowValue+1));
        int[] newElements = new int[p.length() - lowValue];
        int newIndex = 0;
        for (i = 0; i < p.length(); i++) {
            if (p.elements[i] > lowValue) {
                newElements[newIndex] = p.elements[i] - lowValue - 1;
                newElements[newIndex] += (p.elements[i] >= v) ? 1 : 0;
                newIndex++;
            }
        }
        newElements[newIndex] = v - lowValue - 1;
        return new Permutation(newElements, PermUtilities.SAFE);
    }
}
