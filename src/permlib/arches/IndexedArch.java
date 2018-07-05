package permlib.arches;

/**
 * Basic representation of an arch as a pair of integers
 * @author Michael Albert
 */
public class IndexedArch implements Comparable<IndexedArch> {
    
    final int l;
    final int r;

    public IndexedArch(int l, int r) {
        this.l = Math.min(l,r);
        this.r = Math.max(l,r);
    }

    @Override
    public int hashCode() {
        return (this.l << 16) | r;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IndexedArch other = (IndexedArch) obj;
        if (this.l != other.l) {
            return false;
        }
        if (this.r != other.r) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(IndexedArch other) {
        int d = this.l - other.l;
        return (d != 0) ? d : (this.r - other.r);
    }
    
    
    
}
