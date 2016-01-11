package permlib.examples;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * Enumerate paths through a state machine.
 *
 * @author Michael Albert
 */
public class FibStateMachine {

    static HashMap<State, BigInteger> noPop0 = new HashMap<>();
    static HashMap<State, BigInteger> yesPop0 = new HashMap<>();
    static HashMap<State, BigInteger> noPop1 = new HashMap<>();
    static HashMap<State, BigInteger> yesPop1 = new HashMap<>();
    static HashMap<State, BigInteger> noPop2 = new HashMap<>();
    static HashMap<State, BigInteger> yesPop2 = new HashMap<>();

    public static void main(String[] args) {

        yesPop0.put(State.getState(0, 0), BigInteger.ONE);
        noPop1.put(State.getState(0, 1), BigInteger.ONE);

//        doGeneration(); doGeneration(); doGeneration(); doGeneration();
//        System.out.println("N0");
//        for(State s : noPop0.keySet()) System.out.println(s + " " + noPop0.get(s));
//        System.out.println("P0");
//        for(State s : yesPop0.keySet()) System.out.println(s + " " + yesPop0.get(s));
//        System.out.println("N1");
//        for(State s : noPop1.keySet()) System.out.println(s + " " + noPop1.get(s));
//        System.out.println("P1");
//        for(State s : yesPop1.keySet()) System.out.println(s + " " + yesPop1.get(s));
        for (int n = 1; n < 200; n++) {
            doGeneration();
            doGeneration();
            System.out.print(yesPop0.get(State.getState(0, 0)) + "\t");
            System.out.print(yesPop1.get(State.getState(0,1)) + "\t");
            System.out.println(yesPop0.get(State.getState(0,2)));
        }

    }

    static void doGeneration() {
        yesPop2 = new HashMap<>();
        noPop2 = new HashMap<>();
        for (State s : yesPop0.keySet()) {
            increment(yesPop2, s, yesPop0.get(s));
        }
        for (State s : noPop0.keySet()) {
            increment(yesPop2, s, noPop0.get(s));
        }
        for (State s : noPop1.keySet()) {
            for (State t : s.getPush()) {
                increment(noPop2, t, noPop1.get(s));
            }
        }
        for (State s : yesPop1.keySet()) {
            if (s.getPop() != null) {
                increment(yesPop2, s.getPop(), yesPop1.get(s));
            }
            for (State t : s.getPush()) {
                increment(noPop2, t, yesPop1.get(s));
            }
        }
        noPop0 = noPop1;
        yesPop0 = yesPop1;
        noPop1 = noPop2;
        yesPop1 = yesPop2;

    }

    private static void increment(HashMap<State, BigInteger> m, State s, BigInteger i) {
        if (!m.containsKey(s)) {
            m.put(s, i);
        } else {
            m.put(s, m.get(s).add(i));
        }
    }

    static class State {

        static HashMap<Integer, State> knownStates = new HashMap<>();

        private static int hashKey(int a, int b) {
            return (a << 20) | b;
        }

        int a;
        int b;
        State pop = null;
        State[] push = null;

        private State(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public static State getState(int a, int b) {
            int key = hashKey(a, b);
            if (knownStates.containsKey(key)) {
                return knownStates.get(key);
            }
            State s = new State(a, b);
            knownStates.put(hashKey(a, b), s);
            return s;
        }

        public void computePop() {
            if (a > 0) {
                pop = getState(a - 1, b);
            } else if (b > 0) {
                pop = getState(0, b - 1);
            }
        }

        public State getPop() {
            if (pop == null) {
                computePop();
            }
            return pop;
        }

        public void computePush() {
            if (b > 0) {
                push = new State[3 - b];
            } else {
                push = new State[1];
            }
            push[0] = getState(a + b, 1);
            if (b == 1) {
                push[1] = getState(a, 2);
            }
        }

        public State[] getPush() {
            if (push == null) {
                computePush();
            }
            return push;
        }

        @Override
        public int hashCode() {
            return hashKey(a, b);
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
            if (this.a != other.a) {
                return false;
            }
            if (this.b != other.b) {
                return false;
            }

            return true;
        }

        @Override
        public String toString() {
            return "State{" + a + "," + b + '}';
        }

    }

}
