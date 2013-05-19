package jp.goos.sample.ui;

import actionsniper.SniperSnapshot;
import actionsniper.SniperState;
import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel {
    public static final String STATUS_WON = "Won";
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";

    private static String[] STATUS_TEXT = {
        STATUS_JOINING, STATUS_BIDDING, STATUS_WINNING, STATUS_LOST, STATUS_WON};
    private final static SniperSnapshot STARTING_UP = new SniperSnapshot("", 0, 0, SniperState.JOINING);
    private SniperSnapshot snapshot = STARTING_UP;
    
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
        return Column.at(columnIndex).valueIn(snapshot);
    }
    
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        snapshot = newSnapshot;
        fireTableRowsUpdated(0, 0);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }
}
