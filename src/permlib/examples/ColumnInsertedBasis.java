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
import permlib.processor.PermCounter;

/**
 *
 * @author MichaelAlbert
 */
public class ColumnInsertedBasis {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        foo(new Permutation("1423"),1);
    }
    
    public static void doIt() {
        
       for (Permutation p : new Permutations(3)) {
            for (int i = 1; i < p.length(); i++) {
                // System.out.println("Perm " + p + " Column: " + i);
                if (isRep(p)) {
                    spectrum(inserts(p, i), 8);
                    System.out.println(" " + p + " " + "C" + i);
                }
            }
        }
    }
    
    public static void foo(Permutation p, int col) {
        for (Permutation q : inserts(p, col)) System.out.println(q);
    }

    public static ArrayList<Permutation> inserts(Permutation p, int col) {
        ArrayList<Permutation> result = new ArrayList<>();
        for (int i = 0; i <= p.length(); i++) {
            result.add(PermUtilities.insert(p, col, i));
        }
        return result;
    }

    private static void spectrum(ArrayList<Permutation> basis, int bound) {
        // System.out.println("Basis " + basis);
        PermutationClass c = new PermutationClass(basis);
        long[] counts = new long[bound];
        PermCounter counter = new PermCounter();
        for (int i = 1; i <= bound; i++) {
            c.processPerms(i, counter);
            counts[i - 1] = counter.getCount();
            counter.reset();
        }
        System.out.print(Arrays.toString(counts));
        // System.out.println(" Basis" + basis);

    }

    public static boolean isRep(Permutation p) {

        return p.compareTo(p.reverse()) <= 0
                && p.compareTo(p.complement()) <= 0
                && p.compareTo(p.reverse().complement()) <= 0;
    }

}
