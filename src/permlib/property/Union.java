package permlib.property;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import permlib.Permutation;

/**
 * This class represents the union of a collection of permutation properties.
 * 
 * @author Michael Belton, Michael Albert
 */
public class Union implements PermProperty {

    private Collection<PermProperty> properties;

    /**
     * The union of a collection of permutation properties.
     * 
     * @param properties the collection of properties
     */
    public Union(Collection<PermProperty> properties) {
        this.properties = properties;
    }

    /**
     * The union of a variable length list of permutation properties.
     * 
     * @param properties the list of properties
     */
    public Union(PermProperty... properties) {
        this.properties = new ArrayList<PermProperty>();
        this.properties.addAll(Arrays.asList(properties));
    }

    /**
     * Determine whether a permutation belongs to this union.
     * 
     * @param p The permutation to be tested.
     * 
     * @return <code>true</code> if p satisfies any of the properties
     */
    @Override
    public boolean isSatisfiedBy(Permutation p) {
        for (PermProperty t : properties) {
            if (t.isSatisfiedBy(p)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Determine whether this union of properties is satisfied by an
     * array of values.
     * 
     * @param values the array of values
     * @return <code>true</code> if the pattern of the array satisfies any of the
     * properties
     * 
     */
    @Override
    public boolean isSatisfiedBy(int[] values) {
        for (PermProperty property : properties) {
            if (property.isSatisfiedBy(values)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("Union(");
        for (PermProperty property : properties) {
            result.append(property.toString());
            result.append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append(")");
        return result.toString();
    }
}
