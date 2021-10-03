package com.example.stockwatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainActivity";
    private HashMap<String, String> nameList;

    private ArrayList<Stock> arrayList= new ArrayList<>();
    private StockAdaptor stockAdaptor;
    private RecyclerView recyclerView;
    private DatabaseHandler databaseHandler;
    private int pos1;
    private SwipeRefreshLayout swiper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameList= new HashMap<>();
        swiper=findViewById(R.id.swiper);
        recyclerView=findViewById(R.id.recycler);

        NameDownloader nameDownloader = new NameDownloader(this);
        new Thread(nameDownloader).start();

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doRefresh();
            }
        });

        databaseHandler=new DatabaseHandler(this);
        stockAdaptor=new StockAdaptor(arrayList,this);

        recyclerView.setAdapter(stockAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void doRefresh() {
        if(doNetCheck()){
            ArrayList<Stock> t;
            t = databaseHandler.loadStocks();
            arrayList.clear();
            getInformation(t);
            Toast.makeText(this, "Page Reloaded", Toast.LENGTH_SHORT).show();
            swiper.setRefreshing(false);
        }
    }

    private boolean doNetCheck() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
            Toast.makeText(this, "Cannot access ConnectivityManager", Toast.LENGTH_SHORT).show();
            return false;
        }

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            noNetworkDailog();
            swiper.setRefreshing(false);
        }
        return false;
    }

    private void noNetworkDailog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_error);
        builder.setTitle("No Network Connection");
        builder.setMessage("Stocks Cannot Be Added Without A Network Connection");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onResume() {
        if(doNetCheck()) {
            ArrayList<Stock> t;
            t = databaseHandler.loadStocks();
            arrayList.clear();
            getInformation(t);
        }
        else{
            ArrayList<Stock> t;
            t = databaseHandler.loadStocks();
            arrayList.clear();
            arrayList.addAll(t);
            stockAdaptor.notifyDataSetChanged();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        databaseHandler.shutDown();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ((item.getItemId() == R.id.addItem)&&(doNetCheck())) {
            Toast.makeText(this, " Add Stock", Toast.LENGTH_SHORT).show();
            executeDialog();
        }
        return super.onOptionsItemSelected(item);
    }

    private void executeDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Create an edittext and set it to be the builder's view
        final EditText et = new EditText(this);
        et.setInputType(InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
        et.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        et.setGravity(Gravity.CENTER_HORIZONTAL);
        builder.setIcon(R.drawable.ic_search);
        builder.setView(et);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String toSearch=et.getText().toString().toUpperCase().trim();
                if(toSearch.trim().equals("")){
                    executeDialog();
                    Toast.makeText(MainActivity.this,"Null String",Toast.LENGTH_SHORT).show();
                }
                else {
                    searchStock(toSearch);
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setMessage("Please enter a Stock Symbol:");
        builder.setTitle("Stock Selection");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void searchStock(String res){
        ArrayList<Stock> tempList = new ArrayList<>();

        for(String key : nameList.keySet())
        {
            if(key.startsWith(res) || Objects.requireNonNull(nameList.get(key)).contains(res))
            {
                Stock temp = new Stock(key,nameList.get(key));
                tempList.add(temp);
            }
        }
        Log.d(TAG, "updateData: bp: "+tempList.size() +" Stocks found");

        if(tempList.size()== 1){
            if(notADuplicate(tempList.get(0).getSymbol())) {
                databaseHandler.addStock(tempList.get(0));
                getInformation(tempList);
                Toast.makeText(MainActivity.this, tempList.get(0).getSymbol(), Toast.LENGTH_SHORT).show();
            }
        }
        else if(tempList.size() >1){
                multipleStock(tempList);
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Symbol Not Found");
            builder.setMessage("Data for Stock Symbol "+res+" not found");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    private void multipleStock(final ArrayList<Stock> tempList) {
        final CharSequence[] sArray = new CharSequence[tempList.size()];
        for (int i = 0; i < tempList.size(); i++)
            sArray[i] = tempList.get(i).getSymbol() + " | " + tempList.get(i).getCompanyName().toLowerCase();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Make a Selection");

        builder.setItems(sArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which)
            {
                Stock temp  = tempList.get(which);
                ArrayList<Stock>t=new ArrayList<>();
                t.add(temp);
                if(notADuplicate(temp.getSymbol())) {
                    databaseHandler.addStock(temp);
                    getInformation(t);
                }
            }
        });

        builder.setNegativeButton("NEVERMIND", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(MainActivity.this, "You changed your mind!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void getInformation(ArrayList<Stock> t) {
        //Log.d(TAG, "getInformation: "+t.get(0).getSymbol()+t.get(0).getChangePercentage()+t.get(0).getCompanyName());
        StockDownloader stockDownloader=new StockDownloader(this,t);
        new Thread(stockDownloader).start();

    }

    private boolean notADuplicate(String symbol) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getSymbol().equals(symbol)){
                duplicateDailog(symbol);
                return false;
            }
        }
        return true;
    }

    private void duplicateDailog(String symbol){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Duplicate Stock");
        builder.setMessage("Stock Symbol "+symbol+" is already displayed");
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);
        String url = "https://www.marketwatch.com/investing/stock/" + arrayList.get(pos).getSymbol();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);

    }

    @Override
    public boolean onLongClick(View view) {
        pos1 = recyclerView.getChildLayoutPosition(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                databaseHandler.deleteStock(arrayList.get(pos1));
                arrayList.remove(pos1);
                stockAdaptor.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        Stock m1 = arrayList.get(pos1);
        builder.setMessage("Delete Stock Symbol '" +m1.getSymbol()+"'?");
        builder.setTitle("Delete Stock");
        builder.setIcon(R.drawable.ic_delete);
        AlertDialog dialog = builder.create();
        dialog.show();

        return false;
    }

    public void downloadFailed() {
    }

    public void updateData(HashMap<String, String> h) {
        nameList.putAll(h);
        Log.d(TAG, "updateData: "+nameList);
    }

    public void updateAgainData(ArrayList<Stock> st) {
        //Log.d(TAG, "getInformation: bp: "+st.get(0).getSymbol()+" | "+st.get(0).getChangePercentage()+" | "+st.get(0).getCompanyName() + " | " + st.size());
        sortit(st);
        if(st.size()>1)
            arrayList.clear();
        arrayList.addAll(st);
        stockAdaptor.notifyDataSetChanged();
        //Log.d(TAG, "getInformation: "+arrayList.get(0).getSymbol()+arrayList.get(0).getChangePercentage()+arrayList.get(0).getCompanyName());
    }

    private void sortit(ArrayList<Stock> st) {
        Collections.sort(st, new Comparator<Stock>() {
            @Override
            public int compare(Stock s1, Stock s2) {
                return s1.getSymbol().compareTo(s2.getSymbol());
            }
        });
    }

}