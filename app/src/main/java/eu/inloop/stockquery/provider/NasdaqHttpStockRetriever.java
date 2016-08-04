package eu.inloop.stockquery.provider;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NasdaqHttpStockRetriever implements StockRetriever {

    @Override
    public int retrieveStockValue(String symbol) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType text = MediaType.parse("application/text; charset=utf-8");

        FormBody formBody = new FormBody.Builder()
                .add("stock-search-text", "APPL")
                .build();

        Request request = new Request.Builder()
                .url("http://www.nasdaq.com/symbol/")
                .post(formBody)
                .build();

        Response response = client.newCall(request).execute();
        String responseString = response.body().string();

        return 0;
    }
}
