package permlib.property;

import java.util.Collection;
import permlib.Permutation;

/**
 * A class containing synchronized versions of the methods in the class
 * HereditaryProperty.
 * 
 * @author Michael Albert
 */
public class SynchronizedHereditaryProperty implements HereditaryProperty {
    
    HereditaryProperty hProp;

    public SynchronizedHereditaryProperty(HereditaryProperty hProp) {
        this.hProp = hProp;
    }

    @Override
    public synchronized Collection<Permutation> getBasis() {
        return hProp.getBasis();
    }

    @Override
    public synchronized Collection<Permutation> getBasisTo(int n) {
        return hProp.getBasisTo(n);
    }

    @Override
    public synchronized boolean isSatisfiedBy(Permutation p) {
        return hProp.isSatisfiedBy(p);
    }

    @Override
    public synchronized boolean isSatisfiedBy(int[] values) {
        return hProp.isSatisfiedBy(values);
    }
}
