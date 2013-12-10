package permlab.utilities;

import permlib.classes.PermClassInterface;
import javax.swing.SwingWorker;
import permlab.int_classes.INT_InvolutionPermClass;
import permlab.int_classes.INT_PermutationClass;
import permlab.int_classes.INT_SimplePermClass;
import permlab.int_classes.INT_UniversalPermClass;
import permlib.classes.InvolutionPermClass;
import permlib.classes.PermutationClass;
import permlib.classes.SimplePermClass;
import permlib.classes.UniversalPermClass;

/**
 * A factory class for creating interruptible versions of permutation classes from
 * permlib.classes.
 * 
 * @author M Belton
 */
public class UIUtilities {
    
    /**
     * Factory method to create interruptible permutation class.
     * 
     * @param permClass permlib.classes class to get interruptible version of
     * @param parentTask parent thread running the task
     * @return an interruptible permutation class
     */
    public static PermClassInterface createInterruptibleClass(PermClassInterface permClass, SwingWorker parentTask) {
        if (permClass instanceof SimplePermClass) return new INT_SimplePermClass((SimplePermClass)permClass, parentTask);
        if (permClass instanceof InvolutionPermClass) return new INT_InvolutionPermClass((InvolutionPermClass)permClass, parentTask);
        if (permClass instanceof PermutationClass) return new INT_PermutationClass((PermutationClass)permClass, parentTask);
        if (permClass instanceof UniversalPermClass) return new INT_UniversalPermClass((UniversalPermClass)permClass, parentTask);
        return null;
    }
}
