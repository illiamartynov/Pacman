import javax.swing.table.AbstractTableModel;

public class PacmanTableModel extends AbstractTableModel {
    private int rows;
    private int cols;
    private Object[][] data;

    public PacmanTableModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.data = new Object[rows][cols];
    }

    @Override
    public int getRowCount() {
        return rows;
    }

    @Override
    public int getColumnCount() {
        return cols;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        // Set the value at the specified cell
        data[rowIndex][columnIndex] = aValue;
        // Notify the table model that the cell value has been updated
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
