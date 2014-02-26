package permlib.utilities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.property.PermProperty;

/**
 * This class represents a permutation that is restricted in some way. The
 * restrictions might include certain avoidance properties (as set by a basis),
 * simplicity, or other permutation properties. Finally, there may be
 * restrictions imposed by the user in defining certain regions where points may
 * not be added. Since the main use of this class is in static permutation
 * displays and a history mechanism is required there, operations which would
 * modify a restricted permutation actually return a new modified copy, that is,
 * the class is effectively immutable.
 *
 * @author Michael Albert
 */
public class RestrictedPermutation {

    String basisString;
    String permString;
    Permutation p;

    ArrayList<PermProperty> properties = new ArrayList<PermProperty>();
    boolean simple = false;
    boolean involution = false;
    HashSet<IntPair> forbiddenPairs = new HashSet<IntPair>();
    HashSet<IntPair> userForbiddenPairs = new HashSet<IntPair>();

    public static final int NO_MONOTONE_REQUIRMENTS = 0;
    public static final int FORBIDDEN = 1;
    public static final int SINGLETON = 2;
    public static final int INCREASING = 3;
    public static final int DECREASING = 4;

    /**
     * Constructor creates a new restricted permutation from the permutation
     * contained in <code>permString</code>, and with avoidance properties from
     * <code>basisString</code> and whether it is restricted as simple.
     *
     * @param basisString avoidance restrictions
     * @param permString the permutation represented
     * @param simple whether restricted to a simple permutation
     */
    public RestrictedPermutation(String basisString, String permString, boolean simple, boolean involution) {
        this.basisString = basisString;
        this.permString = permString;
        this.p = new Permutation(permString);
        addBasisProperties();
        this.simple = simple;
        this.involution = involution;
        if (simple) {
            properties.add(PermUtilities.SIMPLE);
        }
        if (involution) {
            properties.add(PermUtilities.INVOLUTION);
        }
        createForbiddenPairs();
    }

    /**
     * Returns the basis string containing avoidance restrictions.
     *
     * @return the basis represented as a string
     */
    public String getBasisString() {
        return basisString;
    }

    /**
     * Returns the underlying permutation being represented as a string.
     *
     * @return a string containing the underlying permutation
     */
    public String getPermString() {
        return permString;
    }

    /**
     * Returns whether the simple restriction is in effect.
     *
     * @return true if the simple restriction is in effect
     */
    public boolean isSimple() {
        return simple;
    }

    public boolean isInvolution() {
        return involution;
    }

    /**
     * Returns the underlying permutation being represented.
     *
     * @return the underlying permutation
     */
    public Permutation getPerm() {
        return p;
    }

    /**
     * Checks to see whether a given permutation satisfies the properties
     * required by this restricted permutation.
     *
     * @param other a permutation to test
     * @return true if the given permutation satisfies the properties of this
     * restricted permutation.
     */
    public boolean hasProperties(Permutation other) {
        for (PermProperty prop : properties) {
            if (!prop.isSatisfiedBy(other)) {
                return false;
            }
        }
        return true;
    }

    public boolean hasProperties() {
        return hasProperties(this.p);
    }

    /**
     * Creates forbidden pairs. Here the pairs represent grid location on the
     * display. That is, this method tests where the restricted permutation can
     * be extended.
     */
//    private void createForbiddenPairs() {
//        forbiddenPairs.clear();
//        for (int i = 0; i <= p.length(); i++) {
//            for (int j = 0; j <= p.length(); j++) {
//                Permutation q = p.insert(i, j);
//                if (!hasProperties(q)) {
//                    forbiddenPairs.add(new IntPair(i, j));
//                }
//            }
//        }
//    }
    private void createForbiddenPairs() {
        forbiddenPairs.clear();
        for (int i = 0; i <= p.length(); i++) {
            for (int j = 0; j <= p.length(); j++) {
                Permutation q = null;
                if (!involution) {
                    q = p.insert(i, j);
                } else {
                    q = InvolutionUtilities.insert(p, i, j);
                }
                if (!hasProperties(q)) {
                    forbiddenPairs.add(new IntPair(i, j));
                    if (involution && (i != j)) {
                        forbiddenPairs.add(new IntPair(j, i));
                    }
                }
            }
        }
    }

