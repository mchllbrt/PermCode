package permlib.examples;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 *
 * @author Michael Albert
 */
public class LayeredIs {

    static PermutationClass layered = new PermutationClass("312", "231");
    static PermutationClass fib = new PermutationClass("321");
    static PermutationClass twoLayered = new PermutationClass("312", "231", "123");

    public static HashMap<Permutation, HashSet<Permutation>> subn(int n) {
        HashMap<Permutation, HashSet<Permutation>> result = new HashMap<>();
        for (Permutation p : new Permutations(n)) {
            result.put(p, subLayered(p));
        }
        return result;
    }

    public static void main(String[] args) {
        compFib(5);
    }

    static HashSet<Permutation> subLayered(Permutation p) {
        HashSet<Permutation> result = new HashSet<>();
        for (Permutation q : PermUtilities.subpermutations(p)) {
            if (layered.containsPermutation(q)) {
                result.add(q);
            }
        }
        return result;
    }
    
    static HashSet<Permutation> subFib(Permutation p) {
        HashSet<Permutation> result = new HashSet<>();
        for (Permutation q : PermUtilities.subpermutations(p)) {
            if (fib.containsPermutation(q)) {
                result.add(q);
            }
        }
        return result;
    }
    
    static HashSet<Permutation> subTwo(Permutation p) {
        HashSet<Permutation> result = new HashSet<>();
        for (Permutation q : PermUtilities.subpermutations(p)) {
            if (twoLayered.containsPermutation(q)) {
                result.add(q);
            }
        }
        return result;
    }

    public static void comps(int n) {
        HashMap<Permutation, HashSet<Permutation>> map = new HashMap<>();
        for (Permutation p : new Permutations(n)) {
            map.put(p, subLayered(p));
        }
        for (Permutation p : new Permutations(n)) {
            // System.out.print(p + "->");
            if (!layered.containsPermutation(p)) {
                boolean dominated = false;
                for (Permutation q : new Permutations(layered, n)) {
                    if (PermUtilities.LISLength(q) >= PermUtilities.LISLength(p) && map.get(q).containsAll(map.get(p))) {
                        dominated = true;
                        break;
                    }
                }
                if (!dominated) {
                    System.out.println(p);
                    
                }
            }
            // System.out.println();
        }
        System.out.println("Done");
    }
    
    public static void compFib(int n) {
        HashMap<Permutation, HashSet<Permutation>> map = new HashMap<>();
        for (Permutation p : new Permutations(n)) {
            map.put(p, subFib(p));
        }
        for (Permutation p : new Permutations(n)) {
            // System.out.print(p + "->");
            if (!fib.containsPermutation(p)) {
                boolean dominated = false;
                for (Permutation q : new Permutations(fib, 2, n)) {
                    if (subFib(q).containsAll(map.get(p))) {
                        dominated = true;
                        break;
                    }
                }
                if (!dominated) {
                    System.out.println(p);
                    for(Permutation q : map.get(p)) System.out.println("  " +  q);
                }
            }
            // System.out.println();
        }
        System.out.println("Done");
    }
    
    public static void compTwo(int n) {
        HashMap<Permutation, HashSet<Permutation>> map = new HashMap<>();
        for (Permutation p : new Permutations(n)) {
            map.put(p, subTwo(p));
        }
        for (Permutation p : new Permutations(n)) {
            // System.out.print(p + "->");
            if (!fib.containsPermutation(p)) {
                boolean dominated = false;
                for (Permutation q : new Permutations(twoLayered, n)) {
                    if (!p.equals(q) && map.get(q).containsAll(map.get(p))) {
                        dominated = true;
                        break;
                    }
                }
                if (!dominated) {
                    System.out.println(p);
                }
            }
            // System.out.println();
        }
        System.out.println("Done");
    }

