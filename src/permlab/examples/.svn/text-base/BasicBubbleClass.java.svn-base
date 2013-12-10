package permlab.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.SwingWorker;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermClassInterface;
import permlib.classes.PermutationClass;
import permlib.processor.PermProcessor;
import permlib.property.PermProperty;

/**
 * Investigate bases of Aaron Williams "bubble classes". These have the property
 * that we can swap the leftmost out of order consecutive pair and stay in the
 * class. The basic version here implements only that condition.
 *
 * @author Michael Albert
 */
public class BasicBubbleClass implements PermClassInterface {

    HashSet<Permutation> basis = new HashSet<Permutation>();
    PermutationClass c;
    int nextLength = 0;

    BasicBubbleClass(Collection<Permutation> initialBasis) {
        for (Permutation p : initialBasis) {
            basis.add(p);
        }
        c = new PermutationClass(basis);
    }

    public static void main(String[] args) {
        HashSet<Permutation> b = new HashSet<Permutation>();
        b.add(new Permutation("321"));
        BasicBubbleClass bbc = new BasicBubbleClass(b);
        for (int n = 3; n < 10; n++) {
            bbc.generatePermsTo(n);
            for (Permutation p : bbc.basis) {
                System.out.print(p + " ");
            }
            System.out.println();
        }

    }

    @Override
    public void processPerms(int length, PermProcessor proc) {
        c.processPerms(length, proc);
    }

    @Override
    public Collection<Permutation> getPerms(int length) {
        generatePermsTo(length);
        return c.getPerms(length);
    }

    @Override
    public Collection<Permutation> getPermsTo(int length) {
        generatePermsTo(length);
        return c.getPermsTo(length);
    }

    @Override
    public boolean containsPermutation(Permutation q) {
        generatePermsTo(q.length());
        return c.containsPermutation(q);

    }

    @Override
    public Iterator<Permutation> getIterator(int low, int high) {
        generatePermsTo(high);
        return c.getIterator(low, high);
    }

    @Override
    public Iterator<Permutation> getRestrictedIterator(int low, int high, PermProperty prop) {
        generatePermsTo(high);
        return c.getRestrictedIterator(low, high, prop);
    }

    void generatePermsTo(int length) {
        c = new PermutationClass(basis);
        while (nextLength <= length) {
            HashSet<Permutation> checked = new HashSet<Permutation>();
            HashSet<Permutation> newBasisElements = new HashSet<Permutation>();
            for (Permutation p : new Permutations(c, nextLength)) {
                if (checked.contains(p)) {
                    continue;
                }
                ArrayList<Permutation> pTrail = new ArrayList<Permutation>();
                pTrail.add(p);
                boolean inClass = checkTrailFrom(p, pTrail, checked, newBasisElements);
                if (!inClass) {
                    newBasisElements.addAll(pTrail);
//                    System.out.print("BBC: ");
//                    for(Permutation q : pTrail) System.out.print(q + " ");
//                    System.out.println();
                }
                checked.addAll(pTrail);
            }
            basis.addAll(newBasisElements);
            c = new PermutationClass(basis);
            nextLength++;
        }

    }

    private boolean checkTrailFrom(Permutation p, ArrayList<Permutation> pTrail, HashSet<Permutation> checked, HashSet<Permutation> newBasisElements) {
        if (checked.contains(p)) {
            return !newBasisElements.contains(p);
        }
        int i = firstDescentBottom(p);
        if (i < p.length()) {
            Permutation q = doSwap(p, i, i - 1);
            if (!c.containsPermutation(q)) {
                return false;
            }
            pTrail.add(q);
            return checkTrailFrom(q, pTrail, checked, newBasisElements);
        } else {
            return c.containsPermutation(p);
        }
    }

    private int firstDescentBottom(Permutation p) {
        int i = 1;
        while (i < p.length() && p.at(i) > p.at(i - 1)) {
            i++;
        }
        return i;
    }

    private Permutation doSwap(Permutation p, int i, int j) {
        Permutation result = new Permutation(p.elements);
        int v = result.at(i);
        result.elements[i] = result.elements[j];
        result.elements[j] = v;
        return result;
    }
}
