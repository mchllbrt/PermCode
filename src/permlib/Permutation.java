package permlib;

/**
 * This class represents a permutation. The permutation is represented by a
 * 0-based area of elements. This array is <em>public</em> as a matter of
 * convenience. 
 *
 * @author Michael Albert
 */
public class Permutation implements Comparable<Permutation> {

    public static final Permutation ONE = new Permutation("1");
    
    /**
     * The elements of this permutation. <p> Internally these are represented as
     * 0-based, e.g. the identity permutation of length two has elements {0, 1}.
     * In display, the traditional 1-based form is used. <p> <b>Caution</b>:
     * This field is
     * <code>public</code> and so can be directly accessed. This is for
     * convenience and should be used with extreme care. In particular, no
     * validation is done to check that
     * <code>elements</code> always contains a legitimate permutation. To ensure
     * that condition, use {@link #clean() clean}.
     */
    public int[] elements;

    /**
     * Constructor for the empty permutation.
     */
    public Permutation() {
        elements = new int[0];
    }

    /**
     * Constructor that creates a monotone increasing permutation of length
     * <code>n</code>.
     *
     * @param n the length of this permutation
     */
    public Permutation(int n) {
        this(new int[n]);
    }

    /**
     * Constructor that creates a permutation from a segment of an array. The
     * segment is given as a pair of boundaries
     * <code>left</code> and
     * <code>right</code> and the elements used are from
     * <code>left</code> inclusive to
     * <code>right</code> exclusive. <p> The permutation is the pattern of the
     * input array. Equal elements are replaced by increasing sequences, e.g. 1
     * 2 1 becomes 1 3 2.
     *
     * @param values an array containing the pattern of this permutation
     * @param left the left hand boundary of the pattern
     * @param right the right hand boundary of the pattern
     */
    public Permutation(int[] values, int left, int right) {
        elements = new int[right - left];
        fillElementsFrom(values, left, right);
    }

    /**
     * Constructor that creates a permutation from an array.
     *
     * @param values the pattern of this permutation
     */
    public Permutation(int[] values) {
        this(values, 0, values.length);
    }

    /**
     * Constructor that can use the provided array as
     * <code>elements</code> when the caller knows that it is safe to do so.
     *
     * @param values the pattern of this permutation
     * @param safe the indication of whether <code>values</code> can be used
     * safely for <code>elements</code>
     */
    public Permutation(int[] values, boolean safe) {
        if (safe) {
            this.elements = values;
        } else {
            this.elements = new int[values.length];
            fillElementsFrom(values, 0, values.length);
        }
    }

    /**
     * Constructor that creates a permutation from a string. The string may be
     * white space separated in which case each token is treated as the value of
     * an element, or not, in which case each token is treated as a digit. In
     * the latter case you can use any characters you like in the input string.
     * If they are normal digits they will be ordered normally, but in general
     * the character ordering will be used. Ignoring punctuation symbols, this
     * is 0-9, upper case letters, lower case letters.
     *
     * @param input the string representing this permutation
     */
    public Permutation(String input) {
        input = input.trim();
        if (input.equals("")) {
            elements = new int[0];
            return;
        }
        int[] values;
        String[] stringValues = input.split(" ");
        if (stringValues.length == 1) {
            values = new int[input.length()];
            for (int i = 0; i < values.length; i++) {
                values[i] = input.charAt(i);
            }
        } else {
            values = new int[stringValues.length];
            for (int i = 0; i < values.length; i++) {
                values[i] = Integer.parseInt(stringValues[i]);
            }
        }
        elements = new int[values.length];
        fillElementsFrom(values, 0, elements.length);
    }
    
    public Permutation(Integer[] values) {
        int[] v = new int[values.length];
        elements = new int[values.length];
        for(int i = 0; i < v.length; i++) v[i] = values[i];
        fillElementsFrom(v, 0, v.length);
    }

    /**
     * Creates a clone, i.e. copy of this permutation.
     *
     * @return a copy of this permutation
     */
    @Override
    public Permutation clone() {
        return new Permutation(this.elements, 0, this.elements.length);
    }

    /**
     * Computes the pattern of the segment of this permutation from
     * <code>left</code> (inclusive) to
     * <code>right</code> (exclusive)
     *
     * @param left the first position to include
     * @param right the first position to exclude
     * @return the pattern of the segment p[left..right)
     */
    public Permutation segment(int left, int right) {
        return new Permutation(this.elements, left, right);
    }
    
    /**
     * The element at position <code>i</code> (0-based) in this permutation.
     * @param i the position
     * @return the value at that position
     */
    public int at(int i) {
        return this.elements[i];
    }

