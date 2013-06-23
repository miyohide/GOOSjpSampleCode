package auctionsniper;

import auctionsniper.ui.SwingThreadSniperListener;

public class AuctionSniper implements AuctionEventListener {
    private SniperListener sniperListener;
    private final Auction auction;
    private SniperSnapshot snapshot;
    private Item item = null;

    public AuctionSniper(Item item, Auction auction) {
        this.auction = auction;
        this.item = item;
        this.snapshot = SniperSnapshot.joining(item.identifier);
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
                if (item.allowsBid(bid)) {
                    auction.bid(bid);
                    snapshot = snapshot.bidding(price, bid);
                } else {
                    snapshot = snapshot.losing(price);
                }
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

    @Override
    public void auctionFailed() {
    }
}
