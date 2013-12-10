package permlab.utilities;

import javax.swing.JFrame;
import permlib.Permutation;
import permlib.utilities.RestrictedPermutation;

/**
 * Specifies an interface for exporting from StaticPermFrame.
 * 
 * @author M Belton
 */
public interface ExportInterface {
    
    public static final int DEFAULT_WIDTH = 200;
    public static final int DEFAULT_HEIGHT = 200;
    
    /**
     * Provides an instance of a file chooser that will remember where it last
     * saved a file.
     */
    public javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();

    /**
     * Exports a restricted permutation to some format.
     * 
     * @param parent the StaticPermFrame instance to export from.
     * @param restPerm the restricted permutation to export.
     */
    public void export(RestrictedPermutation restPerm);
    
    /**
     * Exports a permutation to some format.
     * 
     * @param parent the StaticPermFrame instance to export from.
     * @param restPerm the restricted permutation to export.
     */
    public void export(Permutation perm);
    
    public void export(RestrictedPermutation restPerm, boolean monotoneConstraints, boolean showGrid);
    public void export(Permutation perm, boolean showGrid);
    
    public void export (RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid);
    public void export (Permutation p, int width, int height, boolean showGrid);
     
    public void export(JFrame parent, RestrictedPermutation restPerm, int width, int height, boolean monotoneConstraints, boolean showGrid);
    public void export(JFrame parent, Permutation p, int width, int height, boolean showGrid);
}
