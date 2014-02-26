package permlab.examples;

import java.math.BigInteger;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;
import java.util.ArrayList;
import static permlab.examples.PowerSeries.add;
import static permlab.examples.PowerSeries.multiply;

/**
 *
 * @author MichaelAlbert
 */
public class PowerSeriesTest {

    
    public static void main(String[] args) {
        
        PowerSeries a = new PowerSeries() {
          
            @Override
            void computeTo(int i) {
                while(c.size() <= i) {
                    c.add(ONE);
                }
            }
        };
        a.at(7);
        System.out.println(a);
        PowerSeries b = add(a,a);
        b.at(7);
        System.out.println(b);
        b = multiply(a,a);
        b.at(7);
        System.out.println(b);
        b = multiply(2,a);
        b.at(7);
        System.out.println(b);
        PowerSeries c = add(a,a);
        c.at(7);
        System.out.println(c);
        System.out.println(b.equals(c));
        System.out.println(b);
    }
}
