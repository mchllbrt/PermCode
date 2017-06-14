package permlib.examples;

import java.util.Arrays;
import permlib.Permutation;

/**
 * Building on Jinge's work, trying to get a DFS search that beats basic PermLab
 * for Av(321, p) in the case of a rigid permutation p.
 *
 * @author Michael Albert
 */
public class Av321DFSClient {

    private static final int UNDEFINED = -1;
    private static final int MAXSIZE = 64;
    private static final int UPPER = 2;
    private static final int LOWER = 1;
    private static final int FLOATING = 0;

    Permutation p;
    Permutation pi;
    int[] values = new int[0];
    int[] upperSuccessors = new int[0];
    int[] lowerSuccessors = new int[0];
    int[] types = new int[0];
    int lastUpper = UNDEFINED;
    int lastLower = UNDEFINED;

    public Av321DFSClient(Permutation p) {
        this.p = p;
        this.pi = p.inverse();
    }

    public int pLeft(int index) {
        return index - 1;
    }

    public int pDown(int index) {
        int v = p.elements[index] - 1;
        if (v >= 0) {
            return pi.elements[v];
        } else {
            return v;
        }
    }

    // Attempts to extend values by value. Returns the status of the extension.
    // As a side effect, if successful, values, upperSuccessors and lowerSuccesors
    // and types as well as validIndices are updated.
    public boolean extend(int value) {

        int[] newValues = new int[values.length+1];
        for(int i = 0; i < values.length; i++) {
            newValues[i] = (values[i] >= value) ? values[i]+1 : values[i];
        }

        return false;
    }

    public static void main(String[] args) {
        Av321DFSClient c = new Av321DFSClient(new Permutation("2513476"));
        System.out.println(c.p);
        System.out.println(c.pi);
        for (int i = 0; i < c.p.length(); i++) {
            System.out.print(" " + c.pDown(i));
        }
        System.out.println();
    }
}
