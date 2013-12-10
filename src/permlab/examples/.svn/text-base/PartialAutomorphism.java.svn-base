package permlab.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.classes.UniversalPermClass;
import permlib.processor.PermCounter;

/**
 * Represents a partial isomorphism (not necessarily a symmetry) from the bottom
 * up.
 *
 * @author Michael Albert
 */
public class PartialAutomorphism {

    private static Permutation ONE = new Permutation("1");
    HashMap<Permutation, Permutation> iso;
    ArrayList<Permutation> basis;
    int lengthKnown;

    public PartialAutomorphism(HashMap<Permutation, Permutation> iso) {
        this.iso = iso;
        basis = new ArrayList<Permutation>();
        lengthKnown = 1;
    }

    public PartialAutomorphism() {
        this(new HashMap<Permutation, Permutation>());
    }

    public void addPair(Permutation p, Permutation q) {
        iso.put(p, q);
    }

    public void setLengthKnown(int lengthKnown) {
        this.lengthKnown = lengthKnown;
    }

    public Collection<Permutation> getBasis() {
        return basis;
    }

    public HashMap<Permutation, Permutation> getIsomorphism() {
        return iso;
    }

    public void extendToLength(int n) {
        while (lengthKnown < n) {
            lengthKnown++;
            // System.out.println("Extending to " + lengthKnown);
            PermutationClass c = new PermutationClass(basis);
            for (Permutation p : c.getPerms(lengthKnown)) {
                tryAdd(p);
            }
            for (Permutation p : c.getPerms(lengthKnown)) {
                if (iso.containsKey(p)) {
                    boolean goodOrbit = true;
                    Permutation q = iso.get(p);
                    while (iso.containsKey(q) && !q.equals(p)) {
                        q = iso.get(q);
                    }
                    if (!q.equals(p)) {
                        iso.remove(p);
                        basis.add(p);
                    }
                }
            }
        }
    }

    private void tryAdd(Permutation p) {
        if (iso.containsKey(p) || basis.contains(p)) {
            return;
        }
        HashSet<Permutation> pImageShadow = new HashSet<Permutation>();
        for (Permutation sp : shadow(p)) {
            pImageShadow.add(iso.get(sp));
        }
        for (Permutation q : new UniversalPermClass(p.length())) {
            if (shadow(q).equals(pImageShadow)) {
                iso.put(p, q);
                return;
            }
        }
        basis.add(p);
    }

    private static void shuffle(Permutation[] ps) {
        for (int i = 0; i < ps.length; i++) {
            int j = (int) (Math.random() * i);
            Permutation p = ps[i];
            ps[i] = ps[j];
            ps[j] = p;
        }
    }

    private static void smallShuffle(Permutation[] ps) {
        for (int i = 0; i < ps.length; i++) {
            if (Math.random() < 0.1) {
                int j = (int) (Math.random() * i);
                Permutation p = ps[i];
                ps[i] = ps[j];
                ps[j] = p;
            }
        }
    }

    private static void singleTransposition(Permutation[] ps) {
        int i = 0;
        int j = 0;
        while (i == j) {
            i = (int) (Math.random() * ps.length);
            j = (int) (Math.random() * ps.length);
        }
        Permutation t = ps[i];
        ps[i] = ps[j];
        ps[j] = t;

    }

    private HashSet<Permutation> shadow(Permutation q) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for (int i = 0; i < q.length(); i++) {
            result.add(PermUtilities.delete(q, i));
        }
        return result;
    }

    public static void main(String[] args) {
        // doRandom(4);
        doSpecific();
        //doReps();
    }

    private static void doReps() {
        for (PartialAutomorphismSpecification pm : PartialAutomorphismSpecification.getRepSpecifications()) {
            System.out.println(pm);
            System.out.println();
            HashMap<Permutation, Permutation> iso = pm.getSpecification();
            PartialAutomorphism pi = new PartialAutomorphism(iso);
            pi.extendToLength(7);
            Collection<Permutation> b = pi.getBasis();
//            for (Permutation p : b) {
//                System.out.println(p);
//            }
//            System.out.println();
            PermutationClass c = new PermutationClass(b);
            PermCounter counter = new PermCounter();
            for (int n = 4; n <= 8; n++) {
                c.processPerms(n, counter);
                System.out.print(counter.getCount() + " ");
                counter.reset();
            }
            System.out.println();
            for (Permutation p : b) {
                System.out.println(p);
            }
            System.out.println();
//for (Permutation p : PermUtilities.inferredBasis(c.getPerms(10), 6)) {
//                System.out.println(p);
//            }
//            System.out.println();
            System.out.println("-------------");
            System.out.println();
        }

    }

    private static void doRandom(int n) {
        int nf = 1;
        for (int i = 1; i <= n; i++) {
            nf *= i;
        }
        Permutation[] ps = new Permutation[nf];
        int i = 0;
        for (Permutation p : new UniversalPermClass(n)) {
            ps[i++] = p;
        }
        singleTransposition(ps);
        PartialIsomorphism randomIso = new PartialIsomorphism();
        i = 0;
        for (Permutation p : new UniversalPermClass(n)) {
            randomIso.addPair(p, ps[i++]);
        }
        randomIso.setLengthKnown(n);
        randomIso.extendToLength(n + 2);
        for (Permutation p : randomIso.getBasis()) {
            System.out.println(p);
        }
        System.out.println();
        System.out.println(randomIso.getBasis().size());
        for (Permutation p : randomIso.getIsomorphism().keySet()) {
            if (!randomIso.getIsomorphism().get(p).equals(p)) {
                System.out.println(p + " " + randomIso.getIsomorphism().get(p));
            }
        }

    }

    private static void doSpecific() {
        PartialAutomorphism isom = new PartialAutomorphism();
        for (Permutation p : new UniversalPermClass(3)) {
            isom.addPair(p, p);
        }
//        isom.addPair(new Permutation("123"), new Permutation("321"));
//        isom.addPair(new Permutation("321"), new Permutation("123"));
        isom.addPair(new Permutation("132"), new Permutation("312"));
        isom.addPair(new Permutation("213"), new Permutation("231"));
        isom.addPair(new Permutation("231"), new Permutation("132"));
        isom.addPair(new Permutation("312"), new Permutation("213"));
        isom.addPair(new Permutation("2413"), new Permutation("2413"));
        isom.addPair(new Permutation("3142"), new Permutation("3142"));
        isom.setLengthKnown(3);
        isom.extendToLength(7);
        for (Permutation p : isom.getBasis()) {
            System.out.println(p);
        }
        System.out.println();
        // System.out.println(isom.getBasis().size());
        for (Permutation p : isom.getIsomorphism().keySet()) {
            if (!isom.getIsomorphism().get(p).equals(p)) {
                System.out.print(p + " ");
                Permutation q = isom.getIsomorphism().get(p);
                while(!q.equals(p)) {
                    System.out.print(q + " ");
                    q = isom.getIsomorphism().get(q);
                }
                System.out.println();        
            }
        }
        System.out.println();
        for (Permutation p : PermUtilities.inferredBasis(isom.getIsomorphism().values(), 7)) {
            System.out.println(p);
        };


    }
}
