package jp.goos.sample.ui;

import actionsniper.SniperSnapshot;
import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    public static final String STATUS_WON = "won";
    public static final String STATUS_JOINING = "joining";
    public static final String STATUS_LOST = "lost";
    public static final String STATUS_BIDDING = "bidding";
    public static final String STATUS_WINNING = "winning";

    private static String[] STATUS_TEXT = {
        STATUS_JOINING, STATUS_BIDDING, STATUS_WINNING, STATUS_LOST, STATUS_WON};
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, null);
    private String statusText = STATUS_JOINING;
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
    
    public void sniperStateChanged(SniperSnapshot newSniperSnapshot, String newStatusText) {
        sniperState = newSniperSnapshot;
        this.statusText = STATUS_TEXT[newSniperSnapshot.state.ordinal()];
        fireTableRowsUpdated(0, 0);
    }
}
