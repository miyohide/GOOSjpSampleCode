package test.endtoend.auctionsniper;

import static org.hamcrest.Matchers.equalTo;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;
import static com.objogate.wl.swing.matcher.IterableComponentsMatcher.matching;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JButtonDriver;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.driver.JTableHeaderDriver;
import com.objogate.wl.swing.driver.JTextFieldDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.table.JTableHeader;
import auctionsniper.ui.MainWindow;
import static com.objogate.wl.swing.driver.ComponentDriver.named;

public class AuctionSniperDriver extends JFrameDriver {

    public AuctionSniperDriver(int timeoutMillis) {
        super(new GesturePerformer(),
                JFrameDriver.topLevelFrame(
                named(MainWindow.MAIN_WINDOW_NAME),
                showingOnScreen()),
                new AWTEventQueueProber(timeoutMillis, 100));
    }

    public void showsSniperStatus(String statusText) {
        new JTableDriver(this).hasCell(withLabelText(equalTo(statusText)));
    }

    void showsSniperStatus(String itemId, int lastPrice, int lastBid, String statusText) {
        JTableDriver table = new JTableDriver(this);

        table.hasRow(
                matching(withLabelText(itemId), withLabelText(String.valueOf(lastPrice)),
                withLabelText(String.valueOf(lastBid)), withLabelText(statusText)));
    }

    void hasColumnTitles() {
        JTableHeaderDriver headers = new JTableHeaderDriver(this, JTableHeader.class);
        headers.hasHeaders(matching(withLabelText("Item"), withLabelText("Last Price"),
                withLabelText("Last Bid"), withLabelText("State")));
    }

    @SuppressWarnings("unchecked")
    public void startBiddingFor(String itemId) {
        textField(MainWindow.NEW_ITEM_ID_NAME).replaceAllText(itemId);
        bidButton().click();
    }

    public void startBiddingFor(String itemId, int stopPrice) {
        textField(MainWindow.NEW_ITEM_ID_NAME).replaceAllText(itemId);
        textField(MainWindow.NEW_ITEM_STOP_PRICE_NAME).replaceAllText(String.valueOf(stopPrice));
        bidButton().click();
    }

    private JTextFieldDriver textField(final String name) {
        JTextFieldDriver textFieldId =
                new JTextFieldDriver(this, JTextField.class, named(name));
        textFieldId.focusWithMouse();
        return textFieldId;
    }

    private JButtonDriver bidButton() {
        return new JButtonDriver(this, JButton.class, named(MainWindow.JOIN_BUTTON_NAME));
    }
}
