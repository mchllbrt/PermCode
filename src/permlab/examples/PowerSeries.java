package permlab.examples;

import java.math.BigInteger;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.ONE;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Representation of power series
 *
 * @author MichaelAlbert
 */
public class PowerSeries {

    static int LENGTH = 10;

    ArrayList<BigInteger> c;

    public PowerSeries() {
        c = new ArrayList<BigInteger>();
    }

    public static void setLength(int length) {
        LENGTH = length;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        for (int i = 0; i <= LENGTH; i++) {
            hash = 53 * hash + this.at(i).hashCode();
        }
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
//            System.out.println("A");
            return false;
        }
//        if (getClass() != obj.getClass()) {
//            System.out.println("B");
//            return false;
//        }
        final PowerSeries other = (PowerSeries) obj;
        for (int i = 0; i <= LENGTH; i++) {
            if (!this.at(i).equals(other.at(i))) {
//                System.out.println("C " + i);
                return false;
            }
        }
        return true;
    }

    public BigInteger at(int i) {
        if (c.size() <= i) {
            computeTo(i);
        }
        return c.get(i);
    }

    public static PowerSeries add(final PowerSeries a, final PowerSeries b) {
        return new PowerSeries() {
            @Override
            void computeTo(int i) {
                for (int j = c.size(); j <= i; j++) {
                    c.add(a.at(j).add(b.at(j)));
                }
            }
        };
    }
    
    public static PowerSeries subtract(final PowerSeries a, final PowerSeries b) {
        return add(a, multiply(-1,b));
    }

    public static PowerSeries multiply(final PowerSeries a, final PowerSeries b) {
        return new PowerSeries() {
            @Override
            void computeTo(int i) {
                for (int j = c.size(); j <= i; j++) {
                    BigInteger r = ZERO;
                    for (int x = 0; x <= j; x++) {
                        r = r.add(a.at(x).multiply(b.at(j - x)));
                    }
                    c.add(r);
                }
            }
        };
    }

    public static PowerSeries multiply(final int n, final PowerSeries a) {
        return new PowerSeries() {
            BigInteger bn = new BigInteger("" + n);

            @Override
            void computeTo(int i) {
                for (int j = c.size(); j <= i; j++) {
                    c.add(a.at(j).multiply(bn));
                }
            }
        };
    }

    void computeTo(int i) {
    }

    ;

    public String toString() {
        return c.toString();
    }

}
