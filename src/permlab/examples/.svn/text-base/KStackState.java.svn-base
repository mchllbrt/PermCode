package permlab.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.UniversalPermClass;

/**
 * Represents the state of a machine consisting of an infinite stack and a 
 * k-stack in series, in the midst of a sorting sequence.
 * 
 * @author Michael Albert
 */
public class KStackState {

    public static final int EMPTY = -1;
    int nextOut;
    String operationSequence;
    int[] inputAndStack;
    int stackHead;
    int inputHead;
    int[] smallStack;
    int smallStackHead;

    public KStackState(int k) {
        smallStack = new int[k];
        smallStackHead = -1;
    }

    public KStackState(int k, int[] input) {
        this(k);
        nextOut = 0;
        inputAndStack = new int[input.length];
        System.arraycopy(input, 0, inputAndStack, 0, input.length);
        stackHead = -1;
        inputHead = 0;
        operationSequence = "";
    }

    public KStackState mu() {
        if (smallStackHead < 0 || smallStack[smallStackHead] != nextOut || noMu()) {
            return null;
        }
        KStackState result = this.copy();
        result.smallStackHead--;
        result.nextOut++;
        result.operationSequence += "M";
        return result;
    }

    public KStackState lambda() {
        if (noLambda()) {
            return null;
        }

        KStackState result = this.copy();
        result.smallStackHead++;
        result.smallStack[result.smallStackHead] = inputAndStack[stackHead];
        result.stackHead--;
        result.operationSequence += "L";
        return result;
    }

    public KStackState tau() {
        if (noTau() || stackHead < 0 || inputAndStack[stackHead] != nextOut || noSpace()) {
            return null;
        }
        KStackState result = this.copy();
        result.nextOut++;
        result.operationSequence += "T";
        result.stackHead--;
        return result;
    }

    public KStackState rho() {
        if (inputHead >= inputAndStack.length) {
            return null;
        }
        KStackState result = this.copy();
        int v = inputAndStack[inputHead];
        result.stackHead++;
        result.inputAndStack[result.stackHead] = v;
        result.inputHead++;
        result.operationSequence += "R";
        return result;
    }

    private KStackState copy() {
        KStackState result = new KStackState(this.smallStack.length);
        result.nextOut = this.nextOut;
        result.operationSequence = this.operationSequence;
        result.inputAndStack = new int[this.inputAndStack.length];
        System.arraycopy(this.inputAndStack, 0, result.inputAndStack, 0, this.inputAndStack.length);
        result.stackHead = this.stackHead;
        result.inputHead = this.inputHead;
        System.arraycopy(this.smallStack, 0, result.smallStack, 0, result.smallStack.length);
        result.smallStackHead = this.smallStackHead;
        return result;
    }

    public boolean isSorted() {
        return nextOut == inputAndStack.length;
    }

    public boolean finalRuleConfiguration() {

        int d = firstDyck();
        if (d < 0) {
            return false;
        }
        d = secondDyck(d);
        if (d < 0) {
            return false;
        }
        return (d > 0 && operationSequence.charAt(d - 1) == 'L');
    }

    private int firstDyck() {
        int h = 0;
        int i = operationSequence.length() - 1;
        while (i >= 0) {
            if (operationSequence.charAt(i) == 'T') {
                h++;
            } else if (operationSequence.charAt(i) == 'R') {
                h--;
            } else if (operationSequence.charAt(i) == 'L') {
                return -1;
            } else {
                return (h == 0) ? i - 1 : -1;
            }
            i--;
        }
        return -1;
    }

    private int secondDyck(int i) {
        int h = 0;
        while (i >= 0) {
            if (operationSequence.charAt(i) == 'T') {
                h++;
            } else if (operationSequence.charAt(i) == 'R') {
                if (h > 0) {
                    h--;
                } else {
                    return i;
                }
            } else {
                return -1;
            }
            i--;
        }
        return -1;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(nextOut);
        result.append(")");
        result.append("[ ");
        for (int i = smallStackHead; i >= 0; i--) {
            result.append(smallStack[i] + " ");
        }
        result.append("]");
        result.append("< ");
        for (int i = 0; i <= stackHead; i++) {
            result.append(inputAndStack[i]);
            result.append(' ');
        }
        result.append("> ");
        for (int i = inputHead; i < inputAndStack.length; i++) {
            result.append(inputAndStack[i]);
            result.append(" ");
        }
        result.append(operationSequence);
        return result.toString();
    }

