package test.integration.auctionsniper.ui;

import auctionsniper.Item;
import auctionsniper.SniperPortfolio;
import auctionsniper.UserRequestListener;
import static org.hamcrest.Matchers.equalTo;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import auctionsniper.ui.MainWindow;
import org.hamcrest.Matcher;
import org.junit.Test;
import test.endtoend.auctionsniper.AuctionSniperDriver;

public class MainWindowTest {

    private final SniperPortfolio portfolio = new SniperPortfolio();
    private final MainWindow mainWindow = new MainWindow(portfolio);
    private final AuctionSniperDriver driver = new AuctionSniperDriver(100);
    
    @Test
    public void makesUserRequestWhenJoinButtonClicked() {
        final ValueMatcherProbe<Item> itemProbe =
                new ValueMatcherProbe<>(equalTo(new Item("an item-id", 789)), "join request");
        
        mainWindow.addUserRequestListener(new UserRequestListener() {
            public void joinAuction(Item item) {
                itemProbe.setReceivedValue(item);
            }
        });
        
        driver.startBiddingFor("an item-id", 789);
        driver.check(itemProbe);
    }
}