/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.PermClassInterface;
import permlib.classes.SimplePermClass;
import permlib.property.PermProperty;

/**
 *
 * @author Michael Albert
 */
public class PropertyStar implements PermProperty {
    
    private PermClassInterface c;
    
    public PropertyStar(PermClassInterface c) {
        this.c = c;
    }

    @Override
    public boolean isSatisfiedBy(Permutation p) {
        // For each point in p there is are extensions satisfying c which
        // "lie in the same" block
        for(int i = 0; i < p.length(); i++) {
            if (noExtension(p, i)) return false;
        }
        return true;
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private boolean noExtension(Permutation p, int i) {
        int v = p.elements[i];
        for(int w = 0; w <= p.length(); w++) {
            if (w == v || w == v+1) continue;
            Permutation q = PermUtilities.insert(p, i, w);
            if (!c.containsPermutation(q)) continue;
            q = PermUtilities.insert(p, i+1, w);
            if (!c.containsPermutation(q)) continue;
            return false;
        }
        for(int j = 0; j <= p.length(); j++) {
            if (j == i || j == i+1) continue;
            Permutation q = PermUtilities.insert(p, j, v);
            if (!c.containsPermutation(q)) continue;
            q = PermUtilities.insert(p, j+1, v);
            if (!c.containsPermutation(q)) continue;
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        SimplePermClass c = new SimplePermClass(new Permutation("2413)"));
    }
    
}
