/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlab.examples;

import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;

/**
 *
 * @author MichaelAlbert
 */
public class A231Example {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Permutation bca = new Permutation("231");
        Permutation p = new Permutation("5 1 2 4 3 10 9 6 7 8");
        Permutation q = new Permutation("10 5 1 2 4 3 9 6 7 8");
        PermutationClass cp = new PermutationClass(bca, p);
        PermutationClass cq = new PermutationClass(bca, q);
        PermCounter countp = new PermCounter();
        PermCounter countq = new PermCounter();
        for(int n = 10; n <= 18; n++) {
            cp.processPerms(n, countp);
            cq.processPerms(n, countq);
            System.out.println(n + " " + countp.getCount() + " " + countq.getCount());
            countp.reset();
            countq.reset();
        }
        }
                
    }
    

