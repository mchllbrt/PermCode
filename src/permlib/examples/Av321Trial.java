/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.Arrays;
import permlib.Permutation;

/**
 *
 * @author MichaelAlbert
 */
public class Av321Trial {

    private static final int UPPER_ELEMENT = 1;
    private static final int LOWER_ELEMENT = -1;
    private static final int FLUID_ELEMENT = 0;

    public static int[] type(int[] p) {
        int[] result = new int[p.length];
        int maxSeen = -1;
        int blockBegin = 0;
        for (int i = 0; i < p.length; i++) {
            if (p[i] > maxSeen) {
                maxSeen = p[i];
            } else {
                result[i] = LOWER_ELEMENT;
                int j = i - 1;
                System.out.println(i + " " + p[i] + " " + blockBegin);
                while (j >= blockBegin && p[j] > p[i]) {
                    result[j] = UPPER_ELEMENT;
                    j--;
                }
                blockBegin = i + 1;
            }
            
        }

        return result;
    }

    public static int[] type(Permutation p) {
        return type(p.elements);
    }

    /**
     * try some examples here (how do I get it running?)
     */
    public static void main(String[] args) {
        Permutation diag = new Permutation("41627358");
        int[] t = type(diag);
        System.out.println(Arrays.toString(t));
    }

}