    public static void main(String[] args) {
        KStackState s = new KStackState(2, new int[]{1,4,0,3,2});
        Queue<KStackState> q = new ArrayDeque<KStackState>();
                q.add(s);
                while (q.size() > 0) {
                    s = q.remove();
                    System.out.println(s);
                    if (s.isSorted()) break;
                    KStackState r = s.mu();
                    if (r != null) {
                        q.add(r);
                    }
                    r = s.tau();
                    if (r != null) {
                        q.add(r);
                    }
                    r = s.lambda();
                    if (r != null) {
                        q.add(r);
                    }
                    r = s.rho();
                    if (r != null) {
                        q.add(r);
                    }
                }
            }

public void foo() {
    for (int n = 5; n < 6; n++) {
            int count = 0;
            for (Permutation p : new UniversalPermClass(n)) {
                boolean sorted = false;
                // System.out.println(p);
                ArrayList<String> sortings = new ArrayList<String>();
                KStackState s = new KStackState(2, p.elements);
                Queue<KStackState> q = new ArrayDeque<KStackState>();
                q.add(s);
                while (q.size() > 0) {
                    s = q.remove();
                    if (s.isSorted()) {
                        // System.out.println(p + "\n\n" + toPSTricks(s.operationSequence));
                        count++;
                        sorted = true;
                        break;
                        // sortings.add(s.operationSequence);
                    }
                    // System.out.println(s);
                    KStackState r = s.mu();
                    if (r != null) {
                        q.add(r);
                    }
                    r = s.tau();
                    if (r != null) {
                        q.add(r);
                    }
                    r = s.lambda();
                    if (r != null) {
                        q.add(r);
                    }
                    r = s.rho();
                    if (r != null) {
                        q.add(r);
                    }
                }
                if (!sorted) System.out.println(p + " " + p.inverse());
//            if (sortings.size() > 1) {
//                System.out.println(p);
//                System.out.println();
//                for (String ss : sortings) {
//                    System.out.println(ss);
//                    System.out.println();
//                    System.out.println(toPSTricks(ss));
//                }
//            }
            }
            System.out.println(n + " " + count);
        }

}    

    private boolean noSpace() {
        return smallStackHead >= smallStack.length - 1;
    }

    private boolean noMu() {
        return operationSequence.endsWith("L") || operationSequence.endsWith("R") || doubleDyck();
    }

    private boolean doubleDyck() {
        int h = -1;
        int i = operationSequence.length() - 1;
        while (i >= 0) {
            if (operationSequence.charAt(i) == 'L') {
                h++;
                if (h == 0) {
                    break;
                }
            } else if (operationSequence.charAt(i) == 'M') {
                h--;
            } else if (operationSequence.charAt(i) == 'R') {
                return false;
            }
            i--;
        }
        if (h != 0) {
            return false;
        }
        i--;
        if (i < 0 || operationSequence.charAt(i) != 'L') {
            return false;
        }
        h = 1;
        i--;
        while (h > 0 && i >= 0) {
            if (operationSequence.charAt(i) == 'L') {
                h++;
            } else if (operationSequence.charAt(i) == 'R') {
                h--;
            } else {
                return false;
            }
            i--;
        }
        return h == 0;
    }

