package permlib.property;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 *
 * @author MichaelAlbert
 */
public class Vexillary implements HereditaryProperty {

    static HereditaryProperty A2143 = AvoidanceTest.getTest("2143");
    private int bound;
    ArrayDeque<Permutation> q = new ArrayDeque<Permutation>();
    ArrayDeque<Permutation> children = new ArrayDeque<Permutation>();
    ArrayDeque<int[]> arrayChildren = new ArrayDeque<int[]>();
    ArrayDeque<int[]> aq = new ArrayDeque<int[]>();

    public Vexillary(int bound) {
        this.bound = bound;
    }

    public Collection<Permutation> getBasis() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<Permutation> getBasisTo(int n) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return vexBound(p, bound);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        return vexBound(values, bound);
    }

    public ArrayDeque<Permutation> children(Permutation p) {
        children.clear();
        if (A2143.isSatisfiedBy(p.elements)) {
            return children;
        }
//        int pInv = p.inversions();
//        System.out.println("p = " + p);
        RS rs = rs(p);
//        System.out.println("r = " + rs.r + " s = " + rs.s);
//        for (int j = 0; j < rs.r; j++) {
//            if (j != rs.r) {
//                Permutation q = p.clone();
//                int v = q.elements[j];
//                q.elements[j] = q.elements[rs.s];
//                q.elements[rs.s] = q.elements[rs.r];
//                q.elements[rs.r] = v;
//                // System.out.println(q + " " + q.inversions());
//                if (q.inversions() == pInv) {
//                    result.add(q);
//                }
//            }
//        }
        int deltaS = 0;
        for (int j = rs.r - 1; j >= 0; j--) {
            int[] elements = p.elements;
            if (elements[j] < elements[rs.s]) {
                deltaS++;
            } else {
                deltaS--;
            }
            int deltaJ = 0;
            for (int t = j + 1; t < rs.r; t++) {
                if (elements[t] > elements[j]) {
                    deltaJ++;
                } else {
                    deltaJ--;
                }
            }
            // System.out.println("j = " + j  + " delta= " + (deltaS+deltaJ));
            if (deltaS + deltaJ == 1) {
                elements = Arrays.copyOf(p.elements, p.elements.length);
                int v = elements[j];
                elements[j] = elements[rs.s];
                elements[rs.s] = elements[rs.r];
                elements[rs.r] = v;
                children.add(new Permutation(elements, PermUtilities.SAFE));
            }
        }
        if (children.isEmpty()) {
            return children(PermUtilities.sum(Permutation.ONE, p));
        }
        return children;
    }

    public ArrayDeque<int[]> children(int[] elements) {
        arrayChildren.clear();
        if (A2143.isSatisfiedBy(elements)) {
            return arrayChildren;
        }
//        int pInv = p.inversions();
//        System.out.println("p = " + p);
        RS rs = rs(elements);
//        System.out.println("r = " + rs.r + " s = " + rs.s);
//        for (int j = 0; j < rs.r; j++) {
//            if (j != rs.r) {
//                Permutation q = p.clone();
//                int v = q.elements[j];
//                q.elements[j] = q.elements[rs.s];
//                q.elements[rs.s] = q.elements[rs.r];
//                q.elements[rs.r] = v;
//                // System.out.println(q + " " + q.inversions());
//                if (q.inversions() == pInv) {
//                    result.add(q);
//                }
//            }
//        }
        int deltaS = 0;
        for (int j = rs.r - 1; j >= 0; j--) {
            if (elements[j] < elements[rs.s]) {
                deltaS++;
            } else {
                deltaS--;
            }
            int deltaJ = 0;
            for (int t = j + 1; t < rs.r; t++) {
                if (elements[t] > elements[j]) {
                    deltaJ++;
                } else {
                    deltaJ--;
                }
            }
            // System.out.println("j = " + j  + " delta= " + (deltaS+deltaJ));
            if (deltaS + deltaJ == 1) {
                int[] newElements = Arrays.copyOf(elements, elements.length);
                int v = newElements[j];
                newElements[j] = newElements[rs.s];
                newElements[rs.s] = newElements[rs.r];
                newElements[rs.r] = v;
                arrayChildren.add(newElements);
            }
        }
        if (arrayChildren.isEmpty()) {
            int[] newElements = new int[elements.length + 1];
            newElements[0] = 0;
            for (int i = 1; i < newElements.length; i++) {
                newElements[i] = elements[i - 1] + 1;
            }
            return children(newElements);
        }
        return arrayChildren;
    }

    public boolean vexBound(Permutation p, int k) {
        q.clear();
        q.add(p);
        int leaves = 0;
        while (!q.isEmpty()) {
            Collection<Permutation> children = children(q.poll());
            if (children.isEmpty()) {
                leaves++;
            } else {
                q.addAll(children);
            }
            if (q.size() + leaves > k) {
                return false;
            }

        }
        return q.size() + leaves <= k;
    }

    public boolean vexBound(int[] elements, int k) {
        aq.clear();
        aq.add(elements);
        int leaves = 0;
        while (!aq.isEmpty()) {
            Collection<int[]> children = children(aq.poll());
            if (children.isEmpty()) {
                leaves++;
            } else {
                aq.addAll(children);
            }
            if (aq.size() + leaves > k) {
                return false;
            }

        }
        return aq.size() + leaves <= k;
    }

    public RS rs(Permutation p) {
        return rs(p.elements);
    }

    public RS rs(int[] elements) {
        RS result = new RS();
        int r = elements.length - 2;
        while (r >= 0 && elements[r] < elements[r + 1]) {
            r--;
        }
        result.r = r;
        int s = elements.length - 1;
        while (elements[s] > elements[r]) {
            s--;
        }
        result.s = s;
        return result;
    }

    class RS {

        int r, s;
    }

    public static void main(String[] args) {

        Vexillary v = new Vexillary(4);
        Permutation p = new Permutation("32514");
//        checkExtensions(p.elements, 1, v);
        ArrayDeque<Permutation> candidates = new ArrayDeque<Permutation>();
        for (int i = 0; i < 1; i++) {
            do {
                p = PermUtilities.randomPermutation(10);
            } while (!v.isSatisfiedBy(p));
            candidates.add(p);
        }
        long start = System.currentTimeMillis();
//        for (Permutation c : candidates) {
//            System.out.println(c);
//            checkExtensions(c, 4, v);
//        }
//        System.out.println(System.currentTimeMillis() - start);
//        start = System.currentTimeMillis();
        for (Permutation c : candidates) {
            System.out.println(c);
            checkExtensions(c.elements, 6, v);
        }
        System.out.println(System.currentTimeMillis() - start);


    }

    public static void checkExtensions(Permutation p, int k, Vexillary v) {
        if (k == 0) {
            return;
        }
        Iterable<Permutation> children = PermUtilities.rightExtensions(p);
        for (Permutation q : children) {
            if (!v.isSatisfiedBy(q)) {
                // System.out.println(q);
                boolean hasBadDeletion = false;
                for (Permutation r : PermUtilities.deletions(q)) {
                    hasBadDeletion = !v.isSatisfiedBy(r);
                    if (hasBadDeletion) {
                        break;
                    }
                }
                if (!hasBadDeletion) {
                    System.out.println("Basis element " + q);
                }
            } else {
                checkExtensions(q, k - 1, v);
            }
        }
        // System.out.println("Done");
    }

    public static void checkExtensions(int[] elements, int k, Vexillary v) {
        if (k == 0) {
            return;
        }
        for (int i = 0; i <= elements.length; i++) {
            int[] newElements = Arrays.copyOf(elements, elements.length + 1);
            for (int j = 0; j < elements.length; j++) {
                if (newElements[j] >= i) {
                    newElements[j]++;
                }
            }
            newElements[newElements.length - 1] = i;
//            System.out.println(Arrays.toString(newElements));
            if (!v.isSatisfiedBy(newElements)) {
                int[] deletedCopy = Arrays.copyOf(newElements, newElements.length - 1);
                boolean hasBadDeletion = false;
                for (int deletedElement = newElements.length - 1; deletedElement >= 0; deletedElement--) {
                    hasBadDeletion = !v.isSatisfiedBy(deletedCopy);
                    if (hasBadDeletion || deletedElement == 0) {
                        break;
                    }
                    deletedCopy[deletedElement - 1] = newElements[deletedElement];
                }
                if (!hasBadDeletion) {
                    System.out.println("Basis element " + Arrays.toString(newElements));
                }
            } else {
                checkExtensions(newElements, k - 1, v);
            }
        }
    }
}
