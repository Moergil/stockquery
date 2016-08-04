package eu.inloop.stockquery.provider;

import java.io.IOException;

public class DummyStockRetriever implements StockRetriever {

    @Override
    public int retrieveStockValue(String symbol) throws IOException {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // ignore, not important
        }

        switch (symbol) {
            case "INTL":
                return 2000;
            case "IOOP":
                return 10000;
            default:
                return -1;
        }
    }
}
