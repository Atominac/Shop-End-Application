package com.shop.atominac.shopend;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class update extends Fragment {
    protected View mView;
    ProgressBar progressBar,loadMoreProgressBar;
    private List<UpdateModel> activityList = new ArrayList<>();
    private UpdateAdapter mAdapter;
    UpdateModel activityItems;
    LinearLayoutManager layoutManager ;
    RecyclerView recyclerView ;
    private ProgressDialog pDialog;
    private static final String TAG = "PlaceOrder";
    EditText search ;
    int page_count = 1 ;

    String user_id;

    public update() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_update, container, false);
        this.mView = view;
        setHasOptionsMenu(true);

        progressBar = (ProgressBar)mView.findViewById(R.id.update_progressBar);
        loadMoreProgressBar = (ProgressBar)mView.findViewById(R.id.MoreItem_loading);
        recyclerView = (RecyclerView) mView.findViewById(R.id.update_recycler);
        mAdapter = new UpdateAdapter(activityList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        search = (EditText)mView.findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                activityList.clear();
                mAdapter= new UpdateAdapter(activityList,getActivity());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchItemListApiCall(s.toString());
                page_count = 1 ;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    LoadMoreItemListApiCall(search.getText().toString(),++page_count);
                }
            }
        });


        return view;
    }

    void SearchItemListApiCall(final String text){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/search/";
        Map<String, Object> data = new HashMap<>();
        data.put( "text", text );
        data.put( "page_no", 1 );

        VolleyRequester request = new VolleyRequester(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = 0; i <=jsonArray.length(); i++) {
                    try {
                        JSONObject itemDetails = (JSONObject)jsonArray.get(i);

                        String itemName = itemDetails.get("name").toString();
                        String itemPrice = itemDetails.get("price").toString();
                        String itemBrand = itemDetails.get("brand").toString();
                        activityItems = new UpdateModel(itemName , "Rs. "+itemPrice , "Brand : "+itemBrand );
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

    void LoadMoreItemListApiCall(final String text , int page_no){
        showLoadMoreProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/search/";
        Map<String, Object> data = new HashMap<>();
        data.put( "text", text );
        data.put( "page_no", page_no );

        VolleyRequester request = new VolleyRequester(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = 0; i <=jsonArray.length(); i++) {
                    try {
                        JSONObject itemDetails = (JSONObject)jsonArray.get(i);

                        if (!itemDetails.has("status")){
                            String itemName = itemDetails.get("name").toString();
                            String itemPrice = itemDetails.get("price").toString();
                            String itemBrand = itemDetails.get("brand").toString();
                            activityItems = new UpdateModel(itemName , "Rs. "+itemPrice , "Brand : "+itemBrand);
                            activityList.add(activityItems);
                        }

                        else{
//                            Toast.makeText(getActivity(),"No more data",Toast.LENGTH_LONG).show();
                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mAdapter.notifyDataSetChanged();
                hideLoadMoreProgress();
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

    private void showLoadMoreProgress() {

        loadMoreProgressBar.setIndeterminate(true);
        loadMoreProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideLoadMoreProgress() {
        loadMoreProgressBar.setIndeterminate(false);
        loadMoreProgressBar.setVisibility(View.GONE);
    }

}
