package permlab.examples;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Represents the state of a machine consisting of a stack and a two-stack
 * in series, in the course of sorting a permutation.
 * 
 * @author Michael Albert
 */
public class TwoStackState {
    
    public static final int EMPTY = -1;
    
    int nextOut;
    String operationSequence;
    int[] inputAndStack;
    int stackHead;
    int inputHead;
    int smallStackContents = EMPTY;
    
    public TwoStackState() {}
    
    public TwoStackState(int[] input) {
        nextOut = 0;
        inputAndStack = new int[input.length];
        System.arraycopy(input, 0, inputAndStack, 0, input.length);
        stackHead = -1;
        inputHead = 0;
        operationSequence = "";
    }
    
    public TwoStackState mu() {
        if (smallStackContents != nextOut) return null;
        TwoStackState result = this.copy();
        result.smallStackContents = EMPTY;
        result.nextOut++;
        result.operationSequence += "M";
        return result;
    }
    
    public TwoStackState lambda() {
        if (smallStackContents != EMPTY) return null;
        if (stackHead < 0) return null;
        TwoStackState result = this.copy();
        result.smallStackContents = inputAndStack[stackHead];
        result.stackHead--;
        result.operationSequence += "L";
        return result;
    }

    public TwoStackState tau() {
        if (stackHead < 0 || inputAndStack[stackHead] != nextOut) return null;
        TwoStackState result = this.copy();
        result.nextOut++;
        result.operationSequence += "T";
        result.stackHead--;
        return result;
    }
    
    public TwoStackState rho() {
        if (inputHead >= inputAndStack.length) return null;
        TwoStackState result = this.copy();
        int v = inputAndStack[inputHead];
        result.stackHead++;
        result.inputAndStack[result.stackHead] = v;
        result.inputHead++;
        result.operationSequence += "R";
        return result;
    }
    
    private TwoStackState copy() {
        TwoStackState result = new TwoStackState();
        result.nextOut = this.nextOut;
        result.operationSequence = this.operationSequence;
        result.inputAndStack = new int[this.inputAndStack.length];
        System.arraycopy(this.inputAndStack, 0, result.inputAndStack, 0, this.inputAndStack.length);
        result.stackHead = this.stackHead;
        result.inputHead = this.inputHead;
        result.smallStackContents = this.smallStackContents;
        return result;
    }
    
    public boolean isSorted() {
        return nextOut == inputAndStack.length;
    }
    
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("(");
        result.append(nextOut);
        result.append(")");
        result.append("[");
        if (smallStackContents != EMPTY) result.append(smallStackContents);
        result.append("]");
        result.append("< ");
        for(int i = 0; i <= stackHead; i++) {
            result.append(inputAndStack[i]);
            result.append(' ');
        }
        result.append("> ");
        for(int i = inputHead; i < inputAndStack.length; i++) {
            result.append(inputAndStack[i]);
            result.append(" ");
        }
        result.append(operationSequence);
        return result.toString();
    }
    
    public static void main(String[] args) {
        TwoStackState s = new TwoStackState(new int[] {1,2,0});
        Queue<TwoStackState> q = new ArrayDeque<TwoStackState>();
        q.add(s);
        while (q.size() > 0) {
            s = q.remove();
            if (s.isSorted()) System.out.println(s);
            TwoStackState r = s.mu();
            if (r != null) q.add(r);
            r = s.tau();
            if (r != null) q.add(r);
            r = s.lambda();
            if (r != null) q.add(r);
            r = s.rho();
            if (r != null) q.add(r);
        }
    }
}
