package permlib.classes;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import permlib.Permutation;
import permlib.property.Intersection;
import static permlib.PermUtilities.*;
import permlib.Permutations;
import permlib.processor.PermCollector;
import permlib.processor.PermProcessor;
import permlib.property.HereditaryProperty;
import permlib.property.PermProperty;
import permlib.property.Simple;
import permlib.property.Universal;
import permlib.utilities.InvolutionUtilities;

/**
 * This class represents the simple permutations in some class. Specialized
 * algorithms are used to generate the simples of length n+1 from those of
 * length n. Namely, a 'leftmost deletion index' is maintained with each simple.
 * The new permutations are generated in such a way that the element added is
 * always the leftmost one which can be deleted and leave a simple permutation.
 *
 * @author Michael Albert
 */

// TODO (2014-04-05) Iterator seems to be broken with empty basis.

public class SimplePermClass implements PermClassInterface {

    static final int DEFAULT_SIZE = 50;
    PermProperty inClass;
    HashSet<DecoratedSimplePerm>[] simples = (HashSet<DecoratedSimplePerm>[]) new HashSet[DEFAULT_SIZE];
    private Simple simplicity = new Simple();
    
    public SimplePermClass() {
        this(new ArrayList<Permutation>());
    }

    /**
     * Constructor that defines a simple permutation class in terms of a list of
     * avoided permutations, usually the basis.
     *
     * @param pArray the permutations
     */
    public SimplePermClass(Permutation... pArray) {
        ArrayList<PermProperty> tests = new ArrayList<PermProperty>();
        for (Permutation p : pArray) {
            tests.add(avoidanceTest(p));
        }
        inClass = new Intersection(tests);
        initialise();
    }

    /**
     * Constructor that defines a simple permutation class in terms of a
     * collection of avoided permutations, usually the basis.
     *
     * @param perms the permutations
     */
    public SimplePermClass(Collection<Permutation> perms) {
        ArrayList<PermProperty> tests = new ArrayList<PermProperty>();
        for (Permutation p : perms) {
            tests.add(avoidanceTest(p));
        }
        inClass = new Intersection(tests);
        initialise();
    }

    /**
     * Constructor that defines a simple permutation class in terms of a
     * permutation property, which should be hereditary.
     *
     * @param inClass the property
     */
    public SimplePermClass(PermProperty inClass) {
        this.inClass = inClass;
        initialise();
    }

    /**
     * Constructor that defines a simple permutation class in terms of a list of
     * avoided permutations written as strings, usually the basis.
     *
     * @param strings the strings
     */
    public SimplePermClass(String... strings) {
        ArrayList<PermProperty> tests = new ArrayList<PermProperty>();
        for (String s : strings) {
            tests.add(avoidanceTest(s));
        }
        inClass = new Intersection(tests);
        initialise();
    }

    @Override
    public PermClassInterface clone() {
        return new SimplePermClass(inClass);
    }
    
    private void initialise() {
        for (int i = 0; i <= 4; i++) {
            simples[i] = new HashSet<DecoratedSimplePerm>();
        }
        Permutation p = new Permutation(new int[]{0});
        if (inClass.isSatisfiedBy(p)) {
            simples[1].add(new DecoratedSimplePerm(p, 1));
        }
        p = new Permutation(new int[]{0, 1});
        if (inClass.isSatisfiedBy(p)) {
            simples[2].add(new DecoratedSimplePerm(p, 2));
        }
        p = new Permutation(new int[]{1, 0});
        if (inClass.isSatisfiedBy(p)) {
            simples[2].add(new DecoratedSimplePerm(p, 2));
        }
        p = new Permutation(new int[]{1, 3, 0, 2});
        if (inClass.isSatisfiedBy(p)) {
            simples[4].add(new DecoratedSimplePerm(p, 4));
        }
        p = new Permutation(new int[]{2, 0, 3, 1});
        if (inClass.isSatisfiedBy(p)) {
            simples[4].add(new DecoratedSimplePerm(p, 4));
        }
    }

