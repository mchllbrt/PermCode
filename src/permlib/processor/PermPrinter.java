package permlib.processor;

import permlib.Permutation;

/**
 * A processor which simply prints each permutation it encounters.
 * 
 * @author Michael Albert
 */
public class PermPrinter implements PermProcessor{

    @Override
    public void process(Permutation p) {
        System.out.println(p);
    }

    @Override
    public void reset() {}

    @Override
    public String report() {
        return "PermPrinters have nothing to report";
    }

}
