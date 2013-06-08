package test.integration.auctionsniper.xmpp;

import static org.junit.Assert.assertTrue;

import auctionsniper.Auction;
import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.xmpp.XMPPAuction;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.jivesoftware.smack.XMPPConnection;
import org.junit.Test;
import test.endtoend.auctionsniper.ApplicationRunner;
import test.endtoend.auctionsniper.FakeAuctionServer;

public class XMPPAuctionTest {
    @Test public void receivesEventsFromAuctionServerAfterJoining() throws Exception {
        XMPPConnection connection = new XMPPConnection("localhost");
        connection.connect();
        connection.login(ApplicationRunner.SNIPER_ID, ApplicationRunner.SNIPER_PASSWORD, FakeAuctionServer.AUCTION_RESOURCE);
        FakeAuctionServer auctionServer = new FakeAuctionServer("item-54321");
        auctionServer.startSellingItem();
        CountDownLatch auctionWasClosed = new CountDownLatch(1);
        
        Auction auction = new XMPPAuction(connection, auctionServer.getItemId());
        auction.addAuctionEventListener(auctionClosedListener(auctionWasClosed));
        
        auction.join();
        auctionServer.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
        auctionServer.announceClosed();
        
        assertTrue("should have been closed", auctionWasClosed.await(2, TimeUnit.SECONDS));
    }
    
    private AuctionEventListener auctionClosedListener(final CountDownLatch auctionWasClosed) {
        return new AuctionEventListener() {
            public void auctionClosed() { auctionWasClosed.countDown(); }
            public void currentPrice(int price, int increment, PriceSource priceSource) {
                // 未実装
            }
        };
    }
}