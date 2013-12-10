package permlab.examples;

import java.util.ArrayDeque;
import java.util.HashSet;
import permlib.classes.PermutationClass;
import permlib.Permutation;
import permlib.Permutations;
import permlib.processor.PermProcessor;

/**
 *
 * @author MichaelAlbert
 */
public class QueueStackState {

    ArrayDeque<Integer> q = new ArrayDeque<Integer>();
    ArrayDeque<Integer> s = new ArrayDeque<Integer>();
    int nextOut = 0;

    public QueueStackState() {
    }

    ;
    public QueueStackState(QueueStackState other) {
        q = other.q.clone();
        s = other.s.clone();
        nextOut = other.nextOut;
    }

    public QueueStackState clone() {
        return new QueueStackState(this);
    }

    public boolean sortable(int[] input) {
        ArrayDeque<Integer> in = new ArrayDeque<Integer>();
        for (int i : input) {
            in.addLast(i);
        }
        return sortable(in);
    }

    public boolean sortable(Permutation p) {
        return sortable(p.elements);
    }

    public boolean sortable(ArrayDeque<Integer> input) {
        if (input.isEmpty()) {
            return true;
        }

        if (input.getFirst() == nextOut) {
            nextOut++;
            input.removeFirst();
            return sortable(input);
        }

        if (!q.isEmpty() && q.getFirst() == nextOut) {
            nextOut++;
            q.removeFirst();
            return sortable(input);
        }

        if (!s.isEmpty() && s.getFirst() == nextOut) {
            nextOut++;
            s.removeFirst();
            return sortable(input);
        }

        if (q.isEmpty() || input.getFirst() > q.getLast()) {
            QueueStackState other = this.clone();
            other.q.addLast(input.getFirst());
            ArrayDeque<Integer> newInput = input.clone();
            newInput.removeFirst();
            if (other.sortable(newInput)) {
                return true;
            }
        }
        if (s.isEmpty() || input.getFirst() < s.getFirst()) {
            QueueStackState other = this.clone();
            other.s.addFirst(input.getFirst());
            ArrayDeque<Integer> newInput = input.clone();
            newInput.removeFirst();
            if (other.sortable(newInput)) {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        final HashSet<Permutation> basis = new HashSet<Permutation>();
        for (int n = 5; n <= 14; n++) {
            System.out.println("n = " + n);
            PermutationClass c = new PermutationClass(basis);
            c.processPerms(n, new PermProcessor() {
                @Override
                public void process(Permutation p) {
                    if (!(new QueueStackState()).sortable(p)) {
                        System.out.println(p);
                        basis.add(p);
                    }
                }

                @Override
                public void reset() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }

                @Override
                public String report() {
                    throw new UnsupportedOperationException("Not supported yet.");
                }
            });
        }
    }
}
