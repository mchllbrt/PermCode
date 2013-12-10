package permlib.processor;

import permlib.Permutation;

/**
 * Interface that represents a generic processor of permutations.
 * 
 * @author Michael Albert
 */
public interface PermProcessor {

    public void process(Permutation p);

    public void reset();

    public String report();
}
