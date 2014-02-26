package permlib.examples;

import permlib.examples.Polynomial;
import java.util.Objects;

/**
 *
 * @author MichaelAlbert
 */
public class Rational {
    
    static final boolean SAFE = true;
    static final Rational T = new Rational(Polynomial.T);
    static final Rational ZERO = new Rational(Polynomial.ZERO);
     static final Rational ONE = new Rational(Polynomial.ONE);
    
    private Polynomial num;
    private Polynomial den;
    
    public Rational(Polynomial num, Polynomial den) {
        this(num, den, !SAFE);
    }
    
    public Rational(Polynomial num, Polynomial den, boolean safe) {
        if (safe) {
            this.num = num;
            this.den = den;
        } else {
//            System.out.println("Making safe " + num + " / " + den);
            Polynomial gcd = num.gcd(den);
            gcd = gcd.makePrimitive();
            this.num = num.div(gcd);
            this.den = den.div(gcd);
//            System.out.println("Made safe " + this.num + " / " + this.den);
        }
    }
    
    public Rational(Polynomial num) {
        this(num, Polynomial.ONE, SAFE);
    }
    
    public Rational add(Rational other) {
//        System.out.println("In Rational.add, Adding " + this + " to " + other);
        return new Rational(this.num.mul(other.den).add(other.num.mul(this.den)), this.den.mul(other.den));
    }
    
    public Rational sub(Rational other) {
        return this.add(other.negate());
    }
    
    public Rational negate() {
        return new Rational(this.num.negate(), this.den, SAFE);
    }
    
    
    public Rational mul(Rational other) {
        return new Rational(this.num.mul(other.num), this.den.mul(other.den));
    }
    
    Rational div(Rational other) {
        return new Rational(this.num.mul(other.den), this.den.mul(other.num));
    }
    
    public Rational mul(Polynomial other) {
        return new Rational(this.num.mul(other), this.den);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.num);
        hash = 47 * hash + Objects.hashCode(this.den);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rational other = (Rational) obj;
        if (!Objects.equals(this.num, other.num)) {
            return false;
        }
        if (!Objects.equals(this.den, other.den)) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        return "( " + num + " )/( " + den + " )";
    }

    

}
