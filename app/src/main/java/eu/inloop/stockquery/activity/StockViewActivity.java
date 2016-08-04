package eu.inloop.stockquery.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import inloop.eu.stockquery.R;

public class StockViewActivity extends AppCompatActivity {

    public static final String EXTRA_REQUESTING_SYMBOLS = "extraRequestingSymbols";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_view);
    }

}