package permlib.classes;

import java.util.Collection;
import java.util.Iterator;
import permlib.Permutation;
import permlib.processor.PermCollector;
import permlib.processor.PermProcessor;
import permlib.property.PermProperty;
import permlib.property.Universal;

/**
 * A class representing a collection of all permutations. That is, unrestricted
 * by any additional properties or requirements.
 * 
 * @author M Albert
 */
public class UniversalPermClass implements PermClassInterface, Iterable<Permutation> {

    private int minLength = 0;
    private int maxLength = Integer.MAX_VALUE;
    private int currentLength;
    private Iterator<Permutation> currentLengthIterator;
    private PermProperty prop = new Universal();

    /**
     * The class of (effectively) all permutations.
     */
    public UniversalPermClass() {
        this(0, Integer.MAX_VALUE);
    }

    /**
     * The class of all permutations of length
     * <code>n</code>.
     *
     * @param n the length
     */
    public UniversalPermClass(int n) {
        this(n, n);
    }

    /**
     * The class of all permutations of lengths between
     * <code>minLength</code> and
     * <code>minLength</code>.
     *
     * @param minLength the minimum length
     * @param maxLength the maximum length
     *
     */
    public UniversalPermClass(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
        setupIterator();
    }

    @Override
    public PermClassInterface clone() {
        return new UniversalPermClass();
    }

    private void setupIterator() {
        this.currentLength = minLength;
        currentLengthIterator = new UniversalPermClass.SymmetricGroup(minLength).iterator();
    }

    public void setupBoundedIterator(int low, int high) {
        from(low);
        to(high);
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Returns a class that has no maximum length (effectively). Watch out for
     * infinite loops. For instance in:
     * <code>for(Permutation p: new Permutations(4).orLonger()) ... </code>
     * there had better be a
     * <code>break</code> statements somewhere in the loop.
     *
     * @return an example of this type where the length is effectively unbounded
     */
    public UniversalPermClass orLonger() {
        this.maxLength = Integer.MAX_VALUE;
        setupIterator();
        return this;
    }

    /**
     * Returns a class where the maximum length has been set (and the iterator
     * reinitialised).
     *
     * @param maxLength the maximum length
     * @return the class
     */
    public UniversalPermClass to(int maxLength) {
        this.maxLength = maxLength;
        setupIterator();
        return this;
    }

    /**
     * Returns a class where the minimum length has been set (and the iterator
     * reinitialised).
     *
     * @param minLength the minimum length
     * @return an iterator from permutations from the minimum length
     */
    public UniversalPermClass from(int minLength) {
        this.minLength = minLength;
        setupIterator();
        return this;
    }

    @Override
    public Collection<Permutation> getPerms(int length) {
        PermCollector collector = new PermCollector();
        processClass(length, length, collector);
        return collector.getCollection();
    }

    @Override
    public Collection<Permutation> getPermsTo(int length) {
        PermCollector collector = new PermCollector();
        processClass(1, length, collector);
        return collector.getCollection();
    }

    @Override
    public void processPerms(int length, PermProcessor proc) {
        processClass(length, length, proc);
    }

    public void processClass(int low, int high, PermProcessor proc) {
        setupBoundedIterator(low, high);
        while (iterator().hasNext()) {
            proc.process(iterator().next());
        }
    }

    @Override
    public boolean containsPermutation(Permutation q) {
        return (q.length() >= minLength && q.length() <= maxLength);
    }

    @Override
    public Iterator<Permutation> getIterator(int low, int high) {
        return getRestrictedIterator(low, high, new Universal());
    }

    @Override
    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {
        setupBoundedIterator(low, high);
        this.prop = prop;
        return iterator();
    }

    /**
     * Returns the iterator for this class.
     *
     * @return the iterator for this class
     */
    @Override
    public Iterator<Permutation> iterator() {

        return new Iterator<Permutation>() {
            @Override
            public boolean hasNext() {
                if (currentLengthIterator.hasNext()) {
                    return true;
                }
                if (currentLength == maxLength) {
                    return false;
                }
                currentLength++;
                currentLengthIterator = new UniversalPermClass.SymmetricGroup(currentLength).iterator();
                return currentLengthIterator.hasNext();
            }

            @Override
            public Permutation next() {
                return currentLengthIterator.next();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove is not supported.");
            }
        };
    }

    class SymmetricGroup implements Iterable<Permutation> {

        private int length;

        /**
         * Constructor that takes an integer
         * <code>length</code> as input and creates the collection of
         * permutations of that length.
         *
         * @param length the length of the permutations to be created.
         */
        public SymmetricGroup(int length) {
            this.length = length;
        }

        /**
         * Defines an iterator that iterates through all permutations of the
         * given length in lexicographical order. The algorithm is a standard
         * one. Given permutation
         * <code>p</code> find the last ascent in
         * <code>p</code>. Swap the low end of that ascent with the least larger
         * value in the following decreasing sequence, then reverse the elements
         * of the new decreasing sequence.
         *
         * @return an iterator for all permutations of a given length
         */
        @Override
        public Iterator<Permutation> iterator() {

            return new Iterator<Permutation>() {
                private int rightDecreasingLength = -1;
                private int[] elements;

                /**
                 * Returns true if there is a next permutation
                 *
                 * @return <code>true</code> if there is a next permutation
                 */
                @Override
                public boolean hasNext() {
                    while (rightDecreasingLength < length) {
                        if (elements == null) {
                            elements = new int[length];
                            for (int i = 0; i < length; i++) {
                                elements[i] = i;
                            }
                            rightDecreasingLength = 1;
                        } else {
                            makeNext();
                        }
                        if (prop.isSatisfiedBy(new Permutation(elements))) {
                            return true;
                        }
                    }
                    return false;
                }

                /**
                 * Finds the next permutation in the group.
                 *
                 * @return The next permutation in the group.
                 */
                @Override
                public Permutation next() {
                    return new Permutation(elements);
                }

                /**
                 * Removes a permutation. (Unsupported for obvious reasons).
                 */
                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Remove is not supported.");
                }

                /**
                 * Finds the next permutation by altering the values in
                 * elements.
                 *
                 */
                private void makeNext() {

                    int j = length - rightDecreasingLength - 1;
                    int k = length - 1;
                    while (elements[k] < elements[j]) {
                        k--;
                    }
                    swap(j, k);
                    reverse(j + 1);
                    rightDecreasingLength = 1;
                    for (int i = length - 2; i >= 0 && elements[i] > elements[i + 1]; i--) {
                        rightDecreasingLength++;
                    }
                }

                private void swap(int j, int k) {
                    int t = elements[j];
                    elements[j] = elements[k];
                    elements[k] = t;
                }

                private void reverse(int start) {
                    int finish = length - 1;
                    while (start < finish) {
                        swap(start, finish);
                        start++;
                        finish--;
                    }
                }
            }; // End of anonymous class
        } //End of Iterator
    } // End of SymmetricGroup class.
}