    /**
     * Checks whether the given location is forbidden by the required
     * properties.
     *
     * @param i location reference
     * @param j location reference
     * @return true if the properties forbid extension in this location.
     */
    public boolean propertiesForbid(int i, int j) {
        return forbiddenPairs.contains(new IntPair(i, j));
    }

    /**
     * Checks whether the given location is forbidden by user restrictions.
     *
     * @param i location reference
     * @param j location reference
     * @return true if the user restrictions forbid extension in this location.
     */
    public boolean userForbids(int i, int j) {
        return userForbiddenPairs.contains(new IntPair(i, j));
    }

    /**
     * Checks whether the given location is forbidden by either the properties
     * or the user restrictions.
     *
     * @param i location reference
     * @param j location reference
     * @return true if forbidden by user restrictions or properties
     */
    public boolean forbids(int i, int j) {
        return userForbids(i, j) || propertiesForbid(i, j);
    }

    /**
     * Checks the monotone requirements at a given location. The options are
     * represented as and returned with the following integer encodings,
     *
     * NO_MONOTONE_REQUIRMENTS = 0 FORBIDDEN = 1 SINGLETON = 2 INCREASING = 3
     * DECREASING = 4
     *
     * @param i location reference
     * @param j location reference
     * @return an integer encoding of a requirement.
     */
    public int montoneRequirements(int i, int j) {
        if (forbids(i, j)) {
            return FORBIDDEN;
        }
        boolean mustIncrease = !hasProperties(PermUtilities.insert(p, i, j, new Permutation("21")));
        boolean mustDecrease = !hasProperties(PermUtilities.insert(p, i, j, new Permutation("12")));
        if (mustIncrease && mustDecrease) {
            return SINGLETON;
        }
        if (mustIncrease) {
            return INCREASING;
        }
        if (mustDecrease) {
            return DECREASING;
        }
        return NO_MONOTONE_REQUIRMENTS;
    }

    /**
     * Creates a user restriction at a given position and returns the resulting
     * restricted permutation.
     *
     * @param i location reference
     * @param j location reference
     * @return a restricted permutation with the new restriction
     */
    public RestrictedPermutation addUserForbiddenPair(int i, int j) {
        RestrictedPermutation result = this.copy();
        result.userForbiddenPairs.add(new IntPair(i, j));
        if (involution && i != j) {
            result.userForbiddenPairs.add(new IntPair(j, i));
        }
        return result;
    }

    /**
     * Removes a user restriction at a given position and returns the resulting
     * restricted permutation.
     *
     * @param i location reference
     * @param j location reference
     * @return a restricted permutation with the restriction removed
     */
    public RestrictedPermutation removeUserForbiddenPair(int i, int j) {
        RestrictedPermutation result = this.copy();
        result.userForbiddenPairs.remove(new IntPair(i, j));
        if (involution) {
            result.userForbiddenPairs.remove(new IntPair(j, i));
        }
        return result;
    }

    /**
     * Replaces the element at the given position <code>i</code> by an interval
     * isomorphic to the permutation <code>rep</code>.
     *
     * @param i the position of the element to replace
     * @param rep the permutation to insert
     * @return the modified restricted permutation
     */
    public RestrictedPermutation replacePoint(int i, Permutation rep) {
        RestrictedPermutation result = this.copy();
        result.p = PermUtilities.replace(this.p, i, rep);
        result.permString = result.p.toString();
        result.createForbiddenPairs();
        result.userForbiddenPairs.clear();
        for (int r = 0; r < p.length() + rep.length(); r++) {
            for (int c = 0; c < p.length() + rep.length(); c++) {
                if (shouldForbid(r, c, i, p.elements[i], rep.length())) {
                    result.userForbiddenPairs.add(new IntPair(c, r));
                }
            }
        }
        return result;
    }

