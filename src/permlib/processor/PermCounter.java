package permlib.processor;

import java.util.ArrayList;
import permlib.Permutation;
import permlib.property.PermProperty;
import permlib.property.Universal;

/**
 * A permutation processor that just counts the number of times it is called.
 * An optional {@link PermProperty property} limits the
 * counter to only counting permutations that satisfy the property. Additionally
 * it is possible to specify secondary properties in order to keep track of a
 * variety of counts.
 * 
 * @author Michael Albert
 */
public class PermCounter implements PermProcessor {
    
    private long count = 0;
    private PermProperty property;
    private long[] secondaryCounts;
    PermProperty[] secondary = null;
    
    /**
     * Constructor for counting all permutations.
     */
    public PermCounter() {
        this(new Universal());
    }
    /**
     * Constructor with a filtering property included.
     * @param property the property
     */
    public PermCounter(PermProperty property) {
        this.property = property;
    }
    
    public PermCounter(PermProperty main, PermProperty[] secondary) {
        this.property = main;
        this.secondary = secondary;
        this.secondaryCounts = new long[secondary.length];
    }
    
    public PermCounter(PermProperty main, ArrayList<PermProperty> secondaryArrayList) {
        this.property = main;
        PermProperty[] secondaryArray = new PermProperty[secondaryArrayList.size()];
        for (int i = 0; i < secondaryArray.length; i++) {
            secondaryArray[i] = secondaryArrayList.get(i);
        }
        this.secondary = secondaryArray;
        this.secondaryCounts = new long[secondaryArray.length];
    }

    /**
     * Adds one to the count if the permutation has the property.
     * 
     * @param p the permutation
     */
    @Override
    public void process(Permutation p) {
        if (property.isSatisfiedBy(p)) {
            count++;
            if (secondary != null) {
                for(int i = 0; i < secondary.length; i++) {
                    if (secondary[i].isSatisfiedBy(p)) secondaryCounts[i]++;
                }
            }
        }
    }

    /**
     * Resets the count to zero.
     */
    @Override
    public void reset() {
        count = 0;
        if (secondary != null) {
            for(int i = 0; i < secondaryCounts.length; i++) secondaryCounts[i] = 0;
        }
    }
    
    /**
     * Returns the number of times this processor has been called with a 
     * permutation satisfying the property since construction or the last 
     * reset.
     * 
     * @return the number of permutations with the property
     */
    public long getCount() {
        return count;
    }
    
    /**
     * Returns counts for secondary properties recorded.
     * 
     * @return counts for secondary properties
     */
    public long[] getSecondaryCounts() {
        return secondaryCounts;
    }

    @Override
    public String report() {
        return "" + count;
    }
    
}
