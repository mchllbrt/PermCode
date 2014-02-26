package permlib.examples;

import java.math.BigInteger;
import java.util.Arrays;

/**
 *
 * @author Michael Albert
 */
public class Polynomial {

    static final boolean SAFE = true;
    static final Polynomial ZERO = new Polynomial(new BigInteger[0], SAFE);
    static final Polynomial ONE = new Polynomial(new BigInteger[] {BigInteger.ONE}, SAFE);
    static final Polynomial T = new Polynomial(new BigInteger[] {BigInteger.ZERO, BigInteger.ONE}, SAFE);

    BigInteger[] coeffs;

    public Polynomial(BigInteger[] coeffs) {
        this(coeffs, !SAFE);
    }

    public Polynomial(BigInteger[] coeffs, boolean safe) {
        if (safe) {
            this.coeffs = coeffs;
        } else {
            this.coeffs = Arrays.copyOf(coeffs, coeffs.length);
            clearZeroes();
        }
    }

    private void clearZeroes() {
        int d = coeffs.length;
        while (d > 0 && coeffs[d - 1].equals(BigInteger.ZERO)) {
            d--;
        }
        if (d < coeffs.length) {
            coeffs = Arrays.copyOf(coeffs, d);
        }
    }

    public Polynomial add(Polynomial other) {
        int d = (this.coeffs.length > other.coeffs.length) ? this.coeffs.length : other.coeffs.length;
        BigInteger[] result = new BigInteger[d];
        for (int i = 0; i < d; i++) {
            if (i < this.coeffs.length) {
                result[i] = coeffs[i].add((i < other.coeffs.length) ? other.coeffs[i] : BigInteger.ZERO);
            } else {
                result[i] = other.coeffs[i];
            }
        }
        return new Polynomial(result);
    }
    
    public Polynomial sub(Polynomial other) {
        return add(other.negate());
    }
    
    public Polynomial negate() {
        return this.mul(BigInteger.ONE.negate());
    }

    public Polynomial mod(Polynomial q) {
        if (this.coeffs.length < q.coeffs.length) {
            return this;
        }
        BigInteger leadq = q.coeffs[q.coeffs.length - 1];
        BigInteger[] c = Arrays.copyOf(coeffs, coeffs.length);
        for (int d = c.length - 1; d >= q.coeffs.length - 1; d--) {
            if (c[d].equals(BigInteger.ZERO)) {
                continue;
            }
            BigInteger cf = c[d].gcd(leadq);
            BigInteger mc = leadq.divide(cf);
            BigInteger mq = c[d].divide(cf);
            for (int i = 0; i < c.length; i++) {
                c[i] = c[i].multiply(mc);
            }
//            System.out.println("A " + Arrays.toString(c));
            for (int j = 0; j < q.coeffs.length; j++) {
                c[d - j] = c[d - j].subtract(q.coeffs[q.coeffs.length - j - 1].multiply(mq));
                // System.out.println("X  " + (d-j) + " " + c[d-j]);
            }
//            System.out.println("Y " + Arrays.toString(c));
        }
        return new Polynomial(c, !SAFE);
    }

    public Polynomial gcd(Polynomial q) {
        if (q.equals(ZERO)) {
            return this;
        }
        return q.gcd(this.mod(q));
    }

    public Polynomial mul(BigInteger m) {
        return shift(0, m);
    }

    public Polynomial makePrimitive() {
        if (coeffs.length == 0) {
            return this;
        }
        BigInteger gcd = coeffs[coeffs.length - 1];
        int i = coeffs.length - 2;
        while (i >= 0 && !gcd.equals(BigInteger.ONE)) {
            gcd = gcd.gcd(coeffs[i]);
            i--;
        }
        BigInteger[] c = Arrays.copyOf(coeffs, coeffs.length);
        for (i = 0; i < c.length; i++) {
            c[i] = c[i].divide(gcd);
        }
        return new Polynomial(c, SAFE);
    }

    public Polynomial mul(Polynomial q) {
//        System.out.println("Multiplying " + this + " by " + q);
        if (this.equals(ZERO) || q.equals(ZERO)) {
            return ZERO;
        }
        BigInteger[] c = new BigInteger[this.coeffs.length + q.coeffs.length - 1];
        for (int i = 0; i < c.length; i++) {
            BigInteger ci = BigInteger.ZERO;
            for (int j = 0; j < coeffs.length && j <= i; j++) {
                if (i - j < q.coeffs.length) {
                    ci = ci.add(coeffs[j].multiply(q.coeffs[i - j]));
                }
            }
            c[i] = ci;
        }
//        System.out.println(Arrays.toString(c));
        return new Polynomial(c, !SAFE);
    }

    public Polynomial div(Polynomial q) {
        if (this.coeffs.length < q.coeffs.length) {
            return ZERO;
        }
        BigInteger leadq = q.coeffs[q.coeffs.length - 1];
        BigInteger[] p = Arrays.copyOf(this.coeffs, this.coeffs.length);
        BigInteger[] c = new BigInteger[this.coeffs.length - q.coeffs.length + 1];
        for (int i = 0; i < c.length; i++) {
            BigInteger x = p[p.length - 1 - i].divide(leadq);
            c[c.length - 1 - i] = x;
            for (int j = 0; j < q.coeffs.length; j++) {
                p[p.length - 1 - i - j] = p[p.length - 1 - i - j].subtract(x.multiply(q.coeffs[q.coeffs.length - 1 - j]));
            }
        }
        return new Polynomial(c, SAFE);
    }

    private Polynomial shift(int k, BigInteger m) {
        if (m.equals(BigInteger.ZERO)) {
            return Polynomial.ZERO;
        }
        BigInteger[] c = new BigInteger[coeffs.length + k];
        for (int i = 0; i < coeffs.length; i++) {
            c[i + k] = coeffs[i].multiply(m);
        }
        return new Polynomial(c, SAFE);
    }

    public int degree() {
        return coeffs.length - 1;
    }

    @Override
    public int hashCode() {
        clearZeroes();
        int hash = 7;
        hash = 59 * hash + Arrays.deepHashCode(this.coeffs);
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
        final Polynomial other = (Polynomial) obj;
        if (!Arrays.deepEquals(this.coeffs, other.coeffs)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return textString('t');
    }

    public String textString(char x) {
        if (coeffs.length == 0) {
            return "0";
        }
        StringBuilder result = new StringBuilder();
        for (int i = coeffs.length - 1; i >= 0; i--) {
            result.append(term(coeffs[i], i, x));
        }
        if (result.charAt(0) == '+') {
            result.deleteCharAt(0);
        }
        return result.toString();
    }

    private String term(BigInteger c, int i, char x) {
        if (c.equals(BigInteger.ZERO)) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        BigInteger cp = c.abs();
        if (c.compareTo(BigInteger.ZERO) < 0) {
            result.append('-');
        } else {
            result.append('+');
        }
        if (!cp.equals(BigInteger.ONE) || i == 0) {
            result.append(cp);
        }
        if (i > 0) {
            if (!cp.equals(BigInteger.ONE)) {
                result.append('*');
            }
            result.append(x);
            result.append('^');
            result.append(i);
        }
        return result.toString();
    }
}
