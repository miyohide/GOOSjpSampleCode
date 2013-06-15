package test.auctionsniper;

import static org.hamcrest.Matchers.equalTo;
import auctionsniper.Auction;
import auctionsniper.AuctionEventListener.PriceSource;
import auctionsniper.AuctionSniper;
import auctionsniper.Item;
import auctionsniper.SniperListener;
import auctionsniper.SniperSnapshot;
import auctionsniper.SniperState;
import org.hamcrest.FeatureMatcher;
import org.hamcrest.Matcher;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.States;
import org.jmock.integration.junit4.JMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class AuctionSniperTest {
    public final String ITEM_ID = "item_id";
    private final Mockery context = new Mockery();
    private final SniperListener sniperListener = context.mock(SniperListener.class);
    private final Auction auction = context.mock(Auction.class);
    private final Item item = new Item(ITEM_ID, 1234);
    private final AuctionSniper sniper = new AuctionSniper(item, auction);
    private final States sniperState = context.states("sniper");

    @Before public void attachSniperListenerToSniper() {
        sniper.addSniperListener(sniperListener);
    }

    @Test public void reportsLostIfAuctionClosesImmediately() {
        context.checking(new Expectations() {{
            atLeast(1).of(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.LOST)));
        }});
        sniper.auctionClosed();
    }
    
    @Test public void reportsLostIfAuctionClosesWhenBidding() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.BIDDING))); then(sniperState.is("bidding"));
            atLeast(1).of(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.LOST))); when(sniperState.is("bidding"));
        }});

        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
        sniper.auctionClosed();
    }

    @Test public void reportsWonIfAuctionClosesWhenWinning() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.BIDDING))); then(sniperState.is("bidding"));
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.WINNING))); then (sniperState.is("winning"));
            atLeast(1).of(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.WON))); when(sniperState.is("winning"));
        }});

        sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
        sniper.currentPrice(135, 45, PriceSource.FromSniper);
        sniper.auctionClosed();
    }

    @Test public void bidsHigherAndReportsBiddingWhenNewPriceArrives() {
        final int price = 1001;
        final int increment = 25;
        final int bid = price + increment;
        context.checking(new Expectations() {{
            one(auction).bid(bid);
            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, price, bid, SniperState.BIDDING));
        }});
        
        sniper.currentPrice(price, increment, PriceSource.FromOtherBidder);
    }
    
    @Test public void reportsIsWinningWhenCurrentPriceComesFromSniper() {
        context.checking(new Expectations() {{
            ignoring(auction);
            allowing(sniperListener).sniperStateChanged(
                    with(aSniperThatIs(SniperState.BIDDING))); then(sniperState.is("bidding"));

            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 135, 135, SniperState.WINNING));
                      when(sniperState.is("bidding"));
        }});
        sniper.currentPrice(123, 12, PriceSource.FromOtherBidder);
        sniper.currentPrice(135, 45, PriceSource.FromSniper);
    }

    // P219 制限を超えた入札をアナウンスする
    @Test public void doesNotBidAndReportsLosingIfSubsequentPriceIsAboveStopPrice() {
        allowingSniperBidding();
        context.checking(new Expectations() {{
            int bid = 123 + 45;
            allowing(auction).bid(bid);
            atLeast(1).of(sniperListener).sniperStateChanged(
                    new SniperSnapshot(ITEM_ID, 2345, bid, SniperState.LOSING));
                        when(sniperState.is("bidding"));
        }});
        sniper.currentPrice(123, 45, PriceSource.FromOtherBidder);
        sniper.currentPrice(2345, 25, PriceSource.FromOtherBidder);
    }

    private Matcher<SniperSnapshot> aSniperThatIs(final SniperState state) {
        return new FeatureMatcher<SniperSnapshot, SniperState>(
                equalTo(state), "sniper that is ", "was") {
                    @Override
                    protected SniperState featureValueOf(SniperSnapshot actual) {
                        return actual.state;
                    }
                };
    }

    private void allowingSniperBidding() {
        context.checking(new Expectations() {{
            allowing(sniperListener).sniperStateChanged(with(aSniperThatIs(SniperState.BIDDING)));
              then(sniperState.is("bidding"));
        }});
    }
}
