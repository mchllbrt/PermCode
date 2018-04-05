package permlib.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 *
 * @author MichaelAlbert
 */
public class Cover<A, B> {

    HashMap<A, Collection<B>> ab;
    HashMap<B, Collection<A>> ba;
    int aSize;
    int bSize;
    HashSet<A> available;

    public Cover(HashMap<A, Collection<B>> ab) {
        this.ab = ab;
        computeBA();
        aSize = ab.keySet().size();
        bSize = ba.keySet().size();
        available = new HashSet<>(ab.keySet());
    }

//    public Cover(HashMap<B, Collection<A>> ba) {
//        this.ba = ba;
//        computeAB();       
//    }
    private void computeBA() {
        this.ba = new HashMap<>();
        for (A a : ab.keySet()) {
            for (B b : ab.get(a)) {
                if (!this.ba.containsKey(b)) {
                    this.ba.put(b, new HashSet<A>());
                }
                this.ba.get(b).add(a);
            }
        }
        // for(B b : this.ba.keySet()) System.out.println(b + " " + this.ba.get(b));
    }

    private void computeAB() {
        this.ab = new HashMap<>();
        for (B b : ba.keySet()) {
            for (A a : ba.get(b)) {
                if (!this.ab.containsKey(a)) {
                    this.ab.put(a, new HashSet<B>());
                }
                this.ab.get(a).add(b);
            }
        }
    }

//If the matrix A has no columns, the current partial solution is a valid solution; terminate successfully.
//Otherwise choose a column c (deterministically).
//Choose a row r such that Ar, c = 1 (nondeterministically).
//Include row r in the partial solution.
//For each column j such that Ar, j = 1,
//for each row i such that Ai, j = 1,
//delete row i from matrix A.
//delete column j from matrix A.
// Repeat this algorithm recursively on the reduced matrix A.
    public HashSet<ArrayList<A>> exactCover(ArrayList<A> usedA, HashSet<A> availableA, ArrayDeque<B> toCover) {
        // System.out.println("Starting from " + usedA + " with " + toCover.size() + " left to cover.");
        HashSet<ArrayList<A>> result = new HashSet<>();
        if (toCover.isEmpty()) {
            // System.out.println("Success! " + usedA);
            result.add(usedA);
            return result;
        }
        B c = toCover.poll();
        for (A r : ba.get(c)) {
            if (!availableA.contains(r)) {
                continue;
            }
            // System.out.println("Trying " + r + " to cover " + c);
            ArrayList<A> nu = new ArrayList<A>(usedA);
            nu.add(r);
            HashSet<A> na = new HashSet<>(availableA);
            HashSet<B> ntc = new HashSet<B>(toCover);
            for (B b : ab.get(r)) {
                // System.out.println("Also covered " + b);
                for (A a : ba.get(b)) {
                    na.remove(a);
                }
                ntc.remove(b);
            }
            // System.out.println("Calling with " + nu + " " + na + " " + ntc);
            result.addAll(exactCover(nu, na, new ArrayDeque<B>(ntc)));
        }
        if (result.size() == 0 && usedA.size() <= 10) {
            // System.out.println("Failed on " + usedA);
        }
        return result;
    }

    public ArrayList<ArrayList<A>> solveWith(ArrayList<A> solution) {

        for (A a : solution) {
            for (B b : ab.get(a)) {
                for (A ar : ba.get(b)) {
                    available.remove(ar);
                }
                ba.remove(b);
            }
        }
        return solve(solution);
    }

    public ArrayList<ArrayList<A>> solve(ArrayList<A> solution) {
        // System.out.println("Enter: " + solution);
        // System.out.println(ba);
        ArrayList<ArrayList<A>> result = new ArrayList<>();
        if (ba.keySet().isEmpty()) {
            ArrayList<A> sol = new ArrayList<A>(solution);
            result.add(sol);
            System.out.println(sol);
            return result;
        }

        B workingCol = minSize();
        // System.out.println("Working column: " + workingCol);
        if (workingCol == null) {
            return result;
        }
        // System.out.println("Using " + workingCol);
        HashSet<A> as = new HashSet(ba.get(workingCol));
        // System.out.println(as);
        for (A a : as) {
            if (available.contains(a)) {
                solution.add(a);
                // System.out.println("Adding " + a);
                HashSet<A> removedRows = new HashSet<>();
                HashMap<B, Collection<A>> deletedCols = deleteCols(a, removedRows);
                // System.out.println("Deleted columns: " + deletedCols);
                // System.out.println("Deleted rows: " + removedRows);
                result.addAll(solve(solution));
                replaceCols(deletedCols);
                for (A ra : removedRows) {
                    available.add(ra);
                }
                solution.remove(solution.size() - 1);
            }
        }
        return result;
    }

    private HashMap<B, Collection<A>> deleteCols(A a, HashSet<A> removedRows) {
        HashMap<B, Collection<A>> result = new HashMap<>();
        for (B b : ab.get(a)) {
            // System.out.println("Removing " + b);
            result.put(b, ba.remove(b));
            for (A ra : result.get(b)) {
                if (available.contains(ra)) {
                    removedRows.add(ra);
                    available.remove(ra);
                }
            }
        }
        return result;
    }