    private boolean noTau() {
        if (operationSequence.endsWith("RL") || operationSequence.endsWith("RRLL") || finalRuleConfiguration()) {
            return true;
        }
        int h = 1;
        for (int i = operationSequence.length() - 1; i >= 0; i--) {
            if (operationSequence.charAt(i) == 'T') {
                h++;
            } else if (operationSequence.charAt(i) == 'R') {
                h--;
            } else if (operationSequence.charAt(i) == 'L' && h == 0) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String toPSTricks(String opSeq) {
        return toPSTricks(opSeq, 4, -3);
    }

    public static String toPSTricks(String opSeq, int max, int min) {
        StringBuilder result = new StringBuilder();
        result.append("\\begin{pspicture}(0,");
        result.append(2 * min);
        result.append(")(");
        result.append(2 * opSeq.length());
        result.append(",");
        result.append(2 * max);
        result.append(")\n");
        result.append("\\psline(0,0)(");
        result.append(2 * opSeq.length());
        result.append(",0)\n");
        ArrayList<Integer> pathHeights = new ArrayList<Integer>();
        int t = 0;
        for (char c : opSeq.toCharArray()) {
            switch (c) {
                case 'R':
                    doRho(pathHeights, result, t);
                    break;
                case 'L':
                    doLambda(pathHeights, result, t);
                    break;
                case 'M':
                    doMu(pathHeights, result, t);
                    break;
                case 'T':
                    doTau(pathHeights, result, t);
                    break;
                default:
                    break;
            }
            t += 2;
        }
        result.append("\\end{pspicture}\n");
        return result.toString();
    }

    private static void doRho(ArrayList<Integer> pathHeights, StringBuilder s, int t) {
        for (int i = 0; i < pathHeights.size(); i++) {
            int h = pathHeights.get(i);
            if (h > 0) {
                s.append("\\psline(");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 2);
                s.append(',');
                s.append(h + 2);
                pathHeights.set(i, h + 2);
                s.append(")\n");
            } else {
                s.append("\\psline[linecolor=red](");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 2);
                s.append(',');
                s.append(h);
                s.append(")\n");
            }
        };
        s.append("\\psline(");
        s.append(t + 1);
        s.append(',');
        s.append(0);
        s.append(")(");
        s.append(t + 2);
        s.append(',');
        s.append(1);
        pathHeights.add(0, 1);
        s.append(")\n");
    }

    private static void doLambda(ArrayList<Integer> pathHeights, StringBuilder s, int t) {
        for (int i = 0; i < pathHeights.size(); i++) {
            int h = pathHeights.get(i);
            if (h != 1) {
                s.append("\\psline");
                if (h < 0) {
                    s.append("[linecolor=red]");
                }
                s.append("(");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 2);
                s.append(',');
                s.append(h - 2);
                s.append(")\n");
            } else {
                s.append("\\psline(");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 1);
                s.append(',');
                s.append(h - 1);
                s.append(")\n");
                s.append("\\psline[linecolor=red](");
                s.append(t + 1);
                s.append(',');
                s.append(h - 1);
                s.append(")(");
                s.append(t + 2);
                s.append(',');
                s.append(h - 2);
                s.append(")\n");
            }
            pathHeights.set(i, h - 2);
        }
    }

    private static void doMu(ArrayList<Integer> pathHeights, StringBuilder s, int t) {
        for (int i = 0; i < pathHeights.size(); i++) {
            int h = pathHeights.get(i);
            if (h > 0) {
                s.append("\\psline(");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 2);
                s.append(',');
                s.append(h);
                pathHeights.set(i, h);
                s.append(")\n");
            } else if (h < -1) {
                s.append("\\psline[linecolor=red](");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 2);
                s.append(',');
                s.append(h + 2);
                pathHeights.set(i, h + 2);
                s.append(")\n");
            } else {
                s.append("\\psline[linecolor=red](");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 1);
                s.append(',');
                s.append(h + 1);
                pathHeights.set(i, 0);
                s.append(")\n");
            }
        }
        for (int i = 0; i < pathHeights.size(); i++) {
            if (pathHeights.get(i) == 0) {
                pathHeights.remove(i);
                break;
            }
        }
    }

    private static void doTau(ArrayList<Integer> pathHeights, StringBuilder s, int t) {
        for (int i = 0; i < pathHeights.size(); i++) {
            int h = pathHeights.get(i);
            if (h > 1) {
                s.append("\\psline(");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 2);
                s.append(',');
                s.append(h - 2);
                pathHeights.set(i, h - 2);
                s.append(")\n");
            } else if (h < 0) {
                s.append("\\psline[linecolor=red](");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 2);
                s.append(',');
                s.append(h);
                pathHeights.set(i, h);
                s.append(")\n");
            } else {
                s.append("\\psline(");
                s.append(t);
                s.append(',');
                s.append(h);
                s.append(")(");
                s.append(t + 1);
                s.append(',');
                s.append(h - 1);
                pathHeights.set(i, 0);
                s.append(")\n");
            }
        }
        for (int i = 0; i < pathHeights.size(); i++) {
            if (pathHeights.get(i) == 0) {
                pathHeights.remove(i);
                break;
            }
        }
    }

    private boolean noLambda() {
        if (smallStackHead + 1 >= smallStack.length) {
            return true;
        }
        if (stackHead < 0) {
            return true;
        }
        if (finalRuleConfiguration()) {
            return true;
        }
        return false;
    }
}
