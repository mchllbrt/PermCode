package permlib.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import permlib.Permutation;

/**
 * This class represents the intersection of a collection of properties.
 * 
 * @author Michael Albert
 */
public class Intersection implements PermProperty {

    private final Collection<PermProperty> properties;

    /**
     * The intersection of a collection of permutation properties.
     * 
     * @param properties the collection of properties
     */
    public Intersection(Collection<PermProperty> properties) {
        this.properties = properties;
    }

    /**
     * The intersection of a variable length list of permutation properties.
     * 
     * @param properties the list of properties
     */
    public Intersection(PermProperty... properties) {
        this.properties = new ArrayList<PermProperty>();
        this.properties.addAll(Arrays.asList(properties));
    }

    /**
     * Determine whether a permutation belongs to this intersection.
     * 
     * @param p the permutation to be tested.
     * 
     * @return <code>true</code> if p satisfies all the properties
     */
    @Override
    public boolean isSatisfiedBy(Permutation p) {
        for (PermProperty property : properties) {
            if (!property.isSatisfiedBy(p)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determine whether this intersection of properties is satisfied by an
     * array of values.
     * 
     * @param values the array of values
     * @return <code>true</code> if the pattern of the array satisfies all the
     * properties
     * 
     */
    @Override
    public boolean isSatisfiedBy(int[] values) {
        for (PermProperty property : properties) {
            if (!property.isSatisfiedBy(values)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Intersection(");
        for (PermProperty property : properties) {
            result.append(property.toString());
            result.append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append(")");
        return result.toString();
    }
}
