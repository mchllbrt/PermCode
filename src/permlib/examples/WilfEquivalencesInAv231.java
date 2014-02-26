package permlib.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import static permlib.PermUtilities.skewSum;
import static permlib.PermUtilities.sum;
import permlib.Permutation;
import permlib.utilities.IntPair;
import permlib.utilities.Multisets;
import permlib.utilities.Partitions;

/**
 * Check out the results on WE for Av(231, p).
 *
 * @author Michael Albert
 */
public class WilfEquivalencesInAv231 {

    static HashSet<Permutation>[] T;
    static HashSet<Permutation>[] X;

    public static void generateClasses(int maxSize) {

        int n = maxSize+1;
        T = (HashSet<Permutation>[]) new HashSet[n];
        X = (HashSet<Permutation>[]) new HashSet[n];
        for (int i = 0; i < n; i++) {
            T[i] = new HashSet<Permutation>();
            X[i] = new HashSet<Permutation>();
        }
        T[1].add(Permutation.ONE);
        T[2].add(new Permutation("21"));
        for (int m = 3; m < n; m++) {
            generateAtoms(m);
        }
        for (int i = 0; i < T.length; i++) {
            System.out.println(T[i].size());
        }
    }
    
    public static HashSet<Permutation>[] getT() {
        return T;
    }
    
    public static HashSet<Permutation> getT(int n) {
        return T[n];
    }

    private static void generateAtoms(int m) {
        for (Permutation p : T[m - 1]) {
            T[m].add(skewSum(Permutation.ONE, p));
        }
        generateX(m);
        T[m].addAll(X[m]);
        for (int k = 2; k <= m; k++) {
            generateBinary(m, k);
        }
    }

    private static void generateX(int m) {
        for (ArrayList<Integer> partition : new Partitions(m - 1)) {
//            System.out.println(partition);
            if (partition.size() >= 3) {
                // System.out.println("OK");
                ArrayList<IntPair> parts = Partitions.toExponentForm(partition);
                // System.out.println(parts);
                ArrayList<Permutation> currentP = new ArrayList<Permutation>();
                currentP.add(new Permutation(0));
                for (IntPair part : parts) {
                    int size = part.getFirst();
                    int count = part.getSecond();
                    ArrayList<Permutation> newP = new ArrayList<Permutation>();
                    for (Collection<Permutation> ps : new Multisets<>(T[size], count)) {
                        Permutation q = new Permutation();
                        for (Permutation pp : ps) {
                            q = sum(q, pp);
                        }
                        for (Permutation p : currentP) {
                            newP.add(sum(p, q));
                        }
                    }
                    currentP = newP;
                }
                for (Permutation p : currentP) {
                    // System.out.println(skewSum(Permutation.ONE, p));
                    X[m].add(skewSum(Permutation.ONE, p));
                }
            }
        }
    }

    private static void generateBinary(int m, int k) {
        // System.out.println("Doing " + m + " " + k);
        for (ArrayList<Integer> partition : new Partitions(m - k + 1, k)) {
            // System.out.println("Using partition " + partition);
            generateBinaryFor(Partitions.toExponentForm(partition));
        }

    }

    private static void generateBinaryFor(ArrayList<IntPair> pairs) {

        // First check that there are X's for all the sizes
        for (IntPair pair : pairs) {
            if (X[pair.getFirst()].isEmpty()) {
                return;
            }
        }
        // System.out.println("Ok so far");
        IntPair firstPair = pairs.get(0);
        ArrayList<Permutation> partialPerms = assembleBase(firstPair.getFirst(), firstPair.getSecond());
        for (int i = 1; i < pairs.size(); i++) {
            partialPerms = extendBase(partialPerms, pairs.get(i).getFirst(), pairs.get(i).getSecond());
        }
        for(Permutation p : partialPerms) {
            // System.out.println(p);
            T[p.length()].add(p);
        }

    }

    private static ArrayList<Permutation> assembleBase(int s, int c) {
        ArrayList<Permutation> result = new ArrayList<>();
        for (Collection<Permutation> ps : new Multisets<>(X[s], c)) {
            Permutation q = new Permutation(0);
            for (Permutation p : ps) {
                if (q.length() == 0) {
                    q = sum(q, p);
                } else {
                    q = skewSum(Permutation.ONE, sum(q, p));
                }
            }
            result.add(q);
        }
        return result;
    }

    private static ArrayList<Permutation> extendBase(ArrayList<Permutation> partialPerms, int s, int c) {
        ArrayList<Permutation> result = new ArrayList<>();
        for (Collection<Permutation> ps : new Multisets<>(X[s], c)) {
            for (Permutation qq : partialPerms) {
                Permutation q = qq.clone();
                for (Permutation p : ps) {
                    q = skewSum(Permutation.ONE, sum(q, p));
                }
                result.add(q);
            }

        }
        return result;
    }
}
