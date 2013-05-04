package actionsniper;

import java.util.EventListener;

/*
 * 第13章 スナイパーが入札するで導入されたinterface
 */
public interface SniperListener extends EventListener {
    void sniperLost();

    // コンパイルを通すために追加。
    public void sniperBidding();

    public void sniperWinning();
}
