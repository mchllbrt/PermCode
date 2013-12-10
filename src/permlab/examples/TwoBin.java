/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.utilities.Combinations;

/**
 *
 * @author MichaelAlbert
 */
public class TwoBin implements Comparable<TwoBin> {

    int n;
    boolean[][] a;
    boolean[][] b;
    static HashMap<TwoBin, TwoBin> isoReps = new HashMap<TwoBin, TwoBin>();
    static TwoBin[] scratch = initialiseScratch(10);

    public TwoBin(int n) {
        this.n = n;
        a = new boolean[n][n];
        b = new boolean[n][n];
    }

    public void setA(boolean[][] a) {
        this.a = a;
    }

    public void setB(boolean[][] b) {
        this.b = b;
    }

    public TwoBin sub(int[] indices) {
        TwoBin result = new TwoBin(indices.length);
        for (int i = 0; i < indices.length; i++) {
            for (int j = 0; j < indices.length; j++) {
                result.a[i][j] = this.a[indices[i]][indices[j]];
                result.b[i][j] = this.b[indices[i]][indices[j]];
            }
        }
        return result.isoRep();
    }

    public boolean avoids(Collection<TwoBin> forbidden, int k) {
        for (int[] c : new Combinations(this.n, k)) {
            putSub(c, scratch[k]);
            if (forbidden.contains(scratch[k])) return false;        
        }
        return true;
    }

    public boolean iso(TwoBin other) {
        return this.isoRep().equals(other.isoRep());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + this.n;
        hash = 17 * hash + Arrays.deepHashCode(this.a);
        hash = 17 * hash + Arrays.deepHashCode(this.b);
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
        final TwoBin other = (TwoBin) obj;
        if (this.n != other.n) {
            return false;
        }
        if (!Arrays.deepEquals(this.a, other.a)) {
            return false;
        }
        if (!Arrays.deepEquals(this.b, other.b)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(TwoBin other) {
        if (other.n != this.n) {
            return this.n - other.n;
        }
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                if (this.a[i][j] != other.a[i][j]) {
                    return this.a[i][j] ? 1 : -1;
                }
                if (this.b[i][j] != other.b[i][j]) {
                    return this.b[i][j] ? 1 : -1;
                }

            }
        }
        return 0;
    }

    public TwoBin isoRep() {
        if (isoReps.containsKey(this)) {
            return isoReps.get(this);
        }
        TwoBin result = this;
//        HashSet<TwoBin> cands = new HashSet<TwoBin>();
        for (Permutation p : new Permutations(this.n)) {
            TwoBin cand = this.apply(p.elements);
//            if (isoReps.containsKey(cand)) {
//                result = isoReps.get(cand);
//                break;
//            } else {
//                cands.add(cand);
//            }
            if (cand.compareTo(result) > 0) {
                result = cand;
            }
        }
        isoReps.put(this, result);
//        for (TwoBin cand : cands) {
//            isoReps.put(cand, result);
//        }
        return result;
    }

