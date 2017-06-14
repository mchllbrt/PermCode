/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Properties of the class that avoids all cycles of a fixed length (FOLP connection)
 * @author MichaelAlbert
 */
public class AvoidCycles {

     public static void main(String[] args) {
        for(int n = 7; n <= 13; n++) doK(7,n);
    }
     
     public static void do4() {
         PermutationClass c = new PermutationClass("2341", "2413", "3421", "3142", "4312","4123");
        for(Permutation p : new Permutations(c,12)) {
            if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
                ArrayList<ArrayList<Integer>> cs = PermUtilities.cycles(p);
                int[] cl = new int[cs.size()];
                int i = 0;
                for(ArrayList<Integer> cc : cs) {
                    cl[i++] = cc.size();
                }
                Arrays.sort(cl);
                System.out.println(p + " " + Arrays.toString(cl));
            }
        }
     }
     
     public static void doK(int k, int n) {
         ArrayList<Permutation> b = new ArrayList<>();
         for(Permutation p : new Permutations(k)) {
             if (PermUtilities.cycles(p).size() == 1) {
                 b.add(p);
             }
         }
        PermutationClass c = new PermutationClass(b);
        for(Permutation p : new Permutations(c,n)) {
            if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
                ArrayList<ArrayList<Integer>> cs = PermUtilities.cycles(p);
                int[] cl = new int[cs.size()];
                int i = 0;
                for(ArrayList<Integer> cc : cs) {
                    cl[i++] = cc.size();
                }
                Arrays.sort(cl);
                if(cl[cl.length-1] >= k) {
                System.out.println(p + " " + Arrays.toString(cl));
                }
            }
        }
        System.out.println("Done " + k + " " + n);
     }
    
}
