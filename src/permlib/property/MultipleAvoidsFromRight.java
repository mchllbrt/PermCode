package permlib.property;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import permlib.classes.MultiPermutationClass;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.Permutations;

/**
 *
 * @author MichaelAlbert
 */
public class MultipleAvoidsFromRight implements HereditaryProperty {

    private Collection<Permutation> basis;
    private HashSet<ArrayWrapper> basisSuffixes = new HashSet<ArrayWrapper>();
    private HashSet<ArrayWrapper> basisWrappers = new HashSet<ArrayWrapper>();
    private int maxBasisLength;

    public static void main(String[] args) {

        HashSet<Permutation> b = new HashSet<Permutation>();
        b.add(new Permutation("321"));
        PermutationClass a321 = new PermutationClass(b);
        Random R = new Random();
        for (Permutation p : new Permutations(a321,9)) {
            if (R.nextInt(10) == 0) {
                b.add(p); System.out.println(p);
            }
        }
        System.out.println("Basis size " + b.size());
        Permutation[] bs = new Permutation[b.size()];
        b.toArray(bs);
        Arrays.sort(bs);
       {
            int count = 0;
            long start = System.currentTimeMillis();
            PermutationClass c = new PermutationClass(bs);
            for (Permutation p : new Permutations(c, 12)) {
                
                    count++;
                
            }
            System.out.println("PC " + count + " (" + (System.currentTimeMillis() - start) + ")");
        }
        {
            int count = 0;
            long start = System.currentTimeMillis();
            PermutationClass c = new MultiPermutationClass(bs);
            for (Permutation p : new Permutations(c, 12)) {
                
                    count++;
                
            }
            System.out.println("MA " + count + " (" + (System.currentTimeMillis() - start) + ")");
        }
    }

    public MultipleAvoidsFromRight(Collection<Permutation> basis) {
        this.basis = basis;
        maxBasisLength = 0;
        for (Permutation p : basis) {
            maxBasisLength = (maxBasisLength > p.elements.length) ? maxBasisLength : p.elements.length;
        }
        buildWrappers();
    }
    
    public MultipleAvoidsFromRight(Permutation[] basis) {
        this(Arrays.asList(basis));
    }

    @Override
    public Collection<Permutation> getBasis() {
        return basis;
    }

    @Override
    public Collection<Permutation> getBasisTo(int n) {
        return basis;
    }

    @Override
    public boolean isSatisfiedBy(Permutation p) {
        if (basis.size() == 0) return true;
        int[] ranks = new int[maxBasisLength];
        Arrays.fill(ranks, -1);
        int[] indices = new int[maxBasisLength];
        for (int i = p.elements.length - 1; i >= 0; i--) {
            indices[0] = i;
            ranks[0] = 0;
            if (involves(p, ranks, indices, 1)) {
                return false;
            }
        }
        return true;
    }
    
    public boolean isSatisfiedBy(Permutation p, int includeFinal) {
        int[] ranks = new int[maxBasisLength];
        Arrays.fill(ranks, -1);
        int[] indices = new int[maxBasisLength];
        for(int i = 0; i < includeFinal; i++) {
            indices[i] = p.elements.length-i-1;
            ranks[i] = 0;
            for(int j = 0; j < i; j++) {
                if (p.elements[indices[j]] < p.elements[indices[i]]) {
                    ranks[i]++;
                }
            }
        }
        for (int i = p.elements.length - 1 - includeFinal; i >= 0; i--) {
            indices[includeFinal] = i;
            ranks[includeFinal] = 0;
            for(int j = 0; j < includeFinal; j++) {
                if (p.elements[indices[j]] < p.elements[indices[includeFinal]]) {
                    ranks[includeFinal]++;
                }
            }
            if (involves(p, ranks, indices, includeFinal+1)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isSatisfiedBy(int[] values) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void buildWrappers() {
        for (Permutation p : basis) {
            for (int i = 1; i <= p.elements.length; i++) {
                ArrayWrapper a = new ArrayWrapper(p, i);
//                System.out.println("Basis suffixes added " + a);
                basisSuffixes.add(new ArrayWrapper(p, i));
            }
            ArrayWrapper pWrapper = new ArrayWrapper(p, p.length());
//            System.out.println("Basis wrapper added " + pWrapper);
            basisWrappers.add(pWrapper);
        }
    }

    private boolean involves(Permutation p, int[] ranks, int[] indices, int nextIndex) {
//        System.out.println(p + " " + nextIndex + " " + Arrays.toString(indices) + " " + Arrays.toString(ranks));
        ArrayWrapper awr = new ArrayWrapper(ranks);
        if (!basisSuffixes.contains(awr)) {
//            System.out.println("No wrapper found in basis suffixes");
            return false;
        }
        if (basisWrappers.contains(awr)) {
//            System.out.println("Found " + awr);
            return true;
        }
        if (nextIndex >= ranks.length || nextIndex >= p.elements.length) {
            return false;
        }
        for (int index = indices[nextIndex - 1] - 1; index >= 0; index--) {
//            System.out.println("index = " + index);
            indices[nextIndex] = index;
            int rank = 0;
            for (int j = 0; j < nextIndex; j++) {
//                System.out.println(j + " " + "(" + indices[j] + "," + p.elements[indices[j]] + ") (" + nextIndex + "," + p.elements[index] + ")");
                if (p.elements[indices[j]] < p.elements[index]) {
                    rank++;
                }
            }
            ranks[nextIndex] = rank;
            if (involves(p, ranks, indices, nextIndex + 1)) {
                return true;
            }
        }
        ranks[nextIndex] = -1;
        return false;
    }

    private class ArrayWrapper {

        int[] elements;

        @Override
        public String toString() {
            return "ArrayWrapper" + Arrays.toString(elements);
        }

        public ArrayWrapper(int[] elements) {
            this.elements = elements;
        }

        public ArrayWrapper(Permutation p, int length) {
            elements = new int[maxBasisLength];
            Arrays.fill(elements, -1);
            for (int i = 0; i < length; i++) {
                int j = p.elements.length - i - 1;
                int rank = 0;
                for (int k = p.elements.length - i; k < p.elements.length; k++) {
                    if (p.elements[k] < p.elements[j]) {
                        rank++;
                    }
                }
                elements[i] = rank;
            }
        }

        @Override
        public int hashCode() {
            int hash = 5;
            hash = 89 * hash + Arrays.hashCode(this.elements);
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
            final ArrayWrapper other = (ArrayWrapper) obj;
            if (!Arrays.equals(this.elements, other.elements)) {
                return false;
            }
            return true;
        }
    }
}
