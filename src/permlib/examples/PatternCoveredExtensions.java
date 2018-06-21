package permlib.examples;

import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.utilities.Combinations;

/**
 *
 * @author Michael Albert
 */
public class PatternCoveredExtensions {
  
  HashSet<Permutation> onePointExtensions(Permutation p, PermutationClass c) {
    HashSet<Permutation> result = new HashSet<>();
    for(Permutation q : PermUtilities.onePointExtensions(p)) {
      if (c.containsPermutation(q)) result.add(q);
    }
    return result;
  }
  
  boolean[] coveredIndices(Permutation text, Permutation pattern) {
    boolean[] result = new boolean[text.length()];
    for(int[] c : new Combinations(text.length(), pattern.length())) {
      if (text.patternAt(c).equals(pattern)) {
        for(int i : c) result[i] = true;
      }
    }
    return result;
  }
  
  boolean covered(Permutation text, Permutation pattern) {
    for(boolean b : coveredIndices(text, pattern)) {
      if (!b) return false;
    }
    return true;
  }
  

}
