package permlab.examples;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import static permlib.PermUtilities.*;

/**
 *
 * @author Michael Albert
 */
public class ConsecutiveOrder {

    static HashMap<HashSet<Permutation>, HashSet<Permutation>> children = new HashMap<HashSet<Permutation>, HashSet<Permutation>>();
    static HashMap<Permutation, HashSet<Permutation>> parents = new HashMap<Permutation, HashSet<Permutation>>();
    static HashSet<Integer> knownMaps = new HashSet<Integer>();

    public static HashSet<Permutation> children(Permutation p) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for (int value = 0; value <= p.length(); value++) {
            result.add(insert(p, 0, value));
            result.add(insert(p, p.length(), value));
        }
        return result;
    }

    public static HashSet<Permutation> children(Collection<Permutation> ps) {
        return children.get(ps);
    }

    public static void buildMaps(int n) {
        if (knownMaps.contains(n)) {
            return;
        }
        for (Permutation p : new Permutations(n)) {
            HashSet<Permutation> sp = parents(p);
            if (!children.containsKey(sp)) {
                children.put(sp, new HashSet<Permutation>());
            }
            children.get(sp).add(p);
            parents.put(p, parents(p));
        }
        knownMaps.add(n);
    }

    public static HashSet<Permutation> allParents(Collection<Permutation> ps) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for (Permutation p : ps) {
            result.addAll(parents(p));
        }
        return result;
    }

    public static HashSet<Permutation> parents(Permutation p) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        result.add(delete(p, 0));
        result.add(delete(p, p.length() - 1));
        return result;
    }
    
    public static HashSet<Permutation> siblings(Permutation p) {
        return children.get(parents.get(p));
    }
}
