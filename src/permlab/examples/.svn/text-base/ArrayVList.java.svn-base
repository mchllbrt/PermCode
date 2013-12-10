/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package permlab.examples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

/**
 *
 * @author MichaelAlbert
 */
public class ArrayVList {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HashSet<ArrayWrapper> array = new HashSet<ArrayWrapper>();
        HashSet<ArrayList<Integer>> list = new HashSet<ArrayList<Integer>>();
        Random r = new Random();
        for(int i = 0; i < 10000; i++) {
            int size = r.nextInt(10)+1;
            int[] a = new int[size];
            ArrayList<Integer> l = new ArrayList<Integer>();
            for(int j = 0; j < a.length; j++) {
                int t = r.nextInt(6);
                a[j] = t;
                l.add(t);
            }
            array.add(new ArrayWrapper(a));
            list.add(l);
        }
        System.out.println(array.size() + " " + list.size());
        long start = System.currentTimeMillis();
        int count = 0;
        ArrayWrapper aw = new ArrayWrapper(new int[10]);
        for(int i = 0; i < 10000000; i++) {
            int size = r.nextInt(10)+1;
            int[] a = new int[size];
            for(int j = 0; j < a.length; j++) {
                int t = r.nextInt(6);
                a[j] = t;
            }
            aw.content = a;
            if (array.contains(aw)) count++;
        }
        System.out.println(count + " ( " + (System.currentTimeMillis() - start) + " )");
        start = System.currentTimeMillis();
        count = 0;
        for(int i = 0; i < 10000000; i++) {
        ArrayList<Integer> l = new ArrayList<Integer>();
            for(int j = 0; j < 10; j++) {
                l.add(r.nextInt(6));
                if (list.contains(l)) count++;
            }
            
        //    l.clear();
        }
        System.out.println(count + " ( " + (System.currentTimeMillis() - start) + " )");
    }
    
    private static class ArrayWrapper {
        private int[] content;
        
        public ArrayWrapper(int[] content) {
            this.content = content;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(content);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ArrayWrapper other = (ArrayWrapper) obj;
            return Arrays.equals(other.content, this.content);
        }
        
    }
}
