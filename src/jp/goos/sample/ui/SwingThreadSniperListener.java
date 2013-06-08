package jp.goos.sample.ui;

import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import javax.swing.SwingUtilities;

public class SwingThreadSniperListener implements SniperListener {
    private final SniperListener delegate;
    
    public SwingThreadSniperListener(SniperListener delegate) {
        this.delegate = delegate;
    }

    @Override
    public void sniperStateChanged(final SniperSnapshot sniperSnapshot) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                delegate.sniperStateChanged(sniperSnapshot);
            }
        });
    }
}
