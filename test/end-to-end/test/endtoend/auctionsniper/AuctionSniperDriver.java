package test.endtoend.auctionsniper;

import static org.hamcrest.Matchers.equalTo;
import static com.objogate.wl.swing.matcher.JLabelTextMatcher.withLabelText;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.JFrameDriver;
import com.objogate.wl.swing.driver.JLabelDriver;
import com.objogate.wl.swing.driver.JTableDriver;
import com.objogate.wl.swing.gesture.GesturePerformer;
import jp.goos.sample.ui.MainWindow;

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
}
