package com.example.recyclerviewtest;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.concurrent.TimeoutException;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {
    public item[] I;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_time;
        public TextView tv_others;
        public TextView tv_bmi;
        public TextView tv_status;
        public ViewHolder(View view) {
            super(view);
            tv_time = (TextView)view.findViewById(R.id.time);
            tv_others = (TextView)view.findViewById(R.id.others);
            tv_bmi = (TextView)view.findViewById(R.id.bmi);
            tv_status = (TextView)view.findViewById(R.id.status);
        }
        public TextView getTextView(int i){
            switch (i){
                case 1: return tv_time;
                case 2: return tv_bmi;
                case 3: return tv_others;
                case 4:return tv_status;
                default:break;
            }
            TextView tv = (TextView) itemView.findViewById(R.id.bmi);
            tv.setText("error");
            return tv;
        }
    }
    public recyclerViewAdapter(item[] it){
        I = it;
    }
    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public recyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull recyclerViewAdapter.ViewHolder holder, int position) {
        holder.getTextView(1).setText(I[position].time);
        holder.getTextView(2).setText("BMI:" + String.format("%.2f", I[position].bmi));
        holder.getTextView(3).setText(" 身高:" + String.valueOf(I[position].height) + "m 体重:" + String.valueOf(I[position].weight) + "kg");
        double bmiHere = I[position].bmi;
        String status = new String();

        if(bmiHere <= 18.4) {
            status = "偏瘦";
            holder.getTextView(4).setText(status);
            holder.getTextView(4).setBackgroundColor(Color.parseColor("#48D1CC"));
        }
        else if(bmiHere <= 23.9) {
            status = "适中";
            holder.getTextView(4).setText(status);
            holder.getTextView(4).setBackgroundColor(Color.parseColor("#90EE90"));
        }
        else if(bmiHere <= 27.9) {
            status = "过重";
            holder.getTextView(4).setText(status);
            holder.getTextView(4).setBackgroundColor(Color.parseColor("#FFA500"));
        }
        else {
            status = "肥胖";
            holder.getTextView(4).setText(status);
            holder.getTextView(4).setBackgroundColor(Color.parseColor("#FF4500"));
        }
    }

    @Override
    public int getItemCount() {
        int i = 0;
        for( ; i <= I.length; i++)
        {
            if(I[i] == null)
                break;
        }
        return i;
    }

    public void newItem(item itemNew){
        int i = getItemCount();
        for( ; i > 0 ; i--){
            I[i+1] = I[i];
        }
        I[0] = itemNew;
        notifyItemInserted(0);
    }

}