    public static void betterTransposition(Permutation p) {

        HashSet<Permutation> slp = subLayered(p);
        System.out.print(p + " -> ");
        for (int i = 1; i < p.length(); i++) {
            if (p.elements[i] < p.elements[i - 1]) {
                int[] qe = Arrays.copyOf(p.elements, p.elements.length);
                int t = qe[i];
                qe[i] = qe[i - 1];
                qe[i - 1] = t;
                Permutation q = new Permutation(qe, PermUtilities.SAFE);
                if (subLayered(q).containsAll(slp)) {
                    System.out.print(q + " ");
                }
            }
        }
        System.out.println();

    }

    public static void extendRight(int k, Permutation p) {

        HashSet<Permutation> slp = subLayered(p);
        System.out.print(p + " -> ");
        Permutation pre = p.segment(0, k);
        Permutation suf = p.segment(k + 1, p.length());
        for (Permutation prem : PermUtilities.juxtapose(pre, Permutation.ONE)) {
            if (layered.containsPermutation(prem)) {
                for (Permutation q : PermUtilities.juxtapose(prem, suf)) {
                    if (subLayered(q).containsAll(slp)) {
                        System.out.print(q + " ");
                    }
                }

            }

        }
        System.out.println();
    }

    public static void allBetter(Permutation p) {
        HashSet<Permutation> slp = subLayered(p);
        for (Permutation q : new Permutations(p.length())) {
            if (subLayered(q).containsAll(slp)) {
                System.out.println(q);
            }
        }
    }

    public static boolean RSKIdea(Permutation p) {
        int[][] tp = PermUtilities.tableau(p);
        HashSet<Permutation> slp = subLayered(p);
        for (Permutation q : slp) {
            System.out.println(q);
        }
        int[] cl = new int[tp[0].length];
        for (int i = 0; i < tp.length; i++) {
            for (int j = 0; j < tp[i].length; j++) {
                cl[j]++;
            }
        }
        System.out.println(Arrays.toString(cl));
        // System.out.print(p + " --> ");
        for (Permutation q : new Permutations(cl.length)) {
            int[] cq = new int[cl.length];
            for (int i = 0; i < cq.length; i++) {
                cq[i] = cl[q.elements[i]];
            }
            Permutation pq = layeredPermutationFromLengths(cq);
            if (subLayered(pq).containsAll(slp)) {
                // System.out.print(pq + " ");
                return true;
            }
        }
        return false;
    }

    private static Permutation layeredPermutationFromLengths(int[] l) {
        int s = 0;
        for (int i : l) {
            s += i;
        }
        int[] v = new int[s];
        int vi = 0;
        for (int i : l) {
            int vv = vi + i - 1;
            for (int j = 0; j < i; j++) {
                v[vi++] = vv--;
            }
        }
        return new Permutation(v, PermUtilities.SAFE);
    }

    private static void doNeighbours(int n) {
        HashMap<Permutation, HashSet<Permutation>> map = subn(n);
        for (Permutation p : new Permutations(n)) {
            boolean hasBetter = false;
            Permutation pi = p.inverse();
            for (int i = 1; i < p.length(); i++) {
                int[] qe = Arrays.copyOf(p.elements, p.elements.length);
                int t = qe[i];
                qe[i] = qe[i - 1];
                qe[i - 1] = t;
                Permutation q = new Permutation(qe, PermUtilities.SAFE);
                hasBetter = map.get(q).containsAll(map.get(p)) && !map.get(p).containsAll(map.get(q));
                if (hasBetter) {
                    break;
                }
                qe = Arrays.copyOf(p.elements, p.elements.length);
                t = qe[pi.elements[i]];
                qe[pi.elements[i]] = qe[pi.elements[i - 1]];
                qe[pi.elements[i - 1]] = t;
                q = new Permutation(qe, PermUtilities.SAFE);
                hasBetter = map.get(q).containsAll(map.get(p)) && !map.get(p).containsAll(map.get(q));
                if (hasBetter) {
                    break;
                }
            }
            if (!hasBetter && !layered.containsPermutation(p)) {
                System.out.println(p);
            }
        
        }
        System.out.println("Done");
    }

}

