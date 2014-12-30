/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import java.util.Arrays;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;

/**
 * Investigate Conj 19 from the paper.
 * 
 * @author Michael Albert
 */
public class Conjecture19 {
    
    public static void main(String[] args) {
        
        int n = 8;
        for(Permutation p : new Permutations(new PermutationClass("231"), n)) {
            System.out.println("GF" + Arrays.toString(p.elements) + ",");
        }
        
    }
    
    public static void testOne() {
        PermutationClass c1 = new PermutationClass("312", "23415");
        PermutationClass c2 = new PermutationClass("312", "23451");
        for(int n = 6; n <= 15; n++) {
            PermCounter counter = new PermCounter();
            c1.processPerms(n, counter);
            System.out.print(counter.getCount());
            System.out.print(" ");
            counter.reset();
            c2.processPerms(n, counter);
            System.out.println(counter.getCount());
        }
    }
    
    public static void testTwo() {
        int m = 4;
        Permutation a = new Permutation("1");
        PermutationClass c = new PermutationClass("312");
        PermCounter counter = new PermCounter();
        for(Permutation p : new Permutations(c, m)) {
            Permutation p1 = PermUtilities.sum(a, p);
            Permutation p2 = PermUtilities.skewSum(p, a);
            PermutationClass c1 = new PermutationClass(new Permutation("312"), p1);
            PermutationClass c2 = new PermutationClass(new Permutation("312"), p2);
            System.out.println(p);
            System.out.print(p1 + ": ");
                for(int k = m+2; k <= 10; k++) {
                counter.reset();
                c1.processPerms(k, counter);
                System.out.print(counter.getCount() + "   ");
            }
            System.out.println();
            System.out.print(p2 + ": ");
                for(int k = m+2; k <= 10; k++) {
                counter.reset();
                c2.processPerms(k, counter);
                System.out.print(counter.getCount() + "   ");
            }
            System.out.println();

            
        }
    }
    
    public static void testThree(int m) {
        Permutation a = new Permutation("1");
        PermutationClass c = new PermutationClass("312");
        PermCounter counter = new PermCounter();
        for(Permutation p : new Permutations(c, m)) {
            Permutation p1 = PermUtilities.sum(a, p);
            Permutation p2 = PermUtilities.skewSum(p, a);
            PermutationClass c1 = new PermutationClass(new Permutation("312"), p1);
            PermutationClass c2 = new PermutationClass(new Permutation("312"), p2);
            if (!smaller(c1, c2, m+2, 2*m+2)) {
                System.out.println(p1 + " " + p2);
            }            
        }
        System.out.println("Done");
    }
    
    public static boolean smaller(PermutationClass c1, PermutationClass c2, int low, int high) {
        
        PermCounter counter = new PermCounter();
        for(int n = low; n <= high; n++) {
            counter.reset();
            c1.processPerms(n, counter);
            long cc1 = counter.getCount();
            counter.reset();
            c2.processPerms(n, counter);
            long cc2 = counter.getCount();
            if (cc1 > cc2) return false;
          
        }
        return true;
    }
    
}
