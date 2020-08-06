package com.example.famileche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class productDetails extends AppCompatActivity {
    private TextView productDetailsName,productDetailsPrice,productDetailsDescription;
    private ImageView productDetailsImage;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        init();
    }

    private void init() {
        productDetailsName=findViewById(R.id.productDetailsName);
        productDetailsPrice=findViewById(R.id.productDetailsPrice);
        productDetailsImage=findViewById(R.id.productDetailsImage);
        productDetailsDescription=findViewById(R.id.productDetailsDescription);
        sharedPreferences=this.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);



        getProduct();
    }

    public void getProduct() {

        Volley.newRequestQueue(this).getCache().clear();

        StringRequest request=new StringRequest(Request.Method.GET, Constant.PRODUCTS+"/"+getIntent().getStringExtra("productId"), response -> {
            try {

                JSONObject object=new JSONObject(response);

                //if(object.getString("user")==sharedPreferences.getString("email",""))
                {
                    JSONObject product=new JSONObject(object.getString("product"));
                    Log.d("detailedproduct",response);
                    productDetailsName.setText(product.getString("name"));
                    productDetailsPrice.setText(product.getString("price"));
                    productDetailsDescription.setText(product.getString("productDescription"));
                    Picasso.get().load(Constant.URL+product.getString("productImage")).placeholder(R.drawable.my_bg).into(productDetailsImage);

                }



            } catch (JSONException err){
                err.printStackTrace();
            }



        },error -> {
            error.printStackTrace();
        }){ @Override
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

        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }



}
