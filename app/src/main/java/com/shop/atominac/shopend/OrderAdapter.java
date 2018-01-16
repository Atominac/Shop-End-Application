package com.shop.atominac.shopend;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder>{
    private List<OrderModel> activityList;
    private Context mContext;

    OrderAdapter(List<UpdateModel> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView list , name , address , payment,amount,time ,status;

        ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            list = (TextView) view.findViewById(R.id.list);
            address = (TextView) view.findViewById(R.id.address);
            amount = (TextView) view.findViewById(R.id.amount);
            payment = (TextView) view.findViewById(R.id.payment);
            time = (TextView) view.findViewById(R.id.time);

            status = (TextView) view.findViewById(R.id.status);        }
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_model, parent, false);
        return new OrderAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        OrderModel activityListItems = activityList.get(position);
        holder.amount.setText(activityListItems.getAmount());
        holder.list.setText(activityListItems.getList());
        holder.name.setText(activityListItems.getName());
        holder.address.setText(activityListItems.getAddress());
        holder.payment.setText(activityListItems.getPayment());
        holder.time.setText(activityListItems.getTime());
        holder.status.setText(activityListItems.getStatus());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
