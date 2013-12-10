package permlab.ui;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import permlib.classes.InvolutionPermClass;
import permlib.classes.PermutationClass;
import permlib.classes.PermClassInterface;
import permlib.PermUtilities;
import permlib.Permutation;
import permlib.classes.SimplePermClass;
import permlib.processor.PermCounter;
import permlib.property.Intersection;
import permlib.property.Involution;
import permlib.property.MinusIndecomposable;
import permlib.property.MinusIrreducible;
import permlib.property.PermProperty;
import permlib.property.PlusIndecomposable;
import permlib.property.PlusIrreducible;
import permlib.property.Simple;
import permlib.property.Universal;

/**
 * The thread doing background calculations for the enumeration of a class.
 * 
 * @author M Belton
 */
public class ClassEnumerationFrameTask extends SwingWorker<Void, long[]> {

    static final int MAX = 1000000;
    private int[] lengths;
    private PermClassInterface theClass;
    private PermCounter proc;
    private TableFrame tableFrame = null;
    private TextFrame textFrame = null;
    private String[] lines;
    private String title;
    private boolean restrictToSimples;
    private boolean restrictToInvolutions;
    private JFrame parentFrame;
    private ArrayList<Permutation> basis;

    /**
     * Creates the task
     * 
     * @param lengths lengths to be enumerated
     * @param restrictToSimples whether class is restricted to simples
     * @param restrictToInvolutions whether class is restricted to involutions
     * @param basisText the string from the basis area
     * @param parentFrame the parent frame that created this task
     */
    public ClassEnumerationFrameTask(int[] lengths, boolean restrictToSimples, boolean restrictToInvolutions, String basisText, JFrame parentFrame) {
        this.lengths = lengths;
        this.restrictToSimples = restrictToSimples;
        this.restrictToInvolutions = restrictToInvolutions;
        this.parentFrame = parentFrame;
        basis = new ArrayList<Permutation>();
        Scanner in = new Scanner(basisText);
        while (in.hasNextLine()) {
            basis.add(new Permutation(in.nextLine()));
        }
        initialisePermutationClass(basis);
    }

    /**
     * Creates the task
     * 
     * @param length length to be enumerated
     */
    public ClassEnumerationFrameTask(int length) {
        this(new int[]{length}, false, false, null, null);
    }

    /**
     * Creates the task
     * 
     * @param lengths lengths to be enumerated
     */
    public ClassEnumerationFrameTask(int[] lengths) {
        this(lengths, false, false, null, null);
    }

    /**
     * Creates the task
     * 
     * @param lengths lengths to be enumerated
     * @param parentFrame the parent frame that created this task
     */
    public ClassEnumerationFrameTask(int[] lengths, ClassEnumerationFrame parentFrame) {
        this(lengths, false, false, null, parentFrame);
    }
    
    /**
     * Creates the task
     * 
     * @param length length to be enumerated
     * @param basisText the string from the basis area
     */
    public ClassEnumerationFrameTask(int length, String basisText) {
        this(new int[]{length}, false, false, basisText, null);
    }
    
    /**
     * Creates the task
     * 
     * @param lengths lengths to be enumerated
     * @param basisText the string from the basis area
     */
    public ClassEnumerationFrameTask(int[] lengths, String basisText) {
        this(lengths, false, false, basisText, null);
    }
    
    /**
     * Creates the task
     * 
     * @param length length to be enumerated
     * @param basisText the string from the basis area
     * @param parentFrame the parent frame that created this task
     */
    public ClassEnumerationFrameTask(int length, String basisText, ClassEnumerationFrame parentFrame) {
        this(new int[]{length}, false, false, basisText, parentFrame);
    }
    
    /**
     * Creates the task
     * 
     * @param lengths lengths to be enumerated
     * @param basisText the string from the basis area
     * @param parentFrame the parent frame that created this task
     */
    public ClassEnumerationFrameTask(int[] lengths, String basisText, ClassEnumerationFrame parentFrame) {
        this(lengths, false, false, basisText, parentFrame);
    }
    
    @Override
    public Void doInBackground() {
        for (int n : lengths) {
            long[] results = null;
            if (!restrictToSimples) {
                results = new long[2 + PermUtilities.MAIN_PROPERTIES.length];
            } else {
                results = new long[2];
            }

            results[0] = n;
            CalcRun calculation = new CalcRun(theClass, n, proc, results);
            Thread calcThread = new Thread(calculation);
            calcThread.start();

            while (!isCancelled() && !calculation.isCompleted()) {
                int s = 0;
                for (int i = 0; i < MAX; i++) {
                    s += i;
                }
            }

            if (isCancelled()) {
                calcThread.stop();
                // System.out.println("How dare you tell me to stop!");
                if (tableFrame == null) {
                    textFrame.getTextArea().append("\nCancelled");
                } else {
                    String tableTitle = tableFrame.getTitleLabel().getText();
                    tableTitle += " cancelled.";
                    tableFrame.getTitleLabel().setText(tableTitle);
                    tableFrame.getTitleLabel().setForeground(Color.RED);

                }
                return null;
            }

            if (!restrictToSimples) {
                results[1] = proc.getCount();
                System.arraycopy(proc.getSecondaryCounts(), 0, results, 2, results.length - 2);
            } else {
                results[1] = proc.getCount();
            }
            proc.reset();
            publish(results);
        }
        return null;
    }

