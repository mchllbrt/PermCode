package permlib.examples;

import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

import java.util.Arrays;

/**
 * Created by Jinge Li on 2016-02-13.
 */
public class Av321DFS { // extends Av321Helper {

    Permutation p = new Permutation();
    // private Permutation t;

    // private Av321Helper h;

    // Let's first try a fixed length array to count the number of texts of each length avoiding the pattern.
    int[] avoidanceCount = new int[32];

    /**
     * Generating all 321-avoiding permutations of each length up to length maxLength, avoiding p.
     */
    public Av321DFS(Permutation p, int maxLength) {
        this.p = p;
        PermutationClass a321 = new PermutationClass("321");

        for (Permutation t : new Permutations(a321, p.length() + 1)) {
            Av321Helper firstCheck = new Av321Helper(p, t, false);

            if (!firstCheck.hasEmbedding()) {
                avoidanceCount[t.length()]++;
                System.out.println(p + " is not contained in: " + t + " on first check");
                generating321Avoidance(t, maxLength);
            }
        }
        System.out.println(Arrays.toString(avoidanceCount));
    }

    /**
     * find the last lower element in t
     */
    public int lastLower(int[] t) {
        for (int i = t.length - 1; i >= 0; i--) {
            if (Av321Helper.type(t)[i] == -1) {
                return i;
            }
        }
        return 0;
    }

    public int lastLower(Permutation t) {
        return lastLower(t.elements);
    }

    /**
     * recursive adding of last element and testing of avoidance
     */
    public void generating321Avoidance(Permutation t, int maxLength) {
        Permutation q;
        if (t.length() < maxLength) {
            // handles the cases where the added element is not maximum
            for (int i = t.elements[lastLower(t)] + (lastLower(t) != 0 ? 1 : 0); i < t.length(); i++) {

                // insert i into t (and update t)
//                PermUtilities.insert(t, t.length(), i);
                q = t.insert(t.length(), i); // but this inserted t will affect the t in for loop!

                // check if embedding exists using f0 = newInitialMapping()
                Av321Helper e = new Av321Helper(p, q, true);
                if (!e.hasEmbedding()) {
                    // if not, make a count and recursively call generating321Avoidance()
                    avoidanceCount[q.length()]++;
                    System.out.println(p + " is not contained in: " + q);
                    generating321Avoidance(q, maxLength);
                }
            }
            // the case where the added element is maximum
            q = t.insert(t.length(), t.length());
//            PermUtilities.insert(t, t.length(), t.length());
            avoidanceCount[q.length()]++;
            System.out.println(p + " is not contained in: " + q + " by inserting max");
            generating321Avoidance(q, maxLength);
        }
    }

    /**
     * test cases
     */
    public static void main(String[] args) {
        Permutation p = new Permutation("31452");
        long start = System.currentTimeMillis();
        Av321DFS p20 = new Av321DFS(p, 17);
        System.out.println(System.currentTimeMillis() - start);
    }
}
