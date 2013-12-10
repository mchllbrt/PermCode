package permlib;

import java.util.LinkedList;

/**
 * This class implements various partial sorting algorithms on permutations.
 * 
 * @author Michael Albert
 */
public class SortingOperators {
    
    /**
     * Stack sorts a permutation.
     * @param q the permutation to be sorted
     * @return the result of sorting q
     */
     public static Permutation stackSort(Permutation q) {
        int[] output = new int[q.length()];
        LinkedList<Integer> stack = new LinkedList<Integer>();
        int outputIndex = 0;
        for (int i = 0; i < q.elements.length; i++) {
            while (stack.size() > 0 && stack.peek() < q.elements[i]) {
                output[outputIndex++] = stack.pop();
            }
            stack.push(q.elements[i]);
        }
        while (stack.size() > 0) {
            output[outputIndex++] = stack.pop();
        }
        return new Permutation(output, PermUtilities.SAFE);
    }
     
     /**
      * Applies one pass of bubble sort to a permutation.
      * @param q the permutation
      * @return the result of applying one pass of bubble sort to <code>q</code>
      */
     public static Permutation bubbleSort(Permutation q) {
         if (q.length() <= 1) return q.clone();
         int[] output = new int[q.length()];
         int carry = q.elements[0];
         for(int i = 1; i < q.length(); i++) {
             if (q.elements[i] < carry) {
                 output[i-1] = q.elements[i];
             } else {
                 output[i-1] = carry;
                 carry = q.elements[i];
             }
         }
         output[output.length-1] = carry;
         return new Permutation(output, PermUtilities.SAFE);
     }
}
