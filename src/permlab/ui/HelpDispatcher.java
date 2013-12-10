package permlab.ui;

import java.awt.Component;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michael Albert
 */
public class HelpDispatcher implements KeyEventDispatcher {

    private static final char HELP_KEY = '?'; // !
    private final String title;
    private final String fileName;
    private final Component parent;
    private HelpFrame helpFrame;

    public HelpDispatcher(String title, String fileName, Component parent) {
        this.title = title;
        this.fileName = fileName;
        this.parent = parent;

    }

    public HelpFrame getHelpFrame() {
        return helpFrame;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow() == parent && e.getID() == KeyEvent.KEY_TYPED && e.getKeyChar() == HELP_KEY) {
            try {
                if (this.helpFrame != null) {
                    this.helpFrame.setVisible(true);
                    return false;
                }
                // Desktop.getDesktop().browse(new URI("http://www.cs.otago.ac.nz/staffpriv/malbert/index.php"));
                java.net.URL helpURL = ClassEnumerationFrame.class.getResource(fileName);
                if (helpURL != null) {
                    try {
                        helpFrame = new HelpFrame();
                        helpFrame.setTitleLabel(title);
                        helpFrame.setPage(helpURL);
                        helpFrame.setLocation(parent.getX() + parent.getWidth() + 10, parent.getY());
                        helpFrame.setVisible(true);
                    } catch (IOException ex) {
                        System.err.println("Attempted to read a bad URL: " + helpURL);
                    }
                } else {
                    System.err.println("Couldn't find file: " + fileName);
                }
            } catch (Exception ex) {
                Logger.getLogger(ClassEnumerationFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }
}
