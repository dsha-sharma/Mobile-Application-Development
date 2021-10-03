package com.example.stockwatch;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StockViewHolder extends RecyclerView.ViewHolder {

    TextView symbol;
    TextView companyName;
    TextView latestPrice;
    TextView change;
    TextView changePercentage;
    ImageView imageView;

    public StockViewHolder(@NonNull View itemView) {
        super(itemView);
        symbol=itemView.findViewById(R.id.symbol);
        companyName=itemView.findViewById(R.id.cName);
        latestPrice=itemView.findViewById(R.id.latestPrice);
        change=itemView.findViewById(R.id.changes);
        changePercentage=itemView.findViewById(R.id.pChange);
        imageView=itemView.findViewById(R.id.imageView);

    }
}
