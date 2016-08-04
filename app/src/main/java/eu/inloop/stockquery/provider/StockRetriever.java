package eu.inloop.stockquery.provider;

import java.io.IOException;

public interface StockRetriever {

    int retrieveStockValue(String symbol) throws IOException;

}
