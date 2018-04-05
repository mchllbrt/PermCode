/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.HashSet;
import permlib.Permutation;
import permlib.classes.SimplePermClass;
import permlib.processor.PermProcessor;

/**
 * What simple permutations remain simple when every point is deleted
 *
 * @author MichaelAlbert
 */
public class AllDeletableSimples {

    static HashSet<Permutation> pnm;

    public static void main(String[] args) {
        for(int n = 7; n <= 11; n++) System.out.println(allDeletes(n).size());
    }

    private static void foo(int n) {
        pnm = allDeletes(n - 1);
        System.out.println(pnm.size());
        (new SimplePermClass()).processPerms(n, new PermProcessor() {

            int count = 0;
            @Override
            public void process(Permutation p) {
                boolean pGood = true;
                for (int i = 0; i < p.length(); i++) {
                    pGood = pnm.contains(p.delete(i));
                    if (!pGood) {
                        break;
                    }
                }
                if (pGood) {
                    System.out.println(p);
                }
                
            }

            @Override
            public void reset() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String report() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        });
//        for(Permutation p : allDeletes(n)) {
//            boolean pGood = true;
//             for(int i = 0; i < p.length(); i++) {
//                pGood = pnm.contains(p.delete(i));
//                if (!pGood) break;
//            }
//            if (pGood) System.out.println(p);
//        }
    }

    private static HashSet<Permutation> allDeletes(int n) {
        HashSet<Permutation> result = new HashSet<>();
        HashSet<Permutation> snm = (new SimplePermClass()).getPerms(n - 1);
        HashSet<Permutation> sn = (new SimplePermClass()).getPerms(n);
        for (Permutation p : sn) {
            boolean pGood = true;
            for (int i = 0; i < p.length(); i++) {
                pGood = snm.contains(p.delete(i));
                if (!pGood) {
                    break;
                }
            }
            if (pGood) {
                result.add(p);
            }
        }
        return result;
    }

}
