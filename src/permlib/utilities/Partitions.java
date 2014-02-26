package permlib.utilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author Michael Albert
 */
public class Partitions implements Iterable<ArrayList<Integer>> {

    static final int NO_RESTRICTIONS = -1;
    final int n;
    final int k;

    public Partitions(int n) {
        this(n, NO_RESTRICTIONS);
    }
    
    public Partitions(int n, int k) {
        this.n = n;
        this.k = k;
    }

    @Override
    public Iterator<ArrayList<Integer>> iterator() {
        return new Iterator<ArrayList<Integer>>() {

            ArrayList<Integer> p = new ArrayList<Integer>();
            boolean updated;

            {
                p.add(n);
                if (satisfiesRestrictions()) {
                    updated = true;
                } else {
                    if (!hasNext()) {
                        p = null;
                    } else {
                        p = next();
                        updated = true;
                    }
                }
            }

            @Override
            public boolean hasNext() {
                update();
                return p != null;
            }

            @Override
            public ArrayList<Integer> next() {
                if (updated) {
                    updated = false;
                    return p;
                }
                update();
                return p;
            }

            private void update() {
                if (updated || p == null) {
                    return;
                }
                if (p.get(0) == 1) {
                    p = null;
                    return;
                }
                ArrayList<Integer> q = new ArrayList<Integer>();
                int sum = 0;
                for (int i = 0; i < p.size() && p.get(i) > 1; i++) {
                    q.add(p.get(i));
                    sum += p.get(i);
                }
                int m = q.get(q.size() - 1) - 1;
                q.set(q.size() - 1, m);
                sum--;
                int rem = n - sum;
                while (rem >= m) {
                    q.add(m);
                    rem -= m;
                }
                if (rem > 0) {
                    q.add(rem);
                }
                p = q;
                if (!satisfiesRestrictions()) update();
                updated = true;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            private boolean satisfiesRestrictions() {
                return p != null && (k == NO_RESTRICTIONS || p.size() == k);
            }

        };
    }
    
    public static ArrayList<IntPair> toExponentForm(ArrayList<Integer> partition) {
        if (partition.isEmpty()) return new ArrayList<>();
        int currentValue = partition.get(0);
        int currentCount = 1;
        ArrayList<IntPair> result = new ArrayList<>();
        for(int i = 1; i < partition.size(); i++) {
            if (partition.get(i) == currentValue) {
                currentCount++;
            } else {
                result.add(new IntPair(currentValue, currentCount));
                currentValue = partition.get(i);
                currentCount = 1;
            }
        }
        result.add(new IntPair(currentValue, currentCount));
        return result;
    }
    
    public static void main(String[] args) {
        for(ArrayList<Integer> a : new Partitions(5)) {
            for(int i : a) System.out.print(i + " ");
            System.out.println();
        }
        System.out.println();
        for(ArrayList<Integer> a : new Partitions(8,3)) {
            for(int i : a) System.out.print(i + " ");
            System.out.println();
            System.out.println(Partitions.toExponentForm(a));
        }
        
        System.out.println("Should be empty");
        for(ArrayList<Integer> a : new Partitions(2,3)) {
            for(int i : a) System.out.print(i + " ");
            System.out.println();
            System.out.println(Partitions.toExponentForm(a));
        }
        
    }

}
