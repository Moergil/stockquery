package eu.inloop.stockquery.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import eu.inloop.stockquery.data.StockItem;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class NasdaqHttpStockRetriever implements StockRetriever {

    private final Gson gson;
    private final GoogleFinanceService googleFinanceService;

    {
        gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.google.com/finance/")
                .build();

        googleFinanceService = retrofit.create(GoogleFinanceService.class);
    }

    @Override
    public List<StockItem> retrieveStockValue(List<String> symbols) throws IOException {
        String query = assembleQuery(symbols);

        ResponseBody body = googleFinanceService.listStocksRaw(query).execute().body();

        String responseString = extractResponseString(body);

        return parseStockItems(responseString);
    }

    private String assembleQuery(List<String> symbols) {
        StringBuilder builder = new StringBuilder("NASDAQ:");

        boolean first = true;
        for (String symbol : symbols) {
            if (!first) {
                builder.append(",");
            } else {
                first = false;
            }

            builder.append(symbol);

        }

        return builder.toString();
    }

    private String extractResponseString(ResponseBody body) throws IOException {
        byte[] bytes = body.bytes();
        String result = new String(bytes);

        result = result.replace("//", "");
        result = result.replace("\n", "");

        return result;
    }

    private List<StockItem> parseStockItems(String json) {
        List<StockItem> stockItems = new ArrayList<>();

        JsonArray array = gson.fromJson(json, JsonArray.class);

        for (int i = 0; i < array.size(); i++) {
            JsonObject stockItemObject = array.get(i).getAsJsonObject();

            String symbol = stockItemObject.get("t").getAsString();
            BigDecimal value = stockItemObject.get("l").getAsBigDecimal();

            StockItem stockItem = new StockItem(symbol, value);
            stockItems.add(stockItem);
        }

        return stockItems;
    }

    private interface GoogleFinanceService {

        @GET("info")
        Call<ResponseBody> listStocksRaw(@Query("q") String query);

    }

    private class NasdaqStockItem {
        String id;
        String t;
        String e;
        String l;
        String l_fix;
        String l_cur;
        String s;
        String ltt;
        String lt;
        String lt_dts;
        String c;
        String c_fix;
        String cp;
        String cp_fix;
        String ccol;
        String pcls_fix;
    }
}
