package permlib.utilities;

import java.util.Scanner;
import permlib.Permutation;

/**
 * Static utility functions associated with I/O
 * 
 * @author M Albert
 */
public class IOUtilities {

    /**
     * Computes an array of numbers specified by a comma separated list of
     * individual numbers and ranges.
     *
     * @param numberList a string representing a list of numbers
     * @return an array containing the numbers in the list
     */
    public static int[] getNumbers(String numberList) {
        return (new NumberList(numberList)).getNumbers();
    }

    /**
     * Tests whether a string represents a comma separated list of numbers and
     * ranges.
     *
     * @param text the string
     * @return
     * <code>true</code> if the string represents a comma separated list of
     * numbers and ranges
     */
    @SuppressWarnings("ResultOfObjectAllocationIgnored")
    public static boolean isNumbersString(String text) {
        try {
            new NumberList(text);
        } catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    /** 
     * Tests a string for validity as the representation of a permutation.
     * 
     * @param text the string
     * @return <code>true</code> if the string represents a permutation
     * (liberally construed as in the constructor of
     * {@link Permutation Permutation})
     */
    public static boolean isPermString(String text) {
        try {
            Permutation p = new Permutation(text);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Tests a sequence of lines to see if each is the representation of a 
     * permutation.
     * @param text the sequence of lines
     * @return <code>true</code> if each line of the sequence represents a
     * permutation
     */
    public static boolean isPermsString(String text) {
        Scanner check = new Scanner(text);
        while (check.hasNextLine()) {
            if (!isPermString(check.nextLine())) {
                return false;
            }
        }
        return true;
    }
}
