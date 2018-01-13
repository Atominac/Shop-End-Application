package com.shop.atominac.shopend;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class orders extends Fragment {

    protected View mView;
    private static final String TAG = "myOrders";
    RecyclerView recyclerView ;
    private List<OrderModel> activityList = new ArrayList<>();
    private OrderAdapter mAdapter;
    OrderModel activityItems;
    ProgressBar progressBar;
    LinearLayoutManager layoutManager ;


    public orders() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        this.mView = view;

        progressBar = (ProgressBar)mView.findViewById(R.id.order_progressBar);
        recyclerView = (RecyclerView) mView.findViewById(R.id.order_recycler);
        mAdapter = new OrderAdapter(activityList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        MyOrderListApiCall();

        return  view ;
    }

    void MyOrderListApiCall(){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "http://192.168.1.5:8000/viewPendingOrders";

        VolleyRequester request = new VolleyRequester(Request.Method.GET,api,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = 0; i <=jsonArray.length(); i++) {
                    try {
                        JSONObject itemDetails = (JSONObject)jsonArray.get(i);
                        String coustmer_name = itemDetails.get("customer_name").toString();
                        String coustmer_address = itemDetails.get("customer_address").toString();
                        String itemList = itemDetails.get("item_list").toString();
                        String bill = itemDetails.get("amount").toString();
                        String status = itemDetails.get("status").toString();
                        String payment = itemDetails.get("delivery_type").toString();
                        String time = itemDetails.get("order_time").toString();


                        String str[]=time.split("T");
                        String date=str[0];
                        String rawtime=str[1];
                        String strtime[]=rawtime.split(":");
                        String hours=strtime[0];
                        String minute=strtime[1];
                        String finaltime=hours + ":" + minute;
                        String finaldatetime=date + " " + finaltime;

                        activityItems = new OrderModel(coustmer_name , coustmer_address, "Bill amount : Rs " + bill ,payment,itemList,finaldatetime,status);
                        activityList.add(activityItems);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

               mAdapter.notifyDataSetChanged();
                hideProgress();
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

    private void showProgress() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
    }





}
