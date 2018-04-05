package permlib.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermClassInterface;
import permlib.classes.PermutationClass;
import static permlib.examples.SubIncOsc.UP;
import static permlib.examples.SubIncOsc.incosc;
import permlib.processor.PermCounter;
import permlib.property.AvoidanceTest;
import permlib.property.HereditaryProperty;
import permlib.property.PermProperty;

/**
 * Generate the principal spectra for subclasses of a class
 *
 * @author Michael Albert
 */
public class PrincipalSpectra {

    private PermClassInterface c;

    public PrincipalSpectra(PermClassInterface c) {
        this.c = c;
    }

    public HashMap<ArrayList<Long>, HashSet<Permutation>> getSpectra(int k, int high, boolean sym) {
        HashMap<ArrayList<Long>, HashSet<Permutation>> map = new HashMap<>();
        for (Permutation p : new Permutations(c, k)) {
            if (sym && !PermUtilities.isSymmetryRep(p)) {
                continue;
            }
            PermCounter counter = new PermCounter(AvoidanceTest.getTest(p));
            ArrayList<Long> spec = new ArrayList<>();
            for (int n = k + 1; n <= high; n++) {
                counter.reset();
                c.processPerms(n, counter);
                spec.add(counter.getCount());
            }
            if (!map.containsKey(spec)) {
                map.put(spec, new HashSet<Permutation>());
            }
            map.get(spec).add(p);
        }
//        for (ArrayList<Long> spec : map.keySet()) {
//            System.out.print(spec + " ");
//            for (Permutation p : map.get(spec)) {
//                // System.out.print(sumC(p) + " ");
//                System.out.print(p + " ");
//            }
//            System.out.println();
//        }
        return map;
    }

    public ArrayList<Long> getSpectrum(Permutation p, int high) {
        ArrayList<Long> result = new ArrayList<>();
        PermCounter counter = new PermCounter(AvoidanceTest.getTest(p));
        for (int n = p.length() + 1; n <= high; n++) {
            counter.reset();
            c.processPerms(n, counter);
            result.add(counter.getCount());
        }
        return result;
    }

    private static String sumC(Permutation p) {
        StringBuilder result = new StringBuilder();
        Permutation[] c = PermUtilities.sumComponents(p);
        for (Permutation q : c) {
            result.append('(');
            result.append(q);
            result.append(')');
        }
        return result.toString();
    }

    public static void main(String[] args) {
        
//        HereditaryProperty x = new HereditaryProperty() {
//
//            @Override
//            public Collection<Permutation> getBasis() {
//                ArrayList<Permutation> result = new ArrayList<>();
//                result.add(new Permutation("2143"));
//                result.add(new Permutation("2413"));
//                result.add(new Permutation("3142"));
//                result.add(new Permutation("3412"));
//                return result;
//            }
//
//            @Override
//            public Collection<Permutation> getBasisTo(int n) {
//                return getBasis(); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public boolean isSatisfiedBy(Permutation p) {
//                return isSatisfiedBy(p.elements); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public boolean isSatisfiedBy(int[] values) {
//                int left = 0;
//                int right = values.length-1;
//                int min = 0;
//                int max = values.length-1;
//                while (left <= right) {
//                    if (values[left] == min) {
//                        min++; left++;
//                    } else if (values[left] == max) {
//                        max--; left++;
//                    } else if (values[right] == min) {
//                        right--; min++;
//                    } else if (values[right] == max) {
//                        right--; max--;
//                    } else {
//                        return false;
//                    }
//                }
//                return true;
//            }
//        
//    };
//        
        HashSet<Permutation> basis = new HashSet<>();
        basis.add(new Permutation("312654"));
        basis.add(new Permutation("4321"));
        basis.add(new Permutation("231"));
        basis.add(new Permutation("4123"));
        basis.add(new Permutation("4132"));
        basis.add(new Permutation("4213"));
        basis.add(new Permutation("4312"));
        
        PermutationClass c = new PermutationClass(basis);
//        PermProperty a321 = PermUtilities.avoidanceTest(new Permutation("321"));
//        for (int n = 4; n <= 8; n++) {
//            for (Permutation p : new Permutations(c, n)) {
//                if (PermUtilities.PLUSINDECOMPOSABLE.isSatisfiedBy(p)) {
//                    if (!p.equals(incosc(p.length(), true)) && !p.equals(incosc(p.length(), false))) {
//                        System.out.println(p);
//                        basis.add(p);
//                    }
//                }
//            }
//            c = new PermutationClass(basis);
//        }
//        PermutationClass c = new PermutationClass("3412","2143");
        PrincipalSpectra ps = new PrincipalSpectra(c);
//        System.out.println(ps.getSpectrum(new Permutation("23157468"), 18));
//        System.out.println(ps.getSpectrum(new Permutation("13426857"), 18));
//        System.out.println(ps.getSpectrum(new Permutation("1743562"), 12));
        for (int n = 2; n <= 10; n++) {
            HashMap<ArrayList<Long>, HashSet<Permutation>> s = (new PrincipalSpectra(c)).getSpectra(n, n + 7, false);
            System.out.println("Size for " + n + " is " + s.keySet().size());
            for (ArrayList<Long> sp : s.keySet()) {
                if (s.get(sp).size() > 1) {
                    System.out.print(sp + " ");
                    for (Permutation p : s.get(sp)) {
                        System.out.print(p + " ");
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
        

    }
    
    
}
