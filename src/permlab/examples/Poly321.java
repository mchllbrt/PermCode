/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlab.examples;

import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 *
 * @author MichaelAlbert
 */
public class Poly321 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PermutationClass c = new PermutationClass("321", "13245");
        for(int n = 4; n <= 10; n++) {
            for(Permutation p : new Permutations(c,n,PermUtilities.PLUSIRREDUCIBLE)) {
                System.out.println(p);
            }
        }
    }
    
}
