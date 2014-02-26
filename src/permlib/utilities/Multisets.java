package permlib.utilities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Iterate over multisets of size k from a collection
 * @author Michael Albert
 */
public class Multisets<T> implements Iterable<Collection<T>> {
    
    T[] pool;
    final Iterator<int[]> m;
    
    public Multisets(Set<T> pool, int k) {
        this.pool = (T[]) pool.toArray();
        m = new MultisetCodes(pool.size(), k).iterator();
    }

    @Override
    public Iterator<Collection<T>> iterator() {
        return new Iterator<Collection<T>>() {

            @Override
            public boolean hasNext() {
                return m.hasNext();
            }

            @Override
            public Collection<T> next() {
                int[] code = m.next();
                ArrayList<T> result = new ArrayList<T>();
                for(int i : code) result.add(pool[i]);
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        };
    }
    
    public static void main(String[] args) {
        Set<String> s = new HashSet<>();
        s.add("My");
        s.add("cat");
        s.add("Furry");
        for(Collection<String> c : new Multisets<>(s, 4)) {
            for(String a : c) System.out.print(a + " ");
            System.out.println();
        }
    }

}
