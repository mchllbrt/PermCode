package permlab.examples;

import permlib.Symmetry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.UniversalPermClass;

/**
 * Specify the "seed" part of a partial isomorphism
 *
 * @author Michael Albert
 */
public class PartialIsomorphismSpecification {

    HashMap<Permutation, Permutation> specification;
    boolean preservesMonotone;
    boolean preservesSimples;
    Permutation[] nonmonotoneTripleImages;
    static final Permutation[] nm3 = {
        new Permutation("132"),
        new Permutation("213"),
        new Permutation("231"),
        new Permutation("312")
    };

    public HashMap<Permutation, Permutation> getSpecification() {
        return specification;
    }

    public PartialIsomorphismSpecification(boolean preservesMonotone, boolean preservesSimples, Permutation[] nonmonotoneTripleImages) {
        this.preservesMonotone = preservesMonotone;
        this.preservesSimples = preservesSimples;
        this.nonmonotoneTripleImages = nonmonotoneTripleImages;
        setSpecification();
    }

    private void setSpecification() {
        specification = new HashMap<Permutation, Permutation>();
        if (preservesMonotone) {
            specification.put(new Permutation("12"), new Permutation("12"));
            specification.put(new Permutation("21"), new Permutation("21"));
            specification.put(new Permutation("123"), new Permutation("123"));
            specification.put(new Permutation("321"), new Permutation("321"));
        } else {
            specification.put(new Permutation("12"), new Permutation("21"));
            specification.put(new Permutation("21"), new Permutation("12"));
            specification.put(new Permutation("123"), new Permutation("321"));
            specification.put(new Permutation("321"), new Permutation("123"));
        }

        if (preservesSimples) {
            specification.put(new Permutation("2413"), new Permutation("2413"));
            specification.put(new Permutation("3142"), new Permutation("3142"));
        } else {
            specification.put(new Permutation("2413"), new Permutation("3142"));
            specification.put(new Permutation("3142"), new Permutation("2413"));
        }

        specification.put(new Permutation("132"), nonmonotoneTripleImages[0]);
        specification.put(new Permutation("213"), nonmonotoneTripleImages[1]);
        specification.put(new Permutation("231"), nonmonotoneTripleImages[2]);
        specification.put(new Permutation("312"), nonmonotoneTripleImages[3]);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartialIsomorphismSpecification other = (PartialIsomorphismSpecification) obj;
        if (this.preservesMonotone != other.preservesMonotone) {
            return false;
        }
        if (this.preservesSimples != other.preservesSimples) {
            return false;
        }
        if (!Arrays.deepEquals(this.nonmonotoneTripleImages, other.nonmonotoneTripleImages)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.preservesMonotone ? 1 : 0);
        hash = 29 * hash + (this.preservesSimples ? 1 : 0);
        hash = 29 * hash + Arrays.deepHashCode(this.nonmonotoneTripleImages);
        return hash;
    }

    public static ArrayList<PartialIsomorphismSpecification> getAllSpecifications() {
        ArrayList<PartialIsomorphismSpecification> result = new ArrayList<PartialIsomorphismSpecification>();

        for (boolean pm : new boolean[]{true, false}) {
            for (boolean ps : new boolean[]{true, false}) {
                for (Permutation p : new UniversalPermClass(4)) {
                    Permutation[] nti = new Permutation[4];
                    for (int i = 0; i < 4; i++) {
                        nti[i] = nm3[p.elements[i]];
                    }
                    result.add(new PartialIsomorphismSpecification(pm, ps, nti));
                }
            }
        }

        return result;
    }

    public static ArrayList<PartialIsomorphismSpecification> getRepSpecifications() {
        ArrayList<PartialIsomorphismSpecification> result = new ArrayList<PartialIsomorphismSpecification>();
        ArrayList<PartialIsomorphismSpecification> source = getAllSpecifications();
        while (!source.isEmpty()) {
            PartialIsomorphismSpecification pi = source.remove(0);
            result.add(pi);
            for (Symmetry left : Symmetry.values()) {
                for (Symmetry right : Symmetry.values()) {
                    source.remove(pi.applySymmetryPair(left, right));
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "PartialIsomorphismSpecification{" + "preservesMonotone=" + preservesMonotone + ", preservesSimples=" + preservesSimples + ", nonmonotoneTripleImages=" + Arrays.toString(nonmonotoneTripleImages) + '}';
    }

    public PartialIsomorphismSpecification applySymmetryPair(Symmetry left, Symmetry right) {
        boolean newPm = preservesMonotone;
        Permutation i2 = new Permutation("12");
        newPm = (left.on(right.on(i2)).equals(i2)) ? newPm : !newPm;
        boolean newPs = preservesSimples;
        Permutation s4 = new Permutation("2413");
        newPs = (left.on(right.on(s4)).equals(s4)) ? newPs : !newPs;
        Permutation[] nmi = new Permutation[4];
        for (int i = 0; i < 4; i++) {
            nmi[index(left.on(nm3[i]))] = right.on(nonmonotoneTripleImages[i]);
        }
        return new PartialIsomorphismSpecification(newPm, newPs, nmi);
    }

    private int index(Permutation p) {
        int i = 0;
        while (!p.equals(nm3[i])) {
            i++;
        }
        return i;
    }

    public static void main(String[] args) {
        for (PartialIsomorphismSpecification pi : getRepSpecifications()) {
            System.out.println(pi);
        }
    }
}
