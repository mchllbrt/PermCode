/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Positions are Dyck paths, moves are to delete some pyramid.
 *
 * @author Michael Albert
 */
public class LucasDyckGame {

    static HashSet<ArrayList<Integer>>[] dyckPaths;
    static HashMap<ArrayList<Integer>, Integer> grundyMap = new HashMap<>();
    static HashMap<Integer, HashSet<ArrayList<Integer>>> invGrundyMap = new HashMap<>();

    public static void main(String[] args) {
        generatePaths(8);
        for (int n = 0; n <= 8; n++) {

            for (ArrayList<Integer> p : dyckPaths[n]) {
                grundyValue(p);
            }
        }
        invertGrundyMap();
        for (ArrayList<Integer> p : invGrundyMap.get(0)) {
            System.out.println(p);
        }
    }

    static void generatePaths(int maxLength) {
        dyckPaths = new HashSet[maxLength + 1];
        for (int i = 0; i <= maxLength; i++) {
            dyckPaths[i] = new HashSet<ArrayList<Integer>>();
        }
        dyckPaths[0].add(new ArrayList<Integer>());
        for (int n = 1; n <= maxLength; n++) {
            for (int k = 0; k < n; k++) {
                for (ArrayList<Integer> p1 : dyckPaths[k]) {
                    for (ArrayList<Integer> p2 : dyckPaths[n - k - 1]) {
                        dyckPaths[n].add(joinPath(p1, p2));
                    }
                }
            }
            // System.out.println(dyckPaths[n].size());
        }
    }

    static ArrayList<Integer> joinPath(ArrayList<Integer> p1, ArrayList<Integer> p2) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add(1);
        for (int i : p1) {
            result.add(i + 1);
        }
        result.add(0);
        result.addAll(p2);
        return result;
    }

    static HashSet<ArrayList<Integer>> removePyramids(ArrayList<Integer> p) {
        HashSet<ArrayList<Integer>> result = new HashSet<>();
        for (int i = 0; i < p.size() - 1; i++) {
            ArrayList<Integer> pd = peakDelete(p, i);
            if (pd != null) {
                result.add(pd);
            }
        }
        return result;
    }

    private static ArrayList<Integer> peakDelete(ArrayList<Integer> p, int i) {
        if (i > 0 && p.get(i - 1) > p.get(i)) {
            return null;
        }
        int ps = 1;
        while (i + ps < p.size() && p.get(i + ps) > p.get(i + ps - 1)) {
            ps++;
        }
        if (i + 2 * ps - 1 < p.size() && p.get(i + 2 * ps - 1) == p.get(i) - 1) {
            ArrayList<Integer> newPath = new ArrayList<Integer>();
            for (int j = 0; j < i; j++) {
                newPath.add(p.get(j));
            }
            for (int j = i + 2 * ps; j < p.size(); j++) {
                newPath.add(p.get(j));
            }
            // System.out.println("   " + i + "  " + newPath);
            return newPath;
        }
        return null;
    }

    private static ArrayList<Integer> raise(ArrayList<Integer> p) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        result.add(1);
        for (int i : p) {
            result.add(i + 1);
        }
        result.add(0);
        return result;
    }

    private static Integer grundyValue(ArrayList<Integer> p) {
        if (grundyMap.containsKey(p)) {
            return grundyMap.get(p);
        }
        HashSet<Integer> options = new HashSet<Integer>();
        for (ArrayList<Integer> q : removePyramids(p)) {
            options.add(grundyValue(q));
        }
        int i = 0;
        while (options.contains(i)) {
            i++;
        }
        grundyMap.put(p, i);
        return i;
    }

    private static void invertGrundyMap() {
        for (ArrayList<Integer> p : grundyMap.keySet()) {
            int i = grundyMap.get(p);
            if (!invGrundyMap.containsKey(i)) {
                invGrundyMap.put(i, new HashSet<ArrayList<Integer>>());
            }
            invGrundyMap.get(i).add(p);
        }
    }

}
