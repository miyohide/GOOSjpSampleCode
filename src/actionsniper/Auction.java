package actionsniper;

public interface Auction {
    void bid(int amount);
    public void join();

    public void addAuctionEventListener(AuctionSniper auctionSniper);
}
