package permlib.property;

import permlib.Permutation;

/**
 * The property of being minus indecomposable. That is, not having a proper
 * initial segment by position which is also a proper final segment by value.
 * This method supposes that the elements of the permutation are properly coded.
 * 
 * @author Michael Albert
 */
public class MinusIndecomposable implements PermProperty {

    public MinusIndecomposable() {
    }

    /**
     * Determines whether a permutation is minus indecomposable.
     * 
     * @param p the permutation
     * @return <code>true</code> if the permutation is minus indecomposable.
     */
   @Override
    public boolean isSatisfiedBy(Permutation p) {
        if (p.length() == 0) {
            return true;
        }
        int[] elems = p.elements;
        int low = Integer.MAX_VALUE;
        for (int index = 0; index < elems.length; index++) {
            low = (elems[index] < low ? elems[index] : low);
            if (low == 0) {
                return true;
            }
            if (low == elems.length - 1 - index) {
                return false;
            }
        }
        return true;
    }
   
   @Override
   public boolean isSatisfiedBy(int[] values) {
        int low = Integer.MAX_VALUE;
        for (int index = 0; index < values.length; index++) {
            low = (values[index] < low ? values[index] : low);
            if (low == 0) {
                return true;
            }
            if (low == values.length - 1 - index) {
                return false;
            }
        }
        return true;
    }
   
    @Override
   public String toString() {
       return "-Ind";
   }
}
