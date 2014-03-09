/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 *
 * @author MichaelAlbert
 */
public class Temp {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Permutation p = new Permutation("231");
        PermutationClass a = new PermutationClass(new Permutation("4123"));
        PermutationClass b = new PermutationClass(new Permutation("2134"));
        HashSet<Permutation> invAs = new HashSet<>();
        HashSet<Permutation> invBs = new HashSet<>();
        for(Permutation q : new Permutations(new PermutationClass(p), 6)) {
            if (!a.containsPermutation(q)) {
                System.out.println("A " + q);
                invAs.add(q);
            }
            if (!b.containsPermutation(q)) {
                System.out.println("B " + q);
                invBs.add(q);
            }
        }
    }
    
}
