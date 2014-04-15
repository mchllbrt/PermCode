/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.Iterator;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.SimplePermClass;
import permlib.property.PermProperty;

/**
 * A strong simple is one where every point can be deleted leaving a simple
 * permutation
 *
 * @author Michael Albert
 */
public class StrongSimples {

    public static void main(String[] args) {
        PermProperty ss = new StrongSimple();
        SimplePermClass simples = new SimplePermClass();
        for (int n = 5; n <= 7; n++) {
            System.out.println("Checking " + n);
            int count = 0;
            for(Permutation p : simples.getPerms(n)) {
                if (ss.isSatisfiedBy(p)) {
                    count++;
                    System.out.println(p);
                }
            }
            System.out.println(count);
        }
    }

    static class StrongSimple implements PermProperty {

        @Override
        public boolean isSatisfiedBy(Permutation p) {
            for (int i = 0; i < p.length(); i++) {
                if (!PermUtilities.isSimple(p.delete(i))) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public boolean isSatisfiedBy(int[] values) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    };

}
