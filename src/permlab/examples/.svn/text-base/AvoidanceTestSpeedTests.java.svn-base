package permlab.examples;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.Collection;
import permlib.classes.PermutationClass;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.UniversalPermClass;
import permlib.property.HereditaryProperty;
import permlib.utilities.PermPropertyUtilities;

/**
 *
 * @author mbelton
 */
public class AvoidanceTestSpeedTests {

    public static void printTime(Collection<Permutation> c, HereditaryProperty test, String str) {
        ThreadMXBean mx = ManagementFactory.getThreadMXBean();

        int count = 0;
        long startTime = mx.getCurrentThreadCpuTime();
        for (Permutation p : c) {
            if (test.isSatisfiedBy(p)) {
                count++;
            }
        }
        long finishTime = mx.getCurrentThreadCpuTime();
        System.out.println(str);
        System.out.println(count);
        System.out.println("That took: " + (finishTime - startTime) + "ns\n");
    }

    public static void printPermutationsTime(int length, HereditaryProperty test, String str) {
        ThreadMXBean mx = ManagementFactory.getThreadMXBean();

        int count = 0;
        long startTime = mx.getCurrentThreadCpuTime();
        for (Permutation p : new UniversalPermClass(length)) {
            if (test.isSatisfiedBy(p)) {
                count++;
            }
        }
        long finishTime = mx.getCurrentThreadCpuTime();
        System.out.println(str);
        System.out.println(count);
        System.out.println("That took: " + (finishTime - startTime) + "ns\n");
    }

    public static void main(String[] args) {

        // Special tests
        HereditaryProperty a123 = PermUtilities.avoidanceTest(new Permutation("123"), true);
        HereditaryProperty a321 = PermUtilities.avoidanceTest(new Permutation("321"), true);
        HereditaryProperty a312 = PermUtilities.avoidanceTest(new Permutation("312"), true);
        HereditaryProperty a132 = PermUtilities.avoidanceTest(new Permutation("132"), true);
        HereditaryProperty a213 = PermUtilities.avoidanceTest(new Permutation("213"), true);
        HereditaryProperty a231 = PermUtilities.avoidanceTest(new Permutation("231"), true);

        HereditaryProperty i10 = PermUtilities.avoidanceTest(PermUtilities.increasingPermutation(10), true);
        HereditaryProperty d10 = PermUtilities.avoidanceTest(PermUtilities.decreasingPermutation(10), true);

        // 5 random permutations of length 5000.
        ArrayList<Permutation> rand5 = new ArrayList<Permutation>();
        for (int i = 0; i < 4; i++) {
            rand5.add(PermUtilities.randomPermutation(5000));
        }

        // 100 Random Permutations of length 10000.
        ArrayList<Permutation> rand100 = new ArrayList<Permutation>();
        for (int i = 0; i < 100; i++) {
            rand100.add(PermUtilities.randomPermutation(10000));
        }


        // First loop for special tests, second for normal tests
        for (int j = 0; j < 2; j++) {

            // All permutations of length 12
            printPermutationsTime(12, a123, "L(12) | Av(123):");
            printPermutationsTime(12, a321, "L(12) | Av(321):");
            printPermutationsTime(12, a312, "L(12) | Av(312):");
            printPermutationsTime(12, a132, "L(12) | Av(132):");
            printPermutationsTime(12, a213, "L(12) | Av(213):");
            printPermutationsTime(12, a231, "L(12) | Av(231):");
            printPermutationsTime(12, i10, "L(12) | Av(increasing):");
            printPermutationsTime(12, d10, "L(12) | Av(decreasing):");

            // Slow to pass
            Collection<Permutation> testCol = new PermutationClass(new Permutation("123")).getPermsTo(12);
            printTime(testCol, a123, "L(12) Av(123) | Av(123):");

            testCol = new PermutationClass(new Permutation("321")).getPermsTo(12);
            printTime(testCol, a321, "L(12) Av(321) | Av(321):");

            testCol = new PermutationClass(new Permutation("312")).getPermsTo(12);
            printTime(testCol, a312, "L(12) Av(312) | Av(312):");

            testCol = new PermutationClass(new Permutation("132")).getPermsTo(12);
            printTime(testCol, a132, "L(12) Av(132) | Av(132):");

            testCol = new PermutationClass(new Permutation("213")).getPermsTo(12);
            printTime(testCol, a213, "L(12) Av(213) | Av(213):");

            testCol = new PermutationClass(new Permutation("231")).getPermsTo(12);
            printTime(testCol, a231, "L(12) Av(231) | Av(231):");

            testCol = new PermutationClass(new Permutation("12")).getPermsTo(12);
            printTime(testCol, i10, "L(12) Av(increasing) | Av(increasing):");

            testCol = new PermutationClass(new Permutation("21")).getPermsTo(12);
            printTime(testCol, d10, "L(12) Av(decreasing) | Av(increasing):");

            // 5 random permutations of length 5000.
            testCol = rand5;

            printTime(testCol, a123, "L(5000) 5 Random | Av(123):");
            printTime(testCol, a321, "L(5000) 5 Random | Av(321):");
            printTime(testCol, a312, "L(5000) 5 Random | Av(312):");
            printTime(testCol, a132, "L(5000) 5 Random | Av(132):");
            printTime(testCol, a213, "L(5000) 5 Random | Av(213):");
            printTime(testCol, a231, "L(5000) 5 Random | Av(231):");
            printTime(testCol, i10, "L(5000) 5 Random | Av(increasing):");
            printTime(testCol, d10, "L(5000) 5 Random | Av(decreasing):");

            // 100 random permutations of length 10000.
            testCol = rand100;

            printTime(testCol, a123, "L(10000) 5 Random | Av(123):");
            printTime(testCol, a321, "L(10000) 5 Random | Av(321):");
            printTime(testCol, a312, "L(10000) 5 Random | Av(312):");
            printTime(testCol, a132, "L(10000) 5 Random | Av(132):");
            printTime(testCol, a213, "L(10000) 5 Random | Av(213):");
            printTime(testCol, a231, "L(10000) 5 Random | Av(231):");
            printTime(testCol, i10, "L(10000) 5 Random | Av(increasing):");
            printTime(testCol, d10, "L(10000) 5 Random | Av(decreasing):");


            // Extra tests for monotone
            // Permutation upto length 50 av(__), test avoids monotone length 10.
            testCol = new PermutationClass(new Permutation("12")).getPermsTo(50);
            printTime(testCol, i10, "L(50) Av(12) | Av(increasing):");

            testCol = new PermutationClass(new Permutation("21")).getPermsTo(50);
            printTime(testCol, d10, "L(50) Av(21) | Av(decreasing):");

            System.out.println("\n-------------------\n-------------------\n\n");

            // For normal tests
            a123 = PermUtilities.avoidanceTest(new Permutation("123"), false);
            a321 = PermUtilities.avoidanceTest(new Permutation("321"), false);
            a312 = PermUtilities.avoidanceTest(new Permutation("312"), false);
            a132 = PermUtilities.avoidanceTest(new Permutation("132"), false);
            a213 = PermUtilities.avoidanceTest(new Permutation("213"), false);
            a231 = PermUtilities.avoidanceTest(new Permutation("231"), false);

            i10 = PermUtilities.avoidanceTest(PermUtilities.increasingPermutation(10), false);
            d10 = PermUtilities.avoidanceTest(PermUtilities.decreasingPermutation(10), false);
        }
    }
}
