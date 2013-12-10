package permlab.examples;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;

public class Vex {

    static int BOUND = 3;
    static final int MAX_LENGTH = 16;
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

    public static boolean vexBounded(int[] elements) {
        aq.clear();
        aq.add(elements);
        int leaves = 0;
        while (!aq.isEmpty()) {
            Collection<int[]> children = children(aq.poll());
            if (children.isEmpty()) {
                leaves++;
            } else {
                aq.addAll(children);
            }
            if (aq.size() + leaves > BOUND) {
                return false;
            }

        }
        return aq.size() + leaves <= BOUND;
    }

    public static ArrayDeque<int[]> children(int[] elements) {
        arrayChildren.clear();
        if (avoids2143(elements)) {
            return arrayChildren;
        }

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
            if (!vexBounded(newElements)) {
                int[] deletedCopy = Arrays.copyOf(newElements, newElements.length - 1);
                boolean hasBadDeletion = false;
                for (int deletedElement = newElements.length - 1; deletedElement >= 0; deletedElement--) {
                    hasBadDeletion = !vexBounded(deletedCopy);
                    if (hasBadDeletion || deletedElement == 0) {
                        break;
                    }
                    deletedCopy[deletedElement - 1] = newElements[deletedElement];
                }
                if (!hasBadDeletion) {
//                     EUREKA!
                    report(Arrays.toString(newElements));
                }
            } else {
                checkExtensions(newElements, k - 1);
            }
        }
    }

    public static int[] decode(int code, int n) {
        int[] result = new int[n];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = code % (i + 1);
            int t = result[j];
            result[j] = result[i];
            result[i] = t;
            code /= (i + 1);
        }
        return result;
    }

    public static void main(String[] args) {

        int BOUND = Integer.parseInt(args[0]);
        int n = Integer.parseInt(args[1]);
        int extension = Integer.parseInt(args[2]);
        int low = Integer.parseInt(args[3]);
        int high = Integer.parseInt(args[4]);
        report("S:" + n + ", " + low + "-" + high);
        int count = 0;
        for (int i = low; i < high; i++) {
            checkExtensions(decode(i,n), extension);
        }
        report("F:" + n + "+" + extension + ", " + low + "-" + high);
    }
    
    public static void report(String s) {
        System.out.println(s);
    }
}
