/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 *
 * @author malbert
 */
public class IntervalBreakingExtensions {

    public static void main(String[] args) {

        PermutationClass c = new PermutationClass(new Permutation("25314"));
        int n = 11;
        for (Permutation p : new Permutations(c, n)) {
            check(p, c);
        }
    }

    public static void check(Permutation p, PermutationClass c) {
        if (PermUtilities.isSimple(p)) {
            return;
        }
        if (!PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
            return;
        }
        if (!PermUtilities.MINUSINDECOMPOSABLE.isSatisfiedBy(p)) {
            return;
        }

        int m = intervalInvariant(p);
        // System.out.println(p + " " + m);
        for (Permutation q : PermUtilities.onePointExtensions(p)) {
            if (c.containsPermutation(q) && intervalInvariant(q) < m) {
                return;
            }
        }

        System.out.println(p);

    }

    private static int intervalInvariant(Permutation p) {
        int result = 0;
        int i = 0;
        while (i < p.length()) {
            int j = rightEnd(p, i);
            result += j-i;
            i = j + 1;
        }
        return result;
    }

    private static int rightEnd(Permutation p, int i) {
        // System.out.print(p + " " + i + " ");
        for(int result = p.length() - ((i == 0) ? 1 : 0); result > i; result--) {
            if (isInterval(p, i, result)) {
                // System.out.println(result-1);
                return result-1;
            }
        }
        return -1;
    }

    private static boolean isInterval(Permutation p, int i, int result) {
        int min = p.elements[i];
        int max = p.elements[i];
        for(int j = i+1; j < result; j++) {
            min = (min < p.elements[j]) ? min : p.elements[j];
            max = (max > p.elements[j]) ? max : p.elements[j];
        }
        return max - min == result - i - 1;
    }

}
