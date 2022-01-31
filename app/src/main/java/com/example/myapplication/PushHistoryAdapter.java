package com.example.myapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PushHistoryAdapter extends RecyclerView.Adapter<PushHistoryAdapter.CustomViewHolder> {

    private ArrayList<PushHistory> arrayList;
    private Context context;
    public PushHistoryAdapter(ArrayList<PushHistory> arrayList) {
        this.arrayList = arrayList;
    }

    public PushHistoryAdapter(ArrayList<PushHistory> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PushHistoryAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.push_history_list, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PushHistoryAdapter.CustomViewHolder holder, int position) {
        //각 아이템들 실제 매칭
//        Glide.with(holder.itemView)
//                .load(arrayList.get(position).getIv_icon())
//                .into(holder.iv_icon);
//        holder.tv_push_type.setText(arrayList.get(position).getTv_push_type());
//        holder.tv_send_msg.setText(arrayList.get(position).getTv_send_msg());
//        holder.tv_send_time.setText(arrayList.get(position).getTv_send_time());

        //holder.iv_icon.setImageResource(arrayList.get(position).getIv_icon()); //
        holder.tv_push_type.setText(arrayList.get(position).getTv_push_type());
        holder.tv_send_msg.setText(arrayList.get(position).getTv_send_msg());
        holder.tv_send_time.setText(arrayList.get(position).getTv_send_time());
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        protected ImageView iv_icon;
        protected TextView tv_send_time;
        protected TextView tv_send_msg;
        protected TextView tv_push_type;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_icon = (ImageView) itemView.findViewById(R.id.push_history_icon);
            this.tv_send_time = (TextView) itemView.findViewById(R.id.push_history_send_time);
            this.tv_send_msg = (TextView) itemView.findViewById(R.id.push_history_send_msg);
            this.tv_push_type = (TextView) itemView.findViewById(R.id.push_history_push_type);
        }
    }
}
