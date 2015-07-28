/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.classes.SimplePermClass;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;
import permlib.property.Simple;
import permlib.utilities.IntPair;

/**
 *
 * @author Michael Albert
 */
public class SimplesInAv2134AndAv1234 {

    public static void main(String[] args) {
        SimplePermClass s2134 = new SimplePermClass(new Permutation("2134"));
        SimplePermClass s1234 = new SimplePermClass(new Permutation("1234"));
        HereditaryProperty a213 = AvoidanceTest.getTest("213");
        HereditaryProperty a123 = AvoidanceTest.getTest("123");
        int n = 10;
        int k = 5;
        HashMap<Permutation, ArrayList<ArrayList<IntPair>>> d2134 = dataForClass(s2134, n);
        HashMap<Permutation, ArrayList<ArrayList<IntPair>>> d1234 = dataForClass(s1234, n);
        for (Permutation p : d2134.keySet()) {
            if (!a123.isSatisfiedBy(p) && p.length() == k && p.at(0) == k-1) {
                System.out.println(p);
                for(Permutation q : new Permutations(new PermutationClass("123"),k)) {
                    if (d1234.containsKey(q) && !a213.isSatisfiedBy(q) && q.at(0) == k-1) {
                        if (d1234.get(q).containsAll(d2134.get(p))) {
                            System.out.println(" " + q);
                        }
                    }
                }
            }
        }
    }

    public static void doClass(SimplePermClass cl, int n) {
        HashMap<Permutation, Integer> skeletonCounts = new HashMap<>();
        for (Permutation s : cl.getPerms(n)) {
            Permutation skel = minsAndMaxes(s);
            if (!skeletonCounts.containsKey(skel)) {
                skeletonCounts.put(skel, 0);
            }
            skeletonCounts.put(skel, skeletonCounts.get(skel) + 1);
        }
        Permutation[] skels = skeletonCounts.keySet().toArray(new Permutation[0]);
        Arrays.sort(skels);
        for (Permutation skel : skels) {
            System.out.println(skel + " " + skeletonCounts.get(skel));
        }
    }

    public static void doMaxPos(SimplePermClass cl, int n) {
        HashMap<String, Integer> maxPositionCounts = new HashMap<>();
        for (Permutation s : cl.getPerms(n)) {
            String mp = asString(rightLeftMaxPos(s));
            if (!maxPositionCounts.containsKey(mp)) {
                maxPositionCounts.put(mp, 0);
            }
            maxPositionCounts.put(mp, maxPositionCounts.get(mp) + 1);
        }
        String[] maxPositions = maxPositionCounts.keySet().toArray(new String[0]);
        Arrays.sort(maxPositions);
        for (String maxPos : maxPositions) {
            System.out.println(maxPos + " " + maxPositionCounts.get(maxPos));
        }
    }

    public static HashMap<String, Integer> getMaxPos(SimplePermClass cl, int n) {
        HashMap<String, Integer> maxPositionCounts = new HashMap<>();
        for (Permutation s : cl.getPerms(n)) {
            String mp = asString(s, rightLeftMaxPos(s));
            if (!maxPositionCounts.containsKey(mp)) {
                maxPositionCounts.put(mp, 0);
            }
            maxPositionCounts.put(mp, maxPositionCounts.get(mp) + 1);
        }
        return maxPositionCounts;
    }

    public static void doMinPos(SimplePermClass cl, int n) {
        HashMap<String, Integer> minPositionCounts = new HashMap<>();
        for (Permutation s : cl.getPerms(n)) {
            String mp = asString(leftRightMinPos(s));
            if (!minPositionCounts.containsKey(mp)) {
                minPositionCounts.put(mp, 0);
            }
            minPositionCounts.put(mp, minPositionCounts.get(mp) + 1);
        }
        String[] minPositions = minPositionCounts.keySet().toArray(new String[0]);
        Arrays.sort(minPositions);
        for (String minPos : minPositions) {
            System.out.println(minPos + " " + minPositionCounts.get(minPos));
        }
    }

    public static void doMinMaxPos(SimplePermClass cl, int n) {
        HashMap<String, Integer> maxPositionCounts = new HashMap<>();
        for (Permutation s : cl.getPerms(n)) {
            String mp = asString(minAndMaxPositions(s));
            if (!maxPositionCounts.containsKey(mp)) {
                maxPositionCounts.put(mp, 0);
            }
            maxPositionCounts.put(mp, maxPositionCounts.get(mp) + 1);
        }
        String[] maxPositions = maxPositionCounts.keySet().toArray(new String[0]);
        Arrays.sort(maxPositions);
        for (String maxPos : maxPositions) {
            System.out.println(maxPos + " " + maxPositionCounts.get(maxPos));
        }
    }

