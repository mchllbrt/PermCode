package permlab.examples;

import java.util.Iterator;
import java.util.Scanner;
import permlib.classes.InvolutionPermClass;
import permlib.Permutation;

/**
 * Messing around hoping to figure out how to solve the concurrency issues.
 * Basic testing for an interrupt even after each hasNext() doesn't seem to
 * be a performance issue - in fact the non-testing version took longer :)
 *
 * Running two enumerators on the same class creates problems. The problem
 * is that the PermProperty definingProperty is being hit by isSatisfiedBy 
 * requests from two threads -- so ideally they should have their own copies
 * of definingProperty. In general this is problematic since it's not clear how
 * or indeed if PermProperties can be cloned. In practice for the GUI it's not
 * a problem since we could actually just build it from scratch. 
 * 
 * Alternatively synchronize. But trying to synchronize the iterator doesn't
 * work since it's interleaving access to the property that's creating the
 * problem (we have two instances of the iterator). However synchronizing the
 * property as a data field of the class does work. The performance hit is
 * about 100% vs just having two distinct instances of the class. So where it's
 * possible to clone the property that would be much better.
 * 
 * Found the issue was that InvolvesFromRight was using global state. So can 
 * also fix things by simply eliminating this and carrying that state through 
 * method arguments there. A trifle ugly but it seems to work.
 * 
 * Take home message -- PermProperties need to be immutable and the isSatisfiedBy()
 * methods cannot use any mutable global state.
 * 
 * The idea that if we can do away with processors all will be well would
 * seem to have some support -- just need to make sure that different calc
 * tasks are working on different perm class objects.
 * 
 * @author Michael Albert
 */
public class ThreadExperiments {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        InvolutionPermClass i = new InvolutionPermClass(new Permutation("123"));
//        ClassEnumerator c1 = new ClassEnumerator(new InvolutionPermClass(new Permutation("123")));
//        ClassEnumerator c2 = new ClassEnumerator(new InvolutionPermClass(new Permutation("123")));
        ClassEnumerator c1 = new ClassEnumerator(i);
        ClassEnumerator c2 = new ClassEnumerator(i);
        Thread t1 = new Thread(c1);
        Thread t2 = new Thread(c2);
        long start = System.currentTimeMillis();
        t1.start();
        t2.start();
        while (t1.isAlive() || t2.isAlive()) {};
        System.out.println(System.currentTimeMillis() - start);
//        int i = input.nextInt();
//        if (i == 1) t1.interrupt();
//        if (i == 2) t2.interrupt();
//        i = input.nextInt();
//        if (i == 1) t1.interrupt();
//        if (i == 2) t2.interrupt();
        
    }

    static class ClassEnumerator implements Runnable {

        InvolutionPermClass c;

        public ClassEnumerator(InvolutionPermClass c) {
            this.c = c;
        }

        @Override
        public void run() {
            for (int n = 0; n <= 20; n++) {
                int count = 0;
                Iterator<Permutation> it = c.getIterator(n);
//                Iterator<Permutation> it = c.getSynchronizedIterator(n);
                while (it.hasNext()) {
                    if (Thread.interrupted()) {
                        return;
                    }
                    count++;
                    it.next();
                }
                System.out.println("Class " + c + ", n = " + n + ", count = " + count);
            }
        }
    }
}
