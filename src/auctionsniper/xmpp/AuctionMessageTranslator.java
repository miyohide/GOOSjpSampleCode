package auctionsniper.xmpp;

import auctionsniper.AuctionEventListener;
import auctionsniper.AuctionEventListener.PriceSource;
import java.util.HashMap;
import java.util.Map;
import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.packet.Message;

public class AuctionMessageTranslator implements MessageListener {

    private AuctionEventListener listener;
    private final String sniperId;
    private XMPPFailureReporter failureReporter;

    public AuctionMessageTranslator(String sniperId, AuctionEventListener listener, XMPPFailureReporter failureReporter) {
        this.sniperId = sniperId;
        this.listener = listener;
        this.failureReporter = failureReporter;
    }

    public void processMessage(Chat chat, Message message) {
        String messageBody = message.getBody();
        try {
            translate(message.getBody());
        } catch (Exception parseException) {
            failureReporter.cannotTranslateMessage(sniperId, messageBody, parseException);
            listener.auctionFailed();
        }
    }

    private void translate(String messageBody) throws MissingValueException {
        AuctionEvent event = AuctionEvent.from(messageBody);
        String eventType = event.type();

        if ("CLOSE".equals(eventType)) {
            listener.auctionClosed();
        } else if ("PRICE".equals(eventType)) {
            listener.currentPrice(Integer.parseInt(event.get("CurrentPrice")),
                    Integer.parseInt(event.get("Increment")), event.isFrom(sniperId));
        }
    }

    public static class AuctionEvent {

        private final Map<String, String> fields = new HashMap<String, String>();

        public String type() throws MissingValueException {
            return get("Event");
        }

        public int currentPrice() throws MissingValueException {
            return getInt("CurrentPrice");
        }

        public int increment() throws MissingValueException {
            return getInt("Increment");
        }

        public PriceSource isFrom(String sniperId) throws MissingValueException {
            return sniperId.equals(bidder()) ? PriceSource.FromSniper : PriceSource.FromOtherBidder;
        }

        private int getInt(String fieldName) throws MissingValueException {
            return Integer.parseInt(get(fieldName));
        }

        private String get(String fieldName) throws MissingValueException {
            String value = fields.get(fieldName);
            if (null == value) {
                throw new MissingValueException(fieldName);
            }
            return value;
        }

        private void addField(String field) {
            String[] pair = field.split(":");
            fields.put(pair[0].trim(), pair[1].trim());
        }

        private String bidder() throws MissingValueException {
            return get("Bidder");
        }

        static AuctionEvent from(String messageBody) {
            AuctionEvent event = new AuctionEvent();
            for (String field : fieldsIn(messageBody)) {
                event.addField(field);
            }
            return event;
        }

        static String[] fieldsIn(String messageBody) {
            return messageBody.split(";");
        }
    }

    private static class MissingValueException extends Exception {

        public MissingValueException(String fieldName) {
            super("Missing value for " + fieldName);
        }
    }
}
