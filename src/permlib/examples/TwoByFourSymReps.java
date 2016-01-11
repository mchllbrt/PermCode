package permlib.examples;

import java.util.HashSet;
import java.util.Objects;
import permlib.Permutation;

/**
 * What are the sym reps for pairs (4p, 4q)?
 * 
 * @author Michael Albert
 */
public class TwoByFourSymReps {

    
    public static void main(String[] args) {
        
        
    }
    
    
    class PermPair implements Comparable<PermPair> {
        
        Permutation p;
        Permutation q;

        public PermPair(Permutation p, Permutation q) {
            if (p.compareTo(q) > 0) {
                this.p = q;
                this.q = p;            
            } else {
                 this.p = p;
            this.q = q;
            }
        }
        
        @Override
        public String toString() {
            return p + " " + q;
        }

        @Override
        public int hashCode() {
            int hash = 3;
            hash = 53 * hash + Objects.hashCode(this.p);
            hash = 53 * hash + Objects.hashCode(this.q);
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
            final PermPair other = (PermPair) obj;
            if (!Objects.equals(this.p, other.p)) {
                return false;
            }
            if (!Objects.equals(this.q, other.q)) {
                return false;
            }
            return true;
        }

        @Override
        public int compareTo(PermPair o) {
            if (this.p.compareTo(o.p) != 0) return this.p.compareTo(o.p);
            return this.q.compareTo(o.q);
        }
        
        
    }
}
