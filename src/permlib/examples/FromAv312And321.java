/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;

/**
 * For WE we need to enumerate combinations of partitions (representing cycle
 * sizes) and other partitions (representing increasing runs) meeting the
 * following criteria: - Not more runs than cycles + 1 - Not fewer runs than
 * cycles-1 if there is a cycle of size 2
 *
 * @author Michael Albert
 */
public class FromAv312And321 {

    public static ArrayList<ArrayList<Integer>> partitions(int n) {
        if (n == 0) {
            ArrayList<Integer> p = new ArrayList<Integer>();
            ArrayList<ArrayList<Integer>> result = new ArrayList<>();
            result.add(p);
            return result;
        }
        return partitions(n, 1);
    }

    public static ArrayList<ArrayList<Integer>> partitions(int n, int minPart) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        if (minPart > n) {
            return result;
        } else if (minPart == n) {
            ArrayList<Integer> p = new ArrayList<Integer>();
            p.add(n);
            result.add(p);
            return result;
        }
        for (ArrayList<Integer> p : partitions(n - minPart, minPart)) {
            p.add(minPart);
            result.add(p);
        }
        result.addAll(partitions(n, minPart + 1));

        return result;
    }

    public static void main(String[] args) {
        int n = 9;
        for (int c = 2; c <= n; c++) {
            for (ArrayList<Integer> cycles : partitions(c, 2)) {
                for (ArrayList<Integer> runs : partitions(n - c)) {
                    if (runs.size() - cycles.size() <= 1) {
                        if (runs.size() >= cycles.size() - 1 || !cycles.contains(2)) {
                            System.out.println(cycles + " " + runs);
                        }
                    }
                }
            }
        }

    }
}
