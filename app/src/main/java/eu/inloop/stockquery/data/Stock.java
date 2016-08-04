package eu.inloop.stockquery.data;

import java.io.Serializable;

public class Stock implements Serializable {

    private final String symbol;
    private final int value;

    public Stock(String symbol, int value) {
        this.symbol = symbol;
        this.value = value;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getFormattedValue() {
        int decimal = value / 100;
        int fractional = value % 100;
        return decimal + "." + fractional;
    }

}
