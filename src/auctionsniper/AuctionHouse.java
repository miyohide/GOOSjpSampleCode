package auctionsniper;

public interface AuctionHouse {

    Auction auctionFor(String itemId);

    public void disconnect();
}
