package permlib.examples;

import java.math.BigInteger;

/**
 * Enumerate quarter plane walks for given step sets.
 *
 * @author Michael Albert
 */
public class QuarterPlane {

    int[][] steps;
    BigInteger[][] counts;
    int n;

    public QuarterPlane(int[][] steps, int n) {
        this.steps = steps;
        this.n = n;
        counts = new BigInteger[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                counts[i][j] = BigInteger.ZERO;
            }
        }
        counts[0][0] = BigInteger.ONE;
    }

    void doGeneration() {
        BigInteger[][] newCounts = new BigInteger[n][n];
        for(int i = 0; i < n; i++) {
            for(int j = 0; j < n; j++) {
                newCounts[i][j] = BigInteger.ZERO;
            }
        }
        for (int i = 0; i < counts.length; i++) {
            for (int j = 0; j < counts.length; j++) {
                for (int[] step : steps) {
                    int ni = i + step[0];
                    int nj = j + step[1];
                    if (0 <= ni && ni < n && 0 <= nj && nj < n) {
                        newCounts[ni][nj] = newCounts[ni][nj].add(counts[i][j]);
                    }
                }
            }
        }
        counts = newCounts;
    }

    public static void main(String[] args) {

        int n = 500;
        int[][] steps = new int[][]{
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        QuarterPlane q = new QuarterPlane(steps, 500);
        for(int i = 1; i <= 500; i++) {
            q.doGeneration();
            if (i % 2 == 0) System.out.println(i + " " + q.counts[0][0]);
        }

    }
}
