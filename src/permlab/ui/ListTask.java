package permlab.ui;

import javax.swing.SwingWorker;
import permlib.classes.PermClassInterface;
import permlib.Permutation;
import permlib.processor.PermCollector;
import permlib.property.PermProperty;
import java.util.List;

/**
 * Class defines a task for listing a collection of permutations in a text frame.
 * 
 * @author M Belton
 */
public class ListTask extends SwingWorker<Void, Permutation> {

    int[] lengths;
    PermClassInterface theClass;
    PermCollector collector = new PermCollector();
    TextFrame textFrame = null;
    String title = "";
    PermProperty additionalProperty;

    /**
     * Creates the class to list and text frame for displaying.
     * 
     * @param theClass the permutation class
     * @param lengths the lengths of permutations to list
     * @param additionalProperty additional properties of the class
     */
    public ListTask(PermClassInterface theClass, int[] lengths, PermProperty additionalProperty) {
        this.lengths = lengths;
        this.additionalProperty = additionalProperty;
        this.theClass = theClass;
        textFrame = new TextFrame();
        textFrame.setVisible(true);
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (int n : lengths) {
            for (Permutation p : theClass.getPerms(n)) {
                publish(p); // Send list of permutations to process method
            }
        }
        return null;
    }

    @Override
    protected void process(List<Permutation> perms) {
        for (Permutation perm : perms) {
            if (additionalProperty.isSatisfiedBy(perm)) {       
                textFrame.getTextArea().append(perm + "\n"); // append to text frame
            }
        }
    }
}