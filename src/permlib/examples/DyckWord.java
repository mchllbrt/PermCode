/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayDeque;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.property.AvoidanceTest;
import permlib.property.PermProperty;

/**
 * Efficient iterators over Dyck words used e.g., to generate Av(321) quickly.
 *
 * @author Michael Albert
 */
public class DyckWord {

    private final DyckWord head;
    private final DyckWord tail;

    private DyckWord(DyckWord head, DyckWord tn) {
        this.head = head;
        this.tail = tn;
    }

    DyckWord(int n) {
        if (n <= 0) {
            throw new RuntimeException("Attempt to create empty word");
        }
        head = null;
        if (n == 1) {
            tail = null;
        } else {
            tail = new DyckWord(n - 1);
        }
    }

    private int size() {
        return 1 + ((head != null) ? head.size() : 0) + ((tail != null) ? tail.size() : 0);
    }

    public DyckWord next() {
        if (tail != null) {
            DyckWord tn = tail.next();
            if (tn != null) {
                return new DyckWord(head, tn);
            }
            if (head != null) {
                DyckWord hn = head.next();
                if (hn != null) {
                    return new DyckWord(hn, new DyckWord(tail.size()));
                }
            }
            // System.out.println("here B " + tail + " " + tail.size());
            if (tail.size() > 1) {
                // System.out.println("Here A");
                if (head != null) {
                    return new DyckWord(new DyckWord(head.size() + 1), new DyckWord(tail.size() - 1));
                } else {
                    return new DyckWord(new DyckWord(1), new DyckWord(tail.size() - 1));
                }
            } else {
                if (head != null) {
                    return new DyckWord(new DyckWord(head.size() + 1), null);
                } else {
                    return new DyckWord(new DyckWord(1), null);
                }
            }
        } else {
            if (head == null) {
                return null;
            }
            DyckWord hn = head.next();
            if (hn != null) {
                return new DyckWord(hn, null);
            } else {
                return null;
            }
        }

    }

    public boolean[] toBoolean() {
        boolean[] result = new boolean[this.size() * 2];
        int i = 0;
        result[i] = true;
        if (head != null) {
            for (boolean b : head.toBoolean()) {
                result[++i] = b;
            }
        }
        result[++i] = false;
        if (tail != null) {
            for (boolean b : tail.toBoolean()) {
                result[++i] = b;
            }
        }
        return result;
    }

    public Permutation to321Avoider() {
        boolean[] w = this.toBoolean();
        int v = 0;
        int p = 0;
        ArrayDeque<Integer> lows = new ArrayDeque<>();
        int[] e = new int[this.size()];
        for (int i = 1; i < w.length; i++) {
            if (w[i]) {
                if (w[i - 1]) {
                    lows.add(v);
                }
                v++;
            } else {
                if (w[i - 1]) {
                    e[p++] = v;
                } else {
                    e[p++] = lows.poll();
                }
            }
        }
        return new Permutation(e, true);

    }

    public String toString() {
        return "U" + ((head != null) ? head.toString() : "") + "D" + ((tail != null) ? tail.toString() : "");
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Permutation p = PermUtilities.sum(new Permutation("2457136"), new Permutation("1"));
        Permutation q = PermUtilities.sum(new Permutation("2567134"), new Permutation("1"));
        PermProperty ap = AvoidanceTest.getTest(p);
        PermProperty aq = AvoidanceTest.getTest(q);
        PermProperty ac = AvoidanceTest.getTest("24513");
        for (int n = 10; n <= 20; n++) {
            DyckWord d = new DyckWord(n);
            long cp = 0;
            long cq = 0;
            do {
                Permutation t = d.to321Avoider();
                if (ac.isSatisfiedBy(t)) {
                    cp++;
                    cq++;
                    d = d.next();
                    continue;
                }
                if (ap.isSatisfiedBy(t)) {
                    cp++;
                }
                if (aq.isSatisfiedBy(t)) {
                    cq++;
                }
                d = d.next();
            } while (d != null);
            System.out.println(n + ": " + cp + " " + cq + " " + (cp == cq));
        }

    }
}
