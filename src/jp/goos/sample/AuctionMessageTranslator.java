// TODO: Packageは後ほど変更する
package jp.goos.sample;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class AuctionMessageTranslator implements MessageListener {
    private AuctionEventListener listener;

    public AuctionMessageTranslator(AuctionEventListener listener) {
        this.listener = listener;
    }
    public void processMessage(Chat chat, Message message) {
        this.listener.auctionClosed();
    }
}
