package com.example.stockwatch;

import android.net.Uri;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class StockDownloader implements Runnable {

    private static final String TAG = "StockDownloader";
    private static final String urlKey = "pk_5a854e3744f3428aaa9c78c79aef9578";
    private ArrayList<Stock> stockList;
    private MainActivity mainActivity;

    public StockDownloader(MainActivity mainActivity, ArrayList<Stock> stockList) {
        this.stockList = stockList;
        this.mainActivity = mainActivity;
    }

    @Override
    public void run() {
        ArrayList<Stock> tempList = new ArrayList<>();
        for(Stock temp: stockList) {
            String stockdataURL = "https://cloud.iexapis.com/stable/stock/" + temp.getSymbol() + "/quote?token=" + urlKey;
            String data=getStockDataFromURL(stockdataURL);
            final Stock st=parseJSON(data);
            tempList.add(st);

        }
        sendData(tempList);

    }

    private void sendData(final ArrayList<Stock> t) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainActivity.updateAgainData(t);
            }
        });
    }

    private String getStockDataFromURL(String stockdataURL) {

        Uri dataUri = Uri.parse(stockdataURL);
        String urlToUse = dataUri.toString();

        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.d(TAG, "run: HTTP ResponseCode NOT OK: " + conn.getResponseCode());
                return (null);
            }

            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "run:data" + sb.toString());

        } catch (Exception e) {
            Log.e(TAG, "run: ", e);
            return(null);
        }
        Log.d(TAG, "run: "+sb.toString());
        return(sb.toString());

    }


    private Stock parseJSON(String s) {

        Stock st=new Stock();

            String temp_symbol,temp_companyName,temp_latestPrice,temp_changePercent,temp_change;
            String symbol="";
            String companyName="";
            double latestPrice = 0.00;
            double changePercent=0.00;
            double change = 0.00;

            Log.d(TAG, "handleResults:what : "+ symbol+" "+companyName+" "+latestPrice+" "+change+" "+changePercent);

            try {
                JSONObject jCountry = new JSONObject(s);
                temp_symbol = jCountry.getString("symbol");
                symbol = temp_symbol;
            }catch (Exception e) {
                Log.d(TAG, "parseJSON: " + e.getMessage());
                e.printStackTrace();
            }

            Log.d(TAG, "handleResults:what : "+ symbol+" "+companyName+" "+latestPrice+" "+change+" "+changePercent);

            try{
                JSONObject jCountry = new JSONObject(s);
                temp_companyName = jCountry.getString("companyName");
                companyName=temp_companyName;
            }catch (Exception e) {
                Log.d(TAG, "parseJSON: " + e.getMessage());
                e.printStackTrace();
            }

            Log.d(TAG, "handleResults:what : "+ symbol+" "+companyName+" "+latestPrice+" "+change+" "+changePercent);

            try{
                JSONObject jCountry = new JSONObject(s);
                temp_latestPrice = jCountry.getString("latestPrice");
                latestPrice = Math.round(Double.parseDouble(temp_latestPrice)*100.00)/100.00;
            }catch (Exception e) {
                Log.d(TAG, "parseJSON: " + e.getMessage());
                e.printStackTrace();
            }

            Log.d(TAG, "handleResults:what : "+ symbol+" "+companyName+" "+latestPrice+" "+change+" "+changePercent);

            try{
                JSONObject jCountry = new JSONObject(s);
                temp_changePercent = jCountry.getString("changePercent");
                changePercent = Math.round(Double.parseDouble(temp_changePercent)*100.00)/100.00;
            }catch (Exception e) {
                Log.d(TAG, "parseJSON: " + e.getMessage());
                e.printStackTrace();
            }

            Log.d(TAG, "handleResults:what : "+ symbol+" "+companyName+" "+latestPrice+" "+change+" "+changePercent);

            try{
                JSONObject jCountry = new JSONObject(s);
                temp_change = jCountry.getString("change");
                change = Math.round(Double.parseDouble(temp_change)*100.00)/100.00;
            }catch (Exception e) {
                Log.d(TAG, "parseJSON: " + e.getMessage());
                e.printStackTrace();
            }

            Log.d(TAG, "handleResults:what : "+ symbol+" "+companyName+" "+latestPrice+" "+change+" "+changePercent);

            st.setSymbol(symbol);
            st.setCompanyName(companyName);
            st.setLatestPrice(latestPrice);
            st.setChangePercentage(changePercent);
            st.setChange(change);

        return st;


    }





}