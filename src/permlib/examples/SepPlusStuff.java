package permlib.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import static permlab.examples.Av231PlusStuff.eqClasses;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;
import permlib.processor.PermCounter;

/**
 * Check for Wilf equivalences in principal subclasses of Sep
 *
 * @author Michael Albert
 */
public class SepPlusStuff {

    /**
     * @param args the command line arguments
     */
    static Permutation p2413 = new Permutation("2413");
    static Permutation p3142 = new Permutation("3142");
    static PermutationClass sep = new PermutationClass(p2413, p3142);
    static int maxLength = 8;

    public static void main(String[] args) {
        for (int n = 4; n <= 6; n++) {
            maxLength = 2 * n - 2;
            checkSpectra(n);
        }
    }

    private static void checkSpectra(int i) {
        PermCounter counter = new PermCounter();
        sep.processPerms(i, counter);
        long l = counter.getCount() - 1;
        HashMap<Permutation, ArrayList<Long>> spectra = new HashMap<>();
        HashSet<Permutation> block = new HashSet<Permutation>();
        for (Permutation p : sep.getPerms(i)) {
            if (PermUtilities.isSymmetryRep(p)) {
                block.add(p);
                spectra.put(p, new ArrayList<Long>());
                spectra.get(p).add(l);
            }
        }
        splitBlock(block, i + 1, spectra);
        HashMap<ArrayList<Long>, HashSet<Permutation>> blocks = new HashMap<>();
        for (Permutation p : spectra.keySet()) {
            ArrayList<Long> spec = spectra.get(p);
            if (blocks.containsKey(spec)) {
                blocks.get(spec).add(p);
            } else {
                blocks.put(spec, new HashSet<Permutation>());
                blocks.get(spec).add(p);
            }

        }
        System.out.println(blocks.size() + " blocks at length " + i);
        for (ArrayList<Long> spec : blocks.keySet()) {
            System.out.println(spec);
            for (Permutation p : blocks.get(spec)) {
                System.out.println("  " + p);
            }
            System.out.println();
        }
    }

    public static void splitBlock(HashSet<Permutation> block, int m, HashMap<Permutation, ArrayList<Long>> spectra) {
        System.out.println("Splitting a block of size " + block.size() + " at " + m);
        if (block.size() <= 1 || m > maxLength) {
            System.out.println("Got a block at length " + m);
            return;
        }
        HashMap<Long, HashSet<Permutation>> blocks = new HashMap<>();
        PermCounter counter = new PermCounter();
        for (Permutation p : block) {
            PermutationClass d = new PermutationClass(p2413, p3142, p);
            d.processPerms(m, counter);
            if (blocks.containsKey(counter.getCount())) {
                HashSet<Permutation> b = blocks.get(counter.getCount());
                b.add(p);
            } else {
                HashSet<Permutation> b = new HashSet<>();
                b.add(p);
                blocks.put(counter.getCount(), b);
            }
            spectra.get(p).add(counter.getCount());
            counter.reset();
        }
        for (long l : blocks.keySet()) {
            splitBlock(blocks.get(l), m + 1, spectra);
        }

    }
}
