package permlib.property;

import java.util.Arrays;
import permlib.utilities.MultisetCodes;
import permlib.Permutation;

/**
 * The property of being griddable. That is, a class with an associated matrix
 * whose entries represent simpler permutation classes. All permutations in the
 * class can be chopped apart into sections that correspond to the matrix 
 * entries.
 * 
 * @author M Albert
 */
public final class Griddable implements PermProperty {

    final PermProperty[][] propertyGrid;
    int rows;
    int cols;

    public Griddable(PermProperty[][] propertyGrid) {
        computeGridSize(propertyGrid); // Allow for lazy users who can't be bothered to pad rows with Empty()
        this.propertyGrid = new PermProperty[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r < propertyGrid.length && c < propertyGrid[r].length) {
                    this.propertyGrid[r][c] = propertyGrid[r][c];
                } else {
                    this.propertyGrid[r][c] = PermProperty.EMPTY;
                }
            }
        }
    }

    private void computeGridSize(Object[][] g) {
        this.rows = g.length;
        this.cols = 0;
        for (int r = 0; r < rows; r++) {
            this.cols = Math.max(this.cols, g[r].length);
        }
    }

    @Override
    public final boolean isSatisfiedBy(Permutation p) {
        return isSatisfiedBy(p, false);
    }
    public final boolean isSatisfiedBy(Permutation p, boolean verbose) {
        if (p.length() == 0) {
            return true;
        }
        if (rows == 0 || cols == 0) {
            return false;
        }

        for (int[] rowBlockTops : new MultisetCodes(p.length() + 1, rows - 1)) {
            int[] rowBoundaries = new int[rows + 1];
            rowBoundaries[0] = 0;
            System.arraycopy(rowBlockTops, 0, rowBoundaries, 1, rows - 1);
            rowBoundaries[rows] = p.length();
            for (int[] colRightSides : new MultisetCodes(p.length() + 1, cols - 1)) {
                int[] colBoundaries = new int[cols + 1];
                colBoundaries[0] = 0;
                System.arraycopy(colRightSides, 0, colBoundaries, 1, cols - 1);
                colBoundaries[cols] = p.length();
                boolean good = true;
                for (int r = 0; good && r < rows; r++) {
                    for (int c = 0; good && c < cols; c++) {
                        good = propertyGrid[r][c].isSatisfiedBy(
                                p.window(colBoundaries[c], colBoundaries[c + 1],
                                rowBoundaries[r], rowBoundaries[r + 1]));
                    }
                }
                if (good) {
                    if (verbose) {
                        System.out.println("Col boundaries " + Arrays.toString(colBoundaries));
                        System.out.println("Row boundaries " + Arrays.toString(rowBoundaries));
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public final boolean isSatisfiedBy(int[] values) {
        return isSatisfiedBy(new Permutation(values));
    }
}
