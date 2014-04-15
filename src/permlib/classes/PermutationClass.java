package permlib.classes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import permlib.Permutation;
import permlib.Permutations;
import static permlib.classes.PermClassInterface.MAXIMUM_STORED_LENGTH;
import static permlib.PermUtilities.SAFE;
import static permlib.PermUtilities.rightExtensions;
import permlib.processor.PermCollector;
import permlib.processor.PermProcessor;
import permlib.property.AvoidsFromRight;
import permlib.property.HereditaryProperty;
import permlib.property.PermProperty;
import permlib.property.Universal;

/**
 *
 * @author M Belton
 */
public class PermutationClass implements PermClassInterface {

    private HashSet<Permutation>[] storedPermutations;
    protected Permutation[] basis;
    private static long[] RIGHT_MASK;
    private static long[] LEFT_MASK;
    private AvoidsFromRight[] avoidanceTests;

    /**
     * Constructor that defines a permutation class in terms of a list of basis
     * permutations.
     *
     * @param basis the basis of the class
     */
    public PermutationClass(Permutation... basis) {
        setupMasks();
        this.basis = basis;
        addAvoidanceTests();
        computeStoredPermutations();
    }

    public PermutationClass(Collection<Permutation> basis) {
        setupMasks();
        this.basis = new Permutation[basis.size()];
        basis.toArray(this.basis);
        addAvoidanceTests();
        computeStoredPermutations();
    }

    public PermutationClass(String... strings) {
        setupMasks();
        this.basis = new Permutation[strings.length];
        int i = 0;
        for (String string : strings) {
            this.basis[i++] = new Permutation(string);
        }
        addAvoidanceTests();
        computeStoredPermutations();
    }

    public PermutationClass() {
    }

    @Override
    @SuppressWarnings({"CloneDoesntCallSuperClone", "CloneDeclaresCloneNotSupported"})
    public PermClassInterface clone()  {
        return new PermutationClass(basis);
    }
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("Av(");
        for(Permutation p : basis) {
            s.append(p);
            s.append(", ");
        }
        s.delete(s.length()-2, s.length());
        s.append(")");
        
