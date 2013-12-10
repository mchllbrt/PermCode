package permlab.int_classes;

import javax.swing.SwingWorker;
import permlib.classes.UniversalPermClass;
import permlib.processor.PermProcessor;

/**
 * This class is an interruptible extension of InvolutionPermClass. This can be
 * used when working with threads so that iteration over the class can be
 * canceled during traversal.
 * 
 * @author M Belton
 */
public class INT_UniversalPermClass extends UniversalPermClass {
    
    private SwingWorker parentTask;
    private UniversalPermClass parentClass;
    
    /**
     * Constructed from a parent class and parent thread.
     * 
     * @param parentClass
     * @param parentTask 
     */
    public INT_UniversalPermClass(UniversalPermClass parentClass, SwingWorker parentTask) {
        this.parentClass = parentClass;
        this.parentTask = parentTask;
    }
    
    /**
     * This method checks whether the parent thread has been canceled and if not
     * then it will continue processing the permutations in the class.
     */
    @Override
    public void processClass(int low, int high, PermProcessor proc) {
        parentClass.setupBoundedIterator(low, high);
        while (parentClass.iterator().hasNext()) {
            proc.process(iterator().next());
            if (parentTask.isCancelled()) {
                return;
            }
        }
    }
}
