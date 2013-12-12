package permlib.classes;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.processor.PermCollector;
import permlib.processor.PermProcessor;
import permlib.property.AvoidsFromRight;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.property.Intersection;
import permlib.property.PermProperty;
import permlib.property.SynchronizedHereditaryProperty;
import permlib.property.Universal;
import permlib.utilities.InvolutionUtilities;

/**
 * A class of involutions defined by a property.
 * 
 * @author Michael Albert
 */
public class InvolutionPermClass implements PermClassInterface {

    private HereditaryProperty definingProperty;
    private SynchronizedHereditaryProperty syncDefiningProperty;

    public InvolutionPermClass() {
        this(new Universal());
    }

    public InvolutionPermClass(HereditaryProperty definingProperty) {
        this.definingProperty = definingProperty;
    }
    
    public InvolutionPermClass(PermProperty definingProperty) {
        this.definingProperty = HereditaryPropertyAdapter.forceHereditary(definingProperty);
    }
    
    public InvolutionPermClass(final Collection<Permutation> perms) {
        PermProperty[] avoidanceTests = new PermProperty[perms.size()];
        Permutation[] permsArray = new Permutation[perms.size()]; 
        perms.toArray(permsArray);
        for (int i = 0; i < avoidanceTests.length; i++) {
            avoidanceTests[i] = new AvoidsFromRight(permsArray[i]);
        }
        final PermProperty avoidanceTest = new Intersection(avoidanceTests);

        definingProperty = new HereditaryProperty() {
            @Override
            public Collection<Permutation> getBasis() {
                return perms;
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return avoidanceTest.isSatisfiedBy(p);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                return avoidanceTest.isSatisfiedBy(values);
            }
        };
        
        
    }

    public InvolutionPermClass(final Permutation... basis) {
        PermProperty[] avoidanceTests = new PermProperty[basis.length];
        for (int i = 0; i < avoidanceTests.length; i++) {
            avoidanceTests[i] = new AvoidsFromRight(basis[i]);
        }
        final PermProperty avoidanceTest = new Intersection(avoidanceTests);

        definingProperty = new HereditaryProperty() {
            @Override
            public Collection<Permutation> getBasis() {
                return Arrays.asList(basis);
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                return getBasis();
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return avoidanceTest.isSatisfiedBy(p);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                return avoidanceTest.isSatisfiedBy(values);
            }
        };
    }
    
    @Override
    public PermClassInterface clone() {
        return new InvolutionPermClass(definingProperty);
    }

    @Override
    public void processPerms(int length, PermProcessor proc) {
        processClass(length, length, proc);
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
    public boolean containsPermutation(Permutation q) {
        return PermUtilities.isInvolution(q) && definingProperty.isSatisfiedBy(q);
    }

    public void processClass(int low, int high, PermProcessor proc) {
        Iterator<Permutation> it = this.getIterator(low, high);
        while (it.hasNext()) {
            proc.process(it.next());
        }
    }

    @Override
    public Iterator<Permutation> getIterator(int low, int high) {

        return getRestrictedIterator(low, high, new Universal());

    }

    @Override
    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {

        return new InvolutionIterator(this.definingProperty, low, high, prop);

    }

    public Iterator<Permutation> getIterator(int n) {

        return getRestrictedIterator(n, new Universal());

    }

    public Iterator<Permutation> getRestrictedIterator(int n, PermProperty prop) {

        return new InvolutionIterator(this.definingProperty, n, prop);

    }

    public Iterator<Permutation> getSynchronizedIterator(int n) {

        if (syncDefiningProperty == null) {
            syncDefiningProperty = new SynchronizedHereditaryProperty(this.definingProperty);
        }
        return new InvolutionIterator(syncDefiningProperty, n);

    }

    private static class InvolutionIterator implements Iterator<Permutation> {

        Deque<Permutation> stack = new ArrayDeque<Permutation>() {
            {
                add(new Permutation());
            }
        };
        Permutation next = null;
        HereditaryProperty definingProperty;
        int low;
        int high;
        PermProperty extraProperty;

        public InvolutionIterator(HereditaryProperty definingProperty, int low, int high, PermProperty extraProperty) {
            this.definingProperty = definingProperty;
            this.low = low;
            this.high = high;
            this.extraProperty = extraProperty;
        }

        public InvolutionIterator(HereditaryProperty definingProperty, int n, PermProperty extraProperty) {
            this(definingProperty, n, n, extraProperty);
        }

        public InvolutionIterator(HereditaryProperty definingProperty, int n) {
            this(definingProperty, n, n, new Universal());
        }

        public InvolutionIterator(HereditaryProperty definingProperty, int low, int high) {
            this(definingProperty, low, high, new Universal());
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

                if (candidate.length() < high) {
                    for (Permutation p : InvolutionUtilities.directInvolutionExtensions(candidate, definingProperty)) {
                        if (p.length() <= high && (!(extraProperty instanceof HereditaryProperty)
                                || extraProperty.isSatisfiedBy(p))) {
                            stack.push(p);
                        }
                    }
                }


                if (candidate.length() >= low && ((extraProperty instanceof HereditaryProperty) || extraProperty.isSatisfiedBy(candidate))) {
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
}
