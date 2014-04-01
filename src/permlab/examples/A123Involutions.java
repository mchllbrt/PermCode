package permlab.examples;

import permlib.classes.InvolutionPermClass;
import permlib.Permutation;
import permlib.processor.PermCounter;

/**
 * Processing 123 avoiding involutions. Mainly a test on InvolutionClass as it
 * showed a bug which was not appearing for 321 avoiding involutions.
 *
 * Note the number of involutions of length n should be C(n, floor(n/2))
 *
 * @author Michael Albert
 */
public class A123Involutions {

    public static void main(String[] args) {

        InvolutionPermClass a123 = new InvolutionPermClass(new Permutation("123"));
        PermCounter c = new PermCounter();
        for (int n = 1; n <= 20; n++) {
            a123.processPerms(n, c);
            System.out.print(c.getCount() + " ");
            c.reset();
        }

    }
}