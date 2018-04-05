package permlib.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.PermutationClass;

/**
 * Experiment on the matrix version of the 'balanced cover' problem -- finding
 * sets of rows for which the column sums are constant.
 *
 * @author Michael Albert
 */
public class Balancer {

    private static boolean checkBalance(PermutationClass cnn, int k, int n) {
       HashMap<Permutation, Integer> counts = new HashMap<>();
       for(Permutation p : cnn.getPerms(k)) {
           counts.put(p,0);
       }
       for(Permutation q : cnn.getPerms(n)) {
           HashSet<Permutation> subq = PermUtilities.subpermutations(q);
           for(Permutation p : counts.keySet()) {
               if (subq.contains(p)) {
                   counts.put(p, counts.get(p) + 1);
               }
           }
       }
       HashSet<Integer> cv = new HashSet<>();
       for(Permutation p : counts.keySet()) {
           cv.add(counts.get(p));
       }
       return cv.size() == 1;
    }

  
    int[][] m;
    int rows;
    int cols;

    ArrayList<ArrayList<Integer>> solutions = new ArrayList<>();
    ArrayDeque<Integer> candidate = new ArrayDeque<Integer>();
    int[] totals;
    int[][] remaining;

    public Balancer(int[][] m) {
        this.m = m;
        this.rows = m.length;
        this.cols = m[0].length;
        totals = new int[this.cols];
        computeRemaining();
        solve(0);
    }

    private void solve(int i) {
        if (i < this.rows && !completable(i)) {
            return;
        }
        if (satisfied()) {
            addSolution();
        }
        if (this.rows <= i) {
            return;
        }
        solveWith(i);
        solveWithout(i);
    }

    public ArrayList<ArrayList<Integer>> getSolutions() {
        return solutions;
    }

    private boolean completable(int i) {
        int tmax = Integer.MIN_VALUE;
        for (int t : totals) {
            tmax = (tmax > t) ? tmax : t;
        }
        for (int c = 0; c < cols; c++) {
            if (tmax - totals[c] > remaining[i][c]) {
                return false;
            }
        }
        return true;
    }

    private boolean satisfied() {
        for (int t : totals) {
            if (t != totals[0]) {
                return false;
            }
        }
        return totals[0] != 0;
    }

    private void addSolution() {
        solutions.add(new ArrayList<Integer>(candidate));
    }

    private void solveWith(int i) {
        candidate.addLast(i);
        for (int c = 0; c < cols; c++) {
            totals[c] += m[i][c];
        }
        solve(i + 1);
        for (int c = 0; c < cols; c++) {
            totals[c] -= m[i][c];
        }
        candidate.removeLast();
    }

    private void solveWithout(int i) {
        solve(i + 1);
    }

    private void computeRemaining() {
        remaining = new int[rows][cols];
        for (int c = 0; c < cols; c++) {
            remaining[rows - 1][c] = m[rows - 1][c];
        }
        for (int r = rows - 2; r >= 0; r--) {
            for (int c = 0; c < cols; c++) {
                remaining[r][c] = remaining[r + 1][c] + m[r][c];
            }
        }
    }

//    private static void check(HashSet<Permutation> basis) {
//        PermutationClass c = new PermutationClass(basis);
//        ArrayList<Permutation> c5 = new ArrayList<>(c.getPerms(5));
//        ArrayList<Permutation> s3 = new ArrayList<Permutation>();
//        for (Permutation p : new Permutations(3)) {
//            s3.add(p);
//        }
//        if (c5.size() > 0) {
//            System.out.println(basis);
//            int[][] m = new int[c5.size()][s3.size()];
//            for (int i = 0; i < s3.size(); i++) {
//                for (int j = 0; j < c5.size(); j++) {
//                    if (PermUtilities.subpermutations(c5.get(j)).contains(s3.get(i))) {
//                        m[j][i] = 1;
//                    }
//                }
//            }
//
//            Balancer b = new Balancer(m);
//            if (b.getSolutions().size() > 0) {
//                System.out.println(basis);
//                System.out.println(b.getSolutions().size());
//                System.out.println();
//            }
//
//        }
//    }

