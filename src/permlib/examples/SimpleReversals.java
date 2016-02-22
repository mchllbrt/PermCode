package permlib.examples;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.SimplePermClass;

/**
 * What is the length of the skeleton when we reverse the identity randomly until
 * we are no longer plus or minus decomposable?
 *
 * @author MichaelAlbert
 */
public class SimpleReversals {

    static Random R = new Random();
    static HashMap<Permutation, Integer> counts = new HashMap<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        doMany();
    }
    
    public static void doMany() {
        counts.clear();
        int n = 30;
        int trials = 1000000;
        int[] lengths = new int[n+1];
        for (int i = 0; i < trials; i++) {
            doOne(n, lengths);
            
        }
        SimplePermClass s = new SimplePermClass();
        HashMap<Permutation, Integer> dist = reversalDistances(7);
        
        for(Permutation q : s.getPerms(7)) {
            if (counts.containsKey(q)) {
                System.out.println(counts.get(q) + " " + dist.get(q) + " " + Arrays.toString(q.elements));
            } else {
                System.out.println(0 + " "  + dist.get(q) + " "+ Arrays.toString(q.elements));
            }
        }
    }

    public static boolean isPlusDecomposable(int[] values) {
        int max = 0;
        for (int i = 0; i < values.length - 1; i++) {
            max = (max >= values[i]) ? max : values[i];
            if (max == i) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isMinusDecomposable(int[] values) {
        int min = values.length;
        for (int i = 0; i < values.length - 1; i++) {
            min = (min <= values[i]) ? min : values[i];
            if (min == values.length-1-i) {
                return true;
            }
        }
        return false;
    }

    public static void reverse(int[] values, int low, int high) {
        high--;
        while (low < high) {
            int t = values[low];
            values[low++] = values[high];
            values[high--] = t;
        }
    }

    public static void randomReverse(int[] values) {
        int i = R.nextInt(values.length + 1);
        int j = R.nextInt(values.length + 1);
        int low = (i <= j) ? i : j;
        int high = (i >= j) ? i : j;
        reverse(values, low, high);
    }

    public static int[] rotate(int[] source, int pos, int val) {
        int[] result = new int[source.length];
        for (int i = 0; i < result.length; i++) {
            result[(i + pos) % result.length] = (source[i] + val) % result.length;
        }
        return result;
    }

    public static boolean isToricSimple(Permutation p) {
        int[] v = p.elements;
        for (int i = 0; i < v.length; i++) {
            for (int j = 0; j < v.length; j++) {
                Permutation q = new Permutation(rotate(v, i, j), true);
                System.out.println(q);
                if (!PermUtilities.isSimple(q)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void doOne(int length, int[] lengths) {
        int[] v = new Permutation(length).elements;
        // System.out.println(Arrays.toString(v));
        int count = 0;
        while (isPlusDecomposable(v) || isMinusDecomposable(v)) {
            count++;
            randomReverse(v);
        }
        // System.out.println(Arrays.toString(v));
        Permutation p = new Permutation(v, true);
        Permutation q = PermUtilities.skeleton(p);
        lengths[q.length()]++;
        if (!counts.containsKey(q)) {
            counts.put(q, 0);
        }
        counts.put(q, counts.get(q)+1);
        //System.out.println(q.length() + " " + count + " " + Arrays.toString(q.elements));
    }
    
    public static HashMap<Permutation, Integer> reversalDistances(int n) {
        HashMap<Permutation, Integer> result = new HashMap();
        ArrayDeque<Permutation> q = new ArrayDeque();
        result.put(new Permutation(n), 0);
        q.add(new Permutation(n));
        while (!q.isEmpty()) {
            Permutation p = q.poll();
            for(int i = 0; i < n; i++) {
                for(int j = i+2; j <= n; j++) {
                    int[] qe = Arrays.copyOf(p.elements, n);
                    reverse(qe, i, j);
                    Permutation pp = new Permutation(qe, true);
                    if (!result.containsKey(pp)) {
                        result.put(pp, result.get(p)+1);
                        q.add(pp);
                    }
                }
            }
        }
        
        return result;
    }
    
    
}
