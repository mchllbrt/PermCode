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
 * @author Michael Albert
 */
public class LayeredMinusOne {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PermutationClass l = new PermutationClass("231", "312");
        HashSet<Permutation> basis = new HashSet<Permutation>();
        PermutationClass c = new PermutationClass(basis);
        for(int n = 3; n <= 10; n++) {
            for(Permutation p : new Permutations(c,n)) {
                if (!l.containsPermutation(p) && 
                        (!(p.at(p.length()-1) == 0) || 
                        !(l.containsPermutation(p.delete(p.length()-1))))) {
                    basis.add(p);
                    System.out.println(p);
                }
            }
            c = new PermutationClass(basis);
        }
    }
    
}
