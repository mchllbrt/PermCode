package permlab.int_classes;

import javax.swing.SwingWorker;
import permlib.Permutation;
import permlib.classes.SimplePermClass;
import permlib.processor.PermProcessor;
import static permlib.PermUtilities.*;


/**
 * This class is an interruptible extension of SimplePermClass. This can be
 * used when working with threads so that iteration over the class can be
 * canceled during traversal.
 * 
 * @author M Belton
 */
public class INT_SimplePermClass extends SimplePermClass {

    SwingWorker parentTask;
    SimplePermClass parentClass;
    
    /**
     * Constructed from a parent class and parent thread.
     * 
     * @param parentClass
     * @param parentTask 
     */
    public INT_SimplePermClass(SimplePermClass parentClass, SwingWorker parentTask) {
        this.parentClass = parentClass;
        this.parentTask = parentTask;
    }
    
    /**
     * This method checks whether the parent thread has been canceled and if not
     * then it will continue processing the permutations in the class.
     */
    @Override
    public void DFSProcessSimples(SimplePermClass.DecoratedSimplePerm s, int i, int n, PermProcessor processor) {
        if (parentTask == null || !parentTask.isCancelled()) {
            parentClass.DFSProcessSimples(s, i, n, processor);
        }
    }
    
    @Override
    public void processPerms(int n, PermProcessor processor) {
        if (n == 1) {
            processor.process(new Permutation(new int[]{1}));
        } else if (n == 2) {
            processor.process(new Permutation(new int[]{1, 2}));
            processor.process(new Permutation(new int[]{2, 1}));
        } else if (n > 3) {

            for (int k = 4; k <= n; k += 2) {
                for (Permutation p : exceptionalSimples(k)) {
                    if (parentClass.containsPermutation(p)) {
                        DecoratedSimplePerm s = new DecoratedSimplePerm(p, p.length());
                        DFSProcessSimples(s, k, n, processor);
                    }
                }
            }
        }
    }
}
