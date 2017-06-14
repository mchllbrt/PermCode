package permlib.examples;

import permlib.Permutation;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static permlib.PermUtilities.inverse;

/**
 * Created by Jinge Li on 2016-01-18.
 */
public class Av321Helper {

    Permutation p = new Permutation();
    Permutation t = new Permutation();

    int[] nru;
    int[] nrl;
    int[] nau;
    int[] nal;

    int[] f;
    boolean[] problems;
    Deque<Integer> queue = new ArrayDeque<>();

    private boolean hasEmbedding;

    public boolean hasEmbedding() {
        return hasEmbedding;
    }

    public int[] getEmbedding() {
        return f;
    }

    /**
     * Helper class that takes in p and t
     *
     * @param useNewInitialMapping true if using new f0 for embedding test.
     */
    public Av321Helper(Permutation p, Permutation t, boolean useNewInitialMapping) {
        this.p = p;
        this.t = t;
        computeArrays();

        if (useNewInitialMapping) {
            newInitialise();
        } else {
            initialise();
        }

        buildEmbedding();
    }

    /**
     * computes and stores the arrays needed for p and t
     */
    private void computeArrays() {
        nru = nextRightUpper(t);
        nrl = nextRightLower(t);
        nau = nextAboveUpper(t);
        nal = nextAboveLower(t);
    }

    /**
     * Initialise the map f, queue, and problems.
     */
    private void initialise() {
        f = initialMapping(p, t);
        problems = initialProblems(p);
    }

    /**
     * Initialise the new mapping.
     */
    private void newInitialise() {
        f = newInitialMapping(p, t);
        problems = initialProblems(p);
    }

    private void buildEmbedding() {
        hasEmbedding = initialMapped(p.elements);
        while (hasEmbedding && !queue.isEmpty()) {
            hasEmbedding = update();
        }
    }

    /**
     * next element left, right(Upper and Lower), up(Upper and Lower), down e.t.c.
     */
    public int nextRightUpper(int i) {
        return nru[i];
    }

    public int nextRightLower(int i) {
        return nrl[i];
    }

    public int nextAboveUpper(int i) {
        return nau[i];
    }

    public int nextAboveLower(int i) {
        return nal[i];
    }

    public int nextLeft(int i) {
        return i - 1;
    }

    public int nextRight(int i) {
        return i + 1;
    }

    public int nextBelow(int i) {
        if (p.elements[i] > 0) {
            return inverse(p.elements)[p.elements[i] - 1];
        } else {
            return -1;
        }
    }

    public int nextAbove(int i) {
        if (p.elements[i] < p.length() - 1) {
            return inverse(p.elements)[p.elements[i] + 1];
        } else {
            return p.length();
        }
    }

    /**
     * test cases
     */
    public static void main(String[] args) {
        Permutation p = new Permutation("312");
        Permutation t = new Permutation("34152");
        Av321Helper h = new Av321Helper(p, t, true);

        System.out.println("Whether " + p + " is mapped to " + t + ": " + h.initialMapped(p.elements));
        System.out.println("initialMapping from " + p + " to " + t + ": " + Arrays.toString(h.newInitialMapping(p, t)));
        System.out.println("Whether embeddings exist from " + p + " to " + t + ": " + h.hasEmbedding());
        System.out.println("Embedding: " + Arrays.toString(h.getEmbedding()));

    }

    private static final int UPPER_ELEMENT = 1;
    private static final int LOWER_ELEMENT = -1;
    private static final int FLUID_ELEMENT = 0;

    public static int[] type(int[] p) {
        int[] result = new int[p.length];
        int maxSeen = -1;
        int blockBegin = 0;
        for (int i = 0; i < p.length; i++) {
            if (p[i] > maxSeen) {
                maxSeen = p[i];
            } else {
                result[i] = LOWER_ELEMENT;
                int j = i - 1;
                while (j >= blockBegin && p[j] > p[i]) {
                    result[j] = UPPER_ELEMENT;
                    j--;
                }
                blockBegin = i + 1;
            }

        }

        return result;
    }

    public int[] type(Permutation p) {
        return type(p.elements);
    }

