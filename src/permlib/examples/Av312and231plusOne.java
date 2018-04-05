/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import permlib.classes.PermutationClass;

/**
 *
 * @author MichaelAlbert
 */
public class Av312and231plusOne {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PermutationClass c = new PermutationClass("312","321", "1245367");
        for(int n = 7; n <= 15; n++) {
            System.out.print(c.countPerms(n) + " ");
        }
        System.out.println();
        c = new PermutationClass("312","321", "1235647");
        for(int n = 7; n <= 15; n++) {
            System.out.print(c.countPerms(n) + " ");
        }
    }
    
}
