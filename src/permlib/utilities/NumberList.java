package permlib.utilities;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Build a list of numbers from a string of comma separated numbers and ranges.
 * 
 * @author Michael Albert
 */
public final class NumberList {
    
    private ArrayList<Integer> numbers = new ArrayList<Integer>();
    
    /**
     * Constructs a list of numbers from a string. The string allows numbers
     * separated by commas or specified by ranges e.g. "12, 4-8, 21". The list
     * does not include duplicates (i.e. is effectively a set).
     * 
     * @param input the string
     */
    public NumberList(String input) {
        constructList(input);
    }
    
    private void constructList(String input) throws NumberFormatException {
        String[] pieces = input.split(",");
        for(String piece : pieces) {
            String trimmed = piece.trim();
            if (trimmed.length() == 0) continue;
            String[] ends = trimmed.split("-");
            if (ends.length > 2) throw new NumberFormatException();
            if (ends.length == 2) {
                for (int i = Integer.parseInt(ends[0].trim()); i <= Integer.parseInt(ends[1].trim()); i++) {
                    include(i);
                }
            } else {
                include(Integer.parseInt(ends[0]));
            }           
        }
    }

    private void include(int i) {
        if (!numbers.contains(i)) numbers.add(i);
    }
    
    /**
     * Returns a sorted array of the numbers in this list.
     * @return a sorted array of the numbers in this list
     */
    public int[] getNumbers() {
        // Can't use toArray because int is a primitive type.
        int[] result = new int[numbers.size()];
        int i = 0;
        for(int num : numbers) {
            result[i] = num;
            i++;
        }
        Arrays.sort(result);
        return result;
    }
}
