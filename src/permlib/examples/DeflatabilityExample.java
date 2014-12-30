package permlib.examples;

import java.util.Collection;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.classes.SimplePermClass;

/**
 * Is Av(4123, 4132, 4231) deflatable?
 * @author Michael Albert
 */
public class DeflatabilityExample {
    
    public static void main(String[] args) {
        SimplePermClass s = new SimplePermClass(
            new Permutation("4123"),
            new Permutation("4231"),
            new Permutation("4132"));
        
        Collection<Permutation> simples = s.getPermsTo(15);
        Collection<Permutation> subs = PermUtilities.subpermutations(simples);
        HashSet<Permutation> basis = new HashSet<Permutation>();
        PermutationClass c = new PermutationClass(basis);
        for(int n = 3; n < 8; n++) {
            for(Permutation p : new Permutations(c,n)) {
                if (!subs.contains(p)) {
                    System.out.println(p);
                    basis.add(p);
                }
            }
            c = new PermutationClass(basis);
        }
    }
    
}
