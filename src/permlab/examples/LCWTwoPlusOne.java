/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import permlib.classes.PermutationClass;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;

/**
 *
 * @author MichaelAlbert
 */
public class LCWTwoPlusOne {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Permutation p = new Permutation("1");
        RectangularizedPermutation pr = new RectangularizedPermutation(p, new Rectangle(0, 1, 0, 1), null);
        HashSet<Permutation> s = new HashSet<Permutation>();
        for (RectangularizedPermutation qr : pr.children(6)) {
            s.add(qr.getP());
        }
        System.out.println(s.size());
        for (Permutation q : new Permutations(7)) {
            if (!s.contains(q)) {
                System.out.println(q);
            }
        }
        System.out.println();
        s.clear();
        for (RectangularizedPermutation qr : pr.children(7)) {
            s.add(qr.getP());
        }
        System.out.println(s.size());
        ArrayList<Permutation> basis = new ArrayList<Permutation>();
        basis.add(new Permutation("3614725"));
        basis.add(new Permutation("5274163"));
        PermutationClass c = new PermutationClass(basis);
        for (Permutation q : new Permutations(8)) {
            if (!s.contains(q) && c.containsPermutation(q)) {
                System.out.println(q);
                basis.add(q);
            }
        }
        System.out.println();
        c = new PermutationClass(basis);
        s.clear();
        for (RectangularizedPermutation qr : pr.children(8)) {
            s.add(qr.getP());
        }
        System.out.println(s.size());
        for (Permutation q : new Permutations(9)) {
            if (!s.contains(q) && c.containsPermutation(q)) {
                System.out.println(q);
                basis.add(q);
            }
        }
        System.out.println();
        c = new PermutationClass(basis);
        s.clear();
        for (RectangularizedPermutation qr : pr.children(9)) {
            s.add(qr.getP());
        }
        for (Permutation q : new Permutations(10)) {
            if (!s.contains(q) && c.containsPermutation(q)) {
                System.out.println(q);
                basis.add(q);
            }
        }
        System.out.println();


    }

    private static class RectangularizedPermutation {

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 97 * hash + (this.p != null ? this.p.hashCode() : 0);
            hash = 97 * hash + (this.a != null ? this.a.hashCode() : 0);
            hash = 97 * hash + (this.b != null ? this.b.hashCode() : 0);
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
            final RectangularizedPermutation other = (RectangularizedPermutation) obj;
            if (this.p != other.p && (this.p == null || !this.p.equals(other.p))) {
                return false;
            }
            if (this.a != other.a && (this.a == null || !this.a.equals(other.a))) {
                return false;
            }
            if (this.b != other.b && (this.b == null || !this.b.equals(other.b))) {
                return false;
            }
            return true;
        }
        Permutation p;
        Rectangle a;
        Rectangle b;

        public RectangularizedPermutation(Permutation p, Rectangle a, Rectangle b) {
            this.p = p;
            if (a == null) {
                this.a = b;
                this.b = null;
            } else if (b == null) {
                this.a = a;
                this.b = null;
            } else {
                if (a.left < b.left) {
                    this.a = a;
                    this.b = b;
                } else {
                    this.a = b;
                    this.b = a;
                }
            }
        }

        public Permutation getP() {
            return p;
        }

        public HashSet<RectangularizedPermutation> children(int n) {
            HashSet<RectangularizedPermutation> result = new HashSet<RectangularizedPermutation>();
            result.add(this);
            for (int i = 0; i < n; i++) {
                HashSet<RectangularizedPermutation> r = new HashSet<RectangularizedPermutation>();
                for (RectangularizedPermutation pr : result) {
                    r.addAll(pr.children());
                }
                result = r;
            }
            return result;
        }

        public HashSet<RectangularizedPermutation> children() {
            HashSet<RectangularizedPermutation> result = new HashSet<RectangularizedPermutation>();
            ArrayList<Integer> horizontalCandidates = new ArrayList<Integer>();
            horizontalCandidates.add(0);
            horizontalCandidates.add(p.length());
            if (a != null && b != null && a.right <= b.left) {
                horizontalCandidates.add(b.left);
            }
            if (a != null && b != null && b.right <= a.left) {
                horizontalCandidates.add(a.left);
            }
            ArrayList<Integer> verticalCandidates = new ArrayList<Integer>();
            verticalCandidates.add(0);
            verticalCandidates.add(p.length());
            if (a != null && b != null && a.top <= b.bottom) {
                verticalCandidates.add(b.bottom);
            }
            if (a != null && b != null && b.top <= a.bottom) {
                verticalCandidates.add(a.bottom);
            }
            for (int c : horizontalCandidates) {
                for (int r : verticalCandidates) {
                    Permutation q = PermUtilities.insert(p, c, r);
                    Rectangle aq = (a == null) ? null : a.copy();
                    Rectangle bq = (b == null) ? null : b.copy();
                    if (aq != null && c <= aq.left) {
                        aq.shiftRight();
                    }
                    if (bq != null && c <= bq.left) {
                        bq.shiftRight();
                    }
                    if (aq != null && r <= aq.bottom) {
                        aq.shiftUp();
                    }
                    if (bq != null && r <= bq.bottom) {
                        bq.shiftUp();
                    }
                    if (aq != null && bq != null) {
                        result.addAll(merges(q, r, c, aq, bq));
                    } else {
                        result.add(new RectangularizedPermutation(q, (aq == null) ? bq : aq, new Rectangle(c, c + 1, r, r + 1)));
                    }
                }
            }


            return result;
        }

        private Collection<RectangularizedPermutation> merges(Permutation q, int r, int c, Rectangle aq, Rectangle bq) {
            HashSet<RectangularizedPermutation> result = new HashSet<RectangularizedPermutation>();
            Rectangle cq = new Rectangle(c, c + 1, r, r + 1);
            Rectangle abq = aq.merge(bq);
            if (abq.overlaps(cq)) {
                result.add(new RectangularizedPermutation(q, abq.merge(cq), null));
            } else {
                result.add(new RectangularizedPermutation(q, abq, cq));
            }
            Rectangle acq = aq.merge(cq);
            if (acq.overlaps(bq)) {
                result.add(new RectangularizedPermutation(q, acq.merge(bq), null));
            } else {
                result.add(new RectangularizedPermutation(q, acq, bq));
            }
            Rectangle bcq = bq.merge(cq);
            if (bcq.overlaps(aq)) {
                result.add(new RectangularizedPermutation(q, bcq.merge(aq), null));
            } else {
                result.add(new RectangularizedPermutation(q, bcq, aq));
            }


            return result;
        }

        @Override
        public String toString() {
            return p + " " + a + " " + b;
        }
    }

    private static class Rectangle implements Comparable<Rectangle> {

        int left;
        int right;
        int bottom;
        int top;

        public Rectangle(int left, int right, int bottom, int top) {
            this.left = left;
            this.right = right;
            this.bottom = bottom;
            this.top = top;
        }

        public int getLeft() {
            return left;
        }

        public int getRight() {
            return right;
        }

        public int getBottom() {
            return bottom;
        }

        public int getTop() {
            return top;
        }

        public void shiftRight() {
            left++;
            right++;
        }

        public void shiftUp() {
            bottom++;
            top++;
        }

        private Rectangle copy() {
            return new Rectangle(left, right, bottom, top);
        }

        private Rectangle merge(Rectangle other) {
            return new Rectangle(
                    (this.left < other.left) ? this.left : other.left,
                    (this.right > other.right) ? this.right : other.right,
                    (this.bottom < other.bottom) ? this.bottom : other.bottom,
                    (this.top > other.top) ? this.top : other.top);
        }

        private boolean overlaps(Rectangle other) {
            return (Math.max(this.left, other.left) < Math.min(this.right, other.right))
                    && (Math.max(this.bottom, other.bottom) < Math.min(this.top, other.top));
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + this.left;
            hash = 43 * hash + this.right;
            hash = 43 * hash + this.bottom;
            hash = 43 * hash + this.top;
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
            final Rectangle other = (Rectangle) obj;
            if (this.left != other.left) {
                return false;
            }
            if (this.right != other.right) {
                return false;
            }
            if (this.bottom != other.bottom) {
                return false;
            }
            if (this.top != other.top) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "[" + left + ", " + right + ", " + bottom + ", " + top + "]";
        }

        @Override
        public int compareTo(Rectangle other) {
            if (other == null) {
                return -1;
            }
            if (this.left != other.left) {
                return this.left - other.left;
            }
            if (this.right != other.right) {
                return this.right - other.right;
            }
            if (this.bottom != other.bottom) {
                return this.bottom - other.bottom;
            }
            return this.top - other.top;
        }
    }
}
