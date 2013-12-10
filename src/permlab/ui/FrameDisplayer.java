package permlab.ui;

import permlib.Permutation;
import permlab.ui.TextFrame;
import permlib.processor.PermProcessor;

/**
 * Processes permutations by adding them to a TextFrame. That is, displaying them
 * in a scratch pad text window.
 * 
 * @author M Albert
 */
public class FrameDisplayer implements PermProcessor {
    
    private final TextFrame frame;
    
    /**
     * Construct with the frame to output to.
     * 
     * @param frame the frame to send permutations to
     */
    public FrameDisplayer(TextFrame frame) {
        this.frame = frame;
    }

    @Override
    public void process(Permutation p) {
        frame.addText(p);
    }

    @Override
    public void reset() {
        
    }

    @Override
    public String report() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