    @Override
    protected void process(List<long[]> rows) {
        for (long[] row : rows) {
            if (tableFrame == null) {
                processToText(row);
            } else {
                processToTable(row);

            }
        }

    }

    /**
     * Initialises a table to display the output.
     * 
     * @param properties the properties to be displayed in the table
     */
    private void initialiseTableFrame(ArrayList<PermProperty> properties) {
        tableFrame = new TableFrame(theClass, title, lengths, properties, this);
        tableFrame.setLocation(parentFrame.getX() + parentFrame.getWidth() + 10, parentFrame.getY());
        tableFrame.setVisible(true);
    }

    /**
     * Initialises a frame to display the output as text.
     */
    private void initialiseTextFrame() {
        textFrame = new TextFrame();
        textFrame.getTextArea().setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textFrame.setLocation(parentFrame.getX() + parentFrame.getWidth() + 10, parentFrame.getY());
    }

    /**
     * Outputs the calculated values to the table.
     * 
     * @param row the row for output
     */
    private void processToTable(long[] row) {
        int rowNumber = 0;
        while (lengths[rowNumber] != row[0]) {
            rowNumber++;
        }
        for (int i = 1; i < row.length; i++) {
            tableFrame.getModel().setValueAt(new Long(row[i]), rowNumber, i);
        }
    }

    /**
     * Outputs the calculated values to a text frame.
     * 
     * @param row the row for output 
     */
    private void processToText(long[] row) {
        int rowNumber = 0;
        while (lengths[rowNumber] != row[0]) {
            rowNumber++;
        }
        for (int i = 1; i < row.length; i++) {
            lines[i] += String.format("%11d", row[i]) + (rowNumber != lengths.length - 1 ? "," : "");
            fillFromLines();
        }
    }

    /**
     * Appends output to text frame.
     */
    private void fillFromLines() {
        textFrame.getTextArea().append(title + "\n");
        textFrame.getTextArea().setText("");
        for (int i = 0; i < lines.length; i++) {
            textFrame.getTextArea().append(lines[i] + "\n");
        }
        textFrame.setVisible(true);
    }

    /**
     * Initialise the permutation class being enumerated.
     * 
     * @param basis the basis of the permutation class.
     */
    private void initialisePermutationClass(ArrayList<Permutation> basis) {
        ArrayList<PermProperty> properties = new ArrayList<PermProperty>();
        if (restrictToSimples) {
            if (restrictToInvolutions) {
                theClass = new InvolutionPermClass(basis);
                proc = new PermCounter(new Simple());
                title = "Simple Involutions of Av(" + basisString() + ")";
                properties.add(new Intersection(new Involution(), new Simple()));
            } else {
                theClass = new SimplePermClass(basis);
                proc = new PermCounter();
                title = "Simples of Av(" + basisString() + ")";
                properties.add(new Simple());
            }
        } else {
            properties.add(new Simple());
            properties.add(new PlusIndecomposable());
            properties.add(new MinusIndecomposable());
            properties.add(new PlusIrreducible());
            properties.add(new MinusIrreducible());
            if (restrictToInvolutions) {
                theClass = new InvolutionPermClass(basis);
                proc = new PermCounter(new Involution(), properties);
                title = "Involutions of Av(" + basisString() + ")";
                properties.add(0, new Involution());
            } else {
                theClass = new PermutationClass(basis);
                proc = new PermCounter(new Universal(), properties);
                title = "Av(" + basisString() + ")";
                properties.add(0, new Universal());
            }
        }
        initialiseTableFrame(properties);
    }

    /**
     * Scans and returns the string in the basis area.
     * 
     * @return the string in the basis area
     */
    private String basisString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < basis.size() - 1; i++) {
            result.append(basis.get(i));
            result.append(", ");
        }
        if (basis.size() > 0) {
            result.append(basis.get(basis.size() - 1));
        }
        return result.toString();
    }

    /**
     * Inner class to enumerate the permutation class.
     */
    private class CalcRun implements Runnable {

        private final long[] results;
        private final PermClassInterface theClass;
        private final int n;
        private final PermCounter proc;
        private boolean completed;

        public CalcRun(PermClassInterface theClass, int n, PermCounter proc, long[] results) {
            this.theClass = theClass;
            this.n = n;
            this.proc = proc;
            this.results = results;
            this.completed = false;
        }

        public boolean isCompleted() {
            return completed;
        }

        @Override
        public void run() {
            theClass.processPerms(n, proc);
            completed = true;
        }
    }
}
