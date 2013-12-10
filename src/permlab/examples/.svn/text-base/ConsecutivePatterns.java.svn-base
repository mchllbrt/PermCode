/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.Symmetry;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;
import permlib.property.ConsecutiveAvoidanceTest;
import permlib.property.PermProperty;

/**
 *
 * @author MichaelAlbert
 */
public class ConsecutivePatterns {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        for(int trial = 0; trial < 10; trial++) {
            continuumExample();
        }



    }

    public static void generalExample() {
        ConsecutiveIsomorphismSpecification a = new ConsecutiveIsomorphismSpecification();

//        a.add(new Permutation("123"), new Permutation("123"));
//        a.add(new Permutation("132"), new Permutation("132"));
//        a.add(new Permutation("213"), new Permutation("213"));
//        a.add(new Permutation("231"), new Permutation("312"));
//        a.add(new Permutation("312"), new Permutation("231"));
//        a.add(new Permutation("321"), new Permutation("321"));

        a.add(new Permutation("123"), new Permutation("123"));
        a.add(new Permutation("132"), new Permutation("213"));
        a.add(new Permutation("213"), new Permutation("231"));
        a.add(new Permutation("231"), new Permutation("312"));
        a.add(new Permutation("312"), new Permutation("132"));
        a.add(new Permutation("321"), new Permutation("321"));


        for (int trial = 0; trial < 10; trial++) {
            HashSet<Permutation> classBasis = new HashSet<Permutation>();
            PermutationClass theClass = new PermutationClass(classBasis);
            ConsecutiveIsomorphismSpecification b = a.copy();
            int c = b.iso.size();
            int bas = 0;
            for (int n = 4; n <= 9; n++) {
                b = b.randomExtension(n);
                System.out.print(n + ", ");
                System.out.print(b.iso.size() - c);
                c = b.iso.size();
                System.out.print(", ");
                System.out.print(b.basis.size() - bas);
//                System.out.print(", ");
//                System.out.print(b.allExtended(n-1));
                bas = b.basis.size();
                for (Permutation p : b.basis) {
                    if (theClass.containsPermutation(p)) {
                        classBasis.add(p);
                    }
                }
                theClass = new PermutationClass(classBasis);
                System.out.print(", " + classBasis.size());
                PermCounter counter = new PermCounter();
                theClass.processPerms(n, counter);
                System.out.print(", " + counter.report());
                System.out.println();

            }
            System.out.println();
            for (Permutation p : classBasis) {
                System.out.println(p);
            }
            System.out.println();
        }

    }

    private static void alternatingExample() {
        ConsecutiveIsomorphismSpecification a = new ConsecutiveIsomorphismSpecification();
        a.add(new Permutation("132"), new Permutation("132"));
        a.add(new Permutation("213"), new Permutation("213"));
        a.add(new Permutation("2143"), new Permutation("3142"));
        a.add(new Permutation("3142"), new Permutation("2143"));
        a.add(new Permutation("1324"), new Permutation("1324"));

        a.addBasis(new Permutation("123"), new Permutation("321"), new Permutation("312"), new Permutation("231"), new Permutation("415263"));

        PermProperty inClass = new ConsecutiveAvoidanceTest(a.basis);
        for (int n = 4; n <= 12; n++) {
            int c = 0;
            for (Permutation p : new Permutations(n)) {
                if (inClass.isSatisfiedBy(p)) {
                    c++;
                }
            }
            System.out.println(c);
        }
//        
//        for (ConsecutiveIsomorphismSpecification a5 : a.extensions(5)) {
//            if (a5.basis.size() == 5) {
//                for (ConsecutiveIsomorphismSpecification a6 : a5.extensions(6)) {
//                    if (a6.basis.size() == 5) {
//                        for (ConsecutiveIsomorphismSpecification a7 : a6.extensions(7)) {
//                            if (a7.basis.size() == 5) {
//                                System.out.println(a7);
//                                System.out.println(a7.iso.size());
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
    }

    private static void continuumExample() {
        ConsecutiveIsomorphismSpecification a = new ConsecutiveIsomorphismSpecification();
        a.add(new Permutation("123"), new Permutation("123"));
        a.add(new Permutation("321"), new Permutation("321"));
//        a.add(new Permutation("231"), new Permutation("213"));
//        a.add(new Permutation("213"), new Permutation("231"));
        a.add(new Permutation("132"), new Permutation("132"));
        a.add(new Permutation("312"), new Permutation("312"));
        System.out.print("Flipped: ");
        for (int n = 3; n <= 9; n++) {
             boolean flip = Math.random() < 0.5;
            if (flip) {
                System.out.print(n + " ");
                a.add(ab(n), ba(n));
                a.add(ba(n), ab(n));
            } else {
                a.add(ab(n), ab(n));
                a.add(ba(n), ba(n));
            }
            a = a.randomExtension(n);

        }
        System.out.println();
        System.out.println("Basis size: " + a.basis.size());
        PermProperty inClass = new ConsecutiveAvoidanceTest(a.basis);
        for (int n = 4; n <= 9; n++) {
            int c = 0;
            for (Permutation p : new Permutations(n)) {
                if (inClass.isSatisfiedBy(p)) {
                    c++;
                }
            }
            System.out.println(n + " " + c);
        }

    }

    private static Permutation ab(int n) {
        int[] values = new int[n];
        values[0] = (n - 1) / 2;
        int a = values[0] + 1;
        int b = values[0] - 1;
        for (int i = 1; i < n; i++) {
            if (i % 2 == 1) {
                values[i] = a;
                a++;
            } else {
                values[i] = b;
                b--;
            }
        }
        return new Permutation(values);
    }

    private static Permutation ba(int n) {
        return Symmetry.C.on(ab(n));
    }
}
