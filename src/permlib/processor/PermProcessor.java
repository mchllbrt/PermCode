package permlib.processor;

import permlib.Permutation;

/**
 * Interface that represents a generic processor of permutations. The reason
 * for including such is that a processor can be passed e.g. as a parameter in
 * DFS and activated on the permutations found.
 * 
 * @author Michael Albert
 */
public interface PermProcessor {

    public void process(Permutation p);

    public void reset();

    public String report();
}
