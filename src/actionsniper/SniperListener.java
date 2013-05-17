package actionsniper;

import java.util.EventListener;

/*
 * 第13章 スナイパーが入札するで導入されたinterface
 */
public interface SniperListener extends EventListener {
    void sniperLost();

    public void sniperBidding(SniperState sniper_state);

    public void sniperWinning();

    public void sniperWon();
}
