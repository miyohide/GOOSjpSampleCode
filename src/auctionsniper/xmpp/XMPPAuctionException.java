package auctionsniper.xmpp;

// 非チェック例外とするためにRuntimeExceptionを継承している
class XMPPAuctionException extends RuntimeException {
    public XMPPAuctionException(String message, Exception cause) {
        super(message, cause);
    }
}
