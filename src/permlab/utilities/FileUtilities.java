package permlab.utilities;

import java.io.File;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * A general utilities class for file operations.
 *
 * @author M Belton
 */
public class FileUtilities {

    /**
     * Creates a dialogue box to check whether a file can be saved in the
     * selected place.
     *
     * @param parent parent component to give screen position
     * @param file file to try and save
     * @return true if file can be saved there.
     */
    public static boolean canSaveFileHere(JFrame parent, File file) {
        if (file.exists()) {
            int response = JOptionPane.showConfirmDialog(parent,
                    "Overwrite existing file?", "Confirm Overwrite",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.CANCEL_OPTION) {
                return false;
            }
        }
        return true;
    }
}
