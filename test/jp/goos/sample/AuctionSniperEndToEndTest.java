package jp.goos.sample;

import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class AuctionSniperEndToEndTest {
    private final FakeAuctionServer auction = new FakeAuctionServer("item-54321");
    private final ApplicationRunner application = new ApplicationRunner();
    
    @Test public void sniperJoinsAuctionUntilAuctionCloses() throws Exception {
        auction.startSellingItem();
        application.startBinddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper();
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }
    
    @Test public void sniperMakesAHigherBidButLoses() throws Exception {
        auction.startSellingItem();
        
        application.startBinddingIn(auction);
        auction.hasReceivedJoinRequestFromSniper();
        
        auction.reportPrice(1000, 98, "other bidder");
        
        application.hasShownSniperIsBidding();
        auction.hasReceiveBid(1098, ApplicationRunner.SNIPERXMPP_ID);
        
        auction.announceClosed();
        application.showsSniperHasLostAuction();
    }
    
    // 追加のクリーンアップ
    @After public void stopAuction() {
        auction.stop();
    }
    
    @After public void stopApplication() {
        application.stop();
    }

}