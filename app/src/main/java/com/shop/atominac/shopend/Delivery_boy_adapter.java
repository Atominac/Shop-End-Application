package com.shop.atominac.shopend;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

class Delivery_boy_adapter extends RecyclerView.Adapter<Delivery_boy_adapter.ViewHolder>{
    private List<Delivery_boy_model> activityList;
    private Context mContext;
    String orderId ;
    private ProgressDialog pDialog;
    private static final String TAG = "PlaceOrder";
    Delivery_boy_model activityListItems ;

    Delivery_boy_adapter(List<Delivery_boy_model> activityList, Context context,String orderId) {
        this.activityList = activityList;
        mContext = context;
        this.orderId = orderId ;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name , status , location,token;

        ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            status = (TextView) view.findViewById(R.id.status);
            location = (TextView) view.findViewById(R.id.location);
            token = (TextView) view.findViewById(R.id.token);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    ForwardApiCall(orderId,activityListItems.getBoyId());
                }
            });
        }
    }

    @Override
    public Delivery_boy_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_boy_model, parent, false);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        return new Delivery_boy_adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Delivery_boy_adapter.ViewHolder holder, int position) {
        activityListItems = activityList.get(position);
        holder.name.setText(activityListItems.getName());
        holder.status.setText(activityListItems.getStatus());
        holder.location.setText(activityListItems.getLocation());
        holder.token.setText(activityListItems.getToken());

    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }

    private void ForwardApiCall(String orderId , String boyId){
        showProgressDialog();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        String api = "https://homebuddy2018.herokuapp.com/forwardOrder/";
        Map<String, Object> data = new HashMap<>();
        data.put( "order_id", orderId );
        data.put( "boy_id", boyId );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(mContext,status,Toast.LENGTH_SHORT).show();
                        orders fragment = new orders();
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
