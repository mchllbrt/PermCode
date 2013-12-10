package permlab.ui;

import permlab.utilities.UIUtilities;
import java.util.Collection;
import java.util.List;
import javax.swing.SwingWorker;
import permlib.Permutation;
import permlib.classes.PermClassInterface;
import permlib.property.PermProperty;
import permlib.property.Universal;

/**
 * A thread associated with the AnimationPermFrame to process permutations in a
 * collection and pass them to the frame to be displayed.
 * 
 * @author M Belton
 */
public class AnimationTask extends SwingWorker<Void, Collection<Permutation>> {

    ClassEnumerationFrame parent;
    int[] lengths;
    PermClassInterface theClass;
    PermProperty additionalProperty;
    AnimatedPermFrame frame = new AnimatedPermFrame(this);

    /**
     * Creates the task.
     * 
     * @param theClass the permutation class
     * @param lengths the lengths in the class to generate
     */
    public AnimationTask(PermClassInterface theClass, int[] lengths) {
        this(theClass, lengths, new Universal());
    }

    /**
     * Creates the task.
     * 
     * @param theClass the permutation class
     * @param lengths the lengths in the class to generate
     * @param additionalProperty an additional property to add to the class while processing
     */
    public AnimationTask(PermClassInterface theClass, int[] lengths, PermProperty additionalProperty) {
        this.theClass = UIUtilities.createInterruptibleClass(theClass, this);
        this.lengths = lengths;
        this.additionalProperty = additionalProperty;
        frame.setVisible(true);
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (int n : lengths) {
            publish(theClass.getPerms(n)); // Sends list of permutations to process when retrieved
            if (this.isCancelled()) {
                return null;
            }
        }
        return null;
    }

    @Override
    protected void process(List<Collection<Permutation>> permCollections) {
        for (Collection<Permutation> permCollection : permCollections) {
            frame.addPerms(permCollection, additionalProperty); // adds permutation to frame
        }
    }
}