    private void replaceCols(HashMap<B, Collection<A>> deletedCols) {
        for (B b : deletedCols.keySet()) {
            for (A a : deletedCols.get(b)) {
                ab.get(a).add(b);
            }
            ba.put(b, deletedCols.get(b));
        }
    }

//    def solve(X, Y, solution=[]):
//    if not X:
//        yield list(solution)
//    else:
//        c = min(X, key=lambda c: len(X[c]))
//        for r in list(X[c]):
//            solution.append(r)
//            cols = select(X, Y, r)
//            for s in solve(X, Y, solution):
//                yield s
//            deselect(X, Y, r, cols)
//            solution.pop()
//
//def select(X, Y, r):
//    cols = []
//    for j in Y[r]:
//        for i in X[j]:
//            for k in Y[i]:
//                if k != j:
//                    X[k].remove(i)
//        cols.append(X.pop(j))
//    return cols
//
//def deselect(X, Y, r, cols):
//    for j in reversed(Y[r]):
//        X[j] = cols.pop()
//        for i in X[j]:
//            for k in Y[i]:
//                if k != j:
//                    X[k].add(i)
    public String toPythonInput() {
        StringBuffer result = new StringBuffer();
        result.append("X = {\n");
        for (B b : this.ba.keySet()) {
            result.append(b + ": {");
            for (A a : this.ba.get(b)) {
                result.append(a + ",");
            }
            result.deleteCharAt(result.length() - 1);
            result.append("},\n");
        }
        result.delete(result.length() - 2, result.length());
        result.append("\n}\n\n");
        result.append("Y = {\n");
        for (A a : this.ab.keySet()) {
            result.append(a + ": [");
            for (B b : this.ab.get(a)) {
                result.append(b + ",");
            }
            result.deleteCharAt(result.length() - 1);
            result.append("],\n");
        }
        result.delete(result.length() - 2, result.length());
        result.append("}\n");
        return result.toString();
    }

    public static void main(String[] args) {

        // foo();
        int n = 5;
        int k = 3;

         PermutationClass c = new PermutationClass(new ArrayList<Permutation>());

        ArrayList<Permutation> sk = new ArrayList<Permutation>();
        for (Permutation p : new Permutations(c, k)) {
            sk.add(p);
        }
        // System.out.println(sk);

        ArrayList<Permutation> sn = new ArrayList<Permutation>();
        for (Permutation p : new Permutations(c, n)) {
            sn.add(p);
        }

        HashMap<Permutation, Collection<Permutation>> cnk = new HashMap<>();
        for (Permutation p : sn) {
            HashSet<Permutation> cp = new HashSet<>();
            for (Permutation q : PermUtilities.subpermutations(p)) {
                if (q.length() == k) {
                    cp.add(q);
                }
            };
            cnk.put(p, cp);
        }

        Cover<Permutation, Permutation> cov = new Cover(cnk);

        ArrayList<ArrayList<Permutation>> sols = cov.solve(new ArrayList<Permutation>());
        System.out.println(sols.size() + " solutions");
//        for (ArrayList<Permutation> ps : sols) {
//            ArrayList<Permutation> basis = new ArrayList<Permutation>();
//        
//       
//        }
        //System.out.println(c.toPythonInput());
//        for(ArrayList<Permutation> ps : c.exactCover(new ArrayList<Permutation>(), new HashSet<Permutation>(sn), new ArrayDeque<Permutation>(sk))) {
//            System.out.println(ps);
        // }
    }

    public static void foo() {
        int n = 4;
        int k = 3;
        
        
        PermutationClass c = new PermutationClass("312");

        ArrayList<Permutation> sk = new ArrayList<Permutation>();
        for (Permutation p : new Permutations(c, k)) {
            sk.add(p);
        }
        // System.out.println(sk);

        ArrayList<Permutation> sn = new ArrayList<Permutation>();
        for (Permutation p : new Permutations(c, n)) {
            sn.add(p);
        }

        HashMap<Permutation, Collection<Permutation>> cnk = new HashMap<>();
        
            for (Permutation p : sn) {
                HashSet<Permutation> cp = new HashSet<>();
                for (Permutation q : PermUtilities.subpermutations(p)) {
                    if (q.length() == k) {
                        cp.add(q);
                    }
                };
                cnk.put(p, cp);
            }
            Cover<Permutation, Permutation> cov = new Cover(cnk);
            ArrayList<Permutation> s = new ArrayList<Permutation>();
            ArrayList<ArrayList<Permutation>> sols = cov.solve(new ArrayList<Permutation>());
        

    }

    private B minSize() {
        B result = null;
        int s = Integer.MAX_VALUE;
        for (B b : ba.keySet()) {
            int bs = 0;
            for (A a : ba.get(b)) {
                if (available.contains(a)) {
                    bs++;
                }
            }
            if (bs < s) {
                result = b;
                s = bs;
            }
        }
        return result;
    }

}
