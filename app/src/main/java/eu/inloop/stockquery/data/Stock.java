package eu.inloop.stockquery.data;

import java.io.Serializable;

public class Stock implements Serializable {

    private final String symbol;
    private final Integer value;

    public Stock(String symbol) {
        this.symbol = symbol;
        this.value = null;
    }

    public Stock(String symbol, int value) {
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
            int decimal = value / 100;
            int fractional = value % 100;
            return decimal + "." + fractional;
        }
    }

}
