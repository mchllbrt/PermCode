package permlab.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author MichaelAlbert
 */
public class MultiplicityFree {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        int[] e = {1,0,3,2,5,4};
////        System.out.println(vexillarity(e));
//        System.out.println("Vex = " + vexillarity(e));
//        System.out.println(isMultiplicityFree(e));
        for(int i = 0; i < 5040; i++) {
            int[] elements = decode(i, 7);
//            System.out.println("A " + Arrays.toString(elements));
            if (!isMultiplicityFree(elements)) System.out.println(Arrays.toString(decode(i,7)));
        }
    }
    static final int MAX_LENGTH = 200;
    static final int[] max3s = new int[MAX_LENGTH];
    static final int[] largeValues = new int[MAX_LENGTH];
    static final int[] smallValues = new int[MAX_LENGTH];
    static int[] stack;
    static int stackTop;
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

    public static int vexillarity(int[] elements) {
        aq.clear();
        aq.add(elements);
        int leaves = 0;
        while (!aq.isEmpty()) {
//            System.out.println(Arrays.toString(aq.peekFirst()));
            Collection<int[]> children = children(aq.poll());
            if (children.isEmpty()) {
                leaves++;
            } else {
                aq.addAll(children);
            }

        }
        return leaves;
    }

    static boolean isMultiplicityFree(int[] elements) {
        int v = vexillarity(elements);
        int inv = inversions(elements);
        if (inv == 0) return true;
        HashSet<Tableau> tableaux = new HashSet<Tableau>();
        HashSet<AW> shapes = new HashSet<AW>();
        createStack(inv);
        int reducedWordCount = 0;
        int nextPush = 0;
        while (true) {
//            System.out.println(Arrays.toString(elements) + " " + stackTop + " " + nextPush + " " + reducedWordCount);
            if (stackTop == inv - 1) {
                // Now we need to compute the tableau associated with the stack
                // Add it to the collected tableaux 
//                System.out.println("Added word");
//                System.out.println(Arrays.toString(stack));
//                Tableau t = new Tableau(stack);
//                System.out.println(t);
                Tableau t = new Tableau(stack);
                if (!tableaux.contains(t)) {
                    AW st = new AW(t.shape());
                    if (shapes.contains(st)) return false;
                    tableaux.add(t);
                    shapes.add(st);
                }
//                System.out.println();
                
                nextPush = pop(elements);
//                System.out.println("A" + " " + Arrays.toString(elements));
                continue;
            }
            while (nextPush < elements.length - 1 && elements[nextPush] < elements[nextPush + 1]) {
                nextPush++;
            }
            if (nextPush >= elements.length - 1) {
                nextPush = pop(elements);
//                System.out.println("B " + " " + Arrays.toString(elements));
                if (nextPush == Integer.MIN_VALUE) {
                    return true;
                }
                continue;
            }
            push(nextPush, elements);
//            System.out.println("C" + " " + Arrays.toString(elements));
            nextPush = 0;
        }
    }

    static void createStack(int inv) {
        stack = new int[inv];
        stackTop = -1;
    }

    static void push(int i, int[] elements) {
        stack[++stackTop] = i;
        int t = elements[i];
        elements[i] = elements[i + 1];
        elements[i + 1] = t;
    }

    static int pop(int[] elements) {
        if (stackTop < 0) {
            return Integer.MIN_VALUE;
        }
        int i = stack[stackTop--];
        int t = elements[i];
        elements[i] = elements[i + 1];
        elements[i + 1] = t;
        return i + 1;
    }

    public static int inversions(int[] elements) {
        int result = 0;
        for (int i = 0; i < elements.length; i++) {
            for (int j = i + 1; j < elements.length; j++) {
                if (elements[i] > elements[j]) {
                    result++;
                }
            }
        }
        return result;
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

    static class Tableau {

        ArrayList<Row> rows = new ArrayList<Row>();

        Tableau(int[] elements) {
            rows.add(new Row(elements.length));
            int v = 0;
            for (int c : elements) {
                v = c;
                for (Row row : rows) {
                    v = row.bump(v);
                    if (v == Integer.MAX_VALUE) {
                        break;
                    }
                }
                if (v != Integer.MAX_VALUE) {
                    rows.add(new Row(elements.length, v));
                }
            }
        }
        
        public int[] shape() {
            int[] result = new int[rows.size()];
            for(int i = 0; i < rows.size(); i++) {
                result[i] = rows.get(i).length();
            }
            return result;
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 83 * hash + (this.rows != null ? this.rows.hashCode() : 0);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Tableau other = (Tableau) obj;

            return this.rows.equals(other.rows);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (Row row : rows) {
                result.append(row);
                result.append('\n');
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        }

        class Row {

            int[] row;

            private Row(int length) {
                row = new int[length];
                Arrays.fill(row, Integer.MAX_VALUE);
            }

            private Row(int length, int v) {
                this(length);
                row[0] = v;
            }

            private int bump(int v) {
                int i = 0;
                while (row[i] <= v) {
                    i++;
                }
                int w = row[i];
                if (i == 0 || w != v + 1 || (i > 0 && row[i-1] != v)) {
                    row[i] = v;
                }
                return w;
            }

            @Override
            public int hashCode() {
                int hash = 5;
                hash = 23 * hash + Arrays.hashCode(this.row);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final Row other = (Row) obj;
                if (!Arrays.equals(this.row, other.row)) {
                    return false;
                }
                return true;
            }

            public String toString() {
                StringBuilder result = new StringBuilder();
                for (int c : row) {
                    if (c == Integer.MAX_VALUE) {
                        break;
                    }
                    result.append(c + " ");
                }
                if (result.length() > 0) result.deleteCharAt(result.length()-1);
                return result.toString();
            }

            private int length() {
                int result = 0;
                for(int c : row) {
                    if (c == Integer.MAX_VALUE) return result;
                    result++;
                }
                return result;
            }
        }
    }
    
    static class AW{
        
       int[] elements;
       
       AW(int[] elements) {
           this.elements = elements;
       }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + Arrays.hashCode(this.elements);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final AW other = (AW) obj;
            if (!Arrays.equals(this.elements, other.elements)) {
                return false;
            }
            return true;
        }
       
       
    }
}
