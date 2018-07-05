package permlib.arches;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import static permlib.arches.PairType.*;
import permlib.utilities.Combinations;

/**
 * A family of arches. At various points the code assumes that the arches are
 * labelled in increasing order of left hand endpoints. If this condition fails
 * then undefined behaviour may occur -- this is intended as a bug catcher.
 *
 * @author Michael Albert
 */
public class ArchSystem {

    int[] labels;
    PairType[][] pairTypes;

    private int[] indices;

    public ArchSystem(int[] labels) {
        this(labels, false);
    }

    public ArchSystem(int[] labels, boolean safe) {
        if (safe) {
            this.labels = labels;
        } else {
            this.labels = standardise(labels);
        }
        computePairTypes();
    }
    
    public ArchSystem(PairType[][] pairTypes) {
        this.pairTypes = pairTypes;
        computeLabels();
    }

    private int[] standardise(int[] labels) {
        int[] result = new int[labels.length];
        for (int i = 0; i < labels.length; i++) {
            for (int j = i + 1; j < labels.length; j++) {
                if (labels[i] < labels[j]) {
                    result[j]++;
                } else if (labels[i] > labels[j]) {
                    result[i]++;
                }
            }
        }
        for (int i = 0; i < labels.length; i++) {
            result[i] /= 2;
        }
        return result;
    }

    public String toString() {
        return Arrays.toString(labels);
    }

    public int size() {
        return pairTypes.length;
    }

    private void computePairTypes() {
        int n = labels.length / 2;
        pairTypes = new PairType[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                pairTypes[i][j] = getType(i, j);
                // System.out.println(i + " " + j + " " + pairTypes[i][j]);
            }
        }
    }

    private void computeLabels() {
        labels = new int[2 * pairTypes.length];
        for (int i = 0; i < pairTypes.length; i++) {
            // System.out.println("Arch " + i);
            int il = 0;
            int ir = 1;
            for (int j = 0; j < i; j++) {
                switch (pairTypes[j][i]) {
                    case I:
                        il += 2;
                        ir += 2;
                        break;
                    case C:
                        il += 1;
                        ir += 2;
                        break;
                    case N:
                        il += 1;
                        ir += 1;
                }
            }
            labels[il] = i;
            for (int j = i + 1; j < pairTypes.length && !(pairTypes[i][j] == I); j++) {
                switch (pairTypes[i][j]) {
                    case N:
                        ir += 2;
                        break;
                    default:
                        ir += 1;
                }
            }
            labels[ir] = i;
            // System.out.println(il + " " + ir);
        }
    }



public boolean contains(ArchSystem other) {
        indices = new int[other.size()];
        for(indices[0] = 0; indices[0] < this.size(); indices[0]++) {
            if (check(other, 1)) return true;
        }
        return false;
    }
    
    private boolean check(ArchSystem other, int i) {
        if (i == other.size()) return true;
        for(indices[i] = indices[i-1] + 1; indices[i] < this.size(); indices[i]++) {
            int j = 0;
            while (j < i && this.pairTypes[indices[j]][indices[i]] == other.pairTypes[j][i]) j++;
            if (j == i && check(other, i+1)) return true;
        }
        return false;
    }

    private PairType getType(int a, int b) {
        int al = -1;
        int bl = -1;
        int ar = -1;
        int br = -1;
        for(int i = 0; i < labels.length; i++) {
            if (labels[i] == a) {
                if (al < 0) {
                    al = i;
                } else {
                    ar = i;
                    if (br >= 0) break;
                }
            }
            if (labels[i] == b) {
                if (bl < 0) {
                    bl = i;
                } else {
                    br = i;
                    if (ar >= 0) break;
                }
            }
        }
        if (ar < bl) return PairType.I;
        if (br < ar) return PairType.N;
        if (ar < br) return PairType.C;
        return null; // Should not happen bug finder
    }

    @Override
        public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Arrays.hashCode(this.labels);
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
        final ArchSystem other = (ArchSystem) obj;
        if (!Arrays.equals(this.labels, other.labels)) {
            return false;
        }
        return true;
    }

    
    public static void main(String[] args) {
        ArchSystem a = new ArchSystem(new int[] {0, 1,0, 2, 2, 3, 1, 3}, true);
        System.out.println(a);
        // System.out.println(Arrays.toString(a.pairTypes));
        a.computeLabels();
        System.out.println(a);
    }
    

}
