package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.utilities.Combinations;

/**
 * When is the inversion graph of a permutation decomposable into triangles?
 *
 * @author Michael Albert
 */
public class TriangleDecompositions {

    public static void main(String[] args) {
        ArrayList<Permutation> good = new ArrayList<Permutation>();
        for (Permutation p : new Permutations(6)) {
            if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
                // System.out.println(p);
                Graph gp = new Graph(p);
                ArrayList<int[]> ts = gp.P3Decomposition();
                if (ts != null) {
                    good.add(p);
                    System.out.println(p);
                    for (int[] t : ts) {
                        System.out.println(" " + Arrays.toString(t));
                    }
                }
            }
        }
        System.out.println("------------------");
//        HashSet<Permutation> patterns = new HashSet<>();
//        for(int[] c : new Combinations(8,5)) {
//            for(Permutation p : good) {
//                patterns.add(p.patternAt(c));
//            }
//        }
//        for(Permutation p : new Permutations(5)) {
//            if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p) && !patterns.contains(p)) System.out.println(p);
//        }
    }

    static class Graph {

        HashSet<Edge> edges = new HashSet<Edge>();
        int n;

        public Graph(int n, HashSet<Edge> edges) {
            this.n = n;
            this.edges = edges;
        }

        public Graph(int n) {
            this.n = n;
        }

        public Graph(Permutation p) {
            n = p.length();
            for (int i = 0; i < p.length(); i++) {
                for (int j = i + 1; j < p.length(); j++) {
                    if (p.at(i) > p.at(j)) {
                        edges.add(new Edge(i, j));
                    }
                }
            }
        }

        public Graph deleteEdges(Collection<Edge> es) {
            HashSet<Edge> d = new HashSet<>(edges);
            d.removeAll(es);
            return new Graph(n, d);
        }

        public Graph deleteEdges(int[] v) {
            HashSet<Edge> d = new HashSet<>(edges);
            for (int i = 0; i < v.length; i++) {
                for (int j = i + 1; j < v.length; j++) {
                    d.remove(new Edge(v[i], v[j]));
                }
            }
            return new Graph(n, d);
        }

        boolean isEdge(int i, int j) {
            return edges.contains(new Edge(i, j));
        }

        boolean isClique(int[] c) {
            for (int i = 0; i < c.length; i++) {
                for (int j = i + 1; j < c.length; j++) {
                    if (!isEdge(c[i], c[j])) {
                        return false;
                    }
                }
            }
            return true;
        }
        
        int countEdges(int[] c) {
            int result = 0;
            for (int i = 0; i < c.length; i++) {
                for (int j = i + 1; j < c.length; j++) {
                    if (isEdge(c[i], c[j])) result++;
                }
            }
            return result;
        }

        public ArrayList<int[]> getTriangles() {
            ArrayList<int[]> result = new ArrayList<>();
            for (int[] t : new Combinations(n, 3)) {
                if (isClique(t)) {
                    result.add(Arrays.copyOf(t, 3));
                }
            }
            return result;
        }
        
        public ArrayList<int[]> getP3() {
            ArrayList<int[]> result = new ArrayList<>();
            for (int[] t : new Combinations(n, 3)) {
                if (countEdges(t) == 2) {
                    result.add(Arrays.copyOf(t, 3));
                }
            }
            return result;
        }

        public ArrayList<int[]> triangleDecomposition() {
            if (failsDivisibility()) {
                return null;
            }
            ArrayList<int[]> ts = getTriangles();
            return triangleDecomposition(ts, 0);
        }
        
        public ArrayList<int[]> P3Decomposition() {
            ArrayList<int[]> ts = getP3();
            return P3Decomposition(ts, 0);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + Objects.hashCode(this.edges);
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
            final Graph other = (Graph) obj;
            if (!Objects.equals(this.edges, other.edges)) {
                return false;
            }
            return true;
        }

        private ArrayList<int[]> triangleDecomposition(ArrayList<int[]> ts, int i) {
            if (edges.size() == 0) {
                return new ArrayList<int[]>();
            }
            if (i >= ts.size()) {
                // System.out.println("Failed at end");
                return null;
            }
            if (!isClique(ts.get(i))) {
                // System.out.println("Not clique " + Arrays.toString(ts.get(i)));
                return triangleDecomposition(ts, i + 1);
            }
            // System.out.println("Deleting " + Arrays.toString(ts.get(i)));
            Graph gd = deleteEdges(ts.get(i));
            // System.out.println("Now have " + gd.edges.size());
            ArrayList<int[]> rd = gd.triangleDecomposition(ts, i + 1);
            if (rd != null) {
                rd.add(ts.get(i));
                return rd;
            }
            return triangleDecomposition(ts, i + 1);
        }
        
        private ArrayList<int[]> P3Decomposition(ArrayList<int[]> ts, int i) {
            if (edges.size() == 0) {
                return new ArrayList<int[]>();
            }
            if (i >= ts.size()) {
                // System.out.println("Failed at end");
                return null;
            }
            if (!(countEdges(ts.get(i)) == 2)) {
                // System.out.println("Not clique " + Arrays.toString(ts.get(i)));
                return P3Decomposition(ts, i + 1);
            }
            // System.out.println("Deleting " + Arrays.toString(ts.get(i)));
            Graph gd = deleteEdges(ts.get(i));
            // System.out.println("Now have " + gd.edges.size());
            ArrayList<int[]> rd = gd.P3Decomposition(ts, i + 1);
            if (rd != null) {
                rd.add(ts.get(i));
                return rd;
            }
            return P3Decomposition(ts, i + 1);
        }

        private ArrayList<int[]> interestingTriangleDecomposition() {
            ArrayList<int[]> ts = getTriangles();
            int[] c = new int[n];
            for (int[] t : ts) {
                c[t[0]]++;
                c[t[1]]++;
                c[t[2]]++;
            }
            for (int i = 0; i < n; i++) {
                if (c[i] < 2) {
                    return null;
                }
            }
            return triangleDecomposition(ts, 0);
        }

        private boolean failsDivisibility() {
            int[] d = new int[n];
            int sum = 0;
            for (Edge e : edges) {
                d[e.i]++;
                d[e.j]++;
                sum++;
            }
            if (sum % 3 != 0) return true;
            for (int i = 0; i < n; i++) {
                if (d[i] % 2 != 0) {
                    return true;
                }
            }
            return false;

        }

    }

    static class Edge {

        int i, j;

        public Edge(int i, int j) {
            this.i = i;
            this.j = j;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 29 * hash + this.i;
            hash = 29 * hash + this.j;
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
            final Edge other = (Edge) obj;
            if (this.i != other.i) {
                return false;
            }
            if (this.j != other.j) {
                return false;
            }
            return true;
        }

    }
}
