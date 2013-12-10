package permlab.ui;

import javax.swing.table.AbstractTableModel;

/**
 * The model for enumeration tables.
 * 
 * @author Michael Albert
 */
public final class EnumerationTableModel extends AbstractTableModel {
    
    int[] lengths;
    Long[][] data;
    String[] titles;
    String[] columnNames;
    
    /**
     * Creates a new model for the table enumeration.
     * 
     * 
     * @param lengths the lengths to enumerate
     * @param titles the titles of the columns to display
     */
    public EnumerationTableModel(int[] lengths, String[] titles) {
        this.lengths = lengths;
        this.titles = titles;
        data = new Long[lengths.length][titles.length+1];
        columnNames = new String[titles.length+1];
        columnNames[0] = "N";
        System.arraycopy(titles, 0, columnNames, 1, titles.length);
        for(int i = 0; i < lengths.length; i++) {
            setValueAt(new Long(lengths[i]), i, 0);
        }
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }
    
    @Override
    public int getRowCount() {
        return lengths.length;
    }

    @Override
    public int getColumnCount() {
        return titles.length+1;
    }

    @Override
    public Object getValueAt(int i, int i1) {
        return data[i][i1];
    }
    
    public long getRowLength(int r) {
        return data[r][0];
    }
    
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    
    @Override
    public Class getColumnClass(int c) {
        return java.lang.Long.class;
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        data[row][col] = (Long) value;
        fireTableCellUpdated(row, col);
    }
}
