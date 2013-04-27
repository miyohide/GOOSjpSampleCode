package actionsniper;

// 第13章にて導入。Mainクラスから処理を抽出。

import jp.goos.sample.ui.MainWindow;

public class AuctionSniper implements AuctionEventListener {
    private MainWindow ui;
    private final SniperListener sniperListener;
    
    public AuctionSniper(Auction auction, SniperListener sniperListener) {
        this.sniperListener = sniperListener;
    }

    @Override
    public void auctionClosed() {
        // Mainにあった実装をsniperListenerの実装に委譲。
        sniperListener.sniperLost();
    }

    @Override
    public void currentPrice(int price, int increment) {
        /* bidsHigherAndReportsBiddingWhenNewPriceArrives()で呼び出しているため、
         * 一旦、未実装を表す処理をコメント化
         */
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
