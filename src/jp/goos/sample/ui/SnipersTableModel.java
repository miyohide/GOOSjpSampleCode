package jp.goos.sample.ui;

import actionsniper.SniperSnapshot;
import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, null);
    private String statusText = MainWindow.STATUS_JOINING;
    private SniperSnapshot sniperState = STARTING_UP;
    
    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (Column.at(columnIndex)) {
            case ITEM_IDENTIFIER:
                return sniperState.itemId;
            case LAST_PRICE:
                return sniperState.lastPrice;
            case LAST_BID:
                return sniperState.lastBid;
            case SNIPER_STATE:
                return statusText;
            default:
                throw new IllegalArgumentException("No column at " + columnIndex);
        }
    }
    
    public void sniperStatusChanged(SniperSnapshot newSniperSnapshot, String newStatusText) {
        sniperState = newSniperSnapshot;
        statusText = newStatusText;
        fireTableRowsUpdated(0, 0);
    }

    public void setStatusText(String newStatusText) {
        statusText = newStatusText;
        fireTableRowsUpdated(0, 0);
    }
    
}
