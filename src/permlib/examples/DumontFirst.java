/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.property.AvoidsFromRight;
import permlib.property.PermProperty;

/**
 *
 * @author MichaelAlbert
 */
public class DumontFirst implements PermProperty {

    static final DumontFirst DUMONT = new DumontFirst();
    static PrintWriter out;
    static int count;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // doClass("3421", 6);
        dfs("2143", 20);
        
    }

    private static void doClass(String string, int i) throws IOException {
        DumontFirst d1 = new DumontFirst();
        PermutationClass c = new PermutationClass(string);
        // PrintWriter out = new PrintWriter(System.out);
                 //= new PrintWriter(new BufferedWriter(new FileWriter("C" + string)));

        for (int length = 4; length <= i; length += 2) {
            int count = 0;
            for (Permutation p : new Permutations(c, length)) {
                if (d1.isSatisfiedBy(p)) {
                    System.out.println(p);
                    count++;
                }
            }
            System.out.println();
            System.out.println(length + " " + count);
        }
        // out.close();
        System.out.println();
    }

    private static void dfs(String string, int n) throws IOException {
        out = new PrintWriter(new BufferedWriter(new FileWriter("C" + string + ".txt")));
        AvoidsFromRight a = new AvoidsFromRight(string);
        for(int length = 4; length <= n; length += 2) {
            int[] v = new int[length];
        for (int i = 0; i < length; i++) {
            v[i] = i;
        }
        dfs(a, length, 0, v);
        System.out.println(length + " " + count);
        count = 0;
        }
        out.close();
    }

    private static void dfs(AvoidsFromRight a, int n, int i, int[] v) {
        // System.out.println("Entering with " + Arrays.toString(v) + " using " + i);
        if (n == i) {
            Permutation p = new Permutation(v);
            // System.out.println(p + " " + DUMONT.isSatisfiedBy(p));
            if (DUMONT.isSatisfiedBy(p)) {
                out.println(p);
                count++;
                return;
            }
        }
        for (int w = i; w < v.length; w++) {
            // System.out.println("w = " + w + " for " + Arrays.toString(v));
            int[] vv = Arrays.copyOf(v, v.length);
            vv[i] = v[w];
            vv[w] = v[i];
            // System.out.println(Arrays.toString(vv) + " " + DUMONT.isSatisfiedBy(vv, w));
            if (DUMONT.isSatisfiedBy(vv, i) && a.isSatisfiedBy(Arrays.copyOf(vv, i + 1), 1)) {
                dfs(a,n,i+1,vv);
            }
        }
    }

    @Override
    public boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p.elements);
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        // Even values start descents
        // Odd values start ascents (or are final)
        if (values[values.length - 1] % 2 != 0) {
            return false;
        }
        for (int i = 0; i < values.length - 1; i++) {
            if (values[i] % 2 == 1) {
                if (values[i + 1] > values[i]) {
                    return false;
                }
            } else {
                if (values[i + 1] < values[i]) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isSatisfiedBy(int[] values, int m) {
        // Even values start descents
        // Odd values start ascents (or are final)
        // if (values[values.length-1] % 2 != 0) return false;
        for (int i = 0; i < m; i++) {
            if (values[i] % 2 == 1) {
                if (values[i + 1] > values[i]) {
                    return false;
                }
            } else {
                if (values[i + 1] < values[i]) {
                    return false;
                }
            }
        }
        return true;
    }

}
