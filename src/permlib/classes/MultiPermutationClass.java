package permlib.classes;

import java.util.Collection;
import permlib.Permutation;
import permlib.property.MultipleAvoidsFromRight;

/**
 *
 * @author MichaelAlbert
 */
public class MultiPermutationClass  extends PermutationClass {
    
    private MultipleAvoidsFromRight avoidanceTest;
    
    public MultiPermutationClass(Permutation... basis) {
        setupMasks();
        this.basis = basis;
        avoidanceTest = new MultipleAvoidsFromRight(this.basis);
        computeStoredPermutations();
    }
    
    public MultiPermutationClass(Collection<Permutation> basis) {
        setupMasks();
        this.basis = new Permutation[basis.size()];
        basis.toArray(this.basis);
        avoidanceTest = new MultipleAvoidsFromRight(basis);
        computeStoredPermutations();
    }

    public MultiPermutationClass(String... strings) {
        setupMasks();
        this.basis = new Permutation[strings.length];
        int i = 0;
        for (String string : strings) {
            this.basis[i++] = new Permutation(string);
        }
        avoidanceTest = new MultipleAvoidsFromRight(this.basis);
        computeStoredPermutations();
    }
    
    public PermClassInterface clone() {
        return new MultiPermutationClass(basis);
    }
    
    @Override
    public boolean containsPermutation(Permutation q) {
        return avoidanceTest.isSatisfiedBy(q);
    }

    @Override
    public boolean containsPermutation(Permutation q, int includeFinal) {
        return avoidanceTest.isSatisfiedBy(q, includeFinal);
    }

}
