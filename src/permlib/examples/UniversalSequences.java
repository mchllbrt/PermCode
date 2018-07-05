package permlib.examples;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermutationClass;

/**
 * Look for minimal words in [n]^* containing S_n as subwords
 *
 * @author Michael Albert
 */
public class UniversalSequences {

  private static final int N = 6;

  static HashMap<TupleFamily, Integer> seqLength = new HashMap<>();

  static {
    seqLength.put(new TupleFamily(), 0);
  }

  /*
    The dynamic programming approach will produce sets of suffixes that we
    need to complete. It's probably important to standardise these.
   */
  static class Tuple implements Comparable<Tuple> {

    int[] t;

    Tuple(int[] t) {
      this.t = t;
    }

    Tuple(int[] t, Permutation p) {
      this.t = new int[t.length];
      for (int i = 0; i < t.length; i++) {
        this.t[i] = p.elements[t[i]];
      }
    }

    Tuple apply(Permutation p) {
      return new Tuple(this.t, p);
    }

    Tuple update(int value) {
      if (t[t.length - 1] != value) {
        return this;
      }
      return new Tuple(Arrays.copyOf(t, t.length - 1));
    }
    
    boolean contains(Tuple other) {
      if (other.t.length >= this.t.length) return this.equals(other);
      
      int otherIndex = 0;
      int thisIndex = 0;
      while(otherIndex < other.t.length && thisIndex < this.t.length) {
        if (this.t[thisIndex] == other.t[otherIndex]) {
          thisIndex++;
          otherIndex++;
        } else {
          thisIndex++;
        }
      }
      return otherIndex == other.t.length;    
    }

    @Override
    public int compareTo(Tuple o) {
      if (this.t.length != o.t.length) {
        return this.t.length - o.t.length;
      }
      int i;
      for (i = 0; i < this.t.length && this.t[i] == o.t[i]; i++);
      if (i < this.t.length) {
        return this.t[i] - o.t[i];
      }
      return 0;
    }

    int last() {
      return t[t.length - 1];
    }

    @Override
    public String toString() {
      return Arrays.toString(t);
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 89 * hash + Arrays.hashCode(this.t);
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
      final Tuple other = (Tuple) obj;
      if (!Arrays.equals(this.t, other.t)) {
        return false;
      }
      return true;
    }

  }

  static class TupleFamily implements Comparable<TupleFamily> {

    ArrayList<Tuple> tuples;

    TupleFamily() {
      this.tuples = new ArrayList<Tuple>();
    }

    TupleFamily(ArrayList<Tuple> tuples) {
      ArrayList<Tuple> tupleSource = new ArrayList(new HashSet<Tuple>(tuples));
      Collections.sort(tupleSource);
      Collections.reverse(tupleSource);
      this.tuples = new ArrayList<>();
      for(Tuple t : tupleSource) {
        boolean contained = false;
        for(Tuple s : this.tuples) {
          contained |= s.contains(t);
          if (contained) break;
        }
        if (!contained) {
          this.tuples.add(t);
        }
      }
      Collections.reverse(this.tuples);
    }

    @Override
    public int compareTo(TupleFamily o) {
      if (this.tuples.size() != o.tuples.size()) {
        return this.tuples.size() - o.tuples.size();
      }
      for (int i = 0; i < this.tuples.size(); i++) {
        int d = this.tuples.get(i).compareTo(o.tuples.get(i));
        if (d != 0) {
          return d;
        }
      }
      return 0;
    }

    TupleFamily apply(Permutation p) {
      TupleFamily result = new TupleFamily();
      for (Tuple tuple : this.tuples) {
        result.tuples.add(new Tuple(tuple.t, p));
      }
      Collections.sort(result.tuples);
      return result;
    }

