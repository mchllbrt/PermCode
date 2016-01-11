package permlib.examples;

import java.math.BigInteger;
import java.util.HashMap;

/**
 * Enumerate paths through a state machine.
 *
 * @author Michael Albert
 */
public class StateMachine {

    static HashMap<State, BigInteger> noPop0 = new HashMap<>();
    static HashMap<State, BigInteger> yesPop0 = new HashMap<>();
    static HashMap<State, BigInteger> noPop1 = new HashMap<>();
    static HashMap<State, BigInteger> yesPop1 = new HashMap<>();
    static HashMap<State, BigInteger> noPop2 = new HashMap<>();
    static HashMap<State, BigInteger> yesPop2 = new HashMap<>();

    public static void main(String[] args) {

        yesPop0.put(State.getState(0, 0, 0), BigInteger.ONE);
        noPop1.put(State.getState(0, 0, 1), BigInteger.ONE);

//        doGeneration(); doGeneration(); doGeneration(); doGeneration();
//        System.out.println("N0");
//        for(State s : noPop0.keySet()) System.out.println(s + " " + noPop0.get(s));
//        System.out.println("P0");
//        for(State s : yesPop0.keySet()) System.out.println(s + " " + yesPop0.get(s));
//        System.out.println("N1");
//        for(State s : noPop1.keySet()) System.out.println(s + " " + noPop1.get(s));
//        System.out.println("P1");
//        for(State s : yesPop1.keySet()) System.out.println(s + " " + yesPop1.get(s));
        
        for(int n = 1; n < 200; n++) {
            doGeneration(); doGeneration();
            System.out.println(yesPop0.get(State.getState(0, 0, 0)));
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
            for(State t : s.getPush()) {
                increment(noPop2, t, noPop1.get(s));
            }
        }
        for (State s : yesPop1.keySet()) {
            if (s.getPop() != null) {
                increment(yesPop2, s.getPop(), yesPop1.get(s));
            }
            for(State t : s.getPush()) {
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

        private static int hashKey(int a, int b, int c) {
            return (a << 20) | (b << 10) | c;
        }

        int a;
        int b;
        int c;
        State pop = null;
        State[] push = null;

        private State(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public static State getState(int a, int b, int c) {
            int key = hashKey(a, b, c);
            if (knownStates.containsKey(key)) {
                return knownStates.get(key);
            }
            State s = new State(a, b, c);
            knownStates.put(hashKey(a, b, c), s);
            return s;
        }

        public void computePop() {
            if (a > 0) {
                pop = getState(a - 1, b, c);
            } else if (b > 1) {
                pop = getState(0, 0, b + c - 1);
            } else if (c > 0) {
                pop = getState(0, 0, c - 1);
            }
        }

        public State getPop() {
            if (pop == null) {
                computePop();
            }
            return pop;
        }

        public void computePush() {
            push = new State[c + 1];
            for (int i = 0; i < c; i++) {
                push[i] = getState(a + b + i, c - i + 1, 0);
            }
            push[c] = getState(a, b, c + 1);
        }

        public State[] getPush() {
            if (push == null) {
                computePush();
            }
            return push;
        }

        @Override
        public int hashCode() {
            return hashKey(a, b, c);
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
            if (this.c != other.c) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "State{" + a + "," + b + "," + c + '}';
        }

    }

}
