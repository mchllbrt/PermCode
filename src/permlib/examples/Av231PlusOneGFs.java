/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import permlib.Permutation;
import static permlib.examples.WilfEquivalencesInAv231.getT;

/**
 *
 * @author MichaelAlbert
 */
public class Av231PlusOneGFs {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        Arches a = new Arches(new Permutation("12345"));
//        Arches b = new Arches(new Permutation("12345"));
//        System.out.println(a.equals(b));
//        a = new Arches(new Permutation("21"));
//        System.out.println(GF.gf(a));
//        a = new Arches(new Permutation("2134"));
//        System.out.println(GF.gf(a));
//        
        HashMap<Rational, Permutation> genFs = new HashMap<>();
        int n = 16;
        WilfEquivalencesInAv231.generateClasses(n);
        System.out.println("Done generating");
        // for (Permutation p : getT(n)) System.out.println(Arrays.toString(p.delete(0).elements));
        int count = 0;
        for (Permutation p : getT(n)) {
            System.out.println(Arrays.toString(p.delete(0).elements));
//            Rational r = GF.gf(new Arches(p.delete(0)));
//            count++;
//            System.out.println(count);
//            if (genFs.containsKey(r)) {
//                System.out.println(p + " " + genFs.get(r) + " " + r);
//                break;
//            }
        }
        System.out.println("All distinct");
    }

    static class Arches {

        ArrayList<Arches> components = new ArrayList<>();

        Arches(Permutation p) {
            this(p.elements, 0, p.elements.length);
        }

        Arches(Arches... as) {
            components = new ArrayList<Arches>();
            for (Arches a : as) {
                components.add(a);
            }
        }

        Arches(int[] p, int left, int right) {
            if (left >= right) {
                return;
            }
            components = new ArrayList<>();
            int i = left + 1;
            while (i < right && p[i] < p[left]) {
                i++;
            }
            components.add(new Arches(p, left + 1, i));
            if (i < right) {
                components.addAll((new Arches(p, i, right)).components);
            }
        }

        Arches(ArrayList<Arches> components) {
            this.components = components;
        }

        public boolean isAtom() {
            return components.size() == 1;
        }

        public Arches lastAtom() {
            return new Arches(components.get(components.size() - 1));
        }

        public Arches interior() {
            if (components.size() == 0) {
                throw new IndexOutOfBoundsException("Attempt to access null component");
            }
            return components.get(0);
        }

        public Arches tail() {
            return tail(1);
        }

        public Arches tail(int fromIndex) {
            ArrayList<Arches> c = new ArrayList<Arches>();
            for (int i = fromIndex; i < components.size(); i++) {
                c.add(components.get(i));
            }
            return new Arches(c);
        }

        public Arches head(int toIndex) {
            ArrayList<Arches> c = new ArrayList<Arches>();
            for (int i = 0; i < toIndex && i < components.size(); i++) {
                c.add(components.get(i));
            }
            return new Arches(c);
        }

        public String toString() {
            StringBuilder result = new StringBuilder();
            for (Arches a : components) {
                result.append("A[");
                result.append(a.toString());
                result.append("]");
            }
            return result.toString();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 29 * hash + Objects.hashCode(this.components);
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
            final Arches other = (Arches) obj;
            if (!Objects.equals(this.components, other.components)) {
                return false;
            }
            return true;
        }

    }

    static class GF {

        static HashMap<Arches, Rational> gfs = new HashMap<>();

        static {
            gfs.put(new Arches(), Rational.ZERO);
        }

        private static void computeGF(Arches a) {
//            System.out.println("Computing for " + a);
            Rational result = new Rational(Polynomial.ZERO);
            if (a.isAtom()) {
//                System.out.println("Ok it's an atom");
                Rational den = new Rational(Polynomial.ONE);
                den = den.sub(Rational.T.mul(gf(a.interior())));
                result = Rational.ONE.div(den);
            } else {
//                System.out.println("Recursion for " + a);
                Rational h = Rational.ONE.sub(
                        Rational.T.mul(
                                gf(a.interior()).add(gf(a.lastAtom()))
                        ));
                Rational g = Rational.ONE;
                Rational x = Rational.T;
                x = x.mul(gf(a.interior()));
                x = x.mul(gf(a.tail()));
                g = g.sub(x);
                Rational k = Rational.ZERO;
                for (int i = 1; i < a.components.size() - 1; i++) {
//                    System.out.println("Adding term for: " + a.head(i+1) + ", " + a.tail(i) + ", " + a.tail(i+1));
//                    System.out.println("Known gfs" + gf(a.head(i+1)) + " " + gf(a.tail(i)) + " " + gf(a.tail(i+1)));
//                    // System.out.println(gf(a.tail(i)).sub(gf(a.tail(i+1))));
//                    System.out.println("XXX");
//                    System.out.println("k = " + k);
                    Rational p = gf(a.tail(i)).sub(gf(a.tail(i + 1)));
//                    System.out.println("p = " + p);
//                    System.out.println(a.head(i+1) + " AAAA " + gf(a.head(i+1)));
                    Rational q = gf(a.head(i + 1)).mul(p);
//                    System.out.println("q = " + q);                    
                    k = k.add(q);

                }
//                System.out.println("YYY");
                g = g.add(Rational.T.mul(k));
                result = g.div(h);
            }
//            System.out.println("Got answer for " + a + " which is " + result);
            gfs.put(a, result);
        }

        static Rational gf(Arches a) {
            if (!gfs.containsKey(a)) {
//                System.out.println("Computing for " + a + " which is null " + (a == null));
                computeGF(a);
            }
            return gfs.get(a);
        }
    }
}
