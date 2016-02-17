/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.property.Involves;
import permlib.utilities.Combinations;

/**
 *
 * @author MichaelAlbert
 */
public class RandomAv321 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        doX();

    }

    public static void doX() {
        Permutation[] xb = {new Permutation("2143"), new Permutation("3412")};
        PermutationClass x = new PermutationClass(xb);
        ArrayList<Permutation> ps = new ArrayList<>(x.getPerms(12));
        Random r = new Random();
        for (int i = 0; i < 10; i++) {
            Permutation p = ps.get(r.nextInt(ps.size()));
            for (int v : p.elements) {
                System.out.print((v + 1) + ",");
            }
            System.out.println();
        }

    }

    public static void doC() {
        PermutationClass c = new PermutationClass("321");
        ArrayList<Permutation> ps = new ArrayList<>(c.getPerms(14));
        Random r = new Random();
        Permutation p2413 = new Permutation("2413");
        Involves inv = new Involves(p2413);
        int i = 0;
        while (i < 10) {
            Permutation p = ps.get(r.nextInt(ps.size()));
            if (!inv.isSatisfiedBy(p)) {
                for (int v : p.elements) {
                    System.out.print((v + 1) + ",");
                }
                System.out.println();
                for (int[] cnk : new Combinations(14, 4)) {
                    if (p.patternAt(cnk).equals(p2413)) {
                        System.out.println(Arrays.toString(cnk));
                    }
                }
                i++;
            }
        }
    }
}
