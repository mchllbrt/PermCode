package permlib.classes;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import permlib.Permutation;
import permlib.processor.PermProcessor;
import permlib.property.PermProperty;

/**
 *
 * @author MichaelAlbert
 */
public class PegPermutationClass implements PermClassInterface {
    
    private static final int INC = 1;
    private static final int DEC = -1;
    private static final int DOT = 0; 
    
    private Permutation p;
    private int[] pegging;

    public PegPermutationClass(Permutation p, int[] pegging) {
        this.p = p;
        this.pegging = pegging;
    }

    @Override
    public void processPerms(int length, PermProcessor proc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Permutation> getPerms(int length) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<Permutation> getPermsTo(int length) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean containsPermutation(Permutation q) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Permutation> getIterator(int low, int high) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + Objects.hashCode(this.p);
        hash = 43 * hash + Arrays.hashCode(this.pegging);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PegPermutationClass other = (PegPermutationClass) obj;
        if (!Objects.equals(this.p, other.p)) {
            return false;
        }
        if (!Arrays.equals(this.pegging, other.pegging)) {
            return false;
        }
        return true;
    }

}
