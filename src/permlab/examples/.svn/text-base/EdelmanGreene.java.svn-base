/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.Permutations;
import permlib.property.HereditaryProperty;

/**
 *
 * @author MichaelAlbert
 */
public class EdelmanGreene {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        basisTo(8);

    }

    public static boolean multFree(Permutation p) {

        byte[] l = reducedWord(p);
        Queue<Word> q = new ArrayDeque<Word>();
        Word w = new Word(l, 0);
        HashSet<Word> seen = new HashSet<Word>();
        q.add(w);
        while (!q.isEmpty()) {
            w = q.poll();
            if (w.hasEGNeighbour()) return false;
            seen.add(w);
            for(Word v : w.OtherCoxeterNeighbours()) {
                if (!seen.contains(v)) q.add(v);
            }          
        }
        return true;

//        byte[] l = reducedWord(p);
//        HashMap<Word, Integer> comp = new HashMap<Word, Integer>();
//        Queue<Word> qEg = new ArrayDeque<Word>();
//        Queue<Word> qOther = new ArrayDeque<Word>();
//        Word w = new Word(l, 0);
//        int nextLabel = 1;
//        IntEquivalence iq = new IntEquivalence();
//        qEg.add(w);
//        qOther.add(w);
//        iq.ensureElement(0);
//        while (!qEg.isEmpty() || !qOther.isEmpty()) {
//            if (!qEg.isEmpty()) {
//                w = qEg.poll();
//                if (comp.containsKey(w)) {
//                    continue;
//                }
//                comp.put(w, w.getLabel());
////                if (comp.size() % 100 == 0) {
////                    System.out.println(comp.size());
////                }
//                for (Word ww : w.EGNeighbours()) {
//                    if (!comp.containsKey(ww)) {
//                        qEg.add(ww);
//                        qOther.add(ww);
//                    } else {
//                        // System.out.println("Found neighbour " + ww + " of " + w);
//                        iq.addEquivalence(comp.get(w), comp.get(ww));
//                    }
//                }
//            } else {
//                w = qOther.poll();
//                for (Word ww : w.OtherCoxeterNeighbours()) {
//                    if (!comp.containsKey(ww)) {
//                        iq.ensureElement(nextLabel);
//                        ww.setLabel(nextLabel++);
//                        qEg.add(ww);
//                        qOther.add(ww);
//                    }
//                }
//            }
//        }
//
//        // System.out.println(p + " " + iq.size() + " " + comp.size() + " " + iq);
//        return iq.size();
    }

    public static void basisTo(int m) {
        HereditaryProperty h = new HereditaryProperty() {
            @Override
            public Collection<Permutation> getBasis() {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public Collection<Permutation> getBasisTo(int n) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public boolean isSatisfiedBy(Permutation p) {
                return multFree(p);
            }

            @Override
            public boolean isSatisfiedBy(int[] values) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        HashSet<Permutation> basis = new HashSet<Permutation>();
        PermutationClass c = new PermutationClass(basis);
        for (int n = 4; n <= m; n++) {
            for (Permutation p : new Permutations(c, n)) {
//                System.out.print(p);
//                System.out.println(" " + onePerm(p));
//                System.out.println("Testing " + p);
                if (!h.isSatisfiedBy(p)) {
                    basis.add(p);
                    System.out.println(p);
                }
            }
            c = new PermutationClass(basis);
        }
    }

    static byte[] reducedWord(Permutation p) {

        int[] es = Arrays.copyOf(p.elements, p.length());
        ArrayDeque<Byte> letters = new ArrayDeque<Byte>();
        byte i = 0;
        while (i < es.length - 1) {
            if (es[i] > es[i + 1]) {
                letters.push(i);
                int t = es[i];
                es[i] = es[i + 1];
                es[i + 1] = t;
                i = 0;
            } else {
                i++;
            }
        }
        byte[] result = new byte[letters.size()];
        for (i = 0; i < result.length; i++) {
            result[i] = (byte) letters.pop();
        }
        return result;
    }

    static class Word {

        private static final int NOT_LABELLED = -1;
        final byte[] letters;
        int label = NOT_LABELLED;

        public Word(byte[] letters, int label) {
            this.letters = letters;
            this.label = label;
        }

        public Word(byte[] letters) {
            this(letters, NOT_LABELLED);
        }

        public int getLabel() {
            return label;
        }

        public void setLabel(int label) {
            this.label = label;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 17 * hash + Arrays.hashCode(this.letters);
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
            final Word other = (Word) obj;
            if (!Arrays.equals(this.letters, other.letters)) {
                return false;
            }
            return true;
        }

        public Word ARule(int i) {
            final byte[] newLetters = Arrays.copyOf(letters, letters.length);
            byte b = newLetters[i];
            newLetters[i] = newLetters[i + 1];
            newLetters[i + 1] = b;
            newLetters[i + 2] = newLetters[i];
            return new Word(newLetters, this.label);
        }

        public Word BRule(int i) {
            final byte[] newLetters = Arrays.copyOf(letters, letters.length);
            byte b = newLetters[i];
            newLetters[i] = newLetters[i + 1];
            newLetters[i + 1] = b;
            return new Word(newLetters, NOT_LABELLED);
        }

        // i, i+1 are the elements being exchanged
        public Word CRule(int i) {
            final byte[] newLetters = Arrays.copyOf(letters, letters.length);
            byte b = newLetters[i];
            newLetters[i] = newLetters[i + 1];
            newLetters[i + 1] = b;
            return new Word(newLetters, this.label);
        }

        public ArrayList<Word> EGNeighbours() {
            ArrayList<Word> result = new ArrayList<Word>();
            if (letters.length < 3) {
                return result;
            }
            for (int i = 0; i < letters.length - 1; i++) {
                if (ARuleApplies(i)) {
                    result.add(ARule(i));
                }
                if (CRuleApplies(i)) {
                    result.add(CRule(i));
                }
            }

            return result;
        }

        public boolean hasEGNeighbour() {
            if (letters.length < 3) {
                return false;
            }
            for (int i = 0; i < letters.length - 1; i++) {
                if (ARuleApplies(i) || CRuleApplies(i)) {
                    return true;
                }
            }
            return false;
        }

        public boolean ARuleApplies(int i) {
            return (i < letters.length - 2) && adjacent(letters[i], letters[i + 1]) && letters[i] == letters[i + 2];
        }

        public boolean CRuleApplies(int i) {
            return !adjacent(letters[i], letters[i + 1]) && ((i < letters.length - 2 && betweenRight(letters[i], letters[i + 1], letters[i + 2]))
                    || (i > 0 && betweenLeft(letters[i], letters[i + 1], letters[i - 1])));
        }

        public ArrayList<Word> OtherCoxeterNeighbours() {
            ArrayList<Word> result = new ArrayList<Word>();
            if (letters.length < 2) {
                return result;
            }
            for (int i = 0; i < letters.length - 1; i++) {
                if (!adjacent(letters[i], letters[i + 1]) && !CRuleApplies(i)) {
                    result.add(BRule(i));
                }
            }
            return result;
        }

        private boolean adjacent(byte b, byte c) {
            return (b - c == 1) || (c - b == 1);
        }

        private boolean betweenRight(byte x, byte z, byte y) {
            return (x <= y && y < z) || (z <= y && y < x);
        }

        private boolean betweenLeft(byte x, byte z, byte y) {
            return (x < y && y <= z) || (z < y && y <= x);
        }

        public String toString() {
            return Arrays.toString(letters) + " " + label;
        }
    }

    static class IntEquivalence {

        HashMap<Integer, HashSet<Integer>> equivalenceClasses = new HashMap<Integer, HashSet<Integer>>();
        HashMap<Integer, Integer> equivalenceReps = new HashMap<Integer, Integer>();

        public IntEquivalence() {
        }

        ;
        
        public void ensureElement(int i) {
            if (equivalenceClasses.containsKey(i)) {
                return;
            }
            HashSet<Integer> ci = new HashSet<Integer>();
            ci.add(i);
            equivalenceClasses.put(i, ci);
            equivalenceReps.put(i, i);
        }

        public void addEquivalence(int i, int j) {
            if (i == j) {
                return;
            }
            ensureElement(i);
            ensureElement(j);
            if (equivalenceReps.get(i) == equivalenceReps.get(j)) {
                return;
            }
            HashSet<Integer> ci = equivalenceClasses.get(i);
            ci.addAll(equivalenceClasses.get(j));
            int ri = equivalenceReps.get(i);
            int rj = equivalenceReps.get(j);
            int r = (ri < rj) ? ri : rj;
            for (int k : ci) {
                equivalenceClasses.put(k, ci);
                equivalenceReps.put(k, r);
            }
        }

        public int getRep(int i) {
            return equivalenceReps.get(i);
        }

        public int size() {
            HashSet reps = new HashSet<Integer>();
            for (int i : equivalenceReps.keySet()) {
                reps.add(equivalenceReps.get(i));
            }
            return reps.size();
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            for (int i : equivalenceReps.keySet()) {
                result.append(i + "~" + getRep(i) + ", ");
            }
            return result.toString();
        }
    }
}
