package com.shop.atominac.shopend;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BoyList extends Fragment {
    protected View mView;
    ProgressBar progressBar;
    private List<Delivery_boy_model> activityList = new ArrayList<>();
    private BoyListAdapter mAdapter;
    Delivery_boy_model activityItems;
    LinearLayoutManager layoutManager ;
    RecyclerView recyclerView ;
    private ProgressDialog pDialog;
    private static final String TAG = "PlaceOrder";
    TextView textView;
    Button button;
    String order_id ;

    String user_id;

    public BoyList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        order_id = getArguments().getString("id");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_delivery__boy, container, false);
        this.mView = view;
        setHasOptionsMenu(true);



        progressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView) mView.findViewById(R.id.deliver_recycler);
        mAdapter = new BoyListAdapter(activityList, getActivity(),order_id);
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
        String api = "https://homebuddy2018.herokuapp.com/myBoys";

        VolleyRequester request = new VolleyRequester(Request.Method.GET,api,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {
                String finalLocation = "Loading";
                for (int i = 0; i <=jsonArray.length(); i++) {
                    try {
                        JSONObject itemDetails = (JSONObject)jsonArray.get(i);
                        String boyId = itemDetails.get("id").toString();
                        String name = itemDetails.get("name").toString();
                        String location = itemDetails.get("location").toString();
                        String nLocation= location.replaceAll("[\\[\\](){}]","");
                        String str[]=nLocation.split(",");
                        String lat =str[0];
                        String lng =str[1];
                        Geocoder gcd = new Geocoder(getActivity(), Locale.getDefault());
                        List<Address> start_position_string = gcd.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);

                        if (start_position_string.size()!=0){
                            String city = start_position_string.get(0).getLocality();
                            String state = start_position_string.get(0).getAdminArea();
                            finalLocation  = start_position_string.get(0).getAddressLine(0) + "\n" +
                                    city + "\n" + state;
                        }
//                        String itemList = itemDetails.get("item_list").toString();

                        activityItems = new Delivery_boy_model(boyId,name ,finalLocation ,"available" ," ");
                        activityList.add(activityItems);

                    } catch (JSONException | IOException e) {
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