    public static void doMaxCount(SimplePermClass cl, int n) {
        int[] maxCounts = new int[n + 1];
        for (Permutation s : cl.getPerms(n)) {
            maxCounts[maxCount(s)]++;
        }
        System.out.println(Arrays.toString(maxCounts));
    }

    public static void doMaxDeletable(SimplePermClass cl, int n) {
        int[] maxCounts = new int[n + 1];
        for (Permutation s : cl.getPerms(n)) {
            maxCounts[maxDeletable(s)]++;
        }
        System.out.println(Arrays.toString(maxCounts));
    }

    public static int maxDeletable(Permutation q) {
        Simple s = new Simple();
        int result = q.length() - 1;
        while (result >= 0 && !s.isSatisfiedBy(PermUtilities.delete(q, indexOf(result, q)))) {
            result--;
        }

        return result;
    }

    // Returns the array of positions for the RLMAx of q
    public static boolean[] rightLeftMaxPos(Permutation q) {
        boolean[] maxPos = new boolean[q.length()];
        int rlMax = Integer.MIN_VALUE;
        for (int i = q.length() - 1; i >= 0; i--) {
            if (q.elements[i] > rlMax) {
                maxPos[i] = true;
                rlMax = q.elements[i];
            }
        }
        return maxPos;
    }

    public static ArrayList<IntPair> rightLeftMaxPositionsAndValues(Permutation q) {
        boolean[] pos = rightLeftMaxPos(q);
        ArrayList<IntPair> result = new ArrayList<>();
        result.add(new IntPair(-1, q.length()));
        for (int i = 0; i < pos.length; i++) {
            if (pos[i]) {
                result.add(new IntPair(i, q.at(i)));
            }
        }
        return result;
    }

    public static String asString(ArrayList<IntPair> rlm) {
        int n = rlm.get(0).getSecond();
        StringBuilder result = new StringBuilder();
        int i = 0;
        for (IntPair pair : rlm) {
            while (i < pair.getFirst()) {
                result.append('.');
                i++;
            }
            if (pair.getFirst() >= 0) {
                result.append(pair.getSecond());
                i++;
            }
        }
        return result.toString();

    }

    public static HashMap<Permutation, ArrayList<ArrayList<IntPair>>> dataForClass(SimplePermClass s, int n) {
        HashMap<Permutation, ArrayList<ArrayList<IntPair>>> result = new HashMap<>();
        for (Permutation q : s.getPerms(n)) {
            Permutation lowq = nonRLMaxPattern(q);
            ArrayList<IntPair> highq = rightLeftMaxPositionsAndValues(q);
            if (!result.containsKey(lowq)) {
                result.put(lowq, new ArrayList<ArrayList<IntPair>>());
            }
            result.get(lowq).add(highq);
        }
        return result;
    }

    public static Permutation nonRLMaxPattern(Permutation q) {
        boolean[] pos = rightLeftMaxPos(q);
        for (int i = 0; i < pos.length; i++) {
            pos[i] = !pos[i];
        }
        return PermUtilities.selectByPosition(q, pos);
    }

    public static boolean[] leftRightMinPos(Permutation q) {
        boolean[] minPos = new boolean[q.length()];
        int lrMin = Integer.MAX_VALUE;
        for (int i = 0; i < q.length(); i++) {
            if (q.elements[i] < lrMin) {
                minPos[i] = true;
                lrMin = q.elements[i];
            }
        }
        return minPos;

    }

    public static int maxCount(Permutation q) {
        int result = 0;
        int rlMax = Integer.MIN_VALUE;
        for (int i = q.length() - 1; i >= 0; i--) {
            if (q.elements[i] > rlMax) {
                result++;
                rlMax = q.elements[i];
            }
        }
        return result;

    }

    // Returns the permutation consisting of the LRMin and RLMax of q
    public static Permutation minsAndMaxes(Permutation q) {

        return PermUtilities.selectByPosition(q, minAndMaxPositions(q));

    }

    public static boolean[] minAndMaxPositions(Permutation q) {
        boolean[] toKeep = new boolean[q.length()];
        int lrMin = Integer.MAX_VALUE;
        for (int i = 0; i < q.length(); i++) {
            toKeep[i] = q.elements[i] < lrMin;
            lrMin = (q.elements[i] < lrMin) ? q.elements[i] : lrMin;
        }
        int rlMax = Integer.MIN_VALUE;
        for (int i = q.length() - 1; i >= 0; i--) {
            if (q.elements[i] > rlMax) {
                toKeep[i] = true;
                rlMax = q.elements[i];
            }
        }
        return toKeep;

    }

    private static String asString(boolean[] maxPos) {
        StringBuilder result = new StringBuilder();
        for (boolean b : maxPos) {
            result.append((b) ? '*' : '.');
        }
        return result.toString();
    }

