package permlib.examples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * Investigate Wilf equivalence in objects consisting of nested arch systems and
 * other atoms 'a'.
 *
 * @author Michael Albert
 */
public class AsAndArches {

    public static void main(String[] args) {

        int aMax = 5;
        int archMax = 8;
        HashMap<IntPair, HashSet<AAAtom>> all = generateAll(aMax, archMax);
       
       
        HashSet<AAAtom> patterns = all.get(new IntPair(1,4));
        for(AAAtom p : patterns) {
            AASystem s = p.contents;
            System.out.print(s + " ");
            for(int a = 1; a <= aMax; a++) {
                for(int arch = 3; arch <= archMax; arch++) {
                    int count = 0;
                    for(AAAtom q : all.get(new IntPair(a, arch))) {
                        if (q.contents.contains(s)) count++;
                    }
                    System.out.print(count + " ");
                }
            }
            System.out.println();
        }
    }


static HashMap<IntPair, HashSet<AAAtom>> generateAll(int aCount, int archCount) {

        HashMap<IntPair, HashSet<AAAtom>> result = new HashMap<>();

        result.put(new IntPair(0, 0), new HashSet<AAAtom>());
        HashSet<AAAtom> aSet = new HashSet<>();

        aSet.add(new AAAtom());
        result.put(new IntPair(1, 0), aSet);

        for (int i = 2; i <= aCount; i++) {
            result.put(new IntPair(i, 0), new HashSet<AAAtom>());
        }

        HashSet<AAAtom> ASet = new HashSet<>();
        ASet.add(new AAAtom(new AASystem()));
        result.put(new IntPair(0, 1), ASet);

        for (int a = 1; a <= aCount; a++) {
            ArrayList<AAAtom> as = new ArrayList<>();
            for (int i = 0; i < a; i++) {
                as.add(new AAAtom());
            }
            HashSet<AAAtom> t = new HashSet<>();
            t.add(new AAAtom(new AASystem(as)));
            result.put(new IntPair(a, 1), t);
        }

        for (int arch = 2; arch <= archCount; arch++) {
            for (int a = 0; a <= aCount; a++) {
                HashSet<AAAtom> t = new HashSet<>();
                for (int preArch = 1; preArch <= arch; preArch++) {
                    for (int preA = 0; preA <= a; preA++) {
                        int postArch = arch - preArch;
                        int postA = a - preA;
                        for (AAAtom postAtom : result.get(new IntPair(postA, postArch))) {
                            for (AAAtom preAtom : result.get(new IntPair(preA, preArch))) {
                                ArrayList<AAAtom> newContents = (ArrayList<AAAtom>) preAtom.contents.atoms.clone();
                                newContents.add(postAtom);
                                t.add(new AAAtom(new AASystem(newContents)));
                            }
                        }
                    }
                }
                result.put(new IntPair(a, arch), t);
            }

        }

        return result;
    

}

    static class IntPair {

    int aCount;
    int archCount;

    public IntPair(int aCount, int archCount) {
        this.aCount = aCount;
        this.archCount = archCount;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + this.aCount;
        hash = 71 * hash + this.archCount;
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
        final IntPair other = (IntPair) obj;
        if (this.aCount != other.aCount) {
            return false;
        }
        if (this.archCount != other.archCount) {
            return false;
        }
        return true;
    }

}

static class AASystem {

    @Override
    public String toString() {
        return atoms.toString();
    }

    ArrayList<AAAtom> atoms;

    public AASystem(ArrayList<AAAtom> atoms) {
        this.atoms = (ArrayList<AAAtom>) atoms.clone();
    }

    public AASystem() {
        this(new ArrayList<AAAtom>());
    }

    public boolean contains(AASystem other) {
       
        if (other.atoms.isEmpty()) {
            return true;
        }
        
        if (other.arches() > this.arches()) {
            return false;
        }
        
        if (other.as() > this.as()) {
            return false;
        }
        
        if (this.removeLastAtom().contains(other)) {
            return true;
        }
        
        if (this.lastAtom().isLetter()) {
            if (other.lastAtom().isLetter()) {
                return this.removeLastAtom().contains(other.removeLastAtom());
            } else {
                return false;
            }
        }
        
        if (this.uncoverLastAtom().contains(other)) {
            return true;
        }
        
        if (other.lastAtom().isLetter()) return false;
        
        return (this.lastAtom().contents().contains(other.lastAtom().contents)
                && this.removeLastAtom().contains(other.removeLastAtom()));
    }

    public int arches() {
        int result = 0;
        for (AAAtom atom : atoms) {
            result += atom.arches();
        }
        return result;
    }

    public int as() {
        int result = 0;
        for (AAAtom atom : atoms) {
            result += atom.as();
        }
        return result;
    }

    public AAAtom lastAtom() {
        return atoms.get(atoms.size() - 1);
    }

    public AASystem removeLastAtom() {
        AASystem result = new AASystem(this.atoms);
        result.atoms.remove(result.atoms.size() - 1);
        return result;
    }

    public AASystem uncoverLastAtom() {
        AASystem result = removeLastAtom();
        result.atoms.addAll(lastAtom().contents.atoms);
        return result;
    }

    public AASystem addLastAtom(AAAtom a) {
        AASystem result = new AASystem(this.atoms);
        result.atoms.add(a);
        return result;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (AAAtom atom : atoms) {
            hash = 53 * hash + atom.hashCode();
        }
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
        final AASystem other = (AASystem) obj;
        if (!Objects.equals(this.atoms, other.atoms)) {
            return false;
        }
        return true;
    }

}

static class AAAtom {

    AASystem contents;
    char type;

    public AAAtom(AASystem contents) {
        this.contents = new AASystem();
        this.contents.atoms = (ArrayList<AAAtom>) contents.atoms.clone();
        type = 'A';
    }

    public AAAtom() {
        type = 'a';
    }

    public boolean isLetter() {
        return type == 'a';
    }

    public boolean isArch() {
        return type == 'A';
    }

    public AASystem contents() {
        return contents;
    }

    public int arches() {
        if (type == 'a') {
            return 0;
        }
        return 1 + contents.arches();
    }

    public int as() {
        if (type == 'a') return 1;
        return contents.as();
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.contents);
        hash = 61 * hash + this.type;
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
        final AAAtom other = (AAAtom) obj;
        if (!Objects.equals(this.contents, other.contents)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        if (type == 'a') {
            return "a";
        }
        return "A" + contents + "";
    }

}

}
