package auctionsniper;

import auctionsniper.util.Announcer;
import java.util.ArrayList;
import java.util.List;

public class SniperPortfolio implements SniperCollector, PortfolioListener {

    private Announcer<PortfolioListener> listeners = Announcer.to(PortfolioListener.class);
    private final List<AuctionSniper> snipers = new ArrayList<>();

    @Override
    public void sniperAdded(AuctionSniper sniper) {
    }

    public void addPortfolioListener(PortfolioListener listener) {
        listeners.addListener(listener);
    }

    @Override
    public void addSniper(AuctionSniper sniper) {
        snipers.add(sniper);
        listeners.announce().sniperAdded(sniper);
    }
}
