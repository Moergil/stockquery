package eu.inloop.stockquery.data;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockItem implements Serializable {

    private final String symbol;
    private final BigDecimal value;

    public StockItem(String symbol) {
        this.symbol = symbol;
        this.value = null;
    }

    public StockItem(String symbol, BigDecimal value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getFormattedValue() {
        if (value == null) {
            return "N/A";
        } else {
            return value.toPlainString();
        }
    }

}