    /**
     *
     * Returns the simples of length
     * <code>n</code>. This method should be used as opposed to processing the
     * simples of length
     * <code>n</code> with a suitable processor when it is intended to reuse the
     * simples in some way. That is, this is most suitable when it is known that
     * the class is relatively small.
     *
     * @param n the length
     * @return the simple permutations of that length in the class
     */
    public HashSet<Permutation> getSimples(int n) {
        if (n >= simples.length) {
            HashSet<DecoratedSimplePerm>[] newSimples = (HashSet<DecoratedSimplePerm>[]) new HashSet[Math.max(simples.length * 2, n + 5)];
            System.arraycopy(simples, 0, newSimples, 0, simples.length);
            simples = newSimples;
        }
        if (simples[n] == null) {
            computeSimples(n);
        }
        HashSet<Permutation> result = new HashSet<Permutation>();
        for (DecoratedSimplePerm ds : simples[n]) {
            result.add(ds.p);
        }
        return result;
    }

    private void computeSimples(int n) {
        int i = 4;
        while (simples[i] != null) {
            i++;
        }
        while (i <= n) {
            simples[i] = new HashSet<DecoratedSimplePerm>();
            for (Permutation p : exceptionalSimples(i)) {
                if (inClass.isSatisfiedBy(p)) {
                    simples[i].add(new DecoratedSimplePerm(p, i));
                }
            }
            for (DecoratedSimplePerm q : simples[i - 1]) {
                for (DecoratedSimplePerm cand : q.leftSimpleExtensions()) {
                    if (inClass.isSatisfiedBy(cand.getPermutation())) {
                        simples[i].add(cand);
                    }
                }
            }
            i++;
        }
    }

    /**
     * Returns the simples of length
     * <code>n</code> in the class. Uses depth first search, so stores no
     * results.
     *
     * @param n the length
     * @return the simples of length <code>n</code>
     */
    @Override
    public HashSet<Permutation> getPerms(int n) {
        PermCollector collector = new PermCollector();
        processPerms(n, collector);
        return collector.getCollection();
    }

    @Override
    public Collection<Permutation> getPermsTo(int length) {
        PermCollector collector = new PermCollector();
        for (int i = 4; i <= length; i++) {
            processPerms(i, collector);
        }
        return collector.getCollection();
    }

    /**
     * Processes the simples of length
     * <code>n</code> with the given processor.
     *
     * @param n the length
     * @param processor the processor
     */
    @Override
    public void processPerms(int n, PermProcessor processor) {
        if (n == 1) {
            processor.process(new Permutation(new int[]{1}));
        } else if (n == 2) {
            processor.process(new Permutation(new int[]{1, 2}));
            processor.process(new Permutation(new int[]{2, 1}));
        } else if (n > 3) {
            for (int k = 4; k <= n; k += 2) {
                for (Permutation p : exceptionalSimples(k)) {
                    if (inClass.isSatisfiedBy(p)) {
                        DecoratedSimplePerm s = new DecoratedSimplePerm(p, p.length());
                        DFSProcessSimples(s, k, n, processor);
                    }
                }
            }
        }
    }

    /**
     * Processes the simples up to length
     * <code>n</code> in the class with the given processor.
     *
     * @param n the length
     * @param processor the processor
     */
    public void processWithTo(PermProcessor processor, int n) {
        for (DecoratedSimplePerm s : simples[4]) {
            DFSProcessSimplesTo(s, 4, n, processor);
        }
        for (int k = 6; k <= n; k += 2) {
            for (Permutation p : exceptionalSimples(k)) {
                if (inClass.isSatisfiedBy(p)) {
                    DecoratedSimplePerm s = new DecoratedSimplePerm(p, p.length());
                    DFSProcessSimplesTo(s, k, n, processor);
                }
            }
        }
    }

    public void DFSProcessSimples(DecoratedSimplePerm s, int i, int n, PermProcessor processor) {
        if (i > n) {
            return;
        }
        if (i == n) {
            processor.process(s.getPermutation());
            return;
        }
        for (DecoratedSimplePerm q : s.leftSimpleExtensions()) {
            if (inClass.isSatisfiedBy(q.getPermutation())) {
                DFSProcessSimples(q, i + 1, n, processor);
            }
        }
    }

    private void DFSProcessSimplesTo(DecoratedSimplePerm s, int i, int n, PermProcessor processor) {
        if (i > n) {
            return;
        }
        processor.process(s.getPermutation());
        if (i == n) {
            return;
        }
        for (DecoratedSimplePerm q : s.leftSimpleExtensions()) {
            if (inClass.isSatisfiedBy(q.getPermutation())) {
                DFSProcessSimplesTo(q, i + 1, n, processor);
            }
        }
    }

