package permlab.examples;

import java.util.Collection;
import java.util.HashSet;
import permlib.Permutation;
import permlib.Permutations;
import permlib.SortingOperators;
import permlib.classes.UniversalPermClass;
import permlib.mesh.MeshInference;
import permlib.mesh.MeshPattern;

/**
 *
 * @author Michael Albert
 */
public class MeshInferenceExample {

    public static void main(String[] args) {
        
//        HashSet<Permutation> instances = new HashSet<Permutation>();
//        instances.add(new Permutation("132"));
//        MeshInference mi = new MeshInference(instances);
//        for(MeshPattern patt : mi.minimalPatternsFor(new Permutation("12"))) {
//            System.out.println(patt);
//        }
        HashSet<Permutation> twoSortables = new HashSet<Permutation>();
        for(Permutation p : new UniversalPermClass(1, 8)) {
            if (SortingOperators.stackSort(SortingOperators.stackSort(p)).equals(new Permutation(p.length())))
                twoSortables.add(p);
        }
        System.out.println(twoSortables.size());
        MeshInference mi = new MeshInference(twoSortables);
//        for(Permutation q : new Permutations(5)) {
//            if (!twoSortables.contains(q)) {
//                System.out.println(q);
//                Collection<MeshPattern> patterns = mi.minimalPatternsFor(q);
//                for(MeshPattern pattern : patterns) {
//                    System.out.println(pattern);
//                }
//            }
//            
//        }
        
        mi.computeAvoidedPatternsTo(6);
        for(MeshPattern p : mi.getAvoidedPatterns()) System.out.println(p);
    }
}
