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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.famileche.Adapters.ProductsAdapter;
import com.example.famileche.Constant;
import com.example.famileche.HomeActivity;
import com.example.famileche.Models.Product;
import com.example.famileche.R;
import com.google.android.material.appbar.MaterialToolbar;




import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<Product> arrayList;
    private SwipeRefreshLayout refreshLayout;
    private ProductsAdapter productsAdapter;
    private MaterialToolbar toolbar;
    private SharedPreferences sharedPreferences;


    public HomeFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout_home,container,false);
        init();
        return view;
    }

    private void init(){
        sharedPreferences=getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView=view.findViewById(R.id.recyclerHome);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        refreshLayout=view.findViewById(R.id.swipHome);
        toolbar=view.findViewById(R.id.toolbarHome);
        ((HomeActivity)getContext()).setSupportActionBar(toolbar);
        ((HomeActivity)getContext()).getSupportActionBar().setDisplayShowTitleEnabled(false);


        getProducts();



        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getProducts();
            }
        });
    }

    private void getProducts() {
        arrayList = new ArrayList<>();

        Volley.newRequestQueue(getContext()).getCache().clear();
        StringRequest request = new StringRequest(Request.Method.GET,
                Constant.PRODUCTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object2 = new JSONObject(response);

                            Log.d("abcd'''''''''", response);
                            //Log.d("token",sharedPreferences.getString("value",""));
                            JSONArray array = new JSONArray(object2.getString("products"));
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject productObject = array.getJSONObject(i);
                                Log.d("HomeHarsh", productObject.getString("name"));
                                Product product = new Product();
                                product.set_id(productObject.getString("_id"));
                                product.setName(productObject.getString("name"));
                                product.setPrice(productObject.getString("price"));
                                product.setProductImage(productObject.getString("productImage"));
                                Log.d("HomeHarsh", productObject.getString("name"));
                                Log.d("HomeHarsh", "product set in request");
                                Log.d("Picasso", product.getProductImage());
                                arrayList.add(product);
                            }

                            productsAdapter = new ProductsAdapter(getContext(), arrayList);
                            recyclerView.setAdapter(productsAdapter);
                            Log.d("HomeHarsh", "Adapter Set");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        refreshLayout.setRefreshing(false);
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
