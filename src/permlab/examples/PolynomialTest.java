package permlab.examples;


import permlib.examples.Rational;
import permlib.examples.Polynomial;
import java.math.BigInteger;
import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author MichaelAlbert
 */
public class PolynomialTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Polynomial a = new Polynomial( new BigInteger[] {ONE, ONE, ONE});
        Polynomial one = new Polynomial(new BigInteger[] {ONE});
        Polynomial b = new Polynomial(new BigInteger[] {ONE, ONE});
        Rational a1 = new Rational(one, a);
        Rational b1 = new Rational(one, b);
        System.out.println(a1.add(b1));
    }
    
}
