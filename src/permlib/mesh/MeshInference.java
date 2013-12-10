package permlib.mesh;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.UniversalPermClass;
import permlib.utilities.Combinations;

/**
 * This class contains methods that allow for the inference of mesh patterns avoided
 * by given collections of permutations.
 * 
 * @author Michael Albert
 */
public class MeshInference {

    Permutation[] instances;
    HashSet<MeshPattern> avoidedPatterns = new HashSet<MeshPattern>();

    public MeshInference(Collection<Permutation> instances) {
        this.instances = new Permutation[instances.size()];
        instances.toArray(this.instances);
        Arrays.sort(this.instances);
    }

    public MeshInference(Permutation[] instances) {
        this.instances = instances;
        Arrays.sort(this.instances);
    }

    public Collection<MeshPattern> minimalPatternsFor(Permutation q) {

        HashSet<MeshPattern> result = new HashSet<MeshPattern>();
        int i = Arrays.binarySearch(instances, q);
        if (i >= 0 && i < instances.length && instances[i].equals(q)) {
            return result;
        }
        result.add(new MeshPattern(q));
        for (Permutation p : instances) {
            HashSet<MeshPattern> newResult = new HashSet<MeshPattern>();
            for (MeshPattern pattern : result) {
                if (pattern.isAvoidedBy(p)) {
                    newResult.add(pattern);
                } else {
                    newResult.addAll(requiredExtensionsFor(pattern, p));
                }
            }
            result.clear();
            result.addAll(filterToMinimals(newResult));
        }
        return result;
    }

    private Collection<MeshPattern> requiredExtensionsFor(MeshPattern pattern, Permutation q) {

        int[] indices = Combinations.firstCombination(pattern.p.length());
        LinkedList<MeshPattern> result = new LinkedList<MeshPattern>();
        LinkedList<MeshPattern> patternsToProcess = new LinkedList<MeshPattern>();
        result.add(pattern);
        do {
            LinkedList<MeshPattern> newResult = new LinkedList<MeshPattern>();
            for (MeshPattern currentPattern : result) {
                newResult.addAll(currentPattern.extensionsFor(q, indices));
            }
            result.clear();
            result.addAll(filterToMinimals(newResult));
        } while (Combinations.nextCombination(indices, q.length()));
        return result;

    }

    private LinkedList<MeshPattern> filterToMinimals(Collection<MeshPattern> patterns) {
        MeshPattern[] patternsArray = new MeshPattern[patterns.size()];
        patterns.toArray(patternsArray);
        Arrays.sort(patternsArray);
        LinkedList<MeshPattern> result = new LinkedList<MeshPattern>();
        for (int i = 0; i < patternsArray.length; i++) {
            boolean goodPattern = true;
            for (int j = 0; j < i; j++) {
                if (patternsArray[i].contains(patternsArray[j])) {
                    goodPattern = false;
                    break;
                }
            }
            if (goodPattern) {
                result.add(patternsArray[i]);
            }
        }
        return result;
    }
    
    public void computeAvoidedPatternsTo(int n) {
        for(Permutation q : new UniversalPermClass(1,n)) {
            // System.out.println("Trying " + q);
            for(MeshPattern pattern : minimalPatternsFor(q)) {
                boolean newPattern = true;
                for(int index = 0; index < q.length(); index++) {
                    if (pattern.isRedundantPoint(index)) {
                        MeshPattern deletedPattern = pattern.deletePoint(index);
                        if (isAvoidedPattern(deletedPattern)) {
                            newPattern = false;
                            break;
                        }
                    }
                }
                if (newPattern) {
                    System.out.println(pattern);
                    avoidedPatterns.add(pattern);
                }
            }
        }
    }

    private boolean isAvoidedPattern(MeshPattern pattern) {
        for(Permutation p : instances) {
            if (!pattern.isAvoidedBy(p)) return false;
        }
        return true;
    }
    
    public MeshPattern[] getAvoidedPatterns() {
        MeshPattern[] result = new MeshPattern[avoidedPatterns.size()];
        avoidedPatterns.toArray(result);
        Arrays.sort(result);
        return result;
    }
}
