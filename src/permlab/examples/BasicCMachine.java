/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author MichaelAlbert
 */
public class BasicCMachine {

    public static void main(String[] args) {

        HashMap<State, Counter> states = new HashMap<State, Counter>();
        Counter c = new Counter(BigInteger.ONE, BigInteger.ONE);
        State init = new State(0, 0, 0, false);
        states.put(init, c);

        for (int i = 0; i < 1000; i++) {
//            System.out.println("Step " + i);
            doStep(states);
            if (i % 2 == 0) {
                System.out.println((i / 2) + " " + states.get(init) + "[" + states.size() + "]");
            }
        }

    }

    public static void doStep(HashMap<State, Counter> states) {
        for (State s : states.keySet()) {
            states.get(s).reset();
        }
        Collection<State> currentStates = new HashSet<State>();
        currentStates.addAll(states.keySet());
        for (State s : currentStates) {
//            System.out.println("Updating from " + s + " of value " + states.get(s).current);
            if (states.get(s).current.equals(BigInteger.ZERO)) {
                continue;
            }
            if (s.children == null) {
                s.generateChildren();
            }
            for (State child : s.children) {
//                System.out.print("  updating child " + child);
                if (!states.keySet().contains(child)) {
                    states.put(child, new Counter());
                }
                states.get(child).updateCount(states.get(s).current);
//                System.out.println(" to value " + states.get(child).next);
            }
        }
    }

    static class Counter {

        BigInteger current = new BigInteger("0");
        BigInteger next = new BigInteger("0");

        Counter(BigInteger current, BigInteger next) {
            this.current = current;
            this.next = next;
        }

        Counter() {
        }

        public void updateCount(BigInteger c) {
            next = next.add(c);
        }

        public void reset() {
            current = next;
            next = new BigInteger("0");
        }

        public String toString() {
            return current.toString();
        }
    }

    static class State {

        int a;
        int b;
        int c;
        boolean popEligible;
        State[] children;

        public State(int a, int b, int c, boolean popEligible) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.popEligible = popEligible;
        }

        public State(State other) {
            this(other.a, other.b, other.c, other.popEligible);
        }

        public void setPopEligible(boolean popEligible) {
            this.popEligible = popEligible;
        }

        public void generateChildren() {
            ArrayList<State> cs = new ArrayList<State>();
            if (popEligible) {
                if (c > 0) {
                    cs.add(new State(this.a, this.b, this.c - 1, true));
                } else if (a > 1) {
                    cs.add(new State(this.a - 1, this.b, 0, true));
                } else if (a == 1) {
                    cs.add(new State(this.b, 0, 0, this.b > 0));
                }
            }

            if (b == 0) {
                cs.add(new State(this.a + 1, 0, 0, true));
                if (this.a > 0) {
                    cs.add(new State(this.a, 1, 0, false));
                }
            } else {
                if (c == 0) {
                    cs.add(new State(this.a, this.b + 1, 0, false));
                }
                cs.add(new State(this.a, this.b, this.c + 1, true));

            }
            children = new State[cs.size()];
            cs.toArray(children);
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 29 * hash + this.a;
            hash = 29 * hash + this.b;
            hash = 29 * hash + this.c;
            hash = 29 * hash + (this.popEligible ? 1 : 0);
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
            if (this.a != other.a) {
                return false;
            }
            if (this.b != other.b) {
                return false;
            }
            if (this.c != other.c) {
                return false;
            }
            if (this.popEligible != other.popEligible) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "State{" + a + ", " + b + ", " + c + ", " + popEligible + '}';
        }

    }
}
