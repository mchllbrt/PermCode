package permlib.examples;

import java.util.ArrayList;
import java.util.Objects;
import permlib.Permutation;

/**
 * What's going on with the superposition version of the reverse fib machine
 *
 * @author Michael Albert
 */
public class ReverseFib {

    public static void main(String[] args) {

        State t = new State();
        int length = 6;
        int count = 0;
        ArrayList<State> p = new ArrayList<State>();
        p.add(t);
        while (p.size() > 0) {
            ArrayList<State> c = new ArrayList<State>();
            for(State s : p) {
                if (s.in.size() == 0 && s.out.size() == length) {
                    System.out.println(s.outPerm());
                    count++;
                } else {
                    c.addAll(s.children(length));
                }
            }
            p = c;
        }
        System.out.println(count);

    }

    static class State {

        ArrayList<Integer> out;
        ArrayList<Integer> in;
        int next;
        boolean maxMustPop;
        boolean mayPop;

        public State(ArrayList<Integer> out, ArrayList<Integer> in, int next) {
            this.out = out;
            this.in = in;
            this.next = next;
        }

        public State(State t) {
            this.out = (ArrayList<Integer>) t.out.clone();
            this.in = (ArrayList<Integer>) t.in.clone();
            this.next = t.next;
            this.maxMustPop = t.maxMustPop;
            this.mayPop = t.mayPop;
        }

        public State() {
            this(new ArrayList<Integer>(), new ArrayList<Integer>(), 0);
            this.maxMustPop = false;
            this.mayPop = true;
        }

        public ArrayList<State> children(int maxLength) {
            ArrayList<State> result = new ArrayList<State>();
            if (next < maxLength) {
                State t = new State(this);
                t.out.add(next);
                t.next++;
                t.mayPop = true;
                result.add(t);
                t = new State(this);
                t.in.add(next);
                t.next++;
                t.mayPop = false;
                result.add(t);
            }
            result.addAll(popChildren());

            return result;
        }

        public ArrayList<State> popChildren() {
            ArrayList<State> result = new ArrayList<>();
            if (!mayPop || in.size() == 0) {
                return result;
            }

            State t = new State(this);
            t.out.add(t.in.remove(t.in.size() - 1));
            t.maxMustPop = false;
            result.add(t);

            if (!maxMustPop && in.size() > 1) {
                t = new State(this);
                t.out.add(t.in.remove(t.in.size() - 2));
                t.maxMustPop = (t.in.size() > 1);
                result.add(t);
            }

            return result;
        }
        
        public ArrayList<State> allPopChildren() {
            ArrayList<State> result = popChildren();
            int i = 0;
            while (i < result.size()) {
                result.addAll(result.get(i).popChildren());
                i++;
            }
            return result;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Objects.hashCode(this.out);
            hash = 17 * hash + Objects.hashCode(this.in);
            hash = 17 * hash + this.next;
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
            final State other = (State) obj;
            if (!Objects.equals(this.out, other.out)) {
                return false;
            }
            if (!Objects.equals(this.in, other.in)) {
                return false;
            }
            if (this.next != other.next) {
                return false;
            }
            return true;
        }

        public String toString() {
            return "Out " + out + ", In " + in + ", may pop " + mayPop + ", must pop max " + maxMustPop;
        }

        public ArrayList<Permutation> out() {
            ArrayList<Permutation> result = new ArrayList<>();
            if (in.size() > 0) {
                if (!mayPop) {
                    return result;
                }
                for(State s : allPopChildren()) {
                    if (s.in.size() == 0) result.add(s.outPerm());
                }
                return result;
            }
            result.add(outPerm());
            return result;
        }

        public Permutation outPerm() {
            int[] elements = new int[out.size()];
            for (int i = 0; i < out.size(); i++) {
                elements[i] = out.get(i);
            }
            return new Permutation(elements);
        }

    }
}
