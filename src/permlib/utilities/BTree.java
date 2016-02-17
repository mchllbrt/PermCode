package permlib.utilities;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import permlib.Permutation;

/**
 * A bare binary tree.
 *
 * @author Michael Albert
 */
public class BTree {

    private BTree left;
    private BTree right;
    private int size;

    public BTree(BTree left, BTree right) {
        this.size = 1; // For the root
        this.setLeft(left);
        this.setRight(right);
    }

    public BTree() {
        this(null, null);
    }

    public BTree getLeft() {
        return left;
    }

    public void setLeft(BTree left) {
        this.size -= getSize(this.left);
        this.left = left;
        this.size += getSize(this.left);
    }

    public BTree getRight() {
        return right;
    }

    public void setRight(BTree right) {
        this.size -= getSize(this.right);
        this.right = right;
        this.size += getSize(this.right);
    }

    public static int getSize(BTree t) {
        if (t == null) {
            return 0;
        }
        return t.size;
    }

    public static StringBuffer dyckStringBuffer(BTree t) {
        StringBuffer result = new StringBuffer();
        dyckStringBuffer(t, result);
        return result;
    }

    public static void dyckStringBuffer(BTree t, StringBuffer result) {
        if (t == null) {
            return;
        }
        result.append('1');
        dyckStringBuffer(t.left, result);
        result.append('0');
        dyckStringBuffer(t.right, result);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.left);
        hash = 97 * hash + Objects.hashCode(this.right);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BTree other = (BTree) obj;
        if (!Objects.equals(this.left, other.left)) {
            return false;
        }
        if (!Objects.equals(this.right, other.right)) {
            return false;
        }
        return true;
    }

    public static BTree descendingTree(Permutation p) {
        return descendingTree(p.elements, 0, p.length());
    }

    public static BTree descendingTree(int[] v, int low, int high) {
        if (high <= low) {
            return null;
        }
        if (high - low == 1) {
            return new BTree();
        }
        int m = Integer.MIN_VALUE;
        int pm = low - 1;
        for (int i = low; i < high; i++) {
            if (v[i] > m) {
                m = v[i];
                pm = i;
            }
        }
        return new BTree(descendingTree(v, low, pm), descendingTree(v, pm + 1, high));
    }

    public String toString() {
        if (left == null && right == null) {
            return "*";
        }
        StringBuilder result = new StringBuilder();
        if (left != null) {
            result.append('[');
            result.append(left.toString());
            result.append(']');
        }
        if (right != null) {
            result.append('(');
            result.append(right.toString());
            result.append(')');
        }
        return result.toString();
    }

    public static void main(String[] args) {
        Permutation[] ps = {new Permutation("123"), new Permutation("321"), new Permutation("132"), new Permutation("2413")};
        for (Permutation p : ps) {
            BTree t = descendingTree(p);
            System.out.println(p);
            System.out.println(descendingTree(p));
            System.out.println(dyckStringBuffer(t));
        }
    }

    public int getSize() {
        return size;
    }
}
