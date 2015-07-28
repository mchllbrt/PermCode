package permlib.examples;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Rev-321 is the class of staircases where the elements in a cell are in
 * decreasing rather than increasing order. What is this class?
 *
 * @author Michael Albert
 */
public class Rev321 {

    public static void main(String[] args) {
        
        doClass();
        
    }

    public static void doClass() {
        HashSet<Permutation> basis = new HashSet<>();
        PermutationClass r321 = new PermutationClass(basis);
        for(int n = 4; n < 15; n++) {
            HashSet<Permutation> perms = new HashSet<>();
            Codes cs = new Codes(n,n);
            for(int [] c : cs) {
                perms.add(new CodedPermutation(c).getPermutation());
            }
            for(Permutation p : new Permutations(r321, n)) {
                if (!perms.contains(p)) {
                    System.out.println(p);
                    basis.add(p);
                }
            }
            r321 = new PermutationClass(basis);
        }
    }
    
    static class Codes implements Iterable<int[]> {

        int cells;
        int length;

        public Codes(int cells, int length) {
            this.cells = cells;
            this.length = length;
        }

        @Override
        public Iterator<int[]> iterator() {
            return new Iterator<int[]>() {

                int[] c = null;

                @Override
                public boolean hasNext() {
                    if (c == null) {
                        return true;
                    }
                    for (int i = 0; i < c.length; i++) {
                        if (c[i] < cells - 1) {
                            return true;
                        }
                    }
                    return false;
                }

                @Override
                public int[] next() {
                    if (c == null) {
                        c = new int[length];
                    } else {
                        update();
                    }
                    return c;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                private void update() {
                    for (int i = c.length - 1; i >= 0; i--) {
                        if (c[i] < cells - 1) {
                            c[i]++;
                            int v = c[i] - 1;
                            for (int j = i + 1; j < c.length; j++) {
                                c[j] = v;
                                v = (v > 0) ? v - 1 : 0;
                            }
                            return;
                        }

                    }
                }

            };

        }
       

    }

    static class CodedPermutation {

            Point[] c;

            CodedPermutation(int[] c) {
                this.c = new Point[c.length];
                for (int i = 0; i < c.length; i++) {
                    this.c[i] = new Point(c[i]);
                }
                Point[] cc = Arrays.copyOf(this.c, this.c.length);
                Arrays.sort(cc,
                        new Comparator<Point>() {

                            @Override
                            public int compare(Point o1, Point o2) {
                                int d = o2.c - o1.c;
                                if (d > 1 || d < -1 || d == 0) return d;
                                if (d == 1 && o2.c % 2 == 0) return d;
                                if (d == -1 && o2.c % 2 == 1) return d;
                                return 0;
                            }
                        });
                
                int n = this.c.length - 1;
                for (int i = 0; i < cc.length; i++) {
                    cc[i].v = n;
                    n--;
                }
                // System.out.println(Arrays.toString(this.c));
                
                Arrays.sort(this.c, new Comparator<Point>() {

                    @Override
                    public int compare(Point o1, Point o2) {
                        int d = o1.c - o2.c;
                        if (d < -1 || d > 1 || d == 0) {
                            return d;
                        }
                        if (d == 1 && o1.c % 2 == 1) {
                            return d;
                        }
                        if (d == -1 && o1.c % 2 == 0) {
                            return d;
                        }
                        return 0;
                    }
                });
                // System.out.println(Arrays.toString(this.c));

            }
            
            public Permutation getPermutation() {
                int[] v = new int[c.length];
                for(int i = 0; i < c.length; i++) {
                    v[i] = c[i].v;
                }
                return new Permutation(v);
            }

        }
    private static class Point {

        int c;
        int v = -1;

        public Point(int c) {
            this.c = c;
        }
        
        public String toString() {
            return "<"+ c + "," + v + ">";
        }

    }
}
