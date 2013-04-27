package actionsniper;

// 第13章にて導入。Mainクラスから処理を抽出。

import javax.swing.SwingUtilities;
import jp.goos.sample.ui.MainWindow;

/*
 * 第13章にて導入。P128の時点ではまだMainから移動はしていない。
 */
public class AuctionSniper implements AuctionEventListener {
    private MainWindow ui;
    private final SniperListener sniperListener;
    
    public AuctionSniper(SniperListener sniperListener) {
        this.sniperListener = sniperListener;
    }

    @Override
    public void auctionClosed() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ui.showStatus(MainWindow.STATUS_LOST);
            }
        });
    }

    @Override
    public void currentPrice(int price, int increment) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