    private static String asString(Permutation p, boolean[] maxPos) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < maxPos.length; i++) {
            result.append((maxPos[i]) ? "" + p.elements[i] : '.');
        }
        return result.toString();
    }

    private static int indexOf(int v, Permutation q) {
        for (int i = 0; i < q.length(); i++) {
            if (q.elements[i] == v) {
                return i;
            }
        }
        return -1;
    }

    private static boolean hasCatIn2134(Permutation s, int index) {
        Permutation p = s.window(index, s.length(), s.elements[index], s.length());
        if (!decreasing(p)) {
            return false;
        }
        p = s.window(0, index, 0, s.elements[index]);
        if (!increasing(p)) {
            return false;
        }
        return true;
    }

    private static boolean hasCatIn1234(Permutation s, int index) {
        Permutation p = s.window(index, s.length(), s.elements[index], s.length());
        if (!decreasing(p)) {
            return false;
        }
        Permutation q = s.window(0, index, 0, s.elements[index]);
        if (!decreasing(q)) {
            return false;
        }
        if (p.length() > 0 && q.length() > 0) {
            return false;
        }
        return true;
    }

    private static boolean decreasing(Permutation p) {
        for (int i = 1; i < p.length(); i++) {
            if (p.elements[i] > p.elements[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static boolean increasing(Permutation p) {
        for (int i = 1; i < p.length(); i++) {
            if (p.elements[i] < p.elements[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static void doHasCat(int n) {
        SimplePermClass s2143 = new SimplePermClass(new Permutation("2134"));
        SimplePermClass s1234 = new SimplePermClass(new Permutation("1234"));
        for (Permutation s : s2143.getPerms(n)) {
            System.out.print(s + ": ");
            for (int i = 0; i < s.length(); i++) {
                if (hasCatIn2134(s, i)) {
                    System.out.print(i + " ");
                }
            }
            System.out.println();
        }
    }

    private static void doBoth(int n) {
        SimplePermClass s2134 = new SimplePermClass(new Permutation("2134"));
        SimplePermClass s1234 = new SimplePermClass(new Permutation("1234"));
        HashMap<String, Integer> m2134 = getMaxPos(s2134, n);
        HashMap<String, Integer> m1234 = getMaxPos(s1234, n);
        HashSet<String> allmp = new HashSet<>();
        allmp.addAll(m2134.keySet());
        allmp.addAll(m1234.keySet());
        String[] mps = allmp.toArray(new String[0]);
        Arrays.sort(mps);
        for (String mp : mps) {
            System.out.print(mp + " ");
            if (m2134.containsKey(mp)) {
                System.out.print((m2134.get(mp) + "     ").substring(0, 5));
            } else {
                System.out.print("0    ");
            }
            if (m1234.containsKey(mp)) {
                System.out.print((m1234.get(mp) + "     ").substring(0, 5));
            } else {
                System.out.print("0    ");
            }
            System.out.println();
        }

    }

    private static void findPerms(SimplePermClass s, String mp) {
        for (Permutation q : s.getPerms(mp.length())) {
            if (mp.equals(asString(q, rightLeftMaxPos(q)))) {
                System.out.println(q);
            }
        }
    }

    private static boolean boundaryPoint(Permutation p, int index) {

        Permutation q = p.window(index + 1, p.length(), p.elements[index] + 1, p.length());
        return (q.length() > 0) && decreasing(q);

    }

    private static Integer[] boundaryPoints(Permutation p) {
        ArrayList<Integer> bps = new ArrayList<>();
        for (int i = 0; i < p.length(); i++) {
            if (boundaryPoint(p, i)) {
                bps.add(i);
            }
        }
        return bps.toArray(new Integer[0]);
    }

    private static Integer[] lowPoints(Permutation p) {
        Integer[] bps = boundaryPoints(p);
        ArrayList<Integer> lps = new ArrayList<>();
        for (int i = 0; i < p.length(); i++) {
            if (lowPoint(p, bps, i)) {
                lps.add(i);
            }
        }
        return lps.toArray(new Integer[0]);

    }

    private static boolean lowPoint(Permutation p, Integer[] bps, int i) {
        int j = 0;
        while (j < bps.length && i > bps[j]) {
            j++;
        }
        if (j == bps.length) {
            return false;
        }
        return p.elements[i] < p.elements[bps[j]];
    }

    private static Permutation doMap(Permutation p) {
        Integer[] lps = lowPoints(p);
        int[] elements = Arrays.copyOf(p.elements, p.elements.length);
        int[] lpv = new int[lps.length];
        for (int i = 0; i < lpv.length; i++) {
            lpv[i] = p.elements[lps[i]];
        }
        Arrays.sort(lpv);
        int j = lpv.length - 1;
        for (int i = 0; i < lps.length; i++) {
            elements[lps[i]] = lpv[j];
            j--;
        }

        return new Permutation(elements);
    }
}
