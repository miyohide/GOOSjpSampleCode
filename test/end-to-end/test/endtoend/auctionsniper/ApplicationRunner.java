package test.endtoend.auctionsniper;

import actionsniper.Main;
import actionsniper.SniperState;
import static test.endtoend.auctionsniper.FakeAuctionServer.XMPP_HOSTNAME;
import jp.goos.sample.ui.MainWindow;
import jp.goos.sample.ui.SnipersTableModel;

public class ApplicationRunner {

    public static final String SNIPER_ID = "sniper";
    public static final String SNIPER_PASSWORD = "sniper";
    public static final String SNIPER_XMPP_ID = SNIPER_ID + "@" + XMPP_HOSTNAME + "/Auction";
    private AuctionSniperDriver driver;

    public void startBinddingIn(final FakeAuctionServer... auctions) {
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(arguments(auctions));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.setDaemon(true);
        thread.start();
        driver = new AuctionSniperDriver(1000);
        driver.hasTitle(MainWindow.APPLICATION_TITLE);
        driver.hasColumnTitles();
//        for (FakeAuctionServer auction : auctions) {
//            driver.showsSniperStatus(auction.getItemId(), 0, 0,
//                    SnipersTableModel.textFor(SniperState.JOINING));
//        }
    }

    protected static String[] arguments(FakeAuctionServer... auctions) {
        String[] arguments = new String[auctions.length + 3];
        arguments[0] = XMPP_HOSTNAME;
        arguments[1] = SNIPER_ID;
        arguments[2] = SNIPER_PASSWORD;
        for (int i = 0; i < auctions.length; i++) {
            arguments[i + 3] = auctions[i].getItemId();
        }
        return arguments;
    }

    public void showsSniperHasLostAuction() {
        driver.showsSniperStatus(SnipersTableModel.STATUS_LOST);
    }

    public void showsSniperHasWonAuction() {
        driver.showsSniperStatus(SnipersTableModel.STATUS_WINNING);
    }

    public void stop() {
        if (driver != null) {
            driver.dispose();
        }
    }

    void hasShownSniperIsBidding() {
        driver.showsSniperStatus(SnipersTableModel.STATUS_BIDDING);
    }

    public void hasShownSniperIsBidding(FakeAuctionServer auction, int lastPrice, int lastBid) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid, SnipersTableModel.STATUS_BIDDING);
    }

    void hasShownSniperIsWinning() {
        driver.showsSniperStatus(SnipersTableModel.STATUS_WINNING);
    }

    public void hasShownSniperIsWinning(FakeAuctionServer auction, int winningBid) {
        driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid, SnipersTableModel.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(FakeAuctionServer auction, int lastPrice) {
        driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice, SnipersTableModel.STATUS_WON);
    }
}
