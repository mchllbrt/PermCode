package permlab.int_classes;

import javax.swing.SwingWorker;
import permlib.Permutation;
import permlib.Permutations;
import permlib.classes.PermClassInterface;
import permlib.classes.PermutationClass;
import permlib.processor.PermProcessor;

/**
 * This class is an interruptible extension of PermutationClass. This can be
 * used when working with threads so that iteration over the class can be
 * canceled during traversal.
 * 
 * @author M Belton
 */
public class INT_PermutationClass extends PermutationClass {

    private SwingWorker parentTask;
    private PermClassInterface parentClass;

    /**
     * Constructed from a parent class and parent thread.
     * 
     * @param parentClass
     * @param parentTask 
     */
    public INT_PermutationClass(PermClassInterface parentClass, SwingWorker parentTask) {
        this.parentTask = parentTask;
        this.parentClass = parentClass;
    }
    
    /**
     * This method checks whether the parent thread has been canceled and if not
     * then it will continue processing the permutations in the class.
     */
    @Override
    public void processPerms(int length, PermProcessor proc) {
        for (Permutation p : new Permutations(parentClass, length)) {
            proc.process(p);
            if (parentTask.isCancelled()) {
                break;
            }
        }
    }
}
