package com.example.famileche;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class Order extends AppCompatActivity {
    TextView orderName,orderAmount,orderQuantity;
    SharedPreferences sharedPreferences;
    Button COD,OnlinePay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        init();
    }

    private void init() {
        orderName=findViewById(R.id.orderName);
        orderAmount=findViewById(R.id.orderAmount);
        orderQuantity=findViewById(R.id.orderQuantity);
        COD=findViewById(R.id.OrderButton);
        OnlinePay=findViewById(R.id.onlinePayment);
        sharedPreferences=this.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);



        getProduct();

        COD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place_order();
            }
        });

        OnlinePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ada","unavailable");
            }
        });

    }

    private void place_order() {

        Volley.newRequestQueue(this).getCache().clear();

        StringRequest request=new StringRequest(Request.Method.POST, Constant.ORDERS, response -> {
            try {

                JSONObject object=new JSONObject(response);

                //if(object.getString("user")==sharedPreferences.getString("email",""))
                {
                    Log.d("Order",response);
                    Toast.makeText(this,"Order Placed",Toast.LENGTH_LONG).show();



                }



            } catch (JSONException err){
                Toast.makeText(this,"Error placing order",Toast.LENGTH_LONG).show();
                err.printStackTrace();

            }



        },error -> {
            Toast.makeText(this,"Error placing order",Toast.LENGTH_LONG).show();
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

            @Override
            protected Map<String, String> getParams()  {
                HashMap<String,String>map=new HashMap<>();
                map.put("quantity",orderQuantity.getText().toString().trim());
                map.put("productId",getIntent().getStringExtra("productId").toString());
                Log.d("Order","params delivered");
                return map;
            }
        };

        request.setShouldCache(false);

        RequestQueue queue= Volley.newRequestQueue(this);
        queue.add(request);
    }





    private void getProduct() {
        Volley.newRequestQueue(this).getCache().clear();

        StringRequest request=new StringRequest(Request.Method.GET, Constant.PRODUCTS+"/"+getIntent().getStringExtra("productId"), response -> {
            try {

                JSONObject object=new JSONObject(response);

                //if(object.getString("user")==sharedPreferences.getString("email",""))
                {
                    JSONObject product=new JSONObject(object.getString("product"));

                    orderName.setText(product.getString("name"));
                    orderAmount.setText(product.getString("price"));

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