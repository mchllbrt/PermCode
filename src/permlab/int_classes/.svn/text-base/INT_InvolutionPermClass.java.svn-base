package permlab.int_classes;

import java.util.Iterator;
import javax.swing.SwingWorker;
import permlib.Permutation;
import permlib.classes.InvolutionPermClass;
import permlib.processor.PermProcessor;

/**
 * This class is an interruptible extension of InvolutionPermClass. This can be
 * used when working with threads so that iteration over the class can be
 * canceled during traversal.
 * 
 * @author M Belton
 */
public class INT_InvolutionPermClass extends InvolutionPermClass {

    private SwingWorker parentTask;
    private InvolutionPermClass parentClass;

    /**
     * Constructed from a parent class and parent thread.
     * 
     * @param parentClass
     * @param parentTask 
     */
    public INT_InvolutionPermClass(InvolutionPermClass parentClass, SwingWorker parentTask) {
        this.parentClass = parentClass;
        this.parentTask = parentTask;
    }

    /**
     * This method checks whether the parent thread has been canceled and if not
     * then it will continue processing the permutations in the class.
     */
    @Override
    public void processClass(int low, int high, PermProcessor proc) {
        Iterator<Permutation> it = parentClass.getIterator(low, high);
        while (it.hasNext()) {
            proc.process(it.next());
            if (parentTask.isCancelled()) {
                return;
            }
        }
    }
}
