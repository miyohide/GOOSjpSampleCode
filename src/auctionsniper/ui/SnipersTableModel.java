package auctionsniper.ui;

import auctionsniper.AuctionSniper;
import auctionsniper.PortfolioListener;
import auctionsniper.SniperCollector;
import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;
import auctionsniper.util.Defect;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class SnipersTableModel extends AbstractTableModel implements SniperListener, SniperCollector, PortfolioListener {
    public static final String STATUS_WON = "Won";
    public static final String STATUS_JOINING = "Joining";
    public static final String STATUS_LOST = "Lost";
    public static final String STATUS_BIDDING = "Bidding";
    public static final String STATUS_WINNING = "Winning";

    private static String[] STATUS_TEXT = {
        STATUS_JOINING, STATUS_BIDDING, STATUS_WINNING, STATUS_LOST, STATUS_WON};
    private List<SniperSnapshot> snapshots = new ArrayList<>();
    private final List<AuctionSniper> notToBeGCd = new ArrayList<>();
    
    @Override
    public int getColumnCount() {
        return Column.values().length;
    }

    @Override
    public int getRowCount() {
        return snapshots.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return Column.at(columnIndex).valueIn(snapshots.get(rowIndex));
    }
    
    @Override
    public String getColumnName(int column) {
        return Column.at(column).name;
    }
    
    public void sniperStateChanged(SniperSnapshot newSnapshot) {
        int row = rowMatching(newSnapshot);
        snapshots.set(row, newSnapshot);
        fireTableRowsUpdated(row, row);
    }

    public static String textFor(SniperState state) {
        return STATUS_TEXT[state.ordinal()];
    }

    public void addSniper(SniperSnapshot sniperSnapshot) {
        snapshots.add(sniperSnapshot);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }

    @Override
    public void addSniper(AuctionSniper sniper) {
        notToBeGCd.add(sniper);
        addSniperSnapshot(sniper.getSnapshot());
        sniper.addSniperListener(new SwingThreadSniperListener(this));
    }

    private void addSniperSnapshot(SniperSnapshot snapshot) {
        snapshots.add(snapshot);
        int row = snapshots.size() - 1;
        fireTableRowsInserted(row, row);
    }

    private int rowMatching(SniperSnapshot newSnapshot) {
        for (int i = 0; i < snapshots.size(); i++) {
            if (newSnapshot.isForSameItemAs(snapshots.get(i))) {
                return i;
            }
        }
        throw new Defect("Cannot find match for " + newSnapshot);
    }

    @Override
    public void sniperAdded(AuctionSniper sniper) {
        addSniper(sniper);
    }
}
