package permlib.utilities;

import java.util.LinkedList;
import java.util.List;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.property.PermProperty;

/**
 * Utility functions for dealing with involutions.
 * 
 * @author Michael Albert
 */
public class InvolutionUtilities {

    /**
     * Computes the direct, i.e. one or two point extensions of an involution 
     * that are also involutions. Returns an empty list if the input
     * permutation is not an involution.
     * 
     * @param p the involution
     * @return the extensions
     */
    public static List<Permutation> directInvolutionExtensions(Permutation p) {
        LinkedList<Permutation> result = new LinkedList<Permutation>();
        if (!isInvolution(p)) {
            return result;
        }
        result.add(PermUtilities.insert(p, p.length(), p.length()));
        for (int k = 0; k <= p.length(); k++) {
            Permutation q = PermUtilities.insert(p, p.length(), k);
            result.add(PermUtilities.insert(q, k, p.length() + 1));
        }
        return result;
    }

    /**
     * Computes the direct, i.e. one or two point extensions of an involution 
     * that are also involutions and satisfy a given property. Returns an empty
     * list if the input permutation is not an involution.
     * 
     * @param p an involution
     * @param prop a property
     * @return the extensions of the involution that satisfy the property
     */
    public static List<Permutation> directInvolutionExtensions(Permutation p, PermProperty prop) {
        LinkedList<Permutation> result = new LinkedList<Permutation>();
        if (!isInvolution(p)) {
            return result;
        }
        Permutation q = PermUtilities.insert(p, p.length(), p.length());
        if (prop.isSatisfiedBy(q)) {
            result.add(q);
        }
        for (int k = 0; k <= p.length(); k++) {
            q = PermUtilities.insert(p, p.length(), k);
            q = PermUtilities.insert(q, k, p.length() + 1);
            if (prop.isSatisfiedBy(q)) {
                result.add(q);
            }
        }
        return result;
    }

    /**
     * Tests whether a given permutation is an involution or not.
     * 
     * @param p the permutation to test
     * @return true if the permutation is an involution, false otherwise
     */
    public static boolean isInvolution(Permutation p) {
        for (int i = 0; i < p.length(); i++) {
            if (p.elements[p.elements[i]] != i) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts the number of fixed points in a given permutation. That is, the
     * number of times the value is equal to its position.
     * 
     * @param p the permutation to test
     * @return the number of fixed points
     */
    public static int countFixedPoints(Permutation p) {
        int result = 0;
        for(int i = 0; i < p.length(); i++) {
            if (p.elements[i] == i) result++;
        }
        return result;
    }
}
