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
        doMany(true);
    }
    
    public static void doMany() {
        doMany(false);
    }
    
    public static void doMany(boolean atEnd) {
        counts.clear();
        int n = 100;
        int trials = 1000000;
        int[] lengths = new int[n+1];
        for (int i = 0; i < trials; i++) {
            if (atEnd) {
                doOneAtEnds(n, lengths);
            } else {
                doOne(n,lengths);
            }
        }
        for(int i = 0; i < lengths.length; i++) {
            System.out.println(i + " " + lengths[i]);
        }
        
    }
    
    public static void doOneAtEnds(int length, int[] lengths) {
        int[] v = new Permutation(length).elements;
        int count = 0;
        while (isPlusDecomposable(v) || isMinusDecomposable(v)) {
            count++;
            randomReverseAtEnd(v);
        }
        // System.out.println(Arrays.toString(v));
        Permutation p = new Permutation(v, true);
        Permutation q = PermUtilities.skeleton(p);
        lengths[q.length()]++;
        if (!counts.containsKey(q)) {
            counts.put(q, 0);
            // System.out.println(q.length() + " " + count);
        }
        counts.put(q, counts.get(q)+1);
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
            // System.out.println(q.length() + " " + count);
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

    private static void randomReverseAtEnd(int[] values) {
        int mid = 1+R.nextInt(values.length-1);
        if (R.nextBoolean()) {
            reverse(values, 0, mid);
        } else {
            reverse(values, mid, values.length);
        }
    }
    
    
}
