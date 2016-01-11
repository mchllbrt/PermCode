package permlib.property;

import java.util.ArrayList;
import java.util.Collection;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.utilities.PermPropertyUtilities;

/**
 * This class provides an abstract representation of a hereditary property;
 * one which if true of a permutation is true of all its subpermutations. In
 * other words, the collection of permutations that satisfy such a property is
 * a classical permutation class. Such a class has a basis (the antichain of
 * minimal permutations not satisfying the property).
 * 
 * @author Michael Albert
 */
public abstract class HereditaryPropertyAdapter implements HereditaryProperty {

    /**
     * The partial basis of the corresponding class in so far as it is known.
     */
    private Collection<Permutation> partialBasis = new ArrayList<Permutation>();
    /**
     * The length to which the basis elements of the corresponding class are known.
     */
    private int basisLengthKnown = 0;

    /**
     * Returns (if possible) the basis of a hereditary property. Concrete
     * subclasses should return a collection defining the basis if it is known.
     * 
     * @return the basis (if known) or <code>null</code>
     * 
     */
    public Collection<Permutation> getBasis() {
        return null;
    }

    /**
     * Returns the basis of the class through length <code>n</code>. If 
     * {@link #getBasis() getBasis()} returns a non <code>null</code> value
     * then that is used. Otherwise the basis is computed through length 
     * <code>n</code> by simple iteration.
     * 
     * @param n the length of the longest desired basis element
     * @return the basis of the class corresponding to this property through
     * length <code>n</code>
     */
    public Collection<Permutation> getBasisTo(int n) {

        if (getBasis() != null) {
            return getBasis();
        }

        if (basisLengthKnown >= n) {
            return partialBasis;
        }
        
         for (int k = basisLengthKnown + 1; k <= n; k++) {
            PermutationClass c = new PermutationClass(partialBasis);
            for (Permutation p : new Permutations(c, k)) {
                if (!this.isSatisfiedBy(p)) {
                    partialBasis.add(p);
                }
            }
        }
        basisLengthKnown = n;
        return partialBasis;

    }

    /**
     * This method is used to force a given property to be hereditary.
     * 
     * WARNING: the sensible use of this method is the responsibility of the
     * programmer.
     * 
     * @param property the property to be forced to be hereditary
     * @return a hereditary class
     */
    public static HereditaryProperty forceHereditary(PermProperty property) {

        class HP extends HereditaryPropertyAdapter {

            PermProperty property;

            public HP(PermProperty property) {
                this.property = property;
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return property.isSatisfiedBy(p);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                return property.isSatisfiedBy(values);
            }
        }

        return new HP(property);

    }

    @Override
    public String toString() {
        return "Abstract hereditary property";
    }
}
