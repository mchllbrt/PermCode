/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import permlib.PermUtilities;

/**
 *
 * @author MichaelAlbert
 */
public class TableauState {

    private static int inversions(int[] p) {
        int result = 0;
        for (int i = 0; i < p.length; i++) {
            for (int j = i + 1; j < p.length; j++) {
                if (p[i] > p[j]) {
                    result++;
                }
            }
        }
        return result;
    }

    private static void checkDeletions(int[] newElements) {
        int[] deletedCopy = Arrays.copyOf(newElements, newElements.length - 1);
        boolean hasBadDeletion = false;
        for (int deletedElement = newElements.length - 1; deletedElement >= 0; deletedElement--) {
//            System.out.println("Checking deletion " + Arrays.toString(deletedCopy));
            hasBadDeletion = !isMultiplicityFree(deletedCopy);
            if (hasBadDeletion || deletedElement == 0) {
                break;
            }
            deletedCopy[deletedElement - 1] = newElements[deletedElement];
        }
        if (!hasBadDeletion) {
            // EUREKA!
            report(Arrays.toString(newElements));
        }
    }
    private int[] p;
    private boolean[] pInv;
    private int[][] tableau;
    private int nextR;
    private int nextC;
    static final int DONE = -1;
    static final int NO_OP = 0;

    public TableauState(int[] p, int[] tableauColumnLengths) {
        this.p = Arrays.copyOf(p, p.length);
        this.tableau = new int[tableauColumnLengths.length][];
        for (int c = 0; c < tableauColumnLengths.length; c++) {
            tableau[c] = new int[tableauColumnLengths[c]];
        }
        pInv = new boolean[p.length];
        for (int i = 1; i < p.length; i++) {
            pInv[i] = p[i] < p[i - 1];
        }
        nextC = 0;
        nextR = tableau[nextC].length - 1;
        // System.out.println(Arrays.toString(pInv));
    }

    public boolean tryOp(int i) {
        if (!validOp(i)) {
            return false;
        }
        tableau[nextC][nextR] = i;
        doOp(i);
        if (nextR > 0) {
            nextR--;
        } else if (nextC < tableau.length - 1) {
            nextC++;
            nextR = tableau[nextC].length - 1;
        } else {
            nextC = DONE;
        }
        return true;
    }

    private void doOp(int i) {
        int t = p[i - 1];
        p[i - 1] = p[i];
        p[i] = t;
        pInv[i] = false;
        if (i < p.length - 1) {
            pInv[i + 1] = p[i] > p[i + 1];
        }
        if (i > 1) {
            pInv[i - 1] = p[i - 2] > p[i - 1];
        }
    }

    private int undoOp() {
        if (nextC == DONE) {
            nextR = 0;
            nextC = tableau.length - 1;
        } else if (nextR < tableau[nextC].length - 1) {
            nextR++;
        } else {
            nextC--;
            nextR = 0;
        }
        if (nextC < 0) {
            return DONE;
        }
        int i = tableau[nextC][nextR];
        int t = p[i - 1];
        p[i - 1] = p[i];
        p[i] = t;
        pInv[i] = true;
        if (i < p.length - 1) {
            pInv[i + 1] = p[i] > p[i + 1];
        }
        if (i > 1) {
            pInv[i - 1] = p[i - 2] > p[i - 1];
        }
        tableau[nextC][nextR] = NO_OP;
        return i;
    }

    private boolean validOp(int i) {
        return pInv[i]
                && (nextR == tableau[nextC].length - 1 || tableau[nextC][nextR + 1] > i)
                && (nextC == 0 || tableau[nextC - 1][nextR] <= i);
    }

    private boolean hasMultipleFillings() {
        int lastOp = lastInv() + 1;
        int fillingCount = 0;
        while (fillingCount < 2) {
            boolean didOp = false;
            for (int i = lastOp - 1; i >= 1; i--) {
                // System.out.println(i + " " + Arrays.toString(p) + " " + Arrays.toString(pInv));
                didOp = tryOp(i);
                if (didOp) {
//                    System.out.println("Did op " + i);
//                    System.out.println(this + "[" + nextC + " " + nextR + "]");
//                    System.out.println(Arrays.toString(p) + " " + Arrays.toString(pInv));
                    break;
                }
            }
            if (didOp) {
                if (nextC == DONE) {
                    fillingCount++;
                    lastOp = undoOp();
//                    System.out.println("Undid " + lastOp + " " + Arrays.toString(p));
                    continue;
                }
                lastOp = lastInv() + 1;
//                System.out.println("A break");
                continue;
            }
            lastOp = undoOp();
            if (lastOp == DONE) {
                break;
            }
//            System.out.println("Undid " + lastOp);
//            System.out.println(this + "[" + nextC + " " + nextR + "]");
//            System.out.println(Arrays.toString(p) + " " + Arrays.toString(pInv));
        }
//        System.out.println(fillingCount);
        return fillingCount > 1;
    }

