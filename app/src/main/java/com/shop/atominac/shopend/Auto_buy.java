package com.shop.atominac.shopend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Auto_buy extends Fragment {
    protected View mView;
    ProgressBar progressBar;
    private List<Autobuy_model> activityList = new ArrayList<>();
    private Autobuy_adapter mAdapter;
    Autobuy_model activityItems;
    LinearLayoutManager layoutManager ;
    RecyclerView recyclerView ;
    private ProgressDialog pDialog;
    private static final String TAG = "PlaceOrder";
    TextView textView;
    Button button;

    String user_id;

    public Auto_buy() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_autobuy, container, false);
        this.mView = view;
        setHasOptionsMenu(true);



        progressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) mView.findViewById(R.id.autobuy_recycler);
        mAdapter = new Autobuy_adapter(activityList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        MyboyApiCall();

        return view;
    }

    void MyboyApiCall(){
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

                       // activityItems = new OrderModel(coustmer_name , coustmer_address, "Bill amount : Rs " + bill ,payment,itemList,finaldatetime,status);
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

    private void PlaceOrderApiCall(String id , String paymentType){
        showProgressDialog();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/placeOrder/";
        Map<String, Object> data = new HashMap<>();
        data.put( "id", id );
        data.put( "payment_type", paymentType );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    String status = response.getString("status");
                    if (status.equals("success")) {
                        String bill = response.getString("bill");
                        Intent intent = new Intent("com.homebuddy.homebuddy.OrderSuccess");
                        intent.putExtra("bill",bill);
                        startActivity(intent);
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"Error: " + e.getMessage(),Toast.LENGTH_LONG).show();
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

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}
