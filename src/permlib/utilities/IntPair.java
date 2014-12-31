package permlib.utilities;

/**
 * A convenience class for representing a pair of integers.
 * 
 * @author Michael Albert
 */
public class IntPair implements Comparable<IntPair> {
    
    private int first;
    private int second;
    
    /**
     * Constructs a pair of integers.
     * 
     * @param first the first element of the pair
     * @param second the second element of the pair
     */
    public IntPair(int first, int second) {
        this.first = first;
        this.second = second;
    }
    
    /**
     * Returns the first element of the pair.
     * 
     * @return the first element of the pair
     */
    public int getFirst() {
        return first;
    }
    
    /**
     * Returns the second element of the pair.
     * 
     * @return the second element of the pair
     */
    public int getSecond() {
        return second;
    }
    
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof IntPair)) {
            return false;
        }
        return this.first == ((IntPair) other).first && this.second == ((IntPair) other).second;
    }

    @Override
    public int hashCode() {
        return first*34517 + second;
    }

    @Override
    public int compareTo(IntPair other) {
        if (this.first != other.first) 
            return this.first - other.first;
        return this.second - other.second;
    }
    
    public IntPair reverse() {
            return new IntPair(second, first);
    }
    
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

} 