    @Override
    public boolean containsPermutation(Permutation q) {
        return inClass.isSatisfiedBy(q) && simplicity.isSatisfiedBy(q);
    }

    public class DecoratedSimplePerm {

        Permutation p;
        int leftmostSimpleDeletionIndex;

        public DecoratedSimplePerm(Permutation p) {
            this.p = p;
            leftmostSimpleDeletionIndex = 0;
            while (leftmostSimpleDeletionIndex < p.length() && !simplicity.isSatisfiedBy(delete(p, leftmostSimpleDeletionIndex))) {
                leftmostSimpleDeletionIndex++;
            }
        }

        public DecoratedSimplePerm(Permutation p, int leftmostSimpleDeletionIndex) {
            this.p = p;
            this.leftmostSimpleDeletionIndex = leftmostSimpleDeletionIndex;
        }

        public int getLeftmostSimpleDeletionIndex() {
            return leftmostSimpleDeletionIndex;
        }

        public Permutation getPermutation() {
            return p;
        }

        public ArrayList<DecoratedSimplePerm> leftSimpleExtensions() {
            ArrayList<DecoratedSimplePerm> result = new ArrayList<DecoratedSimplePerm>();
            int insertionIndex = 0;
            while (insertionIndex <= leftmostSimpleDeletionIndex) {
                for (Permutation q : getSimpleExtensions(p, insertionIndex)) {
                    // System.out.println("q = " + q);
                    int i = 0;
                    while (i < insertionIndex && !simplicity.isSatisfiedBy(delete(q, i))) {
                        // System.out.println("i = " + i + " had non simple deletion");
                        i++;
                    }
                    if (i == insertionIndex) {
                        result.add(new DecoratedSimplePerm(q, i));
                    }
                }
                insertionIndex++;
            }

            if (leftmostSimpleDeletionIndex == p.length()) {
                return result; // Exceptional case
            }
            if (leftmostSimpleDeletionIndex == 0) {
                // In this case, an insertion into the next position can only remove this
                // simple deletion if it's the min or max
                if (isSimpleInsertionCandidate(p, insertionIndex, 0)) {
                    Permutation q = insert(p, insertionIndex, 0);
                    result.add(new DecoratedSimplePerm(q, insertionIndex));
                }
                if (isSimpleInsertionCandidate(p, insertionIndex, p.length())) {
                    Permutation q = insert(p, insertionIndex, p.length());
                    result.add(new DecoratedSimplePerm(q, insertionIndex));
                }

            } else {
                // Now the only chance is that we have added an element just above or below
                // the one lying just to the left of the simple deletion point
                int v = p.elements[leftmostSimpleDeletionIndex - 1];
                if (isSimpleInsertionCandidate(p, insertionIndex, v)) {
                    Permutation q = insert(p, insertionIndex, v);
                    if (leftmostSimpleDeletionIndex(q) == insertionIndex) {
                        result.add(new DecoratedSimplePerm(q, insertionIndex));
                    }
                }
                if (isSimpleInsertionCandidate(p, insertionIndex, v + 1)) {
                    Permutation q = insert(p, insertionIndex, v + 1);
                    if (leftmostSimpleDeletionIndex(q) == insertionIndex) {
                        result.add(new DecoratedSimplePerm(q, insertionIndex));
                    }
                }
            }
            insertionIndex++;
            int v = p.elements[leftmostSimpleDeletionIndex];
            while (insertionIndex <= p.length()) {
                // Now the only possibility is that the old value which could be deleted lies
                // between the value we're adding and one of the adjacent points
                if (isSimpleInsertionCandidate(p, insertionIndex, v)) {
                    Permutation q = insert(p, insertionIndex, v);
                    if (leftmostSimpleDeletionIndex(q) == insertionIndex) {
                        result.add(new DecoratedSimplePerm(q, insertionIndex));
                    }
                }
                if (isSimpleInsertionCandidate(p, insertionIndex, v + 1)) {
                    Permutation q = insert(p, insertionIndex, v + 1);

                    if (leftmostSimpleDeletionIndex(q) == insertionIndex) {
                        result.add(new DecoratedSimplePerm(q, insertionIndex));
                    }
                }
                insertionIndex++;
            }
            return result;
        }

