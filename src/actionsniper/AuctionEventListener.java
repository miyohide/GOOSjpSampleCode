package actionsniper;

import java.util.EventListener;

// P201 Mainクラスの変更に対応するためにEventListenerをextendsした
public interface AuctionEventListener extends EventListener{
    enum PriceSource {
        FromSniper,      // スナイパーから入札
        FromOtherBidder; // 他の人から入札
    }
    public void auctionClosed();
    public void currentPrice(int price, int increment, PriceSource pricesource);
}
