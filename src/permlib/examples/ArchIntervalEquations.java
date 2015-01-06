package permlib.examples;

import java.util.Arrays;
import java.util.HashSet;
import permlib.PermUtilities;
import permlib.Permutation;

/**
 * Generate the equations that define the "occurrence of alpha" generating
 * function in arch systems.
 *
 * We have alpha = alpha_1 + ... + alpha_k
 *
 * and F_i counts systems where we have seen alpha_1 + ... + alpha_i
 *
 * The general rule is that for each atom that occurs we see whether adding that
 * atom at the end of F_i has a suffix matching a prefix of alpha. If so that
 * gives a term of that type. If not we get an F_0.
 *
 * @author Michael Albert
 */
public class ArchIntervalEquations {

    public static void main(String[] args) {
        Permutation p = new Permutation("3 2 1 5 4 8 6 7 11 10 9");
        Permutation[] cs = sumComponents(p);
        Arrays.sort(cs);
        Permutation[] comps = PermUtilities.plusComponents(p);
        StringBuilder eqs = new StringBuilder();
        eqs.append("Eqs" + Arrays.toString(p.elements) + "={\n");
        for (int i = -1; i < comps.length; i++) {
            eqs.append(equationFor(i, comps, cs));
            eqs.append(",\n");
        }
        eqs.delete(eqs.length() - 2, eqs.length());
        eqs.append("\n};\n");
        eqs.append("Vars" + Arrays.toString(p.elements) + " = F /@ Range[0," + comps.length + "];\n");
        eqs.append("GF" + Arrays.toString(p.elements) + " = F[0] /. Solve[");
        eqs.append("Eqs" + Arrays.toString(p.elements) + ",");
        eqs.append("Vars" + Arrays.toString(p.elements) + "][[1]] // Simplify");

        System.out.println(eqs);
    }

    public static String equationFor(int i, Permutation[] alpha, Permutation[] cs) {

        StringBuilder result = new StringBuilder();
        StringBuilder aux = new StringBuilder();
        result.append("F[" + (i + 1) + "] == ");
        if (i == alpha.length - 1) {
            result.append("z*(");
        }
        result.append("1+");
        for (Permutation q : cs) {
            int j = suffLength(alpha, i, q);
            result.append("t^" + q.length() + "*F[" + (j + 1) + "]+");
            aux.append("t^" + (q.length() - 1) + "-");
        }
        result.deleteCharAt(result.length() - 1);
        aux.deleteCharAt(aux.length() - 1);
        result.append("+t*(" + "F[0]-" + aux + ")*F[0]");
        if (i == alpha.length - 1) {
            result.append(")");
        }

        return result.toString();
    }

    // The set of sum components as an array
    public static Permutation[] sumComponents(Permutation p) {
        HashSet<Permutation> comps = new HashSet<>();
        for (Permutation c : PermUtilities.plusComponents(p)) {
            comps.add(c);
        }
        return comps.toArray(new Permutation[0]);
    }

    public static int suffLength(Permutation[] alpha, int i, Permutation q) {
        int result = (i + 1 < alpha.length) ? i + 1 : alpha.length - 1;
        while (true) {
            if (alpha[result].equals(q)) {
                int j = result - 1;
                while (j >= 0 && alpha[j].equals(alpha[j + i - result + 1])) {
                    j--;
                }
                if (j < 0) {
                    return result;
                }
            }
            result--;
            if (result < 0) {
                return -1;
            }
        }
    }

}
