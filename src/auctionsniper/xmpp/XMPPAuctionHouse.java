package auctionsniper.xmpp;

import auctionsniper.Auction;
import auctionsniper.AuctionHouse;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuctionHouse implements AuctionHouse{
    public static final String AUCTION_RESOURCE = "Auction";

    private final XMPPConnection connection;
    
    public XMPPAuctionHouse(XMPPConnection connection) {
        this.connection = connection;
    }

    @Override
    public Auction auctionFor(String itemId) {
        return new XMPPAuction(connection, itemId);
    }

    public static XMPPAuctionHouse connect(String hostname, String username, String password) throws XMPPException {
        XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE);
        
        return new XMPPAuctionHouse(connection);
    }

    @Override
    public void disconnect() {
        this.connection.disconnect();
    }
    
}
