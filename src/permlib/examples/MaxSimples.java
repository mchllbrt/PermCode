/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package permlib.examples;

import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.SimplePermClass;

/**
 *
 * @author malbert
 */
public class MaxSimples {
    
    public static void main(String[] args) {
        for (int n = 4; n < 8; n++) {
            for (Permutation p : new Permutations(n)) {
                if (PermUtilities.isSymmetryRep(p)) {
                    check(p, 12);
                }
            }
            System.out.println(n + " done");
        }
    }
    
    private static void check(Permutation p, int m) {
        SimplePermClass s = new SimplePermClass(p);
        HashSet<Permutation>[] simps = new HashSet[m];
        for(int n = 4; n < m; n++) {
            simps[n] = s.getPerms(n);
            if (n > 4) {
                HashSet<Permutation> subSimps = new HashSet<Permutation>();
                for(Permutation q : simps[n]) {
                    for(int i = 0; i < q.length(); i++) {
                        Permutation r = q.delete(i);
                        if (PermUtilities.isSimple(r)) subSimps.add(r);
                    }
                }
                if (!subSimps.equals(simps[n-1])) {
                    System.out.println("Aha " + p + " " + n);
                    return;
                }
            }
        }
    }
    
}
