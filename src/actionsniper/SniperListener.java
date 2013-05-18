package actionsniper;

import java.util.EventListener;

/*
 * 第13章 スナイパーが入札するで導入されたinterface
 */
public interface SniperListener extends EventListener {
    void sniperLost();

    public void sniperBidding(SniperSnapshot sniperSnapshot);

    public void sniperWinning();

    public void sniperWon();
}
