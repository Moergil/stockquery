package eu.inloop.stockquery.provider;

import java.io.IOException;
import java.util.List;

import eu.inloop.stockquery.data.StockItem;

public interface StockRetriever {

    List<StockItem> retrieveStockValue(List<String> symbols) throws IOException;

}
