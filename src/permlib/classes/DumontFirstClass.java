package permlib.classes;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import permlib.Permutation;
import permlib.Permutations;
import permlib.processor.PermCollector;
import permlib.processor.PermProcessor;
import permlib.property.DumontFirst;
import permlib.property.HereditaryProperty;
import permlib.property.PermProperty;
import permlib.property.Universal;

/**
 * Classes of Dumont permutations of the first kind where every even value is a
 * descent top, and every odd value is an ascent bottom or final (note that
 * since this is in 1-based notation, internal even/odd characterisations may
 * differ.)
 *
 * @author Michael Albert
 */
public class DumontFirstClass implements PermClassInterface {

    private HereditaryProperty h;
    private PermProperty dum = new DumontFirst();

    public DumontFirstClass(HereditaryProperty h) {
        this.h = h;
    }

    public DumontFirstClass() {
        this(new Universal());
    }

    @Override
    public void processPerms(int length, PermProcessor proc) {
        for (Permutation p : new Permutations(this, length)) {
            proc.process(p);
        }
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

    @Override
    public boolean containsPermutation(Permutation q) {
        return dum.isSatisfiedBy(q) && h.isSatisfiedBy(q);
    }

    @Override
    public Iterator<Permutation> getIterator(int low, int high) {
        return getRestrictedIterator(low, high, new Universal());
    }

    @Override
    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {
        return new DumontClassIterator(low, high, prop);
    }

    private static class DumontClassIterator implements Iterator<Permutation> {
        
        private PermProperty prop;
        private Permutation next;

        public DumontClassIterator(int low, int high, PermProperty prop) {
            this.prop = prop;
        }

        @Override
        public boolean hasNext() {
            if (next != null) return true;
            
            return false;
        }

        @Override
        public Permutation next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No elements remaining in class");
            }
            Permutation result = next;
            next = null;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

}