    /**
     * Inserts an interval isomorphic to the permutation <code>rep</code> at the
     * given location.
     *
     * @param i location reference
     * @param j location reference
     * @param rep permutation to insert
     * @return the modified restricted permutation
     */
    public RestrictedPermutation replaceSpace(int i, int j, Permutation rep) {
        RestrictedPermutation result = this.copy().addPoint(i, j);
        return result.replacePoint(i, rep);
    }

    /**
     * Modifies the underlying permutation this restricted permutation is based
     * on.
     *
     * @param permString the new permutation
     * @return the modified restricted permutation
     */
    public RestrictedPermutation changePerm(String permString) {
        RestrictedPermutation result = new RestrictedPermutation(this.basisString, permString, simple, involution);
        return result;
    }

    /**
     * Updates the basis. That is, the avoidance properties.
     *
     * @param basisString new basis
     * @return modified restricted permutation
     */
    public RestrictedPermutation updateBasis(String basisString) {
        RestrictedPermutation result = this.copy();
        result.basisString = basisString;
        result.properties.clear();
        result.addBasisProperties();
        result.createForbiddenPairs();
        return result;
    }

    /**
     * Sets whether the simple restriction is in effect.
     *
     * @param isSimple whether to have the simple restriction or not
     * @return modified restricted permutation
     */
    public RestrictedPermutation setSimpleState(boolean isSimple) {
        RestrictedPermutation result = this.copy();
        if (isSimple == simple) {
            return result;
        }
        if (isSimple) {
            result.simple = true;
            result.properties.add(PermUtilities.SIMPLE);
        } else {
            result.simple = false;
            result.properties.remove(PermUtilities.SIMPLE);
        }
        result.createForbiddenPairs();
        return result;
    }

    public RestrictedPermutation setInvolutionState(boolean isInvolution) {
        RestrictedPermutation result = this.copy();
        if (isInvolution == involution) {
            return result;
        }
        if (isInvolution) {
            result.involution = true;
            result.properties.add(PermUtilities.INVOLUTION);
        } else {
            result.involution = false;
            result.properties.remove(PermUtilities.INVOLUTION);
        }
        result.createForbiddenPairs();
        return result;
    }

    /**
     * Creates a point at the given location.
     *
     * @param i location reference
     * @param j location reference
     * @return modified restricted permutation
     */
    public RestrictedPermutation addPoint(int i, int j) {
        RestrictedPermutation result = this.copy();
        result.p = PermUtilities.insert(p, i, j);
        result.permString = result.p.toString();
        result.createForbiddenPairs();
        result.userForbiddenPairs.clear();
        for (IntPair pair : this.userForbiddenPairs) {
            int left = pair.getFirst();
            int bottom = pair.getSecond();
            int newI = left + (left > i ? 1 : 0);
            int newJ = bottom + (bottom > j ? 1 : 0);
            result.userForbiddenPairs.add(new IntPair(newI, newJ));
            if (bottom == j) {
                result.userForbiddenPairs.add(new IntPair(newI, newJ + 1));
            }
            if (left == i) {
                result.userForbiddenPairs.add(new IntPair(newI + 1, newJ));
            }
            if (left == i && bottom == j) {
                result.userForbiddenPairs.add(new IntPair(newI + 1, newJ + 1));
            }
        } 
        // Try this hack for involutions
        if (involution && i != j) {
            result.involution = false;
            if (i < j) {
                result = result.addPoint(j+1, i);
            } else {
                result = result.addPoint(j, i+1);
            }
            result.involution = true;
        }
        return result;
    }

