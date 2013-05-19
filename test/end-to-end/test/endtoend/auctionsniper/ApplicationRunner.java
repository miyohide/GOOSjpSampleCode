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
    private String itemId;
    
    public void startBinddingIn(final FakeAuctionServer auction) {
        itemId = auction.getItemId();
        Thread thread = new Thread("Test Application") {
            @Override
            public void run() {
                try {
                    Main.main(XMPP_HOSTNAME, SNIPER_ID, SNIPER_PASSWORD, auction.getItemId());
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
        driver.showsSniperStatus(
                SnipersTableModel.textFor(SniperState.JOINING));
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

    public void hasShownSniperIsBidding(int lastPrice, int lastBid) {
        driver.showsSniperStatus(itemId, lastPrice, lastBid, SnipersTableModel.STATUS_BIDDING);
    }
    
    void hasShownSniperIsWinning() {
        driver.showsSniperStatus(SnipersTableModel.STATUS_WINNING);
    }

    public void hasShownSniperIsWinning(int winningBid) {
        driver.showsSniperStatus(itemId, winningBid, winningBid, SnipersTableModel.STATUS_WINNING);
    }

    public void showsSniperHasWonAuction(int lastPrice) {
        driver.showsSniperStatus(itemId, lastPrice, lastPrice, SnipersTableModel.STATUS_WON);
    }
}
