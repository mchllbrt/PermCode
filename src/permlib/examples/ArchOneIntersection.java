package permlib.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * Work with arch systems where each arch is allowed to cross at most one other.
 *
 * There are two types of atoms - those with a single exterior arch containing
 * an arch system inside, and those with a crossing pair containing three
 * systems inside.
 *
 * @author Michael Albert
 */
public class ArchOneIntersection {

    static final ArchSystem EMPTY = new ArchSystem();

    static class ArchSystem {

        ArrayDeque<Atom> atoms = new ArrayDeque<>();
        int size = 0;

        ArchSystem() {
            size = 0;
        }

        ;
  
        ArchSystem(ArrayDeque<Atom> atoms) {
            this.atoms.addAll(atoms);
            for (Atom a : atoms) {
                size += a.size;
            }
        }

        private ArchSystem(ArchSystem a, ArchSystem b) {
            this.atoms.addAll(a.atoms);
            this.atoms.addAll(b.atoms);
            for (Atom aa : atoms) {
                size += aa.size;
            }
        }

        private ArchSystem(Atom a, ArchSystem b) {
            this.atoms.add(a);
            this.atoms.addAll(b.atoms);
            for (Atom aa : atoms) {
                size += aa.size;
            }
        }

        ArchSystem add(Atom a) {
            ArchSystem result = new ArchSystem(this.atoms);
            result.atoms.add(a);
            result.size += a.size;
            return result;
        }

        boolean contains(ArchSystem other) {
            if (other.size == 0) {
                return true;
            }

            if (other.size > this.size) {
                return false;
            }

            Atom a = other.atoms.getFirst();
            Atom b = this.atoms.getFirst();

            for (ArchSystem x : this.uncoverFirst()) {
                if (x.contains(other)) {
                    return true;
                }
            }

            if (b.contents.size() == 1) {
                if (a.contents.size() == 3) {
                    return false;
                }
                return b.contents.get(0).contains(a.contents.get(0))
                        && this.dropFirst().contains(other.dropFirst());
            } else {
                if (a.contents.size() == 1) return false;
                return b.contents.get(0).contains(a.contents.get(0))
                        && b.contents.get(1).contains(a.contents.get(1))
                        && b.contents.get(2).contains(a.contents.get(2))
                        && this.dropFirst().contains(other.dropFirst());
            }

           
        }

        private ArrayList<ArchSystem> uncoverFirst() {
            ArrayList<ArchSystem> result = new ArrayList();
            if (this.atoms.getFirst().contents.size() == 1) {
                ArchSystem r = new ArchSystem();
                r.append(this.atoms.getFirst().contents.get(0));
                r.append(this.dropFirst());
                result.add(r);
            } else {
                ArchSystem r = new ArchSystem();
                r.append(this.atoms.getFirst().contents.get(0));
                ArchSystem s = new ArchSystem();
                s.append(this.atoms.getFirst().contents.get(1));
                s.append(this.atoms.getFirst().contents.get(2));
                Atom sa = new Atom(s);
                r.append(sa);
                r.append(this.dropFirst());
                result.add(r);
                
                r = new ArchSystem();
                s = new ArchSystem();
                s.append(this.atoms.getFirst().contents.get(0));
                s.append(this.atoms.getFirst().contents.get(1));
                sa = new Atom(s);
                r.append(sa);
                r.append(this.atoms.getFirst().contents.get(2));
                r.append(this.dropFirst());
                result.add(r);            
            }
            return result;
        }

        private ArchSystem dropFirst() {
            ArchSystem result = new ArchSystem();
            result.atoms.addAll(this.atoms);
            result.atoms.remove();
            for (Atom a : result.atoms) {
                result.size += a.size;
            }
            return result;
        }

        private void append(ArchSystem c) {
            for (Atom a : c.atoms) {
                this.atoms.add(a);
                this.size += a.size;
            }
        }
        
