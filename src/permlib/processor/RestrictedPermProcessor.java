package permlib.processor;

import permlib.Permutation;
import permlib.property.PermProperty;

/**
 * A class representing a processor which only handles permutations satisfying 
 * a given property.
 * 
 * @author Michael Albert
 */
public class RestrictedPermProcessor implements PermProcessor {
    
    private PermProcessor proc;
    private PermProperty prop;

    /**
     * Creates a processor from an instance of a processor and the restricting
     * property.
     * 
     * @param proc an instance of a processor
     * @param prop the restricting property
     */
    public RestrictedPermProcessor(PermProcessor proc, PermProperty prop) {
        this.proc = proc;
        this.prop = prop;
    }
    
    @Override
    public void process(Permutation p) {
        if (prop.isSatisfiedBy(p)) proc.process(p);
    }

    @Override
    public void reset() {
        proc.reset();
    }

    @Override
    public String report() {
        return proc.report();
    }

}