    /**
     * Deletes a point in the permutation at position <code>index</code>.
     *
     * @param index the position of the point to delete
     * @return modified restricted permutation
     */
    public RestrictedPermutation deletePointAt(int index) {
        RestrictedPermutation result = this.copy();
        result.p = PermUtilities.delete(p, index);
        result.permString = result.p.toString();
        result.createForbiddenPairs();
        result.userForbiddenPairs.clear();
        for (int i = 0; i <= result.p.length(); i++) {
            for (int j = 0; j <= result.p.length(); j++) {
                boolean isForbidden = true;
                isForbidden &= userForbiddenPairs.contains(new IntPair(i + (i > index ? 1 : 0), j + (j > p.elements[index] ? 1 : 0)));
                if (i == index) {
                    isForbidden &= userForbiddenPairs.contains(new IntPair(i + 1, j + (j > p.elements[index] ? 1 : 0)));
                }
                if (j == index) {
                    isForbidden &= userForbiddenPairs.contains(new IntPair(i + (i > index ? 1 : 0), j + 1));
                }
                if (i == index && j == index) {
                    isForbidden &= userForbiddenPairs.contains(new IntPair(i + 1, j + 1));
                }
                if (isForbidden) {
                    result.userForbiddenPairs.add(new IntPair(i, j));
                }
            }
        }
        return result;
    }

    /**
     * Returns a copy of the restricted permutation. Note, this performs a deep
     * copy.
     *
     * @return a copy of the restricted permutation
     */
    private RestrictedPermutation copy() {
        RestrictedPermutation result = new RestrictedPermutation(basisString, permString, simple, involution);
        result.p = this.p.clone();
        result.forbiddenPairs = new HashSet<IntPair>(this.forbiddenPairs);
        result.properties = new ArrayList<PermProperty>(this.properties);
        result.userForbiddenPairs = new HashSet<IntPair>(this.userForbiddenPairs);
        return result;
    }

    /**
     * Adds a property to the basis.
     */
    private void addBasisProperties() {
        Scanner basisElements = new Scanner(basisString);
        while (basisElements.hasNextLine()) {
            String line = basisElements.nextLine();
            if (line.length() > 0) {
                properties.add(PermUtilities.avoidanceTest(line));
            }
        }
    }

    /**
     * Checks whether newly created gaps should be marked as forbidden based on
     * user restrictions. That is, if both neighbouring positions along the
     * split are marked forbidden then so will the new space, otherwise not.
     *
     * @param r old row index
     * @param c old column index
     * @param i new row index
     * @param j new column index
     * @param length the length of the inserted permutation
     * @return true if new space should be forbidden, false otherwise
     */
    private boolean shouldForbid(int r, int c, int i, int j, int length) {
        boolean inColGap = c > i && c < i + length;
        boolean inRowGap = r > j && r < j + length;
        int oldCol = c;
        if (c >= i + length) {
            oldCol = c - length + 1;
        } else if (c > i) {
            oldCol = i + 1;
        }
        int oldRow = r;
        if (r >= j + length) {
            oldRow = r - length + 1;
        } else if (r > j) {
            oldRow = j + 1;
        }
        if (!inColGap && !inRowGap) {
            return this.userForbids(oldCol, oldRow);
        }
        if (!inColGap && inRowGap) {
            return this.userForbids(oldCol, oldRow) && this.userForbids(oldCol, oldRow - 1);
        }
        if (inColGap && !inRowGap) {
            return this.userForbids(oldCol, oldRow) && this.userForbids(oldCol - 1, oldRow);
        }
        return this.userForbids(oldCol, oldRow) && this.userForbids(oldCol, oldRow - 1) || this.userForbids(oldCol, oldRow) && this.userForbids(oldCol - 1, oldRow) || this.userForbids(oldCol + 1, oldRow) && this.userForbids(oldCol - 1, oldRow - 1) || this.userForbids(oldCol, oldRow - 1) && this.userForbids(oldCol - 1, oldRow - 1);
    }

    @Override
    public String toString() {
        return "A restricted permutation based on " + p;
    }
}
