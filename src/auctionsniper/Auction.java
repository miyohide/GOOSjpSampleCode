package auctionsniper;

public interface Auction {

    void bid(int amount);

    public void join();

    public void addAuctionEventListener(AuctionEventListener auctionEventListener);
}
