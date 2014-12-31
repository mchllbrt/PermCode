/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.classes.SimplePermClass;
import permlib.processor.PermCounter;
import permlib.property.PermProperty;
import permlib.property.PlusIrreducible;

/**
 * Towards the simples have density 0 conjecture.
 *
 * @author Michael Albert
 */
public class IntervalAvoidanceGrowth {

    public static void main(String[] args) {

        PermutationClass c = new PermutationClass(new Permutation("2134"));
        SimplePermClass s = new SimplePermClass(new Permutation("1234"));
        PermProperty pi = new PlusIrreducible();
        PermCounter counter = new PermCounter();
        for (int n = 1; n <= 14; n++) {
            counter.reset();
            s.processPerms(n, counter);
            System.out.print(counter.getCount()+",");
        }
        System.out.println();
    }

}
