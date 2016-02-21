package permlib;

import permlib.classes.PermutationClass;
import java.util.*;
import permlib.property.*;
import permlib.utilities.Combinations;
import permlib.utilities.IntPair;
import permlib.utilities.InvolutionUtilities;

/**
 * This class contains various utility methods that apply to permutations. It's
 * a bit unwieldy but should be thought of as the equivalent of "Math", i.e. all
 * the general purpose methods that apply to permutations belong in here.
 * Methods that simply compute statistics of permutations (e.g. inversions) can
 * be found in PermStatistics.
 *
 * @author Michael Albert
 */
public class PermUtilities {

    public static final boolean SAFE = true;
    public static final int MAX_PERM_SIZE = 100;
    public static final Simple SIMPLE = new Simple();
    public static final Involution INVOLUTION = new Involution();
    public static final MinusIndecomposable MINUSINDECOMPOSABLE = new MinusIndecomposable();
    public static final PlusIndecomposable PLUSINDECOMPOSABLE = new PlusIndecomposable();
    public static final MinusIrreducible MINUSIRREDUCIBLE = new MinusIrreducible();
    public static final PlusIrreducible PLUSIRREDUCIBLE = new PlusIrreducible();
    public static final PermProperty[] MAIN_PROPERTIES = new PermProperty[]{
        SIMPLE,
        PLUSINDECOMPOSABLE,
        MINUSINDECOMPOSABLE,
        PLUSIRREDUCIBLE,
        MINUSIRREDUCIBLE};
    static final Random R = new Random();

    /**
     * Computes the permutations that arise from extending a given permutation
     * on the right hand side by a single element.
     *
     * @param p the permutation to be extended
     * @return an {@link Iterable Iterable} representing the extensions
     */
    public static Iterable<Permutation> rightExtensions(Permutation p) {
        // Voodoo, -1L is FF....F
        return rightExtensions(p, -1L);
    }

    public static Iterable<Permutation> rightExtensions(Permutation p, long extensionLocations) {
        ArrayList<Permutation> result = new ArrayList<Permutation>();
        int[] values = new int[p.length() + 1];
        System.arraycopy(p.elements, 0, values, 0, p.elements.length);
        for (int i = -1; i < p.length(); i++) {
            if (((extensionLocations >> (i + 1)) & 1L) != 0) {
                values[p.length()] = i;
                result.add(new Permutation(values));
            }
        }
        return result;
    }

    /**
     * Computes the exceptional simple permutations of length <code>n</code>
     * (for <code>n</code> even, the empty set otherwise)
     *
     * @param n the length
     * @return an array containing the exceptional simples of length
     * <code>n</code>
     */
    public static Permutation[] exceptionalSimples(int n) {
        if (n % 2 != 0) {
            return new Permutation[0];
        }

        if (n == 4) {
            return new Permutation[]{new Permutation("3142"), new Permutation("2413")};
        }

        Permutation[] result = new Permutation[4];
        int k = n / 2;
        int[] values = new int[2 * k];
        for (int i = 0; i < values.length; i++) {
            if (i % 2 == 0) {
                values[i] = k + i / 2;
            } else {
                values[i] = i / 2;
            }
        }
        result[0] = new Permutation(values);
        int left = 0;
        int right = values.length - 1;
        while (left < right) {
            int t = values[left];
            values[left] = values[right];
            values[right] = t;
            left++;
            right--;
        }
        result[1] = new Permutation(values);
        result[2] = result[0].inverse();
        result[3] = result[1].inverse();
        return result;
    }

    /**
     * Computes the result of deleting the element at index <code>i</code> in
     * this permutation.
     *
     * @param p the permutation
     * @param index the index
     * @return the result of deleting the element at position <code>index</code>
     * from <code>p</code>
     */
    public static Permutation delete(Permutation p, int index) {
        int v = p.elements[index];
        int[] q = new int[p.length() - 1];
        for (int i = 0; i < index; i++) {
            q[i] = p.elements[i] - (p.elements[i] > v ? 1 : 0);
        }
        for (int i = index + 1; i < p.elements.length; i++) {
            q[i - 1] = p.elements[i] - (p.elements[i] > v ? 1 : 0);
        }
        return new Permutation(q, SAFE);
    }

