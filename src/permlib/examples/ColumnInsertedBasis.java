/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
        // foo(new Permutation("1423"),1);
        Permutation p = new Permutation("312");
        PermutationClass c = new PermutationClass(inserts(p,1));
        LRmaxCategories(c,7);
        c = new PermutationClass(inserts(p,2));
        System.out.println();
        LRmaxCategories(c,7);
    }
    
    public static void doIt() {
        
       for (Permutation p : new Permutations(4)) {
            for (int i = 1; i < p.length(); i++) {
                // System.out.println("Perm " + p + " Column: " + i);
                if (isRep(p)) {
                    spectrum(inserts(p, i), 12);
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
    
    public static void LRmaxCategories(PermutationClass c, int n) {
        HashMap<Integer, Integer> counts = new HashMap<>();
        for(Permutation p : new Permutations(c,n)) {
            int lt = lrMax(p).size();
            if (counts.containsKey(lt)) {
                counts.put(lt, counts.get(lt)+1);
            } else {
                counts.put(lt, 1);
            }
        }
        for(int lr : counts.keySet()) {
            System.out.println(lr + " " + counts.get(lr));
        }
        
    }
    
    public static ArrayList<Integer> lrMax(Permutation p) {
        ArrayList<Integer> result = new ArrayList<>();
        int m = -1;
        for(int i = 0; i < p.length(); i++) {
            if (p.elements[i] > m) {
                result.add(i);
                m = i;
            }
        }
        return result;
    }
}
