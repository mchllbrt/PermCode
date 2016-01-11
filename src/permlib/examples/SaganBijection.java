package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.utilities.BTree;

/**
 * Can we find a bijection between avoiders of 132[I_k, 1, D_m] and  of 
 * 231[I_k,1,D_m] that preserves maj?
 * 
 * First guess is that we can fix the position of the maximum.
 * 
 * @author Michael Albert
 */
public class SaganBijection {

    public static void main(String[] args) {
        checkDescendingTrees();
        // checkDesDistribution();
    }
    
    private static void checkDescendingTrees() {
        int k = 1;
        int m = 1;
        int maxN = 12;
        int[] p = new int[k+m+1];
        int[] q = new int[k+m+1];
        for(int i = 0; i < k; i++) {
            p[i] = i;
            q[i] = m + i;
        }
        p[k] = k+m;
        q[k] = k+m;
        for(int i = k+1; i < k+m+1; i++) {
            p[i] = k+m+k-i;
            q[i] = m+k-i;
        }
        PermutationClass c132 = new PermutationClass(new Permutation(p));
        PermutationClass c231 = new PermutationClass(new Permutation(q));
        for(int n = k+m+2; n < maxN; n++) {
            HashMap<BTree, Integer> t132 = new HashMap<>();
            HashMap<BTree, Integer> t231 = new HashMap<>();
            for(Permutation s : new Permutations(c132, n)) {
                BTree ts = BTree.descendingTree(s);
                if (!t132.containsKey(ts)) {
                    t132.put(ts, 0);
                }
                t132.put(ts, t132.get(ts)+1);
            }
            for(Permutation s : new Permutations(c231, n)) {
                BTree ts = BTree.descendingTree(s);
                if (!t231.containsKey(ts)) {
                    t231.put(ts, 0);
                }
                t231.put(ts, t231.get(ts)+1);
            }
            for(BTree t : t132.keySet()) {
                if (!t231.containsKey(t) || !t231.get(t).equals(t132.get(t))) {
                    System.out.println(t132.get(t));
                    System.out.println(t231.get(t));
                    System.out.println("Bad tree " + t);
                    return;
                }
            }
            
        }
        
    }
    
    private static void checkMaxPositions() {
        int k = 2;
        int m = 3;
        int maxN = 12;
        int[] p = new int[k+m+1];
        int[] q = new int[k+m+1];
        for(int i = 0; i < k; i++) {
            p[i] = i;
            q[i] = m + i;
        }
        p[k] = k+m;
        q[k] = k+m;
        for(int i = k+1; i < k+m+1; i++) {
            p[i] = k+m+k-i;
            q[i] = m+k-i;
        }
        PermutationClass c132 = new PermutationClass(new Permutation(p));
        PermutationClass c231 = new PermutationClass(new Permutation(q));
        for(int n = k+m+2; n < maxN; n++) {
            int[] a = new int[n];
            int[] b = new int[n];
            for(Permutation s : new Permutations(c132,n)) {
                a[maxPosition(s)]++;
            }
            for(Permutation s : new Permutations(c231,n)) {
                b[maxPosition(s)]++;
            }
            System.out.println(Arrays.toString(a));
            System.out.println(Arrays.toString(b));
            if (!Arrays.equals(a, b)) break;
        }
    }
    
    private static void checkDesDistribution() {
        int k = 2;
        int m = 3;
        int maxN = 12;
        int[] p = new int[k+m+1];
        int[] q = new int[k+m+1];
        for(int i = 0; i < k; i++) {
            p[i] = i;
            q[i] = m + i;
        }
        p[k] = k+m;
        q[k] = k+m;
        for(int i = k+1; i < k+m+1; i++) {
            p[i] = k+m+k-i;
            q[i] = m+k-i;
        }
        PermutationClass c132 = new PermutationClass(new Permutation(p));
        PermutationClass c231 = new PermutationClass(new Permutation(q));
        for(int n = k+m+2; n < maxN; n++) {
            HashMap<ArrayList<Integer>, Integer> a = new HashMap<>();
            HashMap<ArrayList<Integer>, Integer> b = new HashMap<>();
            for(Permutation s : new Permutations(c132, n)) {
                ArrayList<Integer> ds = descents(s);
                if (!a.containsKey(ds)) {
                    a.put(ds, 0);
                }
                a.put(ds, a.get(ds)+1);
            }
            for(Permutation s : new Permutations(c231, n)) {
                ArrayList<Integer> ds = descents(s);
                if (!b.containsKey(ds)) {
                    b.put(ds, 0);
                }
                b.put(ds, b.get(ds)+1);
            }
            for(ArrayList<Integer> d : a.keySet()) {
                if (!b.containsKey(d)) {
                    System.out.println(d + " in 132 but not 231");
                } else if (!a.get(d).equals(b.get(d))) {
                    System.out.println(d + " in 132 " + a.get(d) + " and in 231 " + b.get(d));
                }
            }
            if (b.keySet().size() != a.keySet().size()) {
                System.out.println("Different sizes");
            }
        }
        
    }
    
    private static int maxPosition(Permutation p) {
        for(int i = 0; i < p.elements.length; i++) {
            if (p.elements[i] == p.elements.length-1) return i;
        }
        return -1; // Should be unreachable
    }
    
    private static ArrayList<Integer> descents(Permutation p) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        for(int i = 0; i < p.length()-1; i++) {
            if (p.elements[i] > p.elements[i+1]) {
                result.add(i);
            }
        }
        
        return result;
    }
}