        @Override
        public boolean equals(Object other) {
            if (!(other instanceof DecoratedSimplePerm)) {
                return false;
            }
            return this.p.equals(((DecoratedSimplePerm) other).p);
        }

        @Override
        public int hashCode() {
            return this.p.hashCode();
        }

        @Override
        public String toString() {
            return p.toString();
        }

        private int leftmostSimpleDeletionIndex(Permutation q) {
            int result = 0;
            while (result < q.length() && !simplicity.isSatisfiedBy(delete(q, result))) {
                result++;
            }
            return result;
        }

        private Iterable<Permutation> getSimpleExtensions(Permutation p, int insertionIndex) {
            ArrayList<Permutation> result = new ArrayList<Permutation>();

            for (int v = 0; v <= p.length(); v++) {
                if (isSimpleInsertionCandidate(p, insertionIndex, v)) {
                    result.add(insert(p, insertionIndex, v));
                }
            }
            return result;
        }

        private boolean isSimpleInsertionCandidate(Permutation p, int i, int v) {
            if ((i == 0 || i == p.length()) && ((v == 0) || (v == p.length()))) {
                return false;
            }
            if (i < p.length() && (p.elements[i] == v || p.elements[i] == v - 1)) {
                return false;
            }
            if (i > 0 && (p.elements[i - 1] == v || p.elements[i - 1] == v - 1)) {
                return false;
            }
            return true;
        }
    }

    public Iterator<Permutation> getIterator(int low, int high) {

        return getRestrictedIterator(low, high, new Universal());

    }

    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {

        return new SimplePermClass.SimplePermIterator(this.inClass, low, high, prop);

    }

    public Iterator<Permutation> getIterator(int n) {

        return getRestrictedIterator(n, new Universal());

    }

    public Iterator<Permutation> getRestrictedIterator(int n, PermProperty prop) {

        return new SimplePermClass.SimplePermIterator(this.inClass, n, prop);

    }

    private class SimplePermIterator implements Iterator<Permutation> {

        Deque<Permutation> stack = new ArrayDeque<Permutation>() {
            {
                add(new Permutation());
            }
        };
        Permutation next = null;
        PermProperty definingProperty;
        int low;
        int high;
        PermProperty extraProperty;

        public SimplePermIterator(PermProperty definingProperty, int low, int high, PermProperty extraProperty) {
            this.definingProperty = definingProperty;
            this.low = low;
            this.high = high;
            this.extraProperty = extraProperty;
        }

        public SimplePermIterator(PermProperty definingProperty, int n, PermProperty extraProperty) {
            this(definingProperty, n, n, extraProperty);
        }

        public SimplePermIterator(PermProperty definingProperty, int n) {
            this(definingProperty, n, n, new Universal());
        }

        @Override
        public boolean hasNext() {

            if (next != null) {
                return true;
            }

            // MA: Needed to eliminate recursion which was blowing stack
            while (true) {

                if (stack.isEmpty()) {
                    return false;
                }

                Permutation candidate = stack.pop();
// Should not be needed since long permutations are now filtered out in the
//                push step
//                while (candidate.length() > high && !stack.isEmpty()) {
//                    candidate = stack.pop();
//                }

                if (candidate.length() > high) {
                    return false;
                }

                if (candidate.length() < high) {
                    if (!(extraProperty instanceof HereditaryProperty)
                            || extraProperty.isSatisfiedBy(candidate) && simplicity.isSatisfiedBy(candidate)) {
                        for (Permutation p : InvolutionUtilities.directInvolutionExtensions(candidate, definingProperty)) {  //////////////////////////////
                            if (p.length() <= high) {
                                stack.push(p);
                            }
                        }
                    }
                }

                if (candidate.length() >= low) {
                    next = candidate;
                    return true;
                }

            }
        }

        @Override
        public Permutation next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No elements remaining in involution class");
            }
            Permutation result = next;
            next = null;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Remove is not supported.");
        }
    }
    
    public static void main(String[] args) {
        SimplePermClass c = new SimplePermClass("123");
        System.out.println(c.getPerms(6).size());
        for(Permutation p : new Permutations(c, 6)) System.out.println(p); // Iterator is broken
//        InterruptiblePermClassInterface ic;
//        ic = new INT_SimplePermClass(c, null);
//        System.out.println(ic.getPerms(6).size());
    }
}
