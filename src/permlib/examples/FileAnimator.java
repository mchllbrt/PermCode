/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package permlib.examples;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;
import permlab.ui.AnimatedPermFrame;
import permlib.Permutation;

/**
 * Animate a file of permutations
 *
 * @author Michael Albert
 */
public class FileAnimator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("/Users/MichaelAlbert/Desktop/u18.txt"))) {
            String line;
            HashSet<Permutation> egs = new HashSet<>();
            while ((line = br.readLine()) != null) {
                egs.add(new Permutation(line));
            }
            AnimatedPermFrame f = new AnimatedPermFrame(egs);
            f.setVisible(true);
        } catch (Exception e) {
            System.out.println("Boo " + e);
        }
    }

}
