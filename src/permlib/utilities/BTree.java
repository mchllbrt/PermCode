package permlib.utilities;

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

    public BTree(BTree left, BTree right) {
        this.left = left;
        this.right = right;
    }

    public BTree() {
        this(null, null);
    }

    public BTree getLeft() {
        return left;
    }

    public void setLeft(BTree left) {
        this.left = left;
    }

    public BTree getRight() {
        return right;
    }

    public void setRight(BTree right) {
        this.right = right;
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
            System.out.println(descendingTree(p));
        }
    }
}