    private TwoBin apply(int[] e) {
        TwoBin result = new TwoBin(this.n);
        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < this.n; j++) {
                result.a[i][j] = this.a[e[i]][e[j]];
                result.b[i][j] = this.b[e[i]][e[j]];
            }
        }
        return result;
    }

    public HashSet<TwoBin> extensions() {
        HashSet<TwoBin> result = new HashSet<TwoBin>();
        for (int aCode = 0; aCode < (1 << this.n); aCode++) {
            for (int bCode = 0; bCode < (1 << this.n); bCode++) {
                TwoBin ext = new TwoBin(this.n + 1);
                for (int i = 0; i < this.n; i++) {
                    for (int j = 0; j < this.n; j++) {
                        ext.a[i][j] = this.a[i][j];
                        ext.b[i][j] = this.b[i][j];
                    }
                    ext.a[i][this.n] = ((aCode >> i) & 1) == 1;
                    ext.a[this.n][i] = !ext.a[i][this.n];
                    ext.b[i][this.n] = ((bCode >> i) & 1) == 1;
                    ext.b[this.n][i] = !ext.b[i][this.n];
                }
//                System.out.println("Ext " + ext);
//                System.out.println("Iso " + ext.isoRep());
                result.add(ext.isoRep());
            }
        }
        return result;
    }
    
    public Collection<TwoBin> isomorphs() {
        HashSet<TwoBin> result = new HashSet<TwoBin>();
        for(Permutation p : new Permutations(this.n)) {
            result.add(this.apply(p.elements));
        }
        return result;
    } 

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(n + ": ");
        for (int i = 0; i < this.n; i++) {
            for (int j = i + 1; j < this.n; j++) {
                if (a[i][j]) {
                    result.append(i + "A" + j + " ");
                }
                if (b[i][j]) {
                    result.append(i + "B" + j + " ");
                }
            }
        }
        return result.toString();
    }

    public static void main(String[] args) {

        HashSet<TwoBin>[] reps = (HashSet<TwoBin>[]) new HashSet[3];

        TwoBin a = new TwoBin(1);
        reps[0] = new HashSet<TwoBin>();
        reps[0].add(a);
        for (int i = 1; i < reps.length; i++) {
            reps[i] = new HashSet<TwoBin>();
            for (TwoBin b : reps[i - 1]) {
                reps[i].addAll(b.extensions());
            }
            System.out.println(reps[i].size());
        }

        TwoBin[] threes = new TwoBin[reps[2].size()];
        reps[2].toArray(threes);
        for (int i = 0; i < threes.length; i++) {
            System.out.println("Index: " + i + " Rep: " + threes[i]);
        }
        HashSet<TwoBin> basis = (HashSet<TwoBin>) new HashSet();
//        for (int[] basisIndices : new Combinations(12, 7)) {
//            basis.clear();
//            for (int i = 0; i < 7; i++) {
//                basis.add(threes[basisIndices[i]]);
//            }
//            int count = 0;
//            for (TwoBin four : reps[3]) {
////                System.out.println();
////                System.out.println("Testing " + t);
//                boolean inClass = true;
//                for (int[] ind : new Combinations(4, 3)) {
//                    inClass = !basis.contains(four.sub(ind));
//                    if (!inClass) {
////                        System.out.println("Failed due to " + Arrays.toString(ind) + " iso to " + t.sub(ind));
//                        break;
//                    }
//                }
//                if (inClass) {
//                    count++;
//                }
//            }
//            if (count >= 14) {
//                System.out.print(Arrays.toString(basisIndices));
//                System.out.print(" " + count);
//                count = 0;
//                for (TwoBin t : reps[4]) {
////            System.out.println();
////            System.out.println("Testing " + t);
//                    boolean inClass = true;
//                    for (int[] ind : new Combinations(t.n, 3)) {
//                        inClass = !basis.contains(t.sub(ind));
//                        if (!inClass) {
////                    System.out.println("Failed due to " + Arrays.toString(ind) + " iso to " + t.sub(ind));
//                            break;
//                        }
//                    }
//                    if (inClass) {
//                        count++;
//                    }
//                }
//                System.out.println(" " + count);
//            }
//        }

        // int[] basisIndices = {1, 3, 4, 6, 7, 9, 10};
        int[] basisIndices = {0, 1, 3, 4, 6, 9, 10};
        basis.clear();
        for (int i = 0; i < 7; i++) {
            basis.addAll(threes[basisIndices[i]].isomorphs());
        }
        int count = 0;
        HashSet<TwoBin>[] theClass = (HashSet<TwoBin>[]) new HashSet[8];
        theClass[3] = new HashSet<TwoBin>();
        for (TwoBin t : reps[2]) {
//            System.out.println();
//            System.out.println("Testing " + t);
            if (!basis.contains(t)) {
                theClass[3].add(t);
            }
        }
        for (int n = 4; n <= 5; n++) {
            theClass[n] = new HashSet<TwoBin>();
            for (TwoBin t : theClass[n - 1]) {
                for (TwoBin s : t.extensions()) {
                    if (!theClass[n].contains(s)) {
                        boolean inClass = true;
                        for (int[] ind : new Combinations(s.n, s.n - 1)) {
                            inClass = theClass[n - 1].contains(s.sub(ind));
                            if (!inClass) {
                                break;
                            }
                        }
                        if (inClass) {
                            System.out.print("*");
                            theClass[n].add(s);
                        }
                    }
                }
            }
            System.out.println();
            System.out.println(theClass[n].size());
        }

        for (TwoBin b : theClass[5]) {
            System.out.println("Extra basis: " + b);
            theClass[6] = new HashSet<TwoBin>();
            for (TwoBin t : theClass[5]) {
                for (TwoBin s : t.extensions()) {
                    if (!theClass[6].contains(s)) {
                        boolean inClass = true;
                        for (int[] ind : new Combinations(s.n, s.n - 1)) {
                            inClass = theClass[5].contains(s.sub(ind)) && !(s.sub(ind).equals(b));
                            if (!inClass) {
                                break;
                            }
                        }
                        if (inClass) {
                            System.out.print("*");
                            theClass[6].add(s);
                        }
                    }
                }
            }
            System.out.println();
            System.out.println(theClass[6].size());
        }


//        for(TwoBin t : theClass[4]) {
//            HashSet<TwoBin> covers = new HashSet<TwoBin>();
//            for (int[] ind : new Combinations(t.n, t.n - 1)) {
//                covers.add(t.sub(ind));
//                
//            }
//            System.out.println(t + " covers " + covers.size());
//        }
    }

    private static TwoBin[] initialiseScratch(int n) {
        TwoBin[] result = new TwoBin[n];
        for (int i = 0; i < n; i++) {
            result[i] = new TwoBin(i);
        }
        return result;
    }

    private void putSub(int[] c, TwoBin t) {
        for(int i = 0; i < t.n; i++) {
            for(int j = 0; j < t.n; j++) {
                t.a[i][j] = this.a[c[i]][c[j]];
                t.b[i][j] = this.b[c[i]][c[j]];
            }
         }
    }
}
