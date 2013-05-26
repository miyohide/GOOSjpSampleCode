package test.integration.auctionsniper.ui;

import actionsniper.UserRequestListener;
import static org.hamcrest.Matchers.equalTo;

import com.objogate.wl.swing.probe.ValueMatcherProbe;
import jp.goos.sample.ui.MainWindow;
import jp.goos.sample.ui.SnipersTableModel;
import org.junit.Test;
import test.endtoend.auctionsniper.AuctionSniperDriver;

public class MainWindowTest {

    private final SnipersTableModel tableModel = new SnipersTableModel();
    private final MainWindow mainWindow = new MainWindow(tableModel);
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