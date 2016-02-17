package permlib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import permlib.classes.PermutationClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 * Static methods for computing standard (and not so standard) statistics for
 * permutations.
 *
 * @author Michael Albert
 */
public class PermStatistics {

    /**
     * Computes the number of descents of a given permutation.
     *
     * @param p the permutation
     * @return the number of descents in <code>p</code>
     */
    public static int descents(Permutation p) {
        int result = 0;
        for (int i = 1; i < p.length(); i++) {
            result += (p.elements[i] < p.elements[i - 1]) ? 0 : 1;
        }
        return result;
    }

    /**
     * Computes the number of inversions of a given permutation.
     *
     * @param p the permutation
     * @return the number of inversions in <code>p</code>
     */
    public static int inversions(Permutation p) {
        int result = 0;
        for (int i = 0; i < p.length(); i++) {
            for (int j = i + 1; j < p.length(); j++) {
                result += (p.elements[i] < p.elements[j]) ? 0 : 1;
            }
        }
        return result;
    }

    /**
     * Computes the number of fixed points of a given permutation.
     *
     * @param p the permutation
     * @return the number of fixed points of <code>p</code>
     */
    public static int fixPoints(Permutation p) {
        int result = 0;
        for (int i = 0; i < p.length(); i++) {
            result += (p.elements[i] == i) ? 1 : 0;
        }
        return result;
    }

    /**
     * Computes the number of excedances (points above the diagonal) of a given
     * permutation.
     *
     * @param p the permutation
     * @return the number of excedances of <code>p</code>
     */
    public static int excedances(Permutation p) {
        int result = 0;
        for (int i = 0; i < p.length(); i++) {
            result += (p.elements[i] > i) ? 1 : 0;
        }
        return result;
    }

    /**
     * Computes the number of cycles of a given permutation.
     *
     * @param p the permutation
     * @return the number of cycles of <code>p</code>
     */
    public static int cycles(Permutation p) {
        int c = 0;
        boolean[] b = new boolean[p.length()];
        for (int i = 0; i < p.length(); i++) {
            if (!b[i]) {
                c++;
                b[i] = true;
                int j = p.elements[i];
                while (j != i) {
                    b[j] = true;
                    j = p.elements[j];
                }
            }
        }
        return c;
    }
    /**
     * Computes the cycle decomposition of a given permutation. Note that this
     * is returned in 1-based form.
     * 
     * @param p the permutation
     * @return its cycle decomposition in 1-based form
     */
    public static ArrayList<ArrayList<Integer>> cycleDecomposition(Permutation p) {
        ArrayList<ArrayList<Integer>>  result = new ArrayList<>();
        boolean[] b = new boolean[p.length()];
        for(int i = 0; i < p.length(); i++) {
            if (!b[i]) {
                ArrayList<Integer> c = new ArrayList<>();
                c.add(i+1);
                b[i] = true;
                int j = p.elements[i];
                while (j != i) {
                    b[j] = true;
                    c.add(j+1);
                    j = p.elements[j];
                }
                result.add(c);
            }
        }
        return result;
    }

    /**
     * Computes the cycle type (partition in non-increasing order) of a 
     * given permutation.
     * 
     * @param p the permutation
     * @return its cycle type.
     */
    public static int[] cycleType(Permutation p) {
        ArrayList<Integer> cycleLengths = new ArrayList<>();
        boolean[] seen = new boolean[p.length()];
        for (int i = 0; i < p.length(); i++) {
            if (!seen[i]) {
                seen[i] = true;
                int cycleLength = 1;
                for (int j = p.at(i); j != i; j = p.at(j)) {
                    seen[j] = true;
                    cycleLength++;
                }
                cycleLengths.add(cycleLength);
            }
        }
        Collections.sort(cycleLengths);
        Collections.reverse(cycleLengths);
        int[] result = new int[cycleLengths.size()];
        int i = 0;
        for (int c : cycleLengths) {
            result[i++] = c;
        }
        return result;
    }

    /**
     * Computes the depth (Petersen, Tenner) of a permutation which is the sum
     * over excedances of the amount of the excedance.
     *
     * @param p the permutation
     * @return the depth of <code>p</code>
     */
    public static int depth(Permutation p) {
        int d = 0;
        for (int i = 0; i < p.length(); i++) {
            if (p.elements[i] > i) {
                d += p.elements[i] - i;
            }
        }
        return d;
    }

}