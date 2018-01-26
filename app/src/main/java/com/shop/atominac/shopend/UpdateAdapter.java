package com.shop.atominac.shopend;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder>{
    private List<UpdateModel> activityList;
    private Context mContext;
    private ProgressDialog pDialog;
    private static final String TAG = "PlaceOrder";
    UpdateModel activityListItem ;

    UpdateAdapter(List<UpdateModel> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView item_name , price , brand;
        Button update;

        ViewHolder(View view) {
            super(view);

            item_name = (TextView) view.findViewById(R.id.item_name);
            price = (TextView) view.findViewById(R.id.item_price);
            brand = (TextView) view.findViewById(R.id.item_brand);
            update=(Button) view.findViewById(R.id.update);

        }
    }

    @Override
    public UpdateAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_model, parent, false);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        return new UpdateAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UpdateAdapter.ViewHolder holder, int position) {
        activityListItem = activityList.get(position);
        holder.item_name.setText(activityListItem.getItem_name());
        holder.price.setText(activityListItem.getPrice());
        holder.brand.setText(activityListItem.getBrand());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                    Toast.makeText(mContext,categoryName.getText().toString(),Toast.LENGTH_SHORT).show();
                View promptsView;
                final TextView productName , productBrand ,  productPrice ;
                final EditText newPrice ;
                LayoutInflater lil = LayoutInflater.from(mContext);
                promptsView = lil.inflate(R.layout.update2, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                alertDialogBuilder.setView(promptsView);

                productName = (TextView)promptsView.findViewById(R.id.productname);
                productName.setText(activityListItem.getItem_name());
                productBrand = (TextView)promptsView.findViewById(R.id.brand_name);
                productBrand.setText(activityListItem.getBrand());
                productPrice = (TextView)promptsView.findViewById(R.id.old_price);
                productPrice.setText(activityListItem.getPrice());


                newPrice = (EditText) promptsView.findViewById(R.id.new_price);
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Proceed",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (!Objects.equals(newPrice.getText().toString(), "")){
                                            UpdateApiCall(activityListItem.getItem_name(),newPrice.getText().toString());
                                        }
                                        else
                                            Toast.makeText(mContext,"Please enter new Price",Toast.LENGTH_SHORT).show();

                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }


    private void UpdateApiCall(String itemName , String newPrice){
        showProgressDialog();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String api = "https://homebuddy2018.herokuapp.com/editItem/";
        Map<String, Object> data = new HashMap<>();
        data.put( "item_name", itemName );
        data.put( "new_price", newPrice );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(mContext,status,Toast.LENGTH_SHORT).show();
                        update fragment = new update();
                        ((main) mContext).getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,fragment)
                                .addToBackStack(null)
                                .commit();

                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext,"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
                hideProgressDialog();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(request);
    }

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }



}
