package permlib.mesh;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.utilities.Combinations;

/**
 * This class represents a mesh pattern.
 * @author Michael Albert
 */
public class MeshPattern implements Comparable<MeshPattern> {

    Permutation p;
    boolean[][] mesh;
    private int shadedPoints;

    public MeshPattern(Permutation p, boolean[][] mesh) {
        this.p = p;
        this.mesh = mesh;
        countShadings();
    }

    public MeshPattern(Permutation p) {
        this.p = p;
        this.mesh = new boolean[p.length() + 1][p.length() + 1];
        shadedPoints = 0;
    }

    public MeshPattern(String permString) {
        this(new Permutation(permString));
    }

    /**
     * Shades the cell (<code>i</code>, <code>j</code>).
     * @param i The column index of the cell to shade
     * @param j The row index of the cell to shade
     */
    public void addShading(int i, int j) {
        if (!mesh[i][j]) {
            mesh[i][j] = true;
            shadedPoints++;
        }
    }

    /**
     * Removes the shading of the cell (<code>i</code>, <code>j</code>).
     * @param i The column index of the cell to unshade
     * @param j The row index of the cell to unshade
     */
    public void removeShading(int i, int j) {
        if (mesh[i][j]) {
            mesh[i][j] = false;
            shadedPoints--;
        }
    }

    /**
     * Returns a copy of this mesh pattern.
     * @return a copy of this mesh pattern
     */
    public MeshPattern copy() {
        MeshPattern result = new MeshPattern(this.p);
        for (int r = 0; r <= p.length(); r++) {
            for (int c = 0; c <= p.length(); c++) {
                result.mesh[r][c] = this.mesh[r][c];
            }
        }
        return result;
    }
    