    /**
     * Compute the pattern of the segment of this permutation from
     * <code>left</code> (inclusive) to
     * <code>right</code> (exclusive) between the values
     * <code>bottom</code> (inclusive) and
     * <code>top</code> (exclusive)
     *
     * @param left the leftmost position to include
     * @param right the leftmost position to exclude
     * @param bottom the least value to include
     * @param top The least value to exclude
     * @return the pattern of the specified 'window'
     */
    public Permutation window(int left, int right, int bottom, int top) {
        int index = 0;
        int[] q = new int[right - left];
        for (int i = left; i < right; i++) {
            if (this.elements[i] >= bottom && this.elements[i] < top) {
                q[index] = this.elements[i];
                index++;
            }
        }
        return new Permutation(q, 0, index);
    }

    /**
     * The length of this permutation.
     *
     * @return the length of this permutation
     */
    public int length() {
        return elements.length;
    }

    /**
     * Computes the inverse of this permutation.
     *
     * @return the inverse of this permutation.
     */
    public Permutation inverse() {
        return PermUtilities.inverse(this);
    }

    /**
     * Computes the complement of this permutation.
     *
     * @return the complement of this permutation.
     */
    public Permutation complement() {
        return PermUtilities.complement(this);
    }

    /**
     * Computes the reverse of this permutation.
     *
     * @return The reverse of this permutation.
     */
    public Permutation reverse() {
        return PermUtilities.reverse(this);
    }

    /**
     * Computes the result of inserting
     * <code>value</code> at position
     * <code>index</code> in this permutation. Specifically, all the elements of
     * this permutation which are greater than or equal to
     * <code>value</code> are increased by one.
     *
     * @param index the 0-based position of the inserted element
     * @param value the 0-based value of the inserted element
     * @return the result of inserting <code>value</code> at
     * <code>position</code> in this permutation
     */
    public Permutation insert(int index, int value) {
        return PermUtilities.insert(this, index, value);
    }

    /**
     * Cleans the permutation by recoding the elements to ensure that they form
     * a permutation of
     * <code>0</code> through
     * <code>length-1</code> having the same pattern as the current elements.
     */
    public void clean() {
        int[] dirtyElements = new int[elements.length];
        System.arraycopy(elements, 0, dirtyElements, 0, dirtyElements.length);
        fillElementsFrom(dirtyElements, 0, elements.length);
    }

    /**
     * Compares
     * <code>this</code> permutation with
     * <code>other</code>. If the lengths differ, the shorter permutation is
     * considered to be smaller, otherwise the ordering is lexicographic.
     *
     * @param other the permutation to compare to
     * @return a negative number if <code>this</code> is less than
     * <code>other</code>, 0 if they are equal, and a positive number otherwise.
     */
    @Override
    public int compareTo(Permutation other) {
        if (this.length() != other.length()) {
            return this.length() - other.length();
        }

        int diff = 0;
        for (int i = 0;
                i < this.elements.length
                && (diff = this.elements[i] - other.elements[i]) == 0;
                i++);

        return diff;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Permutation)) {
            return false;
        }
        return java.util.Arrays.equals(elements, ((Permutation) other).elements);
    }

    @Override
    public int hashCode() {
        return java.util.Arrays.hashCode(elements);
    }

    @Override
    public String toString() {
        boolean spaces = (elements.length >= 10);
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < elements.length - 1; i++) {
            output.append(elements[i] + 1);
            if (spaces) {
                output.append(' ');
            }
        }
        if (elements.length > 0) {
            output.append((elements[elements.length - 1] + 1));
        }
        return output.toString();
    }

    private void fillElementsFrom(int[] values, int left, int right) {
        for (int i = 0; i < elements.length; i++) {
            elements[i] = 0;
        }
        for (int i = left; i < right; i++) {
            for (int j = i + 1; j < right; j++) {
                if (values[i] <= values[j]) {
                    elements[j - left]++;
                } else {
                    elements[i - left]++;
                }
            }
        }
    }

    /**
     * Computes the pattern of the entries whose indices are given by
     * <code>c</code>.
     *
     * @param c the indices
     * @return the pattern of the entries at those indices
     */
    public Permutation patternAt(int[] c) {
        int[] pVals = new int[c.length];
        for (int i = 0; i < c.length; i++) {
            pVals[i] = elements[c[i]];
        }
        return new Permutation(pVals);
    }

    /**
     * Returns the permutation obtained by deleting the element at a given position. 
     * @param index the position
     * @return the result of deleting the element at that position
     */
    public Permutation delete(int index) {
        return PermUtilities.delete(this, index);
    }
    
}

