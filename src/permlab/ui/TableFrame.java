package permlab.ui;

import java.awt.Desktop;
import java.awt.KeyboardFocusManager;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.TableModel;
import permlib.classes.PermClassInterface;
import permlib.property.PermProperty;
import permlib.property.Union;
import permlib.property.Universal;

/**
 * The frame for an enumeration table.
 *
 * @author Michael Albert
 */
public class TableFrame extends javax.swing.JFrame {

    private static final char CTL_L = (char) 12;
    private static final char CTL_M = (char) 13;
    private PopupMenu popMenu = new PopupMenu();
    private int[] selectedRowIndices;
    private int[] selectedColumnIndices;
    private ClassEnumerationFrameTask parentTask;
    private ArrayList<PermProperty> properties;
    private PermClassInterface theClass;

    
    public TableFrame(String title, int[] lengths, ArrayList<PermProperty> properties, ClassEnumerationFrameTask parentTask) {
        this(null, title, lengths, properties, parentTask);
    }
    
    public TableFrame(String title, int[] lengths, ArrayList<PermProperty> properties) {
        this(null, title, lengths, properties, null);
    }
    
    /**
     * Creates new TableFrame.
     *
     * @param title the title of the frame
     * @param lengths the lengths for which enumerations are carried out
     * @param titles the titles of the columns
     */
    public TableFrame(PermClassInterface theClass, String title, int[] lengths, ArrayList<PermProperty> properties, ClassEnumerationFrameTask parentTask) {
        initComponents();
        this.theClass = theClass;
        this.parentTask = parentTask;
        this.properties = properties;
        if (title.length() <= 40) {
            titleLabel.setText(title);
        } else {
            titleLabel.setText(title.substring(0, 37) + "...");
        }
        String[] titles = new String[this.properties.size()];
        for (int i = 0; i < titles.length; i++) {
            titles[i] = this.properties.get(i).toString();
        }
        enumerationTable.setModel(new EnumerationTableModel(lengths, titles));
        for (int i = 0; i < enumerationTable.getColumnCount(); i++) {
            enumerationTable.getColumnModel().getColumn(i);
            if (i == 0) {
                enumerationTable.getColumnModel().getColumn(i).setPreferredWidth(30);
            } else {
                enumerationTable.getColumnModel().getColumn(i).setPreferredWidth(100);
            }
        }
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(new HelpDispatcher("Table window", "TableFrameHelp.html", this));
        initialisePopupMenu();
    }

