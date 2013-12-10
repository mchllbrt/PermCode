package permlab.examples;

import java.util.HashSet;
import permlib.Permutation;
import permlib.classes.PermutationClass;
import permlib.Permutations;

/**
 * To use for miscellaneous, i.e. not of any long term importance, examples.
 *
 * @author Michael Albert
 */
public class Misc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        int N = 24;
       long[][][] c = new long[N][N][N];
       c[2][1][2] = 1;
       c[2][2][1] = 1;
       c[2][1][1] = 0;
       c[2][2][2] = 0;
       for(int n = 3; n < N; n++) {
           for(int j = 1; j < n; j++) {
               for(int i = j+1; i <= n; i++) {
                   for(int k = 1; k < j; k++) {
                       c[n][i][j] += c[n-1][j][k];
                   }
                   for(int k = i; k < n; k++) {
                       c[n][i][j] += c[n-1][j][k];
                   }
               }
           }
           for(int i = 1; i < n; i++) {
               for(int j = i+1; j <= n; j++) {
                   for(int k = 1; k < i; k++) {
                       c[n][i][j] += c[n-1][j-1][k];
                   }
                   for(int k = j; k < n; k++) {
                       c[n][i][j] += c[n-1][j-1][k];
                   }
               }
           }
           long s = 0;
           for(int i = 1; i <= n; i++) {
               for(int j = 1; j <= n; j++) {
                   s += c[n][i][j];
               }
           }
           System.out.print(", " + s);
       }
       System.out.println();
      
    }
    
}
