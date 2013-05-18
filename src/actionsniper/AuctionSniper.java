package actionsniper;

public class AuctionSniper implements AuctionEventListener {
    private final SniperListener sniperListener;
    private final Auction auction;
    private boolean isWinning = false;
    private String itemId = null;
    private SniperSnapshot snapshot;
    
    public AuctionSniper(Auction auction, SniperListener sniperListener, String itemId) {
        this.sniperListener = sniperListener;
        this.auction = auction;
        this.itemId = itemId;
        this.snapshot = SniperSnapshot.joining(itemId);
    }

    @Override
    public void auctionClosed() {
        if (isWinning) {
            sniperListener.sniperWon();
        } else {
            sniperListener.sniperLost();
        }
    }

    @Override
    public void currentPrice(int price, int increment, PriceSource priceSource) {
        // 引数でとったpriceSourceがFromSniperであればisWinningがtrue、そうでなければfalse
        isWinning = (priceSource == PriceSource.FromSniper);
        if (isWinning) {
            snapshot = snapshot.winning(price);
        } else {
            final int bid = price + increment;
            auction.bid(bid);
            snapshot = snapshot.bidding(price, bid);
        }
        sniperListener.sniperStateChanged(snapshot);
    }
    
}