    public static void main(String[] args) {

//        ArrayList<Permutation> s3 = new ArrayList<Permutation>();
//        for (Permutation p : new Permutations(3)) {
//            s3.add(p);
//        }
//        ArrayList<Permutation> s4 = new ArrayList<Permutation>();
//        for (Permutation p : new Permutations(4)) {
//            // System.out.println(p);
//            s4.add(p);
//        }
//        int[][] m = new int[s4.size()][s3.size()];
//        for (int i = 0; i < s3.size(); i++) {
//            for (int j = 0; j < s4.size(); j++) {
//                if (PermUtilities.subpermutations(s4.get(j)).contains(s3.get(i))) {
//                    m[j][i] = 1;
//                }
//            }
//        }
//
//        Balancer b = new Balancer(m);
//        System.out.println(b.getSolutions().size());
//        for (ArrayList<Integer> s : b.getSolutions()) {
//            if (s.contains(0) && s.contains(s4.size() - 1)) {
//                // System.out.println(s);
//                HashSet<Permutation> basis = new HashSet<>();
//                basis.addAll(s4);
//                for (int i : s) {
//                    basis.remove(s4.get(i));
//                }
//                if (basis.size() > 0) check(basis);
//            }
//        }
        int n = 4;
        int k = 3;

        HashSet<Permutation> b3 = new HashSet<>();
//        b3.add(new Permutation("312"));
//        b3.add(new Permutation("231"));
//        b3.add(new Permutation("213"));
        PermutationClass c = new PermutationClass(b3);

        ArrayList<Permutation> ck = new ArrayList<>(c.getPerms(k));
        ArrayList<Permutation> cn = new ArrayList<>(c.getPerms(n));

        int[][] m = new int[cn.size()][ck.size()];
        for (int i = 0; i < ck.size(); i++) {
            for (int j = 0; j < cn.size(); j++) {
                if (PermUtilities.subpermutations(cn.get(j)).contains(ck.get(i))) {
                    m[j][i] = 1;
                }
            }
        }

//        for (int j = 0; j < cn.size(); j++) {
//            for (int i = 0; i < ck.size(); i++) {
//                System.out.print(m[j][i] + " ");
//            }
//            System.out.println();
//        }

        Balancer b = new Balancer(m);
        System.out.println("Moving on");
        System.out.println(b.getSolutions().size() + " solutions to check");
        for (ArrayList<Integer> s : b.getSolutions()) {
            HashSet<Permutation> newP = new HashSet<>();
            for (int i : s) {
                newP.add(cn.get(i));
            }
            if (!newP.contains(PermUtilities.increasingPermutation(n))
                    || !newP.contains(PermUtilities.decreasingPermutation(n))) {
                continue;
            }
            HashSet<Permutation> newB = new HashSet<>(c.getPerms(n));
            newB.removeAll(newP);
            System.out.println("Checking " + newB);
            newB.addAll(b3);
            HashSet<HashSet<Permutation>> b54 = foo(5, 4, newB);
            for(HashSet<Permutation> bas : b54) {
                // System.out.print("Checking " + bas + " from 4 to 5 ");
                HashSet<Permutation> nnb = new HashSet<>(bas);
                nnb.addAll(newB);
                PermutationClass cnn = new PermutationClass(nnb);
                if (checkBalance(cnn, 3, 5)) {
                    System.out.println(bas + " success");
                } else {
                    // System.out.println(" failure ");
                }
            }
            // System.out.println();
        }
        
          

    }
    
    private static HashSet<HashSet<Permutation>> foo(int n, int k, HashSet<Permutation> basis) {
        HashSet<HashSet<Permutation>> result = new HashSet<>();
//        System.out.println("Basis: " + basis);
//        System.out.println("From " + k + " to " + n);
        PermutationClass c = new PermutationClass(basis);

        ArrayList<Permutation> ck = new ArrayList<>(c.getPerms(k));
        ArrayList<Permutation> cn = new ArrayList<>(c.getPerms(n));

        int[][] m = new int[cn.size()][ck.size()];
        for (int i = 0; i < ck.size(); i++) {
            for (int j = 0; j < cn.size(); j++) {
                if (PermUtilities.subpermutations(cn.get(j)).contains(ck.get(i))) {
                    m[j][i] = 1;
                }
            }
        }

//        for (int j = 0; j < cn.size(); j++) {
//            for (int i = 0; i < ck.size(); i++) {
//                System.out.print(m[j][i] + " ");
//            }
//            System.out.println();
//        }

        Balancer b = new Balancer(m);
        ArrayList<ArrayList<Integer>> sols = b.getSolutions();
//        if (sols.isEmpty()) {
//            System.out.println("No solution");
//        }
        for (ArrayList<Integer> s : sols) {
            HashSet<Permutation> newP = new HashSet<>();
            for (int i : s) {
                newP.add(cn.get(i));
            }
            if (!newP.contains(PermUtilities.increasingPermutation(n))
                    || !newP.contains(PermUtilities.decreasingPermutation(n))) {
                continue;
            }
            HashSet<Permutation> newB = new HashSet<>(c.getPerms(n));
            newB.removeAll(newP);
            result.add(newB);
            // System.out.println(newB);
        }
        return result;
    }

    
    
}
