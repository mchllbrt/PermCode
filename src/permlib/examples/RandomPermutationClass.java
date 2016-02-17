package permlib.examples;

import java.util.ArrayList;
import java.util.Random;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Investigate building a class randomly by choosing basis elements of
 * increasing length from within the class.
 *
 * @author Michael Albert
 */
public class RandomPermutationClass {

    public static final Random R = new Random();

    public static void main(String[] args) {
        
        rand321(12,5);
        
//        for (int i = 0; i < 200; i++) {
//            example01(5);
            //System.out.println();
        // }
//      // example02();

//        for (Permutation p : ul(6)) {
//            System.out.println(p);
//        }
//        System.out.println();
//        for (Permutation p : ur(6)) {
//            System.out.println(p);
//        }
//        System.out.println();
//        for (Permutation p : ul(7)) {
//            System.out.println(p);
//        }
//        System.out.println();
//        for (Permutation p : ur(7)) {
//            System.out.println(p);
//        }
//        System.out.println();

//        for(Permutation p : io(7)) System.out.println(p);
    }

    /* In Av(321) randomly choose a new basis element of each length */
    public static void example01(int k) {
        long s = System.currentTimeMillis();
        // System.out.println("Seed: " + s);
        R.setSeed(s);
        ArrayList<Permutation> basis = new ArrayList<>();
        basis.add(new Permutation("321"));
        for (int n = k; n < 15; n++) {
            int count = 0;
            PermutationClass c = new PermutationClass(basis);
            Permutation newBasis = new Permutation(n);
            boolean hasSimple = false;
            boolean hasIO = true;
            for (Permutation p : io(n)) {
                hasIO &= c.containsPermutation(p);
            }
            if (!hasIO) {
                System.out.println("IO fail");
                return;
            }
            if (n >= 5) {
                boolean hasUR = false;
                for (Permutation p : ur(n)) {
                    hasUR |= c.containsPermutation(p);
                }
                if (!hasUR) {
                    System.out.println("UR fail");
                    return;
                }
                boolean hasUL = false;
                for (Permutation p : ul(n)) {
                    hasUL |= c.containsPermutation(p);
                }
                if (!hasUL) {
                    System.out.println("UL fail");
                    return;
                }
            }
            for (Permutation p : new Permutations(c, n)) {
                hasSimple |= PermUtilities.SIMPLE.isSatisfiedBy(p);
                count++;
                if (R.nextInt(count) == 0) {
                    newBasis = p;
                }
            }
           // System.out.println(newBasis + " " + count);
            if (!hasSimple) {
                System.out.println("Simple fail");
                return;
            }
            if (newBasis.equals(new Permutation(n))) {
                System.out.println("Monotone fail");
                return;
            }
            basis.add(newBasis);
        }
        System.out.print("Passed ");
        for(Permutation p : basis) System.out.print(p + ", ");
        System.out.println();
    }

    public static Permutation[] io(int n) {
        if (n == 4) {
            return new Permutation[]{new Permutation("2413"), new Permutation("3142")};
        }
        Permutation[] result = io(n - 1);
        if (n % 2 == 0) {
            result[0] = result[0].insert(n - 1, n - 2);
            result[1] = result[1].insert(n - 2, n - 1);
        } else {
            result[1] = result[1].insert(n - 1, n - 2);
            result[0] = result[0].insert(n - 2, n - 1);
        }
        return result;
    }

    public static Permutation[] ul(int n) {
        Permutation[] result = io(n - 1);
        result[0] = result[0].insert(0, 1);
        result[1] = result[1].insert(1, 0);
        return result;
    }

    public static Permutation[] ur(int n) {
        Permutation[] result = io(n - 1);
        if (n % 2 == 1) {
            result[0] = result[0].insert(n - 1, n - 2);
            result[1] = result[1].insert(n - 2, n - 1);
        } else {
            result[0] = result[0].insert(n - 2, n - 1);
            result[1] = result[1].insert(n - 1, n - 2);
        }
        return result;
    }

    public static void example02() {
        R.setSeed(1452644322970L);
        ArrayList<Permutation> basis = new ArrayList<>();
        basis.add(new Permutation("321"));
        for (int n = 4; n < 30; n++) {
            int count = 0;
            boolean hasSimple = false;
            PermutationClass c = new PermutationClass(basis);
            Permutation newBasis = new Permutation(n);
            for (Permutation p : new Permutations(c, n)) {
                hasSimple |= PermUtilities.SIMPLE.isSatisfiedBy(p);
                count++;
                if (R.nextInt(count) == 0) {
                    newBasis = p;
                }
            }
            System.out.println(newBasis + " " + count);
            if (newBasis.equals(new Permutation(n)) || !hasSimple) {
                System.out.println("WPO");
                break;
            }
            basis.add(newBasis);
        }
    }
    
    public static void rand321(int n, int k) {
        PermutationClass c = new PermutationClass("321");
        int count = 0;
        Permutation[] choices = new Permutation[k];
        for (Permutation p : new Permutations(c,n)) {
            count++;
            int i = R.nextInt(count);
            if (i < k) choices[i] = p;
        }
        for(int i = 0; i < k; i++) System.out.println(choices[i]);
        
    }

}