        return s.toString();
    }

    protected final void setupMasks() {
        RIGHT_MASK = new long[64];
        LEFT_MASK = new long[64];
        RIGHT_MASK[0] = 1L;
        LEFT_MASK[0] = ~0;
        for (int i = 1; i < 64; i++) {
            RIGHT_MASK[i] = (RIGHT_MASK[i - 1] << 1) | 1L;
            LEFT_MASK[i] = LEFT_MASK[i - 1] << 1;
        }

    }

    @SuppressWarnings("unchecked")
    protected final void computeStoredPermutations() {
        storedPermutations = (HashSet<Permutation>[]) new HashSet[MAXIMUM_STORED_LENGTH + 1];
        storedPermutations[0] = new HashSet<>();
        storedPermutations[0].add(new Permutation(""));
        for (int n = 1; n <= MAXIMUM_STORED_LENGTH; n++) {
            storedPermutations[n] = new HashSet<>();
            for (Permutation p : storedPermutations[n - 1]) {
                for (Permutation q : rightExtensions(p)) {
                    if (containsPermutation(q)) {
                        storedPermutations[n].add(q);
                    }
                }
            }
        }
    }

    @Override
    public boolean containsPermutation(Permutation q) {
        for (AvoidsFromRight a : avoidanceTests) {
            if (!a.isSatisfiedBy(q)) {
                return false;
            }
        }
        return true;
    }

    public boolean containsPermutation(Permutation q, int includeFinal) {
        for (AvoidsFromRight a : avoidanceTests) {
            if (!a.isSatisfiedBy(q, includeFinal)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<Permutation> getPerms(int length) {
        PermCollector collector = new PermCollector();
        processPerms(length, collector);
        return collector.getCollection();
    }

    @Override
    public Collection<Permutation> getPermsTo(int length) {
        PermCollector collector = new PermCollector();
        for (int i = 1; i <= length; i++) {
            processPerms(i, collector);
        }
        return collector.getCollection();
    }

    private void addAvoidanceTests() {
        avoidanceTests = new AvoidsFromRight[basis.length];
        for (int i = 0; i < basis.length; i++) {
            avoidanceTests[i] = new AvoidsFromRight(basis[i]);
        }
    }

    /**
     * Processes all the permutations in the class of the given length with the
     * supplied {@link PermProcessor processor}.
     *
     * @param length the length of permutations to be processed
     * @param proc the processor
     */
    @Override
    public void processPerms(int length, PermProcessor proc) {
        for (Permutation p : new Permutations(this, length)) {
            proc.process(p);
        }
    }

    public void processPerms(int low, int high, PermProcessor proc) {
        for (Permutation p : new Permutations(this, low, high)) {
            proc.process(p);
        }
    }
    
    @Override
    public Iterator<Permutation> getIterator(int low, int high) {
        return getRestrictedIterator(low, high, new Universal());
    }

    @Override
    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {
        return new PermutationClassIterator(low, high, prop);
    }

    public Iterator<Permutation> getIterator(int n) {
        return getIterator(n, n);
    }

    public Iterator<Permutation> getRestrictedIterator(int n, PermProperty prop) {
        return getRestrictedIterator(n, n, prop);
    }

    private class PermutationClassIterator implements Iterator<Permutation> {

        Permutation next = null;
        int low;
        int high;
        PermProperty restrictingProperty;
        Queue<Iterator<Permutation>> iterators = new ArrayDeque<>();
        Iterator<Permutation> currentIterator;
        boolean safeIterator = false;

        PermutationClassIterator(int low, int high, PermProperty restrictingProperty) {
            this.low = low;
            this.high = high;
            this.restrictingProperty = restrictingProperty;
            for (int i = low; i <= high && i <= MAXIMUM_STORED_LENGTH; i++) {
                iterators.add(storedPermutations[i].iterator());
            }
            if (high > MAXIMUM_STORED_LENGTH) {
                iterators.add(new StackIterator());
            }
        }

        public Iterator<Permutation> getIterator(int length) {
            return storedPermutations[length].iterator();
        }

        @Override
        public boolean hasNext() {
            do {
                if (next != null) {
                    return true;
                }
                if (currentIterator == null) {
                    if (iterators.isEmpty()) {
                        return false;
                    }
                    currentIterator = iterators.poll();
                }
                while (currentIterator.hasNext()) {
                    next = currentIterator.next();
                    if ((currentIterator instanceof SafeIterator) || restrictingProperty.isSatisfiedBy(next)) {
                        return true;
                    }
                }
                currentIterator = null;
            } while (true);
        }

        @Override
        public Permutation next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No elements remaining in permutation class");
            }
            Permutation result = next;
            next = null;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        class StackIterator implements Iterator<Permutation>, SafeIterator {

            Deque<InsertionPerm> stack;
            Permutation stackNext = null;

            public StackIterator() {
                stack = new ArrayDeque<>();
                for (Permutation p : storedPermutations[MAXIMUM_STORED_LENGTH]) {
                    stack.add(new InsertionPerm(p));
                }
            }

            private Collection<InsertionPerm> children(InsertionPerm p) {
                ArrayList<InsertionPerm> children = new ArrayList<>();
                Permutation seed = p.perm;
                long insertionOptions = p.insertions;
                // Generate the children
                for (int i = 0; i <= seed.elements.length; i++) {
                    if (((insertionOptions >> i) & 1L) != 0) {
                        int[] es = new int[seed.length() + 1];
                        System.arraycopy(seed.elements, 0, es, 0, seed.elements.length);
                        es[seed.elements.length] = i;
                        for (int k = 0; k < seed.elements.length; k++) {
                            if (es[k] >= i) {
                                es[k]++;
                            }
                        }
                        Permutation child = new Permutation(es, SAFE);
                        if (containsPermutation(child, (seed.length() == MAXIMUM_STORED_LENGTH) ? 1 : 2)) {
                            if (!(restrictingProperty instanceof HereditaryProperty) || restrictingProperty.isSatisfiedBy(child)) {
                                children.add(new InsertionPerm(child));
                            }
                        } else {
                            insertionOptions &= ~(1L << i);
                        }
                    }

                }

                // Otherwise extend the children as noted above
                int i = -1;
                for (InsertionPerm child : children) {
                    i++;
                    while (((insertionOptions >> i) & 1L) == 0) {
                        i++;
                    }
                    long newOptions = (insertionOptions & RIGHT_MASK[i]) | ((insertionOptions & LEFT_MASK[i]) << 1);
                    child.setInsertions(newOptions);
                }
                return children;
            }

            @Override
            public boolean hasNext() {

                if (stackNext != null) {
                    return true;
                }

                while (true) {
                    if (stack.isEmpty()) {
                        return false;
                    }

                    InsertionPerm candidate = stack.pop();
                    if (candidate.length() < high) {
                        for (InsertionPerm q : children(candidate)) {
                            if (q.length() <= high) {
                                stack.push(q);
                            }
                        }
                    }
                    if (candidate.length() >= low && ((restrictingProperty instanceof HereditaryProperty) || restrictingProperty.isSatisfiedBy(candidate.perm))) {
                        stackNext = candidate.perm;
                        return true;
                    }
                }
            }

            @Override
            public Permutation next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No elements remaining in involution class");
                }
                Permutation result = stackNext;
                stackNext = null;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Remove is not supported.");
            }
        }
    }

    private static class InsertionPerm {

        private Permutation perm;
        private long insertions;

        public InsertionPerm() {
            this(new Permutation());
        }

        public InsertionPerm(Permutation perm, long insertions) {
            this.perm = perm;
            this.insertions = insertions;
        }

        public InsertionPerm(Permutation perm) {
            this(perm, RIGHT_MASK[perm.length() + 1]);
        }

        private void setInsertions(long insertions) {
            this.insertions = insertions;
        }

        public int length() {
            return perm.length();
        }
    }

    private static interface SafeIterator {
    }
}
