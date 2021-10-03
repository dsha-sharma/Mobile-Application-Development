package com.example.stockwatch;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StockAdaptor extends RecyclerView.Adapter<StockViewHolder> {
    private List<Stock> stockList;
    private MainActivity mainAct;

    public StockAdaptor(List<Stock> stockList, MainActivity mainAct) {
        this.stockList = stockList;
        this.mainAct = mainAct;
    }

    @NonNull
    @Override
    public StockViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stock_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return new StockViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockViewHolder holder, int position) {
        Stock s=stockList.get(position);

        if(s.getChange()>0.0){
            holder.imageView.setBackgroundResource(R.drawable.ic_up);
            holder.symbol.setTextColor(Color.parseColor("#0f9d58"));
            holder.companyName.setTextColor(Color.parseColor("#0f9d58"));
            holder.latestPrice.setTextColor(Color.parseColor("#0f9d58"));
            holder.change.setTextColor(Color.parseColor("#0f9d58"));
            holder.changePercentage.setTextColor(Color.parseColor("#0f9d58"));
        }
        else if(s.getChange()<0.0)
        {
            holder.imageView.setBackgroundResource(R.drawable.ic_down);
            holder.symbol.setTextColor(Color.parseColor("#db4437"));
            holder.companyName.setTextColor(Color.parseColor("#db4437"));
            holder.latestPrice.setTextColor(Color.parseColor("#db4437"));
            holder.change.setTextColor(Color.parseColor("#db4437"));
            holder.changePercentage.setTextColor(Color.parseColor("#db4437"));
        }
        else{
            holder.imageView.setBackgroundResource(R.color.colorPrimary);
            holder.symbol.setTextColor(Color.parseColor("#FFFFFF"));
            holder.companyName.setTextColor(Color.parseColor("#FFFFFF"));
            holder.latestPrice.setTextColor(Color.parseColor("#FFFFFF"));
            holder.change.setTextColor(Color.parseColor("#FFFFFF"));
            holder.changePercentage.setTextColor(Color.parseColor("#FFFFFF"));
        }
        holder.symbol.setText(s.getSymbol());
        holder.companyName.setText(s.getCompanyName());
        holder.latestPrice.setText(String.valueOf(s.getLatestPrice()));
        holder.change.setText(String.valueOf(s.getChange()));
        holder.changePercentage.setText(String.format("(%s%%)", s.getChangePercentage()));
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }
}
