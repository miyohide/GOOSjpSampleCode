package jp.goos.sample.ui;

import actionsniper.SniperState;
import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    private String statusText = MainWindow.STATUS_JOINING;
    
    @Override
    public int getColumnCount() { return 1; }
    @Override
    public int getRowCount() { return 1; }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) { return statusText; }
    
    public void setStatusText(String newStatusText) {
        statusText = newStatusText;
        fireTableRowsUpdated(0, 0);
    }

    public void sniperStatusChanged(SniperState sniperState, String STATUS_BIDDING) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
