/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.HashSet;
import permlib.Permutation;
import permlib.classes.SimplePermClass;
import permlib.property.PermProperty;
import permlab.ui.AnimatedPermFrame;

/**
 *
 * @author MichaelAlbert
 */
public class Av1342Simple {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SimplePermClass s = new SimplePermClass("1342");
        int n = 12;
        PermProperty minBeforeMax = new PermProperty() {
            @Override
            public boolean isSatisfiedBy(Permutation p) {
                int i = 0;
                while (p.elements[i] != 0 && p.elements[i] != p.elements.length - 1) {
                    i++;
                }
                return p.elements[i] == 0;
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };

        HashSet<Permutation> examples = new HashSet<Permutation>();
        for (Permutation p : s.getPerms(n)) {
            if (minBeforeMax.isSatisfiedBy(p)) {
                examples.add(p);
            }

        }
        AnimatedPermFrame a = new AnimatedPermFrame(examples);
        a.setVisible(true);
    }
}