    /**
     * returns the array of indices of the next right Upper element.
     */
    public int[] nextRightUpper(int[] p) {
        int[] elementType = type(p);
        int[] nextUpper = new int[p.length];
        Arrays.fill(nextUpper, p.length); // picked this magic number
        for (int i = p.length - 1; i > 0; i--) {
            if (elementType[i] == 1) {
                nextUpper[i - 1] = i;
            }
            if (elementType[i] == -1) {
                nextUpper[i - 1] = nextUpper[i];
            }
            if (elementType[i] == 0) {
                nextUpper[i - 1] = nextUpper[i];
            }
        }
        return nextUpper;
    }

    /**
     * returns the array of indices of the next right Lower element.
     */
    public int[] nextRightLower(int[] p) {
        int[] elementType = type(p);
        int[] nextLower = new int[p.length];
        Arrays.fill(nextLower, p.length); // same here
        for (int i = p.length - 1; i > 0; i--) {
            if (elementType[i] == 1) {
                nextLower[i - 1] = nextLower[i];
            }
            if (elementType[i] == -1) {
                nextLower[i - 1] = i;
            }
            if (elementType[i] == 0) {
                nextLower[i - 1] = nextLower[i];
            }
        }
        return nextLower;
    }

    public int[] nextRightUpper(Permutation p) {
        return nextRightUpper(p.elements);
    }

    public int[] nextRightLower(Permutation p) {
        return nextRightLower(p.elements);
    }

    /**
     * returns the array of indices of the next above Lower element.
     */
    public int[] nextAboveLower(int[] p) {
        int[] nextLower = new int[p.length];
        int[] invp = inverse(p);
        int[] inv = nextRightUpper(invp);
        int[] findIndex = new int[p.length];
        for (int i = 0; i < p.length; i++) {
            if (inv[i] >= p.length) {
                findIndex[i] = p.length;
            } else {
                findIndex[i] = invp[inv[i]];
            }
        }
        for (int j = 0; j < p.length; j++) {
            nextLower[j] = findIndex[p[j]];
        }
        return nextLower;
    }

    /**
     * returns the array of indices of the next above Upper element.
     */
    public int[] nextAboveUpper(int[] p) {
        int[] nextUpper = new int[p.length];
        int[] invp = inverse(p);
        int[] inv = nextRightLower(invp);
        int[] findIndex = new int[p.length];
        for (int i = 0; i < p.length; i++) {
            if (inv[i] >= p.length) {
                findIndex[i] = p.length;
            } else {
                findIndex[i] = invp[inv[i]];
            }
        }
        for (int j = 0; j < p.length; j++) {
            nextUpper[j] = findIndex[p[j]];
        }
        return nextUpper;
    }

    public int[] nextAboveLower(Permutation p) {
        return nextAboveLower(p.elements);
    }

    public int[] nextAboveUpper(Permutation p) {
        return nextAboveUpper(p.elements);
    }

    /**
     * defining f_0 as an array which maps each p[i] in p to f0[i] in t.
     */
    public int[] initialMapping(int[] p, int[] t) {
        int[] f0 = new int[p.length];
        int upperTracker = 0;
        int lowerTracker = 0;
        for (int i = 0; i < p.length; i++) {
            if (type(p)[i] == 1) {
                for (int j = upperTracker; j < t.length; j++) {
                    if (type(p)[i] == type(t)[j]) {
                        f0[i] = j;
                        upperTracker = j + 1;
                        break;
                    }
                }
            }
            if (type(p)[i] == -1) {
                for (int j = lowerTracker; j < t.length; j++) {
                    if (type(p)[i] == type(t)[j]) {
                        f0[i] = j;
                        lowerTracker = j + 1;
                        break;
                    }
                }
            }
        }
        return f0;
    }

    public int[] initialMapping(Permutation p, Permutation t) {
        return initialMapping(p.elements, t.elements);
    }

    /**
     * check whether there is a valid initial mapping from p to t.
     */
    public boolean initialMapped(int[] p) {
        int zeroCounter = 0;
        for (int j = 0; j < p.length; j++) {
            if (f[j] == 0) { // remember to change it back to f[j] after testing
                zeroCounter++;
            }
            if (zeroCounter > 1) {
                break;
            }
        }
        return zeroCounter < 2;
    }

