package permlib.property;

import permlib.Permutation;

/**
 * This property is the complement of the one that is used to construct it, i.e.
 * it is
 * <code>true</code> if the other is false, and vice versa.
 *
 * @author MichaelAlbert
 */
public final class Complement implements PermProperty {

    private final PermProperty property;

    /**
     * Constructor that creates the complement of
     * <code>property</code>.
     *
     * @param property the property of which this is the complement
     */
    public Complement(PermProperty property) {
        this.property = property;
    }

    @Override
    public final boolean isSatisfiedBy(Permutation p) {
        return !property.isSatisfiedBy(p);
    }

    @Override
    public final boolean isSatisfiedBy(int[] values) {
        return !property.isSatisfiedBy(values);
    }
}
