/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import java.util.Arrays;
import permlib.Permutation;
import permlib.Permutations;

/**
 *
 * @author MichaelAlbert
 */
public class RandomStuff {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        for(int n = 3; n <= 10; n++) {
            showMax(n);
        }
        
        
    }
    
    public static long compute(int[] values) {
        long result = values[0];
        for(int i = 1; i < values.length; i++) {
            result = (result+1)*(values[i]);
        }
        return result;
    }

    private static void showMax(int n) {
        long max = 0; int maxCount = 0;
        Permutation q = new Permutation();
        for(Permutation p : new Permutations(n)) {
            int[] e = Arrays.copyOf(p.elements, n);
            for(int i = 0; i < e.length; i++) e[i] += 1;
            long vp = compute(e);
            if (vp > max) {
                max = vp; q = p; maxCount = 1;
            } else if (vp == max) {
                maxCount++;
            }
        }
        System.out.println(n + "  " + max + ":  " + q + " (" + maxCount + ")");
    }
    
}