    /**
     * Computes the set of all one element deletions of a permutation.
     *
     * @param p the permutation
     * @return the set of all one element deletions of <code>p</code>
     */
    public static HashSet<Permutation> deletions(Permutation p) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for (int i = 0; i < p.length(); i++) {
            result.add(delete(p, i));
        }
        return result;
    }

    /**
     * Determines whether a permutation is simple. NB: This creates a new simple
     * object for every test to ensure that the test is thread-safe. If this is
     * not an issue it would be more efficient to just create one Simple object
     * and use that as a test instead.
     *
     * @param p the permutation
     * @return <code>true</code> if <code>p</code> is simple, otherwise
     * <code>false</code>
     */
    public static boolean isSimple(Permutation p) {
        return (new Simple()).isSatisfiedBy(p);
    }

    /**
     * Determines whether a permutation is an involution.
     *
     * @param p the permutation
     * @return <code>true</code> if <code>p</code> is involution, otherwise
     * <code>false</code>
     */
    public static boolean isInvolution(Permutation p) {
        return InvolutionUtilities.isInvolution(p);
    }

    /**
     * Computes the result of inserting <code>value</code> at position
     * <code>index</code> in a permutation. Specifically, all the elements of
     * the permutation which are greater than or equal to <code>value</code> are
     * increased by one.
     *
     * @param p the permutation
     * @param index the 0-based position of the inserted element
     * @param value the 0-based value of the inserted element
     * @return the result of inserting <code>value</code> at
     * <code>position</code> in <code>p</code>
     */
    public static Permutation insert(Permutation p, int index, int value) {
        value = (value < 0 ? 0 : value);
        value = (value > p.elements.length ? p.elements.length : value);
        Permutation result = new Permutation(p.elements.length + 1);
        for (int i = 0; i < index; i++) {
            result.elements[i] = p.elements[i] + (p.elements[i] >= value ? 1 : 0);
        }
        result.elements[index] = value;
        for (int i = index + 1; i <= p.elements.length; i++) {
            result.elements[i] = p.elements[i - 1] + (p.elements[i - 1] >= value ? 1 : 0);
        }
        return result;
    }

    /**
     * Computes the result of replacing the element at position
     * <code>index</code> in <code>p</code> by an interval isomorphic to the
     * permutation <code>q</code>.
     *
     * @param p the base permutation
     * @param index the index where replacement is to take place
     * @param q the permutation to be used in the replacement
     * @return the resulting permutation
     */
    public static Permutation replace(Permutation p, int index, Permutation q) {
        Permutation result = new Permutation(p.elements.length + q.elements.length - 1);
        for (int i = 0; i < index; i++) {
            result.elements[i] = p.elements[i] + (p.elements[i] > p.elements[index] ? q.length() - 1 : 0);
        }
        for (int i = index; i < index + q.length(); i++) {
            result.elements[i] = q.elements[i - index] + p.elements[index];
        }
        for (int i = index + q.length(); i < p.length() + q.length() - 1; i++) {
            result.elements[i] = p.elements[i - q.length() + 1] + (p.elements[i - q.length() + 1] > p.elements[index] ? q.length() - 1 : 0);
        }
        return result;
    }

    /**
     * Computes the result of inserting a copy of a permutation into another
     * permutation at a specified index and 0-based value. For example inserting
     * the permutation 10 in 021 at position 1 with value 1 gives 0 21 4 3.
     *
     * @param p the permutation in which insertion takes place
     * @param index the first index of the inserted elements
     * @param value the relative value of the inserted elements
     * @param q the pattern of the inserted elements
     * @return the result of the insertion
     */
    public static Permutation insert(Permutation p, int index, int value, Permutation q) {
        return replace(insert(p, index, value), index, q);
    }

    /**
     * Computes the minimal elements of a set <code>perms</code> of permutations
     * with respect to involvement.
     *
     * @param perms the permutations
     * @return the minimal permutations in <code>perms</code>
     */
    public static Collection<Permutation> getMinimals(Collection<Permutation> perms) {
        Permutation[] sortedPermutations = perms.toArray(new Permutation[0]);
        Arrays.sort(sortedPermutations);
        ArrayList<Permutation> minimals = new ArrayList<Permutation>();
        ArrayList<Involves> involves = new ArrayList<Involves>();
        for (Permutation p : sortedPermutations) {
            boolean dropP = false;
            for (Involves i : involves) {
                dropP |= i.isSatisfiedBy(p);
                if (dropP) {
                    break;
                }
            }
            if (dropP) {
                continue;
            }
            minimals.add(p);
            involves.add(new Involves(p));
        }
        return minimals;
    }

    /**
     * Computes the sum indecomposable components of a permutation.
     *
     * @param p the permutation
     * @return an array of the sum indecomposable components of <code>p</code>
     */
    public static Permutation[] plusComponents(Permutation p) {
        ArrayList<Permutation> result = new ArrayList<Permutation>();
        Permutation q = p.clone();
        while (q.length() > 0) {
            int maxSeen = Integer.MIN_VALUE;
            int index = 0;
            while (index < q.elements.length) {
                maxSeen = (maxSeen > q.elements[index]) ? maxSeen : q.elements[index];
                index++;
                if (index == maxSeen + 1) {
                    break;
                }
            }
            result.add(q.segment(0, index));
            q = q.segment(index, q.length());
        }
        return result.toArray(new Permutation[0]);
    }

    /**
     * Computes the sum of two permutations.
     *
     * @param p the first permutation
     * @param q the second permutation
     * @return their sum
     */
    public static Permutation sum(Permutation p, Permutation q) {
        int[] elements = Arrays.copyOf(p.elements, p.length() + q.length());
        for (int i = p.length(); i < p.length() + q.length(); i++) {
            elements[i] = q.elements[i - p.length()] + p.length();
        }
        return new Permutation(elements, true);
    }

    /**
     * Computes the skew sum of two permutations.
     *
     * @param p the first permutation
     * @param q the second permutation
     * @return their skew sum
     */
    public static Permutation skewSum(Permutation p, Permutation q) {
        int[] elements = new int[p.length() + q.length()];
        for (int i = 0; i < p.length(); i++) {
            elements[i] = p.elements[i] + q.length();
        }
        for (int i = p.length(); i < p.length() + q.length(); i++) {
            elements[i] = q.elements[i - p.length()];
        }
        return new Permutation(elements, true);
    }

    /**
     * Returns the result of composing two permutations.
     *
     * @param p the first permutation
     * @param q the second permutation
     * @return their composition p(q)
     */
    public static Permutation compose(Permutation p, Permutation q) {
        if (p.length() != q.length()) {
            return null;
        }
        int[] elements = new int[p.length()];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = p.elements[q.elements[i]];
        }
        return new Permutation(elements, SAFE);
    }

    /**
     * Computes the collection of all permutations that are the merge of two
     * given permutations.
     *
     * @param p the first permutation
     * @param q the second permutation
     * @return the collection of all merges of the two permutations
     */
    public static Collection<Permutation> merge(Permutation p, Permutation q) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        int n = p.length() + q.length();
        for (int[] pIndices : new Combinations(n, p.length())) {
            int[] qIndices = Combinations.complement(n, pIndices);
            for (int[] pValues : new Combinations(n, p.length())) {
                int[] qValues = Combinations.complement(n, pValues);
                int[] v = new int[n];
                int pIndex = 0;
                int qIndex = 0;
                for (int i = 0; i < n; i++) {
                    if (pIndex < pIndices.length && pIndices[pIndex] == i) {
                        v[i] = pValues[p.elements[pIndex]];
                        pIndex++;
                    } else {
                        v[i] = qValues[q.elements[qIndex]];
                        qIndex++;
                    }
                }
                result.add(new Permutation(v, SAFE));
            }
        }
        return result;
    }

    /**
     * Returns the RSK tableau (P-type) associated to a permutation.
     *
     * @param p the permutation
     * @return the tableau (as an <code>int[][]</code> in "English" form)
     */
    public static int[][] tableau(Permutation p) {
        ArrayList<ArrayList<Integer>> tableau = new ArrayList<ArrayList<Integer>>();
        for (int i : p.elements) {
            System.out.println(i);
            RSKBump(tableau, i);
        }
        int[][] result = new int[tableau.size()][];
        int i = 0;
        for (ArrayList<Integer> row : tableau) {
            result[i] = new int[row.size()];
            for (int j = 0; j < row.size(); j++) {
                result[i][j] = row.get(j);
            }
            i++;
        }
        return result;
    }
    
    public static int LISLength(Permutation p) {
        ArrayList<Integer> row = new ArrayList<>();
        for(int v : p.elements) {
            int i = 0;
            while (i < row.size() && v > row.get(i)) {
                i++;
            }
            if (i < row.size()) {
                row.set(i, v);
            } else {
                row.add(v);
            }
         }
        return row.size();
    }
    
    public static int LDSLength(Permutation p) {
        return LISLength(p.reverse());
    }

    private static void RSKBump(ArrayList<ArrayList<Integer>> tableau, int valueToInsert) {
        int rowIndex = 0;
        while (rowIndex < tableau.size()) {
            int i = 0;
            ArrayList<Integer> row = tableau.get(rowIndex);
            while (i < row.size() && valueToInsert > row.get(i)) {
                i++;
            }
            if (i < row.size()) {
                int t = row.get(i);
                row.set(i, valueToInsert);
                valueToInsert = t;
            } else {
                row.add(valueToInsert);
                return;
            }
            rowIndex++;
        }
        ArrayList<Integer> newRow = new ArrayList<Integer>();
        newRow.add(valueToInsert);
        tableau.add(newRow);
    }

    /**
     * Returns an inferred basis for Sub(c) given a collection of permutations
     * c, where potential basis elements are examined to a given maximum length.
     * That is, we are returning the minimal elements of the set of permutations
     * up to that length which are avoided by all elements of c.
     *
     * @param c the collection of permutations
     * @param maxLength the maximum length basis element considered
     * @return the inferred basis
     */
    public static Collection<Permutation> inferredBasis(Collection<Permutation> c, int maxLength) {
        ArrayList<Permutation> result = new ArrayList<Permutation>();
        for (int n = 1; n <= maxLength; n++) {
            for (Permutation p : (new PermutationClass(result)).getPerms(n)) {
                HereditaryProperty a = PermUtilities.avoidanceTest(p, true);
                //AvoidanceTest a = PermUtilities.avoidanceTest(p);
                //Avoids a = new Avoids(p);
                boolean allAvoid = true;
                for (Permutation q : c) {
                    allAvoid &= a.isSatisfiedBy(q);
                    if (!allAvoid) {
                        break;
                    }
                }
                if (allAvoid) {
                    result.add(p);
                }
            }
        }
        return result;
    }

    /**
     * Computes the reverse of a permutation.
     *
     * @param p the permutation
     * @return the reverse of <code>p</code>
     */
    public static Permutation reverse(Permutation p) {
        return new Permutation(reverse(p.elements), SAFE);
    }

    /**
     * Computes the reverse of an array of integers.
     *
     * @param values the array
     * @return its reverse
     */
    public static int[] reverse(int[] values) {
        int[] result = new int[values.length];
        int j = values.length - 1;
        for (int i = 0; i < values.length; i++) {
            result[j--] = values[i];
        }
        return result;
    }

    /**
     * Computes the complement of a permutation.
     *
     * @param p the permutation
     * @return the complement of <code>p</code>
     */
    public static Permutation complement(Permutation p) {
        return new Permutation(complement(p.elements), SAFE);
    }

    /**
     * Computes the complement of an array of integers.
     *
     * @param values the array
     * @return its complement
     */
    public static int[] complement(int[] values) {
        int[] result = new int[values.length];
        int j = values.length - 1;
        for (int i = 0; i < values.length; i++) {
            result[i] = j - values[i];
        }
        return result;
    }

    /**
     * Computes the inverse of a permutation.
     *
     * @param p the permutation
     * @return the inverse of <code>p</code>
     */
    public static Permutation inverse(Permutation p) {
        return new Permutation(inverse(p.elements), SAFE);
    }

    /**
     * Computes the inverse of an array of integers provided that the array
     * represents a permutation.
     *
     * @param values the array
     * @return its inverse
     */
    public static int[] inverse(int[] values) {
        int[] result = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            result[values[i]] = i;
        }
        return result;
    }

    /**
     * Determines whether or not a permutation is the lex least element of its
     * symmetry class.
     * 
     * @param p the permutation
     * @return <code>true</code> if (and only if) <code>p</code> is lex least
     * in its symmetry class
     */
    public static boolean isSymmetryRep(Permutation p) {
        for (Symmetry s : Symmetry.values()) {
            if (s.on(p).compareTo(p) < 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Returns the symmetry representative for the given permutation.
     * 
     * @param p the permutation
     * @return <code>r</code>, the lex least permutation in the symmetry class
     * of <code>p</code>
     */
    public static Permutation symmetryRep(Permutation p) {
        Permutation r = p;
        for (Symmetry s : Symmetry.values()) {
            if (s.on(p).compareTo(r) < 0) {
                r = s.on(p);
            }
        }
        return r;
    }

    /**
     * Computes the set of common subpermutations of a collection of permutations.
     * @param source the collection of permutations
     * @return the set of permutations that occur in every permutation belonging to
     * <code>source</code>
     */
    public static Collection<Permutation> commonSubpermutations(Collection<Permutation> source) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        Deque<Permutation> queue = new ArrayDeque<Permutation>();
        queue.add(new Permutation(1));
        while (queue.size() > 0) {
            Permutation p = queue.pollFirst();
            Involves invp = new Involves(p);
            boolean pGood = true;
            for (Permutation q : source) {
                if (!invp.isSatisfiedBy(q)) {
                    pGood = false;
                    break;
                }
            }
            if (pGood) {
                result.add(p);
                for (Permutation q : rightExtensions(p)) {
                    queue.add(q);
                }
            }
        }

        return result;
    }

    /**
     * Returns the permutation defined by a set of points. Should points share a
     * common row or column the corresponding elements will form an increasing
     * sequence in the permutation.
     *
     * @param points a list of points
     * @return the permutation defined by the points
     */
    public static Permutation permFromPoints(List<IntPair> points) {
        IntPair[] pointsArray = new IntPair[points.size()];
        points.toArray(pointsArray);
        Arrays.sort(pointsArray);
        int[] elements = new int[pointsArray.length];
        for (int i = 0; i < pointsArray.length; i++) {
            elements[i] = pointsArray[i].getSecond();
        }
        return new Permutation(elements);
    }

    /**
     * Returns the avoidance test for a permutation represented as a string.
     * @param s string representation of a permutation
     * @return an avoidance test for that permutation
     */
    public static HereditaryProperty avoidanceTest(String s) {
        return avoidanceTest(new Permutation(s), true);
    }

    /**
     * Returns an avoidance test to avoid the input permutation (represented as
     * a string here).
     *
     * @param s A string representing the permutation to avoid.
     * @param useSpecialTests whether to check for the implemented special case
     * tests.
     * @return the avoidance test based on the input permutation.
     */
    public static HereditaryProperty avoidanceTest(String s, boolean useSpecialTests) {
        return avoidanceTest(new Permutation(s), useSpecialTests);
    }

    /**
     * Returns an avoidance test for the given permutation.
     * @param p the permutation
     * @return in avoidance test for <code>p</code>
     */
    public static HereditaryProperty avoidanceTest(Permutation p) {
        return AvoidanceTest.getTest(p, true);
    }

    /**
     * Returns an avoidance test to avoid the input permutation.
     *
     * @param p the permutation to avoid.
     * @param useSpecialTests whether to check for the implemented special case
     * tests.
     * @return the avoidance test based on the input permutation.
     */
    public static HereditaryProperty avoidanceTest(Permutation p, boolean useSpecialTests) {
        return AvoidanceTest.getTest(p, useSpecialTests);
    }

    /**
     * Returns the monotone increasing permutation of the given length.
     *
     * @param length a length
     * @return the monotone increasing permutation of that length
     */
    public static Permutation increasingPermutation(int length) {
        int[] elements = new int[length];
        for (int i = 0; i < length; i++) {
            elements[i] = i;
        }
        return new Permutation(elements, SAFE);
    }

    /**
     * Returns the monotone decreasing permutation of the given length.
     *
     * @param length a length
     * @return the monotone decreasing permutation of that length
     */
    public static Permutation decreasingPermutation(int length) {
        return reverse(increasingPermutation(length));
    }

    /**
     * Returns a randomly generated permutation of a given length using an array
     * shuffle method.
     *
     * @param length the length of permutation desired.
     * @return a random permutation of given length.
     */
    public static Permutation randomPermutation(int length) {
        int[] elements = new int[length];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = i;
        }
        Random r = new Random();
        for (int i = elements.length - 1; i >= 0; i--) {
            int randIndex = r.nextInt(i + 1);
            int temp = elements[i];
            elements[i] = elements[randIndex];
            elements[randIndex] = temp;
        }
        return new Permutation(elements, PermUtilities.SAFE);
    }

    /**
     * Returns the subpermutation of a permutation at a given (ordered) array of
     * indices.
     *
     * @param p a permutation
     * @param indices an ordered list of 0-based indices
     * @return the permutation that occurs in the permutation at the indices
     */
    public static Permutation subPermutation(Permutation p, int[] indices) {
        int[] values = new int[indices.length];
        int count = 0;
        for (int i : indices) {
            values[count++] = i;
        }
        return new Permutation(values);
    }

    /**
     * Returns the set of all subpermutations of a permutation
     * @param p a permutation
     * @return the set of subpermutations of <code>p</code>
     */
    public static HashSet<Permutation> subpermutations(Permutation p) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        HashSet<Permutation> toProcess = new HashSet<Permutation>();
        toProcess.add(p);
        result.add(p);
        for (int n = p.length(); n > 0; n--) {
            HashSet<Permutation> newProcess = new HashSet<Permutation>();
            for (Permutation q : toProcess) {
                newProcess.addAll(PermUtilities.deletions(q));
            }
            toProcess = newProcess;
            result.addAll(toProcess);
        }

        return result;
    }

    /**
     * Returns the set of subpermutations of a set of permutations.
     * @param ps the permutations
     * @return the set of subpermutations of <code>ps</code>
     */
    public static HashSet<Permutation> subpermutations(Collection<Permutation> ps) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for (Permutation p : ps) {
            result.addAll(PermUtilities.subpermutations(p));
        }
        return result;
    }

    public static Iterable<Permutation> onePointExtensions(Permutation p) {
        HashSet<Permutation> result = new HashSet<Permutation>();
        for(int i = 0; i <= p.length(); i++) {
            for(int j = 0; j <= p.length(); j++) {
                result.add(insert(p, i, j));
            }
        }
        return result;
    }

    public static Permutation selectByPosition(Permutation q, boolean[] toKeep) {
        int c = 0;
        for(int i = 0; i < toKeep.length; i++) {
            if (toKeep[i]) c++;
        }
        int[] elements = new int[c];
        int index = 0;
        for(int j = 0; j < q.length(); j++) {
            if (toKeep[j]) elements[index++] = q.elements[j];
        }
        return new Permutation(elements);
    }
    
    public static Permutation skeleton(Permutation p) {
        int[] mins = Arrays.copyOf(p.elements, p.elements.length);
        int[] maxs = Arrays.copyOf(p.elements, p.elements.length);
        int[] iLengths = new int[p.elements.length];
        for(int i = 1; i < p.elements.length-1; i++) {
            for(int j = p.elements.length-1; j >= i; j--) {
                mins[j] = Math.min(mins[j-1], p.elements[j]);
                maxs[j] = Math.max(maxs[j-1], p.elements[j]);
                if (maxs[j] - mins[j] == i) iLengths[j-i] = i;
            }    
        }
        ArrayList<Integer> vs = new ArrayList<Integer>();
        int i = 0;
        while (i < p.elements.length) {
            vs.add(p.elements[i]);
            i += iLengths[i]+1;
        }
        int[] result = new int[vs.size()];
        i = 0;
        for(int v : vs) result[i++] = v;
        return new Permutation(result);
    }
    
    
}