    /**
     * modified initial mapping f0 that always make use of the last two elements
     * remark: does it really always make use of the last two?
     * Counterexample? 2456173 is not involved in 35671892 (34561782), but is involved in t = 356718924,
     * while the embedding doesn't make use of the 2 in t.
     */
    public int[] newInitialMapping(int[] p, int[] t) {
        int[] f0 = new int[p.length];
        int lastElement1 = 0;
//        int lastElement2 = 0;
        for (int i = p.length - 1; i >= 0; i--) {
            if (type(t)[t.length - 1] == type(p)[i]) {
                f0[i] = t.length - 1;
                lastElement1 = i;
                break;
            }
        }
//        for (int j = p.length - 1; j >= 0; j--) {
//            if (j != lastElement1 && type(t)[t.length - 2] == type(p)[j]) {
//                f0[j] = t.length - 2;
//                lastElement2 = j;
//                break;
//            }
//        }
        int upperTracker = 0;
        int lowerTracker = 0;
        for (int i = 0; i < p.length; i++) {
//            if (i == lastElement1 || i == lastElement2) { // this part has bugs on p = 321
//                continue;
//            }
            if (i == lastElement1) {
                continue;
            }
            if (type(p)[i] == 1) {
                for (int j = upperTracker; j < t.length; j++) {
                    if (type(p)[i] == type(t)[j]) {
                        f0[i] = j;
                        upperTracker = j + 1;
                        break;
                    }
                }
            }
            if (type(p)[i] == -1) {
                for (int j = lowerTracker; j < t.length; j++) {
                    if (type(p)[i] == type(t)[j]) {
                        f0[i] = j;
                        lowerTracker = j + 1;
                        break;
                    }
                }
            }
        }
        return f0;
    }

    public int[] newInitialMapping(Permutation p, Permutation t) {
        return newInitialMapping(p.elements, t.elements);
    }

    /**
     * max of two mappings
     */
    public int minPossibleValue(int[] f, int i) {
        int first = -1;
        int second = -1;
        if (i == 0) {
            if (type(p)[i] == 1) {
                return nextAboveUpper(f[nextBelow(i)]);
            }
            if (type(p)[i] == -1) {
                return nextAboveLower(f[nextBelow(i)]);
            }
        } else if (nextBelow(i) == -1) {
            return nextRightLower(f[nextLeft(i)]);
        } else {
            if (type(p)[i] == 1) {
                first = nextRightUpper(f[nextLeft(i)]);
                second = nextAboveUpper(f[nextBelow(i)]);
            }
            if (type(p)[i] == -1) {
                first = nextRightLower(f[nextLeft(i)]);
                second = nextAboveLower(f[nextBelow(i)]);
            }
        }
        return Math.max(first, second);
    }

    /**
     * initialising the set of Problems
     */
    public boolean[] initialProblems(int[] f, int[] p) {
        boolean[] problems = new boolean[p.length];
        for (int i = 0; i < p.length; i++) {
            if (f[i] < minPossibleValue(f, i)) {
                problems[i] = true;
                queue.add(i);
            } else {
                problems[i] = false;
            }
        }
        this.problems = problems;
        return problems;
    }

    public boolean[] initialProblems(Permutation p) {
        return initialProblems(f, p.elements);
    }

    /**
     * update the set of problems when an existing problem at i is solved.
     */
    public void updateProblems(int[] f, int i) {
        int iUp = nextAbove(i);
        int iRight = nextRight(i);
        if (iUp < p.length()) {
            if (!problems[iUp] && f[iUp] < minPossibleValue(f, iUp)) {
                problems[iUp] = true;
                queue.add(iUp);
            }
        }
        if (iRight < p.length()) {
            if (!problems[iRight] && f[iRight] < minPossibleValue(f, iRight)) {
                problems[iRight] = true;
                queue.add(iRight);
            }
        }
    }

    /**
     * Update operations
     * 1. Remove first element, x of q
     * 2. Set problems[x] to false;
     * 3. Set f[x] to max(...)
     * 4. if (f[x] >= t.length()) return false;
     * 5. Check for new problems and add to q, problems if necessary
     * 6. return true;
     */
    private boolean update() {
        int i = queue.remove();
        problems[i] = false;
        f[i] = minPossibleValue(f, i);
        if (f[i] >= t.length()) {
            return false;
        }
        updateProblems(f, i);
        return true;
    }

}
