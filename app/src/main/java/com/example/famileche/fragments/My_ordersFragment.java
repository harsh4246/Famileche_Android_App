package com.example.famileche.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.famileche.Adapters.MyOrdersAdapter;
import com.example.famileche.Constant;
import com.example.famileche.HomeActivity;
import com.example.famileche.Models.Order;

import com.example.famileche.MyOrders;
import com.example.famileche.R;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class My_ordersFragment extends Fragment {

    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private ArrayList<Order> arrayList;
    private MaterialToolbar toolbar;
    private MyOrdersAdapter myOrdersAdapter;
    private View view;


    public My_ordersFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout_my_orders,container,false);
        init();
        return view;
    }

    private void init(){

        sharedPreferences=getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView=view.findViewById(R.id.recyclerMyOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        toolbar=view.findViewById(R.id.toolbarMyOrders);
        ((MyOrders)getContext()).setSupportActionBar(toolbar);
        ((MyOrders)getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);


        getOrders();


    }

    private void getOrders() {
        arrayList = new ArrayList<>();

        Volley.newRequestQueue(getContext()).getCache().clear();
        StringRequest request = new StringRequest(Request.Method.GET,
                Constant.ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object2 = new JSONObject(response);

                            Log.d("abcd'''''''''", response);
                            //Log.d("token",sharedPreferences.getString("value",""));
                            JSONArray array = new JSONArray(object2.getString("orders"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject orderObject = array.getJSONObject(i);
                                JSONObject productObject  = orderObject.getJSONObject("product");
                                Log.d("HomeHarsh", orderObject.getString("user"));
                                Order order = new Order();
                                order.set_id(productObject.getString("_id"));
                                order.setOrderId(orderObject.getString("_id"));
                                order.setName(productObject.getString("name"));
                                order.setPrice(productObject.getString("price"));
                                order.setProductImage(productObject.getString("productImage"));
                                order.setQuantity(orderObject.getString("quantity"));
                                Log.d("HomeHarsh", productObject.getString("name"));
                                Log.d("HomeHarsh", "product set in request");
                                Log.d("Picasso", order.getProductImage());
                                arrayList.add(order);
                            }

                             myOrdersAdapter= new MyOrdersAdapter(getContext(), arrayList);
                            recyclerView.setAdapter(myOrdersAdapter);
                            Log.d("HomeHarsh", "Adapter Set");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",error.getMessage());
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = sharedPreferences.getString("value", "");
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("Authorization", token);
                map.put("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                map.put("Pragma", "no-cache"); // HTTP 1.0.
                map.put("Expires", "0");
                Log.d("token", token);

                return map;
            }
        };
        request.setShouldCache(false);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);
    }
}
