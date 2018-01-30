package com.shop.atominac.shopend;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

class ProcessOrderAdapter extends RecyclerView.Adapter<ProcessOrderAdapter.ViewHolder>{
    private List<ProcessOrderModel> activityList;
    private Context mContext;
    private ProcessOrderModel activityListItems;

    ProcessOrderAdapter(List<ProcessOrderModel> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView list , name , address , payment,amount,time ,status;
        Button button;

        ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            list = (TextView) view.findViewById(R.id.list);
            address = (TextView) view.findViewById(R.id.address);
            amount = (TextView) view.findViewById(R.id.amount);
            payment = (TextView) view.findViewById(R.id.payment);
            time = (TextView) view.findViewById(R.id.time);
            button=(Button) view.findViewById((R.id.statusbutton));
            status = (TextView) view.findViewById(R.id.status);        }
    }

    @Override
    public ProcessOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_model, parent, false);
        return new ProcessOrderAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProcessOrderAdapter.ViewHolder holder, int position) {
        activityListItems = activityList.get(position);
        holder.amount.setText(activityListItems.getAmount());
        holder.list.setText(activityListItems.getList());
        holder.name.setText(activityListItems.getName());
        holder.address.setText(activityListItems.getAddress());
        holder.payment.setText(activityListItems.getPayment());
        holder.time.setText(activityListItems.getTime());
        holder.status.setText(activityListItems.getStatus());
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                    Toast.makeText(mContext,categoryName.getText().toString(),Toast.LENGTH_SHORT).show();
                Delivery_Boy fragment = new Delivery_Boy();
                Bundle bundle=new Bundle();
                bundle.putString("id",activityListItems.getId());
                fragment.setArguments(bundle);
                ((main) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
