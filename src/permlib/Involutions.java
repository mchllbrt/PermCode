package permlib;

import permlib.classes.InvolutionPermClass;
import java.util.Iterator;
import permlib.property.HereditaryProperty;
import permlib.property.Universal;

/**
 * This class represents abstractly the collection of all involutions of some
 * given lengths, and allows iteration over this collection without needing to
 * store the entire collection.
 *
 * @author Michael Albert
 */
public class Involutions implements Iterable<Permutation> {
    
    private HereditaryProperty definingProperty;
    private InvolutionPermClass involutionClass;
    private int low;
    private int high;

    /**
     * Creates a collection of involutions.
     * 
     * @param low lower length bound
     * @param high upper length bound
     * @param definingProperty hereditary property of class
     */
    public Involutions(int low, int high, HereditaryProperty definingProperty) {
        this.definingProperty = definingProperty;
        this.low = low;
        this.high = high;
        involutionClass = new InvolutionPermClass(definingProperty);
    }
    
    /**
     * Creates a collection of involutions.
     * 
     * @param low lower length bound
     * @param high upper length bound
     * @param involutionClass an involution class
     */
    public Involutions(int low, int high, InvolutionPermClass involutionClass) {
        this.involutionClass = involutionClass;
        this.low = low;
        this.high = high;
    }
    
    /**
     * Creates a collection of involutions.
     * 
     * @param n lower and upper length bound
     * @param involutionClass an involution class
     */
    public Involutions(int n, InvolutionPermClass involutionClass) {
        this(n, n, involutionClass);
    }
    
    /**
     * Creates a collection of involutions.
     * 
     * @param n lower and upper length bound
     * @param basis basis for class
     */
    public Involutions(int n, Permutation... basis) {
        this(n, n, new InvolutionPermClass(basis));
    }
    
    /**
     * Creates a collection of involutions.
     * 
     * @param low lower length bound
     * @param high upper length bound
     * @param basis basis for class
     */
    public Involutions(int low, int high, Permutation... basis) {
        this(low, high, new InvolutionPermClass(basis));
    }
    
    /**
     * Creates a collection of involutions.
     * 
     * @param low lower length bound
     * @param high upper length bound
     */
    public Involutions(int low, int high) {
        this(low, high, new Universal());
    }
    
    /**
     * Creates a collection of involutions.
     * 
     * @param definingProperty hereditary property of the class
     */
    public Involutions(int n, HereditaryProperty definingProperty) {
        this(n, n, definingProperty);
    }
    
    /**
     * Creates a collection of involutions.
     * 
     * @param n lower and upper length bound
     */
    public Involutions(int n) {
        this(n, n, new Universal());
    }
    

    @Override
    public Iterator<Permutation> iterator() {
        return(involutionClass.getIterator(low, high));
    }
    
}
