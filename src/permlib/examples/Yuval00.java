package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;

/**
 *
 * @author MichaelAlbert
 */
public class Yuval00 {

    public static void main(String[] args) {
        example(new int[][] {{2,4,1,3}}, 8);
    }

    public static Permutation condensation(Permutation p) {
        ArrayList<Integer> blockEnds = new ArrayList<Integer>();
        int b = p.at(0);
        for (int i = 1; i < p.length(); i++) {
            if (p.at(i) != b + 1) {
                blockEnds.add(b);
            }
            b = p.at(i);
        }
        blockEnds.add(b);
        return new Permutation(blockEnds.toArray(new Integer[0]));
    }

    public static ArrayList<Integer> descentSet(Permutation p) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 1; i < p.length(); i++) {
            if (p.at(i) < p.at(i - 1)) {
                result.add(i);
            }
        }
        return result;
    }

    public static void twoDesInFive() {
        HashSet<Permutation> base = new HashSet<Permutation>();
        base.add(new Permutation("42135"));
        base.add(new Permutation("21354"));
        base.add(new Permutation("13542"));
        base.add(new Permutation("21435"));
        base.add(new Permutation("14325"));
        base.add(new Permutation("13254"));
        for (Permutation p : new Permutations(8)) {
            if (base.contains(condensation(p))) {
                System.out.println(Arrays.toString(p.elements));
            }
        }
    }
    
    
    public static void example(int[][] ps, int n) {
        HashSet<Permutation> base = new HashSet<Permutation>();
        for(int[] p : ps) {
            base.add(new Permutation(p));
        }
        for (Permutation p : new Permutations(n)) {
            if (base.contains(condensation(p))) {
                System.out.println(Arrays.toString(p.elements));
            }
        }
    }
}
