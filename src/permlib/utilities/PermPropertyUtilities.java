package permlib.utilities;

import java.util.Collection;
import permlib.property.Complement;
import permlib.property.Intersection;
import permlib.property.PermProperty;
import permlib.property.Union;

/**
 * Utilities related to permutation properties.
 *
 * @author Michael Albert
 */
public final class PermPropertyUtilities {

    /**
     * Returns the property that is the union of the collection of properties.
     *
     * @param properties a collection of properties
     * @return the property that is their union
     */
    public final static PermProperty union(Collection<PermProperty> properties) {
        return new Union(properties);
    }

    /**
     * Returns the property that is the union of the variable length list of
     * properties.
     *
     * @param properties a list of properties
     * @return the property that is their union
     */
    public final static PermProperty union(PermProperty... properties) {
        return new Union(properties);
    }

    /**
     * Returns the property that is the intersection of the collection of
     * properties.
     *
     * @param properties a collection of properties
     * @return the property that is their intersection
     */
    public final static PermProperty intersection(Collection<PermProperty> properties) {
        return new Intersection(properties);
    }

    /**
     * Returns the property that is the intersection of the variable length list
     * of properties.
     *
     * @param properties a list of properties
     * @return the property that is their intersection
     */
    public final static PermProperty intersection(PermProperty... properties) {
        return new Intersection(properties);
    }

    /**
     * Returns the property that is the complement of a property.
     *
     * @param property a property
     * @return the complement of <code>property</code>
     */
    public final static PermProperty complement(PermProperty property) {
        return new Complement(property);
    }
}
