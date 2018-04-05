/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.SimplePermClass;
import permlib.utilities.Combinations;

/**
 * In Av(3142, 2143, 264514, 264135) the simples seem to satisfy s_n = s_{n-1} +
 * 2*s_{n-2}.
 *
 * First check if the basis can be extended
 *
 * @author MichaelAlbert
 */
public class NiceSimples {

    public static void main(String[] args) {
        SimplePermClass s = new SimplePermClass("3142", "2143", "264514", "264135");
        HashSet<Permutation> ins = new HashSet<>();
        for (Permutation q : s.getPerms(8)) {
            for (int[] c : new Combinations(8, 4)) {
                ins.add(q.patternAt(c));
            }
        }
        for(Permutation p : new Permutations(4)) {
            if (!ins.contains(p)) System.out.println(p);
        }
    }

}
