package com.example.famileche.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.famileche.Constant;
import com.example.famileche.Models.Order;
import com.example.famileche.Models.Product;
import com.example.famileche.R;
import com.example.famileche.productDetails;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdersHolder> {

    private Context context;
    private ArrayList<Order> list;


    public MyOrdersAdapter(Context context, ArrayList<Order> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyOrdersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order,parent,false);
        return new MyOrdersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersHolder holder, int position) {
        Order order=list.get(position);
        holder.orderId.setText(order.getOrderId());
        holder.productQuantity.setText(order.getQuantity());
        holder.productFinalPrice.setText(order.getPrice());
        holder.productName.setText(order.getName());
        Picasso.get().load(Constant.URL+order.getProductImage()).placeholder(R.drawable.ic_baseline_shopping_basket_24).fit().centerCrop().into(holder.productImage);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyOrdersHolder extends RecyclerView.ViewHolder{
        TextView productName,productFinalPrice,productQuantity,orderId;
        Button cancelOrder;
        ImageView productImage;
        public MyOrdersHolder(@NonNull View itemView) {
            super(itemView);

            productName=itemView.findViewById(R.id.myOrdersName);
            productFinalPrice=itemView.findViewById(R.id.myOrdersPrice);
            productQuantity=itemView.findViewById(R.id.myOrdersQuantity);
            cancelOrder=itemView.findViewById(R.id.myOrdersDelete);
            productImage=itemView.findViewById(R.id.myOrdersImage);
            orderId=itemView.findViewById(R.id.myOrdersId);




            cancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteOrder();
                }
            });

        }

        private void deleteOrder() {
            SharedPreferences sharedPreferences=itemView.getContext().getApplicationContext().getSharedPreferences("user",Context.MODE_PRIVATE);
            //StringRequest request=new StringRequest(Request.Method.DELETE, Constant.ORDERS+"/"+)
            Volley.newRequestQueue(itemView.getContext()).getCache().clear();

            Log.d("CancelOrder",Constant.ORDERS+"/"+orderId.getText());
            StringRequest request = new StringRequest(Request.Method.DELETE,
                    Constant.ORDERS+"/"+orderId.getText(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);

                                Log.d("abcd'''''''''", response);
                                Toast.makeText(itemView.getContext(),"Order Deleted",Toast.LENGTH_LONG).show();
                                productImage. setImageResource(R.drawable.ic_baseline_delete_sweep_24);


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
                    Log.d("CancelOrders", token);

                    return map;
                }
            };
            request.setShouldCache(false);

            RequestQueue queue = Volley.newRequestQueue(itemView.getContext());
            queue.add(request);
        }
    }
}

