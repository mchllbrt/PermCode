package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 * Check whether the downsets of all compositions are unimodal.
 *
 * @author Michael Albert
 */
public class UnimodalSubCompositions {

    static final Random R = new Random();

    static HashSet<ArrayList<Integer>> singleDeletions(ArrayList<Integer> c) {
        HashSet<ArrayList<Integer>> result = new HashSet<>();
        for (int i = 0; i < c.size(); i++) {
            ArrayList<Integer> d = new ArrayList<>(c);
            if (d.get(i) != 1) {
                d.set(i, d.get(i) - 1);
                result.add(d);
            } else if (i > 0 && d.get(i - 1) != 1) {
                d.remove(i);
                result.add(d);
            }
        }
        return result;
    }

    public static ArrayList<Integer> subspectrum(ArrayList<Integer> c, int low) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);
        HashSet<ArrayList<Integer>> parents = new HashSet<>();
        parents.add(c);
        for (int k = total(c) - 1; k >= low; k--) {
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

    private static int total(ArrayList<Integer> c) {
        int result = 0;
        for (int v : c) {
            result += v;
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

    private static ArrayList<Integer> getComposition(int n, int index) {
        if (n == 0) {
            return new ArrayList<Integer>();
        }
        ArrayList<Integer> result = getComposition(n - 1, index / 2);
        if (index % 2 == 0) {
            result.add(1);
        } else {
            result.set(result.size() - 1, result.get(result.size() - 1) + 1);
        }
        return result;
    }

    public static void main(String[] args) {
        ArrayList<Integer> c = randomComposition(36);
        System.out.println(c);
//        for(int i = 0; i < 5; i++) {
//            c = neighbour(c,30);
//            System.out.println(c);
//        }
        doAnneal(c, 36);

    }

    private static void doAll(int n) {
        int count = 1;
        for (int i = 1; i < n; i++) {
            count *= 2;
        }
        for (int i = 0; i < count; i++) {
            if (i % 10000 == 0) {
                System.out.println("Done " + i + " in " + n);
            }
            ArrayList<Integer> c = getComposition(n, i);
//            System.out.print(c);
//            System.out.print(" ");
            if (isRepresentative(c)) {
                ArrayList<Integer> ss = subspectrum(c, n / 2);
//            System.out.print(ss);
//            System.out.print(" ");
//            System.out.println(unimodal(ss));
                if (!unimodal(ss)) {
                    System.out.println(c + " " + ss);
                }
            }
        }
        System.out.println("Done " + n);
    }

    private static boolean isRepresentative(ArrayList<Integer> c) {
        int lo = 0;
        int hi = c.size() - 1;
        while (lo < hi) {
            if (c.get(lo) > c.get(hi)) {
                return true;
            }
            if (c.get(lo) < c.get(hi)) {
                return false;
            }
            lo++;
            hi--;
        }
        return true;
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
        return global*1000000.0;
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
        System.out.println("Initial composition: " + c);
        ArrayList<Integer> r = subspectrum(c, n / 2);
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
            ArrayList<Integer> d = neighbour(c, n);
            ArrayList<Integer> rd = subspectrum(d, n / 2);
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

    static double T = 100.0;

    private static boolean acceptAnyway(double d) {

        boolean accept = (R.nextDouble() < Math.exp(-d / T));
        if (accept) {
            T = T / 1.1;
        } else {
            T = T * 1.01;
        }
        return accept;
    }

    private static ArrayList<Integer> randomComposition(int n) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);
        for (int i = 1; i < n; i++) {
            if (R.nextBoolean()) {
                result.set(result.size() - 1, result.get(result.size() - 1) + 1);
            } else {
                result.add(1);
            }
        }
        return result;
    }

    // Allows for (limited) weighting of probability of adding a new part.
    private static ArrayList<Integer> randomComposition(int n, int k) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(1);
        for (int i = 1; i < n; i++) {
            if (R.nextInt(k) != 0) {
                result.set(result.size() - 1, result.get(result.size() - 1) + 1);
            } else {
                result.add(1);
            }
        }
        return result;
    }

    private static ArrayList<Integer> neighbour(ArrayList<Integer> c, int n) {

        HashSet<Integer> s = setForm(c);
        if (s.size() < n - 1 && R.nextInt(s.size()) == 0) { // Split a block
            int k = R.nextInt(n - 1);
            while (s.contains(k)) {
                k = R.nextInt(n - 1);
            }
            s.add(k);
            return listForm(s, n);
        } else { // Fuse two blocks
            Integer[] sl = s.toArray(new Integer[0]);
            int i = R.nextInt(sl.length);
            s.remove(sl[i]);
            return listForm(s, n);
        }

//        ArrayList<Integer> result = new ArrayList<>(c);
//        int k = R.nextInt(n);
//        int sum = c.get(0);
//        int index = 0;
//        while (sum < k) {
//            index++;
//            sum += c.get(index);
//        }
//        
//        int choice = R.nextInt(4);
//        switch (choice) {
//
//            case 0: // Add new part of size 1 before the current part. Do nothing if current part is 1
//                if (result.get(index) > 1) {
//                    result.add(index, 1);
//                    result.set(index+1, result.get(index + 1) - 1);
//                }
//                break;
//
//            case 1: // Add one to previous part, do nothing if there is no previous part
//                if (index > 0) {
//                    result.set(index - 1, result.get(index - 1) + 1);
//                    if (result.get(index) > 1) {
//                        result.set(index, result.get(index) - 1);
//                    } else {
//                        result.remove(index);
//                    }
//                }
//                break;
//
//            case 2: // Add new part of size 1 after the current part. Do nothing if current part is 1
//                if (result.get(index) > 1) {
//                    result.add(index+1, 1);
//                    result.set(index, result.get(index) - 1);
//                }
//                break;
//                
//            case 3: // Add one to next part, do nothing if there is no next part
//                if (index < result.size()-1) {
//                    result.set(index+1, result.get(index+1)+1);
//                    if (result.get(index) > 1) {
//                        result.set(index, result.get(index) - 1);
//                    } else {
//                        result.remove(index);
//                    }
//                }
//                break;
//        }
        //return result;
    }

    private static HashSet<Integer> setForm(ArrayList<Integer> c) {
        HashSet<Integer> result = new HashSet<>();
        int sum = 0;
        for (int i : c) {
            sum += i;
            result.add(sum - 1);
        }
        result.remove(sum - 1);
        return result;
    }

    private static ArrayList<Integer> listForm(HashSet<Integer> s, int n) {
        ArrayList<Integer> result = new ArrayList<>();
        Integer[] sl = new Integer[s.size()];
        s.toArray(sl);
        Arrays.sort(sl);
        result.add(sl[0] + 1);
        for (int i = 1; i < sl.length; i++) {
            result.add(sl[i] - sl[i - 1]);
        }
        result.add(n - 1 - sl[sl.length - 1]);
        return result;

    }
}
