/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.Collection;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;

/**
 *
 * @author MichaelAlbert
 */
public class Av321and13254 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        PermutationClass a321beta = new PermutationClass("321", "12354");

        for (int k = 7; k <= 7; k++) {
            for (Permutation p : new Permutations(a321beta, k)) {
                if (PermUtilities.PLUSIRREDUCIBLE.isSatisfiedBy(p)) {
                    System.out.println(p);
                    int dots = 0;
                    for(int i = 0; i < p.length(); i++) {
                        Permutation q = p.insert(i, p.elements[i]);
                        System.out.println(" " + q);
                        if (!a321beta.containsPermutation(q)) dots++;
                    }
                    System.out.println(p + " dots " + dots);
                    System.out.println();
                    
                }
            }
        }

        
    }
}
