package com.shop.atominac.shopend;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class Autobuy_adapter extends RecyclerView.Adapter<Autobuy_adapter.ViewHolder>{
    private List<Autobuy_model> activityList;
    private Context mContext;

    Autobuy_adapter(List<Autobuy_model> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name , status , location,Recievedtoken;

        ViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.name);
            status = (TextView) view.findViewById(R.id.status);
            location = (TextView) view.findViewById(R.id.location);
            Recievedtoken = (TextView) view.findViewById(R.id.Recievedtoken);


                   }
    }

    @Override
    public Autobuy_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_delivery__boy, parent, false);
        return new Autobuy_adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Autobuy_adapter.ViewHolder holder, int position) {
        Autobuy_model activityListItems = activityList.get(position);
        holder.name.setText(activityListItems.getName());
        holder.status.setText(activityListItems.getStatus());
        holder.location.setText(activityListItems.getLocation());
        holder.Recievedtoken.setText(activityListItems.getToken());

    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
