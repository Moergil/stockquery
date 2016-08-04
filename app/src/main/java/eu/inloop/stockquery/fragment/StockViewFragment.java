package eu.inloop.stockquery.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.inloop.stockquery.activity.StockViewActivity;
import eu.inloop.stockquery.data.Stock;
import eu.inloop.stockquery.provider.DummyStockRetriever;
import eu.inloop.stockquery.provider.NasdaqHttpStockRetriever;
import eu.inloop.stockquery.provider.StockRetriever;
import inloop.eu.stockquery.R;

public class StockViewFragment extends Fragment {

    public static final String STATE_STOCKS = "stateStocks";

    @BindView(R.id.stocks_list)
    ListView mStocksListView;

    private final List<String> requestingSymbols = new ArrayList<>();
    private final ArrayList<Stock> stocks = new ArrayList<>();

    private StockAdapter stocksAdapter;

    private StockQuerier stockQuerier;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            List<Stock> savedStocks = (List<Stock>) savedInstanceState.getSerializable(STATE_STOCKS);
            stocks.addAll(savedStocks);
        }

        Activity activity = getActivity();
        Intent intent = activity.getIntent();

        List<String> extraRequestingSymbols = intent.getStringArrayListExtra(StockViewActivity.EXTRA_REQUESTING_SYMBOLS);
        this.requestingSymbols.clear();
        this.requestingSymbols.addAll(extraRequestingSymbols);

        if (savedInstanceState == null) {
            for (String symbol : requestingSymbols) {
                Stock stock = new Stock(symbol);
                stocks.add(stock);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stock_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        stocksAdapter = new StockAdapter();
        mStocksListView.setAdapter(stocksAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_STOCKS, stocks);
    }

    @Override
    public void onStart() {
        super.onStart();

        stockQuerier = new StockQuerier();

        String[] requestingSymbolsArray = new String[requestingSymbols.size()];
        requestingSymbols.toArray(requestingSymbolsArray);

        stockQuerier.execute(requestingSymbolsArray);
    }

    @Override
    public void onStop() {
        super.onStop();

        if (stockQuerier != null) {
            stockQuerier.cancel(true);
        }
    }

    private void setupNewData(List<Stock> stocks) {
        this.stocks.clear();
        this.stocks.addAll(stocks);
        stocksAdapter.notifyDataSetChanged();
    }

    private class StockQuerier extends AsyncTask<String, Void, List<Stock>> {

        @Override
        protected List<Stock> doInBackground(String... params) {
            StockRetriever retriever = new NasdaqHttpStockRetriever();
            List<Stock> stocks = new ArrayList<>();

            for (String symbol : params) {
                try {
                    int currentValue = retriever.retrieveStockValue(symbol);
                    Stock stock = new Stock(symbol, currentValue);
                    stocks.add(stock);
                } catch (IOException e) {
                    Stock stock = new Stock(symbol);
                    stocks.add(stock);
                }
            }

            return stocks;
        }

        @Override
        protected void onPostExecute(List<Stock> stocks) {
            setupNewData(stocks);
        }

    }

    private class StockAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stocks.size();
        }

        @Override
        public Stock getItem(int position) {
            return stocks.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                Context context = getContext();
                LayoutInflater layoutInflater = LayoutInflater.from(context);
                convertView = layoutInflater.inflate(R.layout.item_stock, null);

                StockViewHolder viewHolder = new StockViewHolder();
                viewHolder.stockSymbolTextView = ButterKnife.findById(convertView, R.id.stock_symbol);
                viewHolder.stockValueTextView = ButterKnife.findById(convertView, R.id.stock_value);
                convertView.setTag(viewHolder);
            }

            StockViewHolder viewHolder = (StockViewHolder)convertView.getTag();

            Stock stock = stocks.get(position);
            viewHolder.stockSymbolTextView.setText(stock.getSymbol());
            viewHolder.stockValueTextView.setText(stock.getFormattedValue());

            return convertView;
        }

    }

    private static class StockViewHolder {
        TextView stockSymbolTextView;
        TextView stockValueTextView;
    }

}