    public MeshPattern deletePoint(int index) {
        MeshPattern result = new MeshPattern(PermUtilities.delete(p, index));
        int value = p.elements[index];
        for(int i = 0; i < p.length(); i++) {
            for(int j = 0; j < p.length(); j++) {
                if (shouldBeShaded(i,j,index,value)) result.addShading(i,j);
            }
        }
        return result;
    }

//    /**
//     * Determines whether or not this pattern is weaker than another. To
//     * determine this, all possible instances of the permutation underlying
//     * the other pattern in this one are found. The corresponding reduced
//     * pattern is then computed (by pooling the cells of this pattern that
//     * correspond to cells of the instance), and if the result ever contains
//     * the other pattern, then avoiding the other
//     * @param pattern the pattern to compare this pattern with
//     * @return <code>true</code> if this pattern is weaker than the other
//     */
//    public boolean isWeakerThan(MeshPattern pattern) {
//
//
//        if (this.p.length() < pattern.p.length()) {
//            return false;
//        }
//
//        if (this.p.equals(pattern.p)) {
//            for (int i = 0; i < mesh.length; i++) {
//                for (int j = 0; j < mesh.length; j++) {
//                    if (pattern.mesh[i][j] && !mesh[i][j]) {
//                        return false;
//                    }
//                }
//            }
//            return true;
//        }
//
//        if (this.p.length() == pattern.p.length()) {
//            return false;
//        }
//
//        // Now we know that the other pattern is shorter.
//
//        int[] indices = new int[pattern.p.length()];
//        for (int i = 0; i < indices.length; i++) {
//            indices[i] = i;
//        }
//
//        do {
//            if (this.p.patternAt(indices).equals(pattern.p)) {
//                if (reducedPattern(this, indices).isWeakerThan(pattern)) {
//                    return true;
//                }
//            }
//            if (!PermUtilities.nextCombination(indices, this.p.length())) {
//                return false;
//            }
//        } while (true);
//
//    }
    public boolean hasRedundantPoint() {
        for (int i = 0; i < p.length(); i++) {
            if (isRedundantPoint(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether a point is redundant. To be redundant means that
     * avoiding this pattern will follow from avoidance of a smaller pattern.
     * The criteria here (which are sufficient) are that none of the four cells
     * immediately around the point are shaded, and that every other pair of
     * adjacent cells that the point splits are of the same type. All points
     * in a classical pattern are redundant.
     * 
     * @param index the index of the point
     * @return <code>true</code> if the point is redundant
     */
    public boolean isRedundantPoint(int index) {

        if (isClassicalPattern()) {
            return true;
        }

        int value = p.elements[index];
        if (mesh[index][value]
                || mesh[index + 1][value]
                || mesh[index][value + 1]
                || mesh[index + 1][value + 1]) {
            return false;
        }

        for (int i = 0; i <= p.length(); i++) {
            if (mesh[index][i] && !mesh[index + 1][i]
                    || !mesh[index][i] && mesh[index + 1][i]
                    || mesh[i][value] && !mesh[i][value + 1]
                    || !mesh[i][value] && mesh[i][value + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether this pattern is avoided in a permutation at a given
     * set of indices. Note that it is implicitly assumed that <code>indices</code>
     * is a strictly increasing sequence of positive integers representing a
     * legitimate set of indices in the permutation.
     * @param q the permutation
     * @param indices the indices
     * @return <code>true</code> if this pattern is avoided in the permutation
     * at these indices
     * 
     */
    public boolean isAvoidedAt(Permutation q, int[] indices) {

        if (!q.patternAt(indices).equals(p)) {
            return true;
        }

        int[] values = new int[indices.length];

        for (int i = 0; i < indices.length; i++) {
            values[i] = q.elements[indices[i]];
        }
        Arrays.sort(values);

        int indexPosition = 0;
        for (int i = 0; i < q.length(); i++) {
            if (indexPosition < indices.length && i == indices[indexPosition]) {
                indexPosition++;
                continue;
            }

            int value = q.elements[i];
            int valueIndex = 0;

            while (valueIndex < values.length && value > values[valueIndex]) {
                valueIndex++;
            }

            if (mesh[indexPosition][valueIndex]) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines whether this pattern is avoided by a given permutation.
     * @param q the permutation
     * @return <code>true</code> if the permutation avoids this pattern
     */
    public boolean isAvoidedBy(Permutation q) {

        if (q.length() < p.length()) {
            return true;
        }

        int[] indices = new int[p.length()];

        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

        do {
            if (!isAvoidedAt(q, indices)) {
                return false;
            }

            if (!Combinations.nextCombination(indices, q.length())) {
                return true;
            }


        } while (true);
    }

    @Override
    public String toString() {
        return "MeshPattern: " + p + "\n" + stringForm(mesh);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MeshPattern other = (MeshPattern) obj;
        if (this.p != other.p && (this.p == null || !this.p.equals(other.p))) {
            return false;
        }
        if (!Arrays.deepEquals(this.mesh, other.mesh)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + (this.p != null ? this.p.hashCode() : 0);
        hash = 31 * hash + Arrays.deepHashCode(this.mesh);
        return hash;
    }

    private static String stringForm(boolean[][] mesh) {
        StringBuilder result = new StringBuilder();
        for (int i = mesh.length - 1; i >= 0; i--) {
            for (int j = 0; j < mesh.length; j++) {
                result.append(mesh[j][i] ? '#' : '.');
            }
            if (i > 0) {
                result.append('\n');
            }
        }
        return result.toString();
    }

    /**
     * Compares this mesh pattern to another. The result is the result of
     * comparing the underlying permutations if they differ, otherwise whether
     * more cells are shaded in this pattern than the other, and finally with
     * equal numbers of shaded cells, a lex comparison.
     * 
     * @param other the mesh pattern to compare this pattern with
     * @return the result of the comparison
     */
    @Override
    public int compareTo(MeshPattern other) {
        if (!this.p.equals(other.p)) {
            return this.p.compareTo(other.p);
        }

        if (this.shadedPoints != other.shadedPoints) 
            return this.shadedPoints - other.shadedPoints;

        for (int i = 0; i < mesh.length; i++) {
            for (int j = 0; j < mesh.length; j++) {
                if (mesh[i][j] && !other.mesh[i][j]) {
                    return 1;
                }
                if (other.mesh[i][j] && !mesh[i][j]) {
                    return -1;
                }
            }
        }
        return 0;
    }

    /**
     * Determines whether every cell is shaded.
     * @return <code>true</code> if every cell is shaded
     */
    public boolean isFullPattern() {
        return shadedPoints == (mesh.length * mesh.length);
    }

    public boolean isClassicalPattern() {
        return shadedPoints == 0;
    }

    /**
     * Shades all the cells of this pattern.
     */
    public void makeFullPattern() {
        for (int i = 0; i < mesh.length; i++) {
            for (int j = 0; j < mesh.length; j++) {
                mesh[i][j] = true;
            }
        }
        shadedPoints = mesh.length * mesh.length;
    }

    /**
     * Computes the result of reducing a pattern on a set of indices. That is, 
     * the blocks of cells determined by the partition induced by the indices 
     * and the associated values, form a single cell which is shaded if any one
     * of the consituent cells is shaded.
     * @param mp the mesh pattern to be reduced
     * @param indices the indices forming the partition
     * @return the result of the reduction
     */
    public static MeshPattern reducedPattern(MeshPattern mp, int[] indices) {
        MeshPattern result = new MeshPattern(mp.p.patternAt(indices));
        int[] values = new int[indices.length];
        for (int i = 0; i < values.length; i++) {
            values[i] = mp.p.elements[indices[i]];
        }
        Arrays.sort(values);
        for (int i = 0; i < result.mesh.length; i++) {
            int lowIndex = (i > 0) ? indices[i - 1] + 1 : 0;
            int highIndex = (i < indices.length) ? indices[i] : mp.p.length();
            for (int j = 0; j < result.mesh.length; j++) {
                int lowValue = (j > 0) ? values[j - 1] + 1 : 0;
                int highValue = (j < indices.length) ? values[j] : mp.p.length();
                for (int c = lowIndex; c <= highIndex; c++) {
                    for (int r = lowValue; r <= highValue; r++) {
                        result.mesh[i][j] |= mp.mesh[c][r];
                    }
                }
            }
        }

        return result;
    }

    /**
     * Determines which cells require shading to avoid an involvement of this
     * pattern in a permutation at the given indices.
     * @param q the permutation
     * @param indices the indices
     * @return a boolean grid representing the extra cells that need shading
     */
    boolean[][] occupationRequirements(Permutation q, int[] indices) {
        boolean[][] result = new boolean[p.length() + 1][p.length() + 1];
        if (isAvoidedAt(q, indices)) {
            return result;
        }
        int[] values = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            values[i] = q.elements[indices[i]];
        }
        Arrays.sort(values);
        int indexPosition = 0;
        for (int i = 0; i < q.length(); i++) {
            if (indexPosition < indices.length && i == indices[indexPosition]) {
                indexPosition++;
                continue;
            }
            int value = q.elements[i];
            int valueIndex = 0;
            while (valueIndex < values.length && value > values[valueIndex]) {
                valueIndex++;
            }
            result[indexPosition][valueIndex] = true;
        }
        return result;
    }

    Collection<MeshPattern> extensionsFor(Permutation q, int[] indices) {
        LinkedList<MeshPattern> result = new LinkedList<MeshPattern>();
        if (isAvoidedAt(q, indices)) {
            result.add(this);
            return result;
        }
        int[] values = new int[indices.length];
        for (int i = 0; i < indices.length; i++) {
            values[i] = q.elements[indices[i]];
        }
        Arrays.sort(values);
        int indexPosition = 0;
        for (int i = 0; i < q.length(); i++) {
            if (indexPosition < indices.length && i == indices[indexPosition]) {
                indexPosition++;
                continue;
            }
            int value = q.elements[i];
            int valueIndex = 0;
            while (valueIndex < values.length && value > values[valueIndex]) {
                valueIndex++;
            }
            MeshPattern newPattern = this.copy();
            newPattern.addShading(indexPosition, valueIndex);
            result.add(newPattern);
        }
        return result;
    }

    /**
     * Determines whether this mesh pattern contains another mesh pattern. That
     * means they have the same underlying permutation, and the shaded cells of
     * this pattern contain the set of shaded cells of the other. Note that this
     * means it is easier to avoid this pattern than the other.
     * 
     * @param pattern the other pattern
     * @return <code>true</code> if this pattern contains the other pattern
     */
    boolean contains(MeshPattern pattern) {
        if (!this.p.equals(pattern.p)) {
            return false;
        }
        for (int i = 0; i < mesh.length; i++) {
            for (int j = 0; j < mesh.length; j++) {
                if (pattern.mesh[i][j] && !mesh[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void countShadings() {
        shadedPoints = 0;
        for (int i = 0; i < mesh.length; i++) {
            for (int j = 0; j < mesh.length; j++) {
                if (mesh[i][j]) {
                    shadedPoints++;
                }
            }
        }
    }

    private boolean shouldBeShaded(int i, int j, int index, int value) {
        
        boolean result = false;
        int[] indices = new int[(i == index) ? 2 : 1];
        int[] values = new int[(j == value) ? 2 : 1];
        if (i == index) {
            indices[0] = i; indices[1] = i+1;
        } else {
            indices[0] = (i < index) ? i : i+1;
        }
        if (j == value) {
            values[0] = value; values[1] = value+1;
        } else {
            values[0] = (j < value) ? j : j+1;
        }
        for(int c : indices) {
            for(int r: values) {
                result |= mesh[c][r];
            }
        }
        return result;
    }
}
