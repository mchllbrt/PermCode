package permlab.examples;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import permlib.Permutation;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;
import permlib.property.HereditaryPropertyAdapter;
import permlib.property.Intersection;
import permlib.property.PermProperty;

/**
 * Messing around with C-machines
 *
 * @author Michael Albert
 */
public class CMachine {

    HashMap<State, Counter> states = new HashMap<State, Counter>();
    HereditaryProperty definingProperty;
    final State INITIAL_STATE = new State(new Permutation(0), false);

    public CMachine(HereditaryProperty definingProperty) {
        this.definingProperty = definingProperty;
        initialise();
    }

    public void initialise() {
        states.put(INITIAL_STATE, new Counter());
        states.get(INITIAL_STATE).next = BigInteger.ONE;
    }

    public void doStep() {
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

    public BigInteger currentValue() {
        return states.get(INITIAL_STATE).current;
    }

    public static void main(String[] args) {
        HereditaryProperty av12 = AvoidanceTest.getTest("12");
        PermProperty av123 = AvoidanceTest.getTest("123");
        PermProperty av4312 = AvoidanceTest.getTest("4312");
//        CMachine c = new CMachine(av12);
        CMachine c = new CMachine(HereditaryPropertyAdapter.forceHereditary(new Intersection(av123, av4312)));
        for (int i = 0; i < 100; i++) {
//            System.out.println("Step " + i);
            c.doStep();
            if (i % 2 == 0) {
                System.out.println((i / 2) + " " + c.currentValue() + "[" + c.states.size() + "]");
            }
        }
    }

    class Counter {

        BigInteger current = new BigInteger("0");
        BigInteger next = new BigInteger("0");

        public Counter() {
        }

        ;
        
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

    class State {

        Permutation p;
        boolean popEligible;
        ArrayList<State> children = null;
        int[] insertionPoints = new int[0];

        public State(Permutation p, boolean popEligible) {
            this.p = p;
            this.popEligible = popEligible;
        }

        public State() {
            this(new Permutation(), false);
        }

        public void generateChildren() {

            children = new ArrayList<State>();
            if (popEligible) {
                Permutation q = p.delete(0);
                State child = new State(q, q.length() > 0);
                int[] childInsertionCandidates = new int[insertionPoints.length];
                for (int i = 1; i < insertionPoints.length; i++) {
                    childInsertionCandidates[i - 1] = insertionPoints[i] - 1;
                }
                child.computeInsertionPoints(childInsertionCandidates);
                children.add(child);
            }

            Permutation q = p.insert(0, p.length());
            if (definingProperty.isSatisfiedBy(q)) {
                State child = new State(q, true);
                int[] childInsertionCandidates = new int[insertionPoints.length + 1];
                childInsertionCandidates[0] = 1;
                for (int i = 1; i < childInsertionCandidates.length; i++) {
                    childInsertionCandidates[i] = insertionPoints[i - 1] + 1;
                }
                child.computeInsertionPoints(childInsertionCandidates);
                children.add(child);
            } else {
                return; // Assumes hereditary property
            }
            for (int i = 0; i < insertionPoints.length; i++) {
                q = p.insert(insertionPoints[i], p.length());
                if (definingProperty.isSatisfiedBy(q)) {
                    State child = new State(q, false);
                    int[] childInsertionCandidates = new int[insertionPoints.length + 1];
                    int ci = 0;
                    for (int j = 0; j < insertionPoints.length; j++) {
                        if (insertionPoints[j] <= insertionPoints[i]) {
                            childInsertionCandidates[ci++] = insertionPoints[j];
                        }
                        if (insertionPoints[j] >= insertionPoints[i]) {
                            childInsertionCandidates[ci++] = insertionPoints[j] + 1;
                        }
                    }
                    child.computeInsertionPoints(childInsertionCandidates);
                    children.add(child);
                }
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 43 * hash + Objects.hashCode(this.p);
            hash = 43 * hash + (this.popEligible ? 1 : 0);
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
            if (!Objects.equals(this.p, other.p)) {
                return false;
            }
            if (this.popEligible != other.popEligible) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "State{" + p + "," + popEligible + '}';
        }

        // Compute the possible non-zero insertion points from a list of candidates
        // The zero case is handled separately because of its special nature
        private void computeInsertionPoints(int[] childInsertionCandidates) {
            HashSet<Integer> ip = new HashSet<Integer>();
            for (int i : childInsertionCandidates) {
                if (i > 0 && !ip.contains(i) && definingProperty.isSatisfiedBy(p.insert(i, p.length()))) {
                    ip.add(i);
                }
            }
            insertionPoints = new int[ip.size()];
            int i = 0;
            for (int ipt : ip) {
                insertionPoints[i++] = ipt;
            }
        }

    }
}
