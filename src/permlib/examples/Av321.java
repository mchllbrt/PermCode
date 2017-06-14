package permlib.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;

/**
 * Representing a 321-avoider in a form suitable for the new involvement
 * algorithms.
 *
 * @author Michael Albert
 */
public class Av321 {

    private static final int UNDEFINED = -1;

    ArrayList<Value> upper = new ArrayList<>();
    ArrayList<Value> lower = new ArrayList<>();
    ArrayList<Value> floating = new ArrayList<>();
    ArrayList<Value> tail = new ArrayList<>();

    int length;

    public void extend(int v) {

        if (v == length) {
            tail.add(new Value(v));
            return;
        }
        
        // Update values of large upper elements
        for(int i = upper.size()-1; i >= 0; i--) {
            Value u = upper.get(i);
            if (u.v < v) break;
            u.v++;
        }
        
        int oldUppers = upper.size();
        for(int i = 0; i < tail.size(); i++) {
            Value t = tail.get(i);
            if (t.v < v) {
                floating.add(t);
            } else {
                upper.add(t);
                t.setNextLower(lower.size());
                t.v++;
            }
        }

        if (!lower.isEmpty()) lower.get(lower.size()-1).setNextLower(lower.size());
        lower.add(new Value(v));
       

    }

    private static class Value {

        int v;
        int nextUpper = UNDEFINED;
        int nextLower = UNDEFINED;

        public Value(int v) {
            this.v = v;
        }

        private void setNextLower(int index) {
            nextLower = index;
        }
    }

}