    TupleFamily representative() {
      return this;
//      TupleFamily result = this;
//      for (Permutation p : new Permutations(N)) {
//        TupleFamily n = this.apply(p);
//        if (n.compareTo(result) < 0) {
//          result = n;
//        }
//      }
//      return result;
    }
    
//    TupleFamily representative() {
//      HashMap<Integer, Integer> map = new HashMap<>();
//      HashSet<Integer> values = new HashSet<>();
//      int i = 0;
//      for(Tuple tuple : this.tuples) {
//        for(int j : tuple.t) {
//          if (!map.containsKey(j)) {
//            map.put(j, i);
//            i++;
//          }
//          if (i >= N) break;
//        }
//        if (i >= N) break;     
//      }
//      for(int v = 0; v < N; v++) {
//        if (!values.contains(v)) {
//          map.put(v, i);
//          i++;
//        }
//      }
//      Permutation p = new Permutation(N);
//      for(int index = 0; i < N; i++) {
//        p.elements[index] = map.get(index);
//      }
//      ArrayList<Tuple> newTuples = new ArrayList<>();
//      for(Tuple tuple: this.tuples) {
//        newTuples.add(new Tuple(tuple.t, p));
//      }
//      return new TupleFamily(tuples);
//      
//    }

    // What new family is needed when we add 'value' to the sequence -- note for convenience we're working right to left effectively.
    TupleFamily update(int value) {
      ArrayList<Tuple> tuples = new ArrayList<>();
      for (Tuple tu : this.tuples) {
        Tuple tuu = tu.update(value);
        if (tuu.t.length > 0) {
          tuples.add(tuu);
        }
      }
      return new TupleFamily(tuples);
    }

    HashSet<TupleFamily> updates() {
      HashSet<TupleFamily> result = new HashSet<>();
      for (Tuple tu : this.tuples) {
        result.add(this.update(tu.last()));
      }
      return result;
    }

    int seqLength() {
      TupleFamily rep = this.representative();
      if (seqLength.containsKey(rep)) {
        return seqLength.get(rep);
      }

      ArrayDeque<TupleFamily> stack = new ArrayDeque<>();
      stack.push(rep);
      int count = 1;
      while (!stack.isEmpty()) {
        if (count % 10000 == 0) System.out.println(count);
        count++;
        // System.out.println(stack.size());
        TupleFamily fam = stack.pop();
        // System.out.println(fam);
        if (seqLength.containsKey(fam)) {
          continue;
        }
        boolean childrenKnown = true;
        int value = Integer.MAX_VALUE;
        for (TupleFamily child : fam.updates()) {
          TupleFamily cRep = child.representative();
            if (childrenKnown) {
            if (seqLength.containsKey(cRep)) {
              value = Math.min(value, seqLength.get(cRep) + 1);
            } else {
              childrenKnown = false;
              stack.push(fam);
              stack.push(cRep);
            }
          } else {
            if (!seqLength.containsKey(cRep)) {
              stack.push(cRep);
            }
          }
        }
        if (childrenKnown) {
          seqLength.put(fam, value);
        }
      }
      return seqLength.get(rep);
//      int result = Integer.MAX_VALUE;
//      for(TupleFamily tuf : this.updates()) {
//        // System.out.print("seqL " + tuf);
//        int v = tuf.seqLength();
//        // System.out.println(" " + v);
//        if (v < result) result = v + 1;
//      }
//      seqLength.put(this, result);
//      return result;
    }

    @Override
    public String toString() {
      return "TupleFamily{" + "tuples=" + tuples + '}';
    }

    @Override
    public int hashCode() {
      int hash = 7;
      hash = 17 * hash + Objects.hashCode(this.tuples);
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
      final TupleFamily other = (TupleFamily) obj;
      if (!Objects.equals(this.tuples, other.tuples)) {
        return false;
      }
      return true;
    }

  }

  public static void main(String[] args) {
    ArrayList<Tuple> tus = new ArrayList<>();
    PermutationClass c = new PermutationClass("321");
    for (Permutation p : new Permutations(c,N)) {
      tus.add(new Tuple(p.elements));
    }
    TupleFamily sn = new TupleFamily(tus);
    System.out.println(sn);
    System.out.println(sn.seqLength());

  }

}
