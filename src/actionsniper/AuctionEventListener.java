package actionsniper;

public interface AuctionEventListener {
    enum PriceSource {
        FromSniper,      // スナイパーから入札
        FromOtherBidder; // 他の人から入札
    }
    public void auctionClosed();
    public void currentPrice(int price, int increment);
}
