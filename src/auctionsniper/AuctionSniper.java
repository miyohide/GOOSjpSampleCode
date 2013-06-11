package auctionsniper;

import auctionsniper.ui.SwingThreadSniperListener;

public class AuctionSniper implements AuctionEventListener {
    private SniperListener sniperListener;
    private final Auction auction;
    private String itemId = null;
    private SniperSnapshot snapshot;
    
    public AuctionSniper(String itemId, Auction auction, SniperListener sniperListener) {
        this.sniperListener = sniperListener;
        this.auction = auction;
        this.itemId = itemId;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    public AuctionSniper(String itemId, Auction auction) {
        // P206 sniperListenerの初期化をaddSniperListenerメソッドとして分離している
        this.auction = auction;
        this.itemId = itemId;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    @Override
    public void auctionClosed() {
        snapshot = snapshot.closed();
        notifyChange();
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        switch(priceSource) {
            case FromSniper:
                snapshot = snapshot.winning(price);
                break;
            case FromOtherBidder:
                final int bid = price + increment;
                auction.bid(bid);
                snapshot = snapshot.bidding(price, bid);
                break;
        }
        notifyChange();
    }

    private void notifyChange() {
        sniperListener.sniperStateChanged(snapshot);
    }

    public SniperSnapshot getSnapshot() {
        return snapshot;
    }

    public void addSniperListener(SniperListener listener) {
        this.sniperListener = listener;
    }
}