        private void append(Atom a) {
            this.atoms.add(a);
            this.size += a.size;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (Atom a : atoms) {
                result.append(a);
                result.append(" ");
            }
            if (result.length() > 0) {
                result.deleteCharAt(result.length() - 1);
            }
            return result.toString();
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 83 * hash + Objects.hashCode(this.atoms);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ArchSystem other = (ArchSystem) obj;
            if (!Objects.equals(this.atoms, other.atoms)) {
                return false;
            }
            return true;
        }

    }

    static class Atom {

        ArrayList<ArchSystem> contents = new ArrayList<>();
        int size = 0;

        Atom(ArchSystem a) {
            contents.add(a);
            size = a.size + 1;
        }

        Atom(ArchSystem a, ArchSystem b, ArchSystem c) {
            contents.add(a);
            contents.add(b);
            contents.add(c);
            size = a.size + b.size + c.size + 2;
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            if (contents.size() == 1) {
                result.append("(");
            } else {
                result.append("<");
            }
            for (ArchSystem a : contents) {
                result.append(a);
                result.append(",");
            }
            result.deleteCharAt(result.length() - 1);
            if (contents.size() == 1) {
                result.append(")");
            } else {
                result.append(">");
            }
            return result.toString();
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 19 * hash + Objects.hashCode(this.contents);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Atom other = (Atom) obj;
            if (!Objects.equals(this.contents, other.contents)) {
                return false;
            }
            return true;
        }

    }

    public static void main(String[] args) {
        
        int k = 4;
        int n = 8;


        ArrayList<ArrayList<ArchSystem>> all = new ArrayList<>();
        ArrayList<ArchSystem> size0 = new ArrayList<>();
        size0.add(EMPTY);
        all.add(size0);
        for (int size = 1; size <= n; size++) {
            ArrayList<ArchSystem> current = new ArrayList<>();
            for (int firstContents = 0; firstContents < size; firstContents++) {
                for (ArchSystem c : all.get(firstContents)) {
                    Atom a = new Atom(c);
                    for (ArchSystem tail : all.get(size - firstContents - 1)) {
                        current.add(new ArchSystem(a, tail));
                    }
                }
            }
            for (int doubleContents = 0; doubleContents < size - 1; doubleContents++) {
                for (int x = 0; x <= doubleContents; x++) {
                    for (ArchSystem a : all.get(x)) {
                        for (int y = 0; y <= doubleContents - x; y++) {
                            for (ArchSystem b : all.get(y)) {
                                for (int z = doubleContents - x - y; z <= doubleContents - x - y; z++) {
                                    for (ArchSystem c : all.get(z)) {
                                        Atom f = new Atom(a, b, c);
                                        // System.out.println("First atom " + f);
                                        for (ArchSystem tail : all.get(size - doubleContents - 2)) {
                                            ArchSystem as = new ArchSystem(f, tail);
                                            // System.out.println("Adding " + as);
                                            current.add(as);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            all.add(current);
//            for (ArchSystem a : current) {
//                System.out.println(a);
//            }
//            System.out.println(current.size());
//            System.out.println();
        }

       
//        HashMap<ArchSystem, HashSet<ArchSystem>> sets = new HashMap<>();
//        for (ArchSystem b : all.get(k)) {
//            sets.put(b, new HashSet<ArchSystem>());
//        }
//
//        for (ArchSystem a : all.get(n)) {
//            for (ArchSystem b : all.get(k)) {
//                if (a.contains(b)) {
//                    sets.get(b).add(a);
//                }
//            }
//        }
//
//        for (ArchSystem b : all.get(k)) {
//            System.out.println(b + ": " + sets.get(b));
//        }

        HashMap<ArchSystem, Integer> counts = new HashMap<>();
        for (ArchSystem b : all.get(k)) {
            counts.put(b, 0);
        }

        for (ArchSystem a : all.get(n)) {
            for (ArchSystem b : all.get(k)) {
                if (a.contains(b)) {
                    counts.put(b, counts.get(b) + 1);
                }
            }
        }

        for (ArchSystem b : all.get(k)) {
            System.out.println(counts.get(b) + " " + b);
        }

    }

}
