package permlab.examples;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import javax.swing.SwingWorker;
import permlib.Permutation;
import permlib.classes.PermClassInterface;
import permlib.classes.PermutationClass;
import permlib.processor.PermProcessor;
import permlib.property.PermProperty;

/**
 * Look at "bubble classes" (from Aaron Williams). These are defined by two
 * properties: (A) Closure under transposition of leftmost descent (B) (More
 * complicated) If a transposition in an initial increasing segment belongs to
 * the class, then so do all later transpositions in that segment.
 *
 * @author Michael Albert
 */
public class BubbleClass implements PermClassInterface {

    PermutationClass c;
    private int maxCheckedLength = 0;
    Permutation[] basis;

    public BubbleClass(Collection<Permutation> basis) {
        this.basis = new Permutation[basis.size()];
        int i = 0;
        for (Permutation p : basis) {
            this.basis[i++] = p;
        }
        Arrays.sort(this.basis);
        c = new PermutationClass(this.basis);
        if (this.basis.length > 0) {
            maxCheckedLength = this.basis[0].length() - 1;
        }
    }

    @Override
    public void processPerms(int length, PermProcessor proc) {
        checkBasisTo(length);
        c.processPerms(length, proc);
    }

    @Override
    public Collection<Permutation> getPerms(int length) {
        checkBasisTo(length);
        return c.getPerms(length);
    }

    @Override
    public Collection<Permutation> getPermsTo(int length) {
        checkBasisTo(length);
        return c.getPermsTo(length);
    }

    @Override
    public boolean containsPermutation(Permutation q) {
        checkBasisTo(q.length());
        return c.containsPermutation(q);
    }

    @Override
    public Iterator<Permutation> getIterator(int low, int high) {
        checkBasisTo(high);
        return c.getIterator(low, high);
    }

    @Override
    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {
        checkBasisTo(high);
        return c.getRestrictedIterator(low, high, prop);
    }

    private void checkBasisTo(int length) {
        while (maxCheckedLength < length) {
            maxCheckedLength++;
            check(maxCheckedLength);
        }
    }

    private void check(int maxCheckedLength) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
