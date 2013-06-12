package test.integration.auctionsniper.ui;

import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import static org.hamcrest.Matchers.equalTo;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import auctionsniper.ui.MainWindow;
import org.junit.Test;
import test.endtoend.auctionsniper.AuctionSniperDriver;

public class MainWindowTest {

    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);
    
    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<String> buttonProbe =
                new ValueMatcherProbe<>(equalTo("an item-id"), "join request");
        
        mainWindow.addUserRequestListener(
                new UserRequestListener() {
            public void joinAuction(String itemId) {
                buttonProbe.setReceivedValue(itemId);
            }
        });
        
        driver.startBiddingFor("an item-id");
        driver.check(buttonProbe);
    }
}