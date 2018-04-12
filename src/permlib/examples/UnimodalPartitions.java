package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import permlib.utilities.Partitions;

/**
 *
 * @author Michael Albert
 */
public class UnimodalPartitions {

    static final Random R = new Random();

    static HashSet<ArrayList<Integer>> singleDeletions(ArrayList<Integer> c) {
        HashSet<ArrayList<Integer>> result = new HashSet<>();
        for (int i = 0; i < c.size(); i++) {
            if (i == c.size() - 1 || c.get(i) > c.get(i + 1)) {
                ArrayList<Integer> d = new ArrayList<>(c);
                if (d.get(i) != 1) {
                    d.set(i, d.get(i) - 1);
                    result.add(d);
                } else {
                    d.remove(i);
                    result.add(d);
                }
            }

        }
        return result;

    }

    public static ArrayList<Integer> subspectrum(int n, ArrayList<Integer> c, int low) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);
        HashSet<ArrayList<Integer>> parents = new HashSet<>();
        parents.add(c);
        for (int k = n - 1; k >= low; k--) {
            // System.out.print(k + "   ");
            HashSet<ArrayList<Integer>> children = new HashSet<>();
            for (ArrayList<Integer> q : parents) {
                children.addAll(singleDeletions(q));
            }
            result.add(children.size());
            // System.out.println(children.size());
            parents = children;
        }
        return result;

    }

    private static boolean unimodal(ArrayList<Integer> r) {
        int c = Integer.MIN_VALUE;
        int i = 0;
        while (i < r.size() && c <= r.get(i)) {
            c = r.get(i);
            i++;
        }
        while (i < r.size() && c >= r.get(i)) {
            c = r.get(i);
            i++;
        }
        return i == r.size();

    }

    private static double penalty(ArrayList<Integer> r, ArrayList<Integer> c) {

        double global = Double.MAX_VALUE;
        double max = 0.0;
        for (int i = 1; i < r.size() - 1; i++) {
            double d = Math.max(r.get(i) - r.get(i - 1), r.get(i) - r.get(i + 1));
            if (d < 0) {
                return d;
            }
            double local = (10.0 + d) / (Math.pow((double) r.get(i), 2.0));
            global = Math.min(local, global);
        }
        return global * 1000000.0;
    }

    private static void doAnneal(ArrayList<Integer> c, int n) {
        long s = System.currentTimeMillis();
        System.out.println("Seed " + s);
        R.setSeed(s);
        int count = 0;
        long start = System.currentTimeMillis();
        double lastPenalty = Double.MAX_VALUE;
        double leastPenalty = Double.MAX_VALUE;
        // ArrayList<Integer> c = randomComposition(n);
        System.out.println("Initial partition: " + c);
        ArrayList<Integer> r = subspectrum(n, c, n / 2);
        lastPenalty = penalty(r, c);
        leastPenalty = penalty(r, c);

        while (true) {
            count++;
            if (count % 1000 == 0) {
                System.out.println(count + " trials " + (System.currentTimeMillis() - start));
                System.out.println("Com " + c);
                System.out.println("Pen " + lastPenalty);
                System.out.println("Spe " + r);
                // start = System.currentTimeMillis();
            }
            // System.out.println(p);
            ArrayList<Integer> d = neighbour(c);
            ArrayList<Integer> rd = subspectrum(n, d, n / 2);
            double currentPenalty = penalty(rd, d);
            //System.out.println("Current penalty " + currentPenalty + " for " + q);
            if (currentPenalty < lastPenalty) {
                if (currentPenalty < leastPenalty) {
                    System.out.println("New record " + rd + " from " + d + " with penalty " + currentPenalty);
                    leastPenalty = currentPenalty;
                    c = d;
                    r = rd;
                }
                c = d;
                r = rd;
                lastPenalty = currentPenalty;
                // System.out.println("Current penalty " + lastPenalty + " for " + c + " with spectrum " + r);

            } else {
                if (acceptAnyway(currentPenalty - lastPenalty)) {
                    // System.out.println("Accepting a non-improvement of " + (currentPenalty - lastPenalty));
                    c = d;
                    r = rd;
                    lastPenalty = currentPenalty;
                    // System.out.println("Current penalty " + lastPenalty + " for " + c);
                }

            }
            // System.out.println("Current penalty " + lastPenalty + " for " + c);
        }
    }

    static double T = 1000.0;

    private static boolean acceptAnyway(double d) {

        boolean accept = (R.nextDouble() < Math.exp(-d / T));
        if (accept) {
            T = T / 1.1;
        } else {
            T = T * 1.01;
        }
        return accept;
    }

    private static ArrayList<Integer> neighbour(ArrayList<Integer> c) {
        int[] ca = new int[c.size()];
        for (int i = 0; i < ca.length; i++) {
            ca[i] = c.get(i);
        }
        int a = R.nextInt(ca.length);
        int b = R.nextInt(ca.length);
        if (ca[a] > 1) {
            ca[a]--;
            ca[b]++;
            Arrays.sort(ca);
        }
        ArrayList<Integer> result = new ArrayList<>();
        for (int i : ca) {
            if (i != 0) {
                result.add(i);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        for (int n = 6; n <= 50; n++) {

            for (ArrayList<Integer> p : new Partitions(n)) {
                ArrayList<Integer> r = subspectrum(n, p, n / 2);
                if (!unimodal(r)) {
                    System.out.println(p + " " + r);
                }
            }
            System.out.println("Done " + n);
            // System.out.println();
        }

    }

}
