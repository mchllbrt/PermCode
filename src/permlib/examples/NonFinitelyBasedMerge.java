/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.Arrays;
import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryMerge;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.property.Intersection;

/**
 *
 * @author MichaelAlbert
 */
public class NonFinitelyBasedMerge {

    public static void main(String[] args) {
        bar();
//        HereditaryProperty a1 = AvoidanceTest.getTest("321");
//        HereditaryProperty a2 = AvoidanceTest.getTest("231");
//        HereditaryProperty a3 = AvoidanceTest.getTest("1324");
//        HereditaryProperty a4 = AvoidanceTest.getTest("312");
//        HereditaryProperty z = HereditaryPropertyAdapter.forceHereditary(new Intersection(a1, a2, a3, a4));
//        HereditaryProperty a21 = AvoidanceTest.getTest("21");
//        HereditaryProperty m = new HereditaryMerge(z, a21);
//        HashSet<Permutation> basis = new HashSet<>();
//        for (int n = 3; n <= 12; n++) {
//            PermutationClass c = new PermutationClass(basis);
//            int count = 0;
//            for (Permutation p : new Permutations(c, n)) {
//                if (!m.isSatisfiedBy(p)) {
//                    System.out.println(p);
//                    basis.add(p); count++;
//                } else {
//                    // count++;
//                }
//
//            }
//            System.out.println(count);
//            System.out.println();
     //   }

    }
    
    public static void foo() {
        HereditaryProperty a1 = AvoidanceTest.getTest("2413");
        HereditaryProperty a2 = AvoidanceTest.getTest("3142");
        HereditaryProperty a4 = AvoidanceTest.getTest("12");
        HereditaryProperty z = HereditaryPropertyAdapter.forceHereditary(new Intersection(a1, a2));
        HereditaryProperty m = new HereditaryMerge(z, z);
        HashSet<Permutation> basis = new HashSet<>();
        for (int n = 3; n <= 12; n++) {
            PermutationClass c = new PermutationClass(basis);
            int count = 0;
            for (Permutation p : new Permutations(c, n)) {
                if (!m.isSatisfiedBy(p)) {
                    System.out.println(p);
                    basis.add(p); 
                } else {
                    count++;
                }

            }
            System.out.println(count);
            System.out.println();
        }
    }
    
    public static void bar() {
        
        PermutationClass a321 = new PermutationClass("321");
        for(int n = 3; n <= 10; n++) {
            int[] maxPos = new int[n];
            for(Permutation p : new Permutations(a321,n)) {
                for(int i = 0; i < n; i++) {
                    if (p.elements[i] == n-1) {
                        maxPos[i]++;
                        break;
                    }
                }
                
            }
            System.out.println(Arrays.toString(maxPos));
            
        }
    }

}
