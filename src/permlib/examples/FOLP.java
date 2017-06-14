package permlib.examples;

/**
 * Experiments related to first order logic of permutations
 *
 * @author MichaelAlbert
 */
import java.util.ArrayList;
import java.util.Arrays;
import static permlib.PermUtilities.*;
import permlib.Permutation;

public class FOLP {

    public static void main(String[] args) {
        // try231();
        Permutation p = new Permutation("213");
        Permutation q = substitute(p, new Permutation[] {Permutation.ONE, new Permutation ("2413"), new Permutation("321")});
        System.out.println(q);
    }

    public static Permutation inflateMonotone(Permutation p, int[] sizes) {
        int sum = 0;
        for (int s : sizes) {
            sum += (s > 0) ? s : -s;
        }
        int[] vLow = new int[p.length()];
        for (int i = 0; i < p.length(); i++) {
            for (int j = i + 1; j < p.length(); j++) {
                if (p.elements[i] < p.elements[j]) {
                    vLow[j] += (sizes[i] > 0) ? sizes[i] : -sizes[i];
                } else {
                    vLow[i] += (sizes[j] > 0) ? sizes[j] : -sizes[j];
                }
            }
        }
        int[] result = new int[sum];
        int ri = 0;
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] > 0) {
                int vL = vLow[i];
                for (int j = 0; j < sizes[i]; j++) {
                    result[ri++] = vL++;
                }
            } else {
                int vH = vLow[i] - sizes[i] - 1;
                for (int j = 0; j < -sizes[i]; j++) {
                    result[ri++] = vH--;
                }
            }
        }

        return new Permutation(result);
    }

    private static void try231() {
        Permutation p = new Permutation("635124");
        int n = 10;
        for (int a = 2; a <= n; a++) {
            for (int b = a-1; b <= a+1; b++) {
                for (int c = a-1; c <= a+1; c++) {
                    Permutation q = inflateMonotone(p, new int[]{-a, 1,1, b, 1,-c});
                    if (noThreeCycles(q) && cycles(q).size() == 1) {
                        System.out.print(a + " " + b + " " + c + ": ");
                        System.out.print(q + " -> ");
                        for (ArrayList<Integer> cy : cycles(q)) {
                            System.out.print(" " + cy.size());
                        }
                        System.out.println();
                        for (ArrayList<Integer> cy : cycles(q)) {
                            System.out.print(cy + " ");
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

    private static boolean noThreeCycles(Permutation q) {
        for (ArrayList<Integer> c : cycles(q)) {
            if (c.size() == 3) {
                return false;
            }
        }
        return true;
    }

}