    /**
     * Returns the enumeration model for the table.
     * 
     * @return the enumeration model for the table
     */
    public TableModel getModel() {
        return enumerationTable.getModel();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tablePopupMenu = new javax.swing.JPopupMenu();
        sloaneMenuItem = new javax.swing.JMenuItem();
        titleLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        enumerationTable = new javax.swing.JTable();
        stopButton = new javax.swing.JButton();

        sloaneMenuItem.setText("Sloane selection");
        sloaneMenuItem.setToolTipText("Look up selection in the online encyclopedia of integer sequences");
        sloaneMenuItem.setComponentPopupMenu(tablePopupMenu);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        titleLabel.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        titleLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        titleLabel.setText("Title");

        enumerationTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        enumerationTable.setToolTipText("Type ctl-l (ell) to look up selection from a single column in the OEIS.");
        enumerationTable.setColumnSelectionAllowed(true);
        enumerationTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                enumerationTableMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                enumerationTableMouseClicked(evt);
            }
        });
        enumerationTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tableKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(enumerationTable);

        stopButton.setText("Stop");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(titleLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(stopButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 505, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(stopButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Per a suggestion from V Vatter, on ctl-l, look up selected column (or
     * part thereof) in OEIS.
     *
     * @param evt the key event
     */
    private void tableKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableKeyTyped
        if (evt.getKeyChar() == CTL_L && enumerationTable.getSelectedColumns().length == 1) {
            int c = enumerationTable.getSelectedColumns()[0];
            String url = "https://oeis.org/search?q=";
            for (int r : enumerationTable.getSelectedRows()) {
                url += enumerationTable.getModel().getValueAt(r, c) + (r < enumerationTable.getRowCount() - 1 ? "%2C" : "");
            }
            url += "&language=english&go=Search";
            try {

                Desktop.getDesktop().browse(new URI(url));

            } catch (Exception ex) {
                Logger.getLogger(TableFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_tableKeyTyped

    private void enumerationTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enumerationTableMouseClicked
        tableMouseAction(evt);
    }//GEN-LAST:event_enumerationTableMouseClicked

    private void enumerationTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_enumerationTableMousePressed
        tableMouseAction(evt);
    }//GEN-LAST:event_enumerationTableMousePressed

    /**
     * Stops the thread doing the enumeration.
     * 
     * @param evt stop button being pressed
     */
    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        parentTask.cancel(true);
    }//GEN-LAST:event_stopButtonActionPerformed

    /**
     * Stops the thread doing the enumeration when the window is closed.
     * 
     * @param evt the window closing
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        parentTask.cancel(true);
    }//GEN-LAST:event_formWindowClosing

    /**
     * Deals with the selection in the table if the mouse is clicked.
     * 
     * @param evt the mouse being clicked
     */
    private void tableMouseAction(java.awt.event.MouseEvent evt) {
        if (evt.isPopupTrigger()) {      
            selectedRowIndices = enumerationTable.getSelectedRows();
            selectedColumnIndices = enumerationTable.getSelectedColumns();
            Point point = evt.getPoint();
            int selectedRow = enumerationTable.rowAtPoint(point);
            int selectedColumn = enumerationTable.columnAtPoint(point);

            if (((selectedRowIndices.length == 1) && (selectedColumnIndices.length == 1)) || selectedRowIndices.length == 0) {
                enumerationTable.changeSelection(selectedRow, selectedColumn, false, false);
            } else {
                enumerationTable.changeSelection(selectedRow, selectedColumn, true, true);
            }

            selectedRowIndices = enumerationTable.getSelectedRows();
            selectedColumnIndices = enumerationTable.getSelectedColumns();
            if (selectedColumnIndices[0] != 0) {
                popMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            }
            for (int i = 0; i < selectedColumnIndices.length; i++) {
                selectedColumnIndices[i]--;
            }
            for (int i = 0; i < selectedRowIndices.length; i++) {
                selectedRowIndices[i]++;
            }
        }
    }

    /**
     * Returns the row lengths of the selection.
     * 
     * @return the row lengths of the selection
     */
    private int[] getSelectedRowLengths() {
        int[] result = new int[selectedRowIndices.length];
        for(int i = 0; i < selectedRowIndices.length; i++) {
            result[i] = (int) ((EnumerationTableModel) enumerationTable.getModel()).getRowLength(selectedRowIndices[i]-1);
        }
        return result;
    }
    
    /**
     * Initialises the pop-up menu that is displayed when the table is right-clicked on.
     * 
     */
    private void initialisePopupMenu() {
        popMenu = new PopupMenu();
        MenuItem animate = new MenuItem("Animate");
        animate.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (confirmed()) {
                    ArrayList<PermProperty> selectedProperties = new ArrayList<PermProperty>();
                    for (Integer i : selectedColumnIndices) {
                        selectedProperties.add(properties.get(i));
                    }
                    PermProperty propUnion = new Union(selectedProperties);
                    doAnimationFromTable(theClass, propUnion, getSelectedRowLengths());
                }
            }
        });
        popMenu.add(animate);

        MenuItem list = new MenuItem("List");
        list.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if (confirmed()) {
                    ArrayList<PermProperty> selectedProperties = new ArrayList<PermProperty>();
                    for (Integer i : selectedColumnIndices) {
                        selectedProperties.add(properties.get(i));
                    }
                    PermProperty propUnion = new Union(selectedProperties);
                    makeListFromTable(theClass, propUnion, getSelectedRowLengths());
                }
            }
        });
        popMenu.add(list);

        MenuItem tableToText = new MenuItem("Table to text");
        tableToText.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                Long[][] values = new Long[selectedRowIndices.length][selectedColumnIndices.length]; 
                for(int i = 0; i < values.length; i++) {
                    for (int j = 0; j < values[0].length; j++) {
                        values[i][j] = (Long) (enumerationTable.getValueAt(selectedRowIndices[i] - 1, selectedColumnIndices[j] + 1));
                    }
                }
                new TableTextFrame(values);
            }
        });
        popMenu.add(tableToText);

        enumerationTable.add(popMenu);
    }

    /**
     * If there are more than 100,000 permutations in the selection then this
     * generates a dialog box to check that the user wants to continue with the
     * operation.
     * 
     * @return true if user confirms, false otherwise
     */
    private boolean confirmed() {
        Long totalPerms = new Long(0);
        for (int i = 0; i < selectedRowIndices.length; i++) {
            for (int j = 0; j < selectedColumnIndices.length; j++) {
                totalPerms += (Long) (enumerationTable.getValueAt(selectedRowIndices[i] - 1, selectedColumnIndices[j] + 1));
            }
        }
        if (totalPerms > 100000) {
            int reply = JOptionPane.showConfirmDialog(this,
                    "There are more than 100,000 permutations in this\n"
            + "set. Do you still want to continue?\n", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (reply != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Animates a collection of permutations from the table.
     * 
     * @param theClass the permutation class
     * @param additionalProperty an additional property to impose on the class
     * @param lengths the lengths that will be in the collection to be animated
     */
    public void doAnimationFromTable(PermClassInterface theClass, PermProperty additionalProperty, int[] lengths) {
        AnimationTask task = new AnimationTask(theClass, lengths, additionalProperty);
        task.execute();
    }

    /**
     * Lists the permutations from a collection in the table.
     * 
     * @param theClass the permutation class
     * @param additionalProperty an additional property to impose on the class
     * @param lengths the lengths that will be in the collection to be listed
     */
    public void makeListFromTable(PermClassInterface theClass, PermProperty additionalProperty, int[] lengths) {
        ListTask task = new ListTask(theClass, lengths, additionalProperty);
        task.execute();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TableFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TableFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TableFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TableFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ArrayList<PermProperty> properties = new ArrayList<PermProperty>();
                properties.add(new Universal());
                properties.add(new Universal());
                int[] lengths = {1, 2, 3, 4};
                new TableFrame("Test", lengths, properties).setVisible(true);
            }
        });
    }

    /**
     * Returns the title label.
     * 
     * @return the title label
     */
    public JLabel getTitleLabel() {
        return titleLabel;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable enumerationTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JMenuItem sloaneMenuItem;
    private javax.swing.JButton stopButton;
    private javax.swing.JPopupMenu tablePopupMenu;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
