package eu.inloop.stockquery.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.inloop.stockquery.activity.StockViewActivity;
import inloop.eu.stockquery.R;

public class StockSelectFragment extends Fragment {

    private final String STATE_SYMBOLS = "stateSymbols";

    @BindView(R.id.stock_symbol_input)
    EditText stockSymbolInputField;

    @BindView(R.id.selected_stock_symbol_list)
    ListView selectedStockSymbolListView;

    private final ArrayList<String> symbols = new ArrayList<>();

    private SymbolAdapter symbolAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            List<String> savedSymbols = savedInstanceState.getStringArrayList(STATE_SYMBOLS);
            symbols.addAll(savedSymbols);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stock_select, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        symbolAdapter = new SymbolAdapter();
        selectedStockSymbolListView.setAdapter(symbolAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(STATE_SYMBOLS, symbols);
    }

    @OnClick(R.id.add_stock_symbol_button)
    void addSymbol() {
        String currentInputText = stockSymbolInputField.getText().toString();
        String symbol = currentInputText.trim().toUpperCase();

        if (symbol.isEmpty()) {
            return;
        }

        symbols.add(symbol);

        symbolAdapter.notifyDataSetChanged();

        stockSymbolInputField.setText("");
    }

    @OnClick(R.id.continue_button)
    void openStocksViewActivity() {
        Context context = getContext();
        Intent intent = new Intent(context, StockViewActivity.class);

        intent.putExtra(StockViewActivity.EXTRA_REQUESTING_SYMBOLS, symbols);

        startActivity(intent);
    }

    private class SymbolAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return symbols.size();
        }

        @Override
        public String getItem(int position) {
            return symbols.get(position);
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
                convertView = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);

                SymbolViewHolder viewHolder = new SymbolViewHolder();
                viewHolder.symbolTextView = ButterKnife.findById(convertView, android.R.id.text1);
                convertView.setTag(viewHolder);
            }

            SymbolViewHolder viewHolder = (SymbolViewHolder)convertView.getTag();

            String symbol = symbols.get(position);
            viewHolder.symbolTextView.setText(symbol);

            return convertView;
        }

    }

    private static class SymbolViewHolder {
        TextView symbolTextView;
    }

}