    public static void main(String[] args) {

//        {
//            int[] p = {12, 3, 14, 9, 2, 5, 0, 13, 1, 11, 4, 10, 8, 6, 7};
//            for (int i = 0; i <= p.length; i++) {
//                int[] newElements = Arrays.copyOf(p, p.length + 1);
//                for (int j = 0; j < p.length; j++) {
//                    if (newElements[j] >= i) {
//                        newElements[j]++;
//                    }
//                }
//                newElements[newElements.length - 1] = i;
//                boolean foundMulti = false;
//                for (int[] l : LSLeaves(newElements)) {
//                    System.out.println(Arrays.toString(newElements));
////                    System.out.println(Arrays.toString(l));
//                    int[] rw = reducedWord(l);
////                    System.out.println(Arrays.toString(rw));
//                    int[] eg = EGTableauShape(rw);
//                    System.out.println(Arrays.toString(eg));
//                    TableauState t = new TableauState(newElements, eg);
//                    // System.out.println(Arrays.toString(p) + "\n" + t);
//                    foundMulti = t.hasMultipleFillings();
//                    if (foundMulti) {
//                        checkDeletions(newElements);
//                        break;
//                    }
//                }
//            }
//        }
        System.out.println("*******************");
        {
            int n = 13;
            long start = System.currentTimeMillis();

            for (int trial = 1; trial <= 1000; trial++) {
                int[] p = PermUtilities.randomPermutation(n).elements;
                if (!isMultiplicityFree(p)) {
                    System.out.println(Arrays.toString(p) + " inv=" + inversions(p));
                    checkDeletions(p);

                }
            }
            System.out.println(" " + (System.currentTimeMillis() - start));
        }
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int r = 0; r < tableau[0].length; r++) {
            for (int c = 0; c < tableau.length && r < tableau[c].length; c++) {
                result.append(tableau[c][r] + " ");
            }
            result.append('\n');
        }
        return result.toString();
    }

    private int lastInv() {
        int result = p.length - 1;
//        System.out.println("Looking for last inversion " + Arrays.toString(p) + " " + Arrays.toString(pInv));
        while (!pInv[result]) {
            result--;
        }
        return result;
    }

    private static int[] reducedWord(int[] p) {
        int inv = 0;
        for (int i = 0; i < p.length; i++) {
            for (int j = i + 1; j < p.length; j++) {
                if (p[i] > p[j]) {
                    inv++;
                }
            }
        }
        int[] result = new int[inv];
        for (int i = 0; i < inv; i++) {
            int j = 1;
            while (p[j] > p[j - 1]) {
                j++;
            }
            result[i] = j;
            int t = p[j];
            p[j] = p[j - 1];
            p[j - 1] = t;
        }
        return result;
    }

    static int[] EGTableauShape(int[] word) {
        ArrayList<ArrayList<Integer>> rows = new ArrayList<ArrayList<Integer>>();
        for (int w : word) {
            bump(rows, w);
        }
        return columnLengths(rows);
    }

    private static void bump(ArrayList<ArrayList<Integer>> rows, int w) {
        if (rows.size() == 0) {
            rows.add(new ArrayList<Integer>());
            rows.get(0).add(w);
            return;
        }
        for (ArrayList<Integer> row : rows) {
            int i = 0;
            while (i < row.size() && row.get(i) <= w) {
                i++;
            }
            if (i == row.size()) {
                row.add(w);
                return;
            } else if (i == 0 || i > 0 && (row.get(i - 1) != w || row.get(i) != w + 1)) {
                int t = row.get(i);
                row.set(i, w);
                w = t;
            } else {
                w++;
            }
        }
        ArrayList<Integer> newRow = new ArrayList<Integer>();
        newRow.add(w);
        rows.add(newRow);
    }

    private static int[] columnLengths(ArrayList<ArrayList<Integer>> rows) {
        int[] result = new int[rows.get(0).size()];
        for (ArrayList<Integer> row : rows) {
            for (int i = 0; i < row.size(); i++) {
                result[i]++;
            }
        }
        return result;
    }

    private static ArrayList<int[]> LSLeaves(int[] p) {
        ArrayList<int[]> result = new ArrayList<int[]>();
        LSProcess(Arrays.copyOf(p, p.length), result);
        return result;
    }

    private static void LSProcess(int[] p, ArrayList<int[]> result) {
        ArrayDeque<int[]> q = new ArrayDeque<int[]>();
        q.add(p);
        while (!q.isEmpty()) {
            int[] v = q.poll();
            if (avoids2143(v)) {
                result.add(v);
                continue;
            }
            q.addAll(children(v));
        }


    }

    public static ArrayDeque<int[]> children(int[] elements) {
        arrayChildren.clear();

        RS rs = rs(elements);
        int deltaS = 0;
        for (int j = rs.r - 1; j >= 0; j--) {
            if (elements[j] < elements[rs.s]) {
                deltaS++;
            } else {
                deltaS--;
            }
            int deltaJ = 0;
            for (int t = j + 1; t < rs.r; t++) {
                if (elements[t] > elements[j]) {
                    deltaJ++;
                } else {
                    deltaJ--;
                }
            }
            if (deltaS + deltaJ == 1) {
                int[] newElements = Arrays.copyOf(elements, elements.length);
                int v = newElements[j];
                newElements[j] = newElements[rs.s];
                newElements[rs.s] = newElements[rs.r];
                newElements[rs.r] = v;
                arrayChildren.add(newElements);
            }
        }
        if (arrayChildren.isEmpty()) {
            int[] newElements = new int[elements.length + 1];
            newElements[0] = 0;
            for (int i = 1; i < newElements.length; i++) {
                newElements[i] = elements[i - 1] + 1;
            }
            return children(newElements);
        }
        return arrayChildren;
    }

    public static RS rs(int[] elements) {
        RS result = new RS();
        int r = elements.length - 2;
        while (r >= 0 && elements[r] < elements[r + 1]) {
            r--;
        }
        result.r = r;
        int s = elements.length - 1;
        while (elements[s] > elements[r]) {
            s--;
        }
        result.s = s;
        return result;


    }

    static class RS {

        int r, s;
    }
    static final int MAX_LENGTH = 100;
    static final int[] max3s = new int[MAX_LENGTH];
    static final int[] largeValues = new int[MAX_LENGTH];
    static final int[] smallValues = new int[MAX_LENGTH];
    static final ArrayDeque<int[]> aq = new ArrayDeque<int[]>();
    static final ArrayDeque<int[]> arrayChildren = new ArrayDeque<int[]>();
    static int largeStackTop = -1;
    static int smallStackTop = -1;

    public static boolean avoids2143(int[] values) {
        java.util.Arrays.fill(max3s, Integer.MIN_VALUE);
        int t = values.length - 1;
        largeStackTop = -1;
        do {
            largeValues[++largeStackTop] = values[t];
            t--;
        } while (t > 0 && values[t] < values[t + 1]);

        if (t == 0) {
            return true; // The permutation is increasing
        }
        int max3 = Integer.MIN_VALUE;
        while (largeStackTop >= 0 && largeValues[largeStackTop] < values[t]) {
            max3 = largeValues[largeStackTop--];
        }
        largeValues[++largeStackTop] = values[t];
        max3s[t] = max3;
        t--;
        while (t > 0) { // No point for smaller t, no room for 21
            if (values[t] > max3) { // We might do better
                while (largeStackTop >= 0 && largeValues[largeStackTop] < values[t]) {
                    max3 = largeValues[largeStackTop--];
                }
                largeValues[++largeStackTop] = values[t];
            }
            max3s[t] = max3;
            t--;
        }
        smallStackTop = -1;
        t = 0;
        // The perm is not increasing so this won't overrun
        int small2 = Integer.MAX_VALUE;
        do {
            smallValues[++smallStackTop] = values[t];
            t++;
        } while (values[t - 1] < values[t]);
        while (smallStackTop >= 0 && smallValues[smallStackTop] > values[t]) {
            small2 = smallValues[smallStackTop--];
        }
        smallValues[++smallStackTop] = values[t];
        t++;
        while (t <= values.length - 2) {
            if (small2 < max3s[t]) {
                return false;
            }
            if (values[t] < small2) { // We might do better
                while (smallStackTop >= 0 && smallValues[smallStackTop] > values[t]) {
                    small2 = smallValues[smallStackTop--];
                }
                smallValues[++smallStackTop] = values[t];
            }
            t++;
        }
        return true;

    }

    public static int[] decode(long code, int n) {
        int[] result = new int[n];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = (int) (code % (i + 1));
            int t = result[j];
            result[j] = result[i];
            result[i] = t;
            code /= (i + 1);
        }
        return result;
    }

    public static void checkExtensions(int[] elements, int k) {
        if (k == 0) {
            return;
        }
        for (int i = 0; i <= elements.length; i++) {
            int[] newElements = Arrays.copyOf(elements, elements.length + 1);
            for (int j = 0; j < elements.length; j++) {
                if (newElements[j] >= i) {
                    newElements[j]++;
                }
            }
            newElements[newElements.length - 1] = i;
            if (!isMultiplicityFree(newElements)) {
                int[] deletedCopy = Arrays.copyOf(newElements, newElements.length - 1);
                boolean hasBadDeletion = false;
                for (int deletedElement = newElements.length - 1; deletedElement >= 0; deletedElement--) {
                    hasBadDeletion = !isMultiplicityFree(deletedCopy);
                    if (hasBadDeletion || deletedElement == 0) {
                        break;
                    }
                    deletedCopy[deletedElement - 1] = newElements[deletedElement];
                }
                if (!hasBadDeletion) {
                    // EUREKA!
                    report(Arrays.toString(newElements));
                }
            } else {
                checkExtensions(newElements, k - 1);
            }
        }
    }

    public static void report(String s) {
        System.out.println(s);
    }

    public static boolean isMultiplicityFree(int[] p) {

        int i = 0;
        while (i < p.length - 1 && p[i] < p[i + 1]) {
            i++;
        }
        if (i == p.length - 1) {
            return true;
        }
        for (int[] l : LSLeaves(p)) {
            TableauState t = new TableauState(p, EGTableauShape(reducedWord(l)));
            if (t.hasMultipleFillings()) {
                return false;
            }
        }
        return true;
    }
}
