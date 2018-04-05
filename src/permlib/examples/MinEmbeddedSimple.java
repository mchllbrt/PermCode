/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.SimplePermClass;
import permlib.processor.PermProcessor;
import permlib.utilities.Combinations;

/**
 * If an arbitrary permutation p is contained in a simple permutation s then is
 * there a length bound on the minimal simple subpermutation of s containing p?
 *
 * We first address this as a covering problem (i.e., we think of the occurrence
 * of p as in a fixed location)
 *
 * @author MichaelAlbert
 */
public class MinEmbeddedSimple {

    /**
     * @param args the command line arguments
     */
    static int m;
    static final int PATTERN_LENGTH = 4;

    public static void main(String[] args) {
        // findMax();
        badGuys(new Permutation("11 1 5 3 6 2 8 4 10 7 12 9"), 4);
    }

    private static void findMax() {
        m = 0;
        int simpleLength = 12;

        (new SimplePermClass()).processPerms(simpleLength,
                new PermProcessor() {

                    @Override
                    public void process(Permutation s) {
                        int ms = minPatternCover(s, PATTERN_LENGTH);
                        if (ms > m) {
                            m = ms;
                            System.out.println("(" + m + ") " + s);
                        }
                    }

                    @Override
                    public void reset() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                    @Override
                    public String report() {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }

                });
    }

    public static int minIndexCover(Permutation s, int k) {
        int n = s.length();
        for (int m = 4; m <= n; m++) {
            HashSet<ArrayList<Integer>> covered = new HashSet<>();
            for (int[] c : new Combinations(n, m)) {
                Permutation p = s.patternAt(c);
                if (PermUtilities.isSimple(p)) {
                    for (int[] d : new Combinations(m, k)) {
                        ArrayList<Integer> cov = new ArrayList<Integer>();
                        for (int i : d) {
                            cov.add(c[i]);
                        }
                        covered.add(cov);
                    }
                }
            }
            if (covered.size() == binomial(n, k)) {
                return m;
            }
        }

        return 0;
    }

    public static int minPatternCover(Permutation s, int k) {

        HashSet<Permutation> patternsInSimple = new HashSet<>();
        for (int[] c : new Combinations(s.length(), k)) {
            patternsInSimple.add(s.patternAt(c));
        }
        for (int m = Math.max(4, k); m < s.length(); m++) {
            for (int[] c : new Combinations(s.length(), m)) {
                Permutation p = s.patternAt(c);
                if (PermUtilities.isSimple(p)) {
                    for (int[] d : new Combinations(m, k)) {
                        patternsInSimple.remove(p.patternAt(d));
                    }
                }
                if (patternsInSimple.isEmpty()) {
                    break;
                }
            }
            if (patternsInSimple.isEmpty()) {
                return m;
            }
        }

        return s.length();
    }

    private static int binomial(int n, int k) {
        int result = 1;
        for (int i = 0; i < k; i++) {
            result *= (n - i);
            result /= i + 1;
        }
        return result;
    }

    private static void badGuys(Permutation s, int k) {
        HashSet<Permutation> patternsInSimple = new HashSet<>();
        for (int[] c : new Combinations(s.length(), k)) {
            patternsInSimple.add(s.patternAt(c));
        }
        for (int m = Math.max(4, k); m < s.length(); m++) {
            for (int[] c : new Combinations(s.length(), m)) {
                Permutation p = s.patternAt(c);
                if (PermUtilities.isSimple(p)) {
                    for (int[] d : new Combinations(m, k)) {
                        patternsInSimple.remove(p.patternAt(d));
                    }
                }
                if (patternsInSimple.isEmpty()) {
                    break;
                }
            }
        }
        for (Permutation p : patternsInSimple) {
            System.out.println(p);
        }

    }

}
