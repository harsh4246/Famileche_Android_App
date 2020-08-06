package com.example.famileche.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.famileche.Constant;
import com.example.famileche.Models.Product;
import com.example.famileche.Order;
import com.example.famileche.R;
import com.example.famileche.productDetails;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsHolder> {

    private Context context;
    private ArrayList <Product> list;


    public ProductsAdapter(Context context, ArrayList<Product> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ProductsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_product,parent,false);
        Log.d("Adapter","layout inflated");
        return new ProductsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsHolder holder, int position) {
        Product product=list.get(position);

        Picasso.get().load(Constant.URL+product.getProductImage()).placeholder(R.drawable.ic_baseline_shopping_basket_24).fit().centerCrop().into(holder.productImage);
        Log.d("Picasso",product.getProductImage());
        holder.productName.setText(product.getName());
        Log.d("Adapter",product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productId.setText(product.get_id());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }



    static class ProductsHolder extends RecyclerView.ViewHolder{

        private TextView productName,productPrice,productId;
        private ImageView productImage;
        private MaterialButton productOrder;
        public ProductsHolder(@NonNull View itemView) {
            super(itemView);
            productName=itemView.findViewById(R.id.orderName);
            productPrice=itemView.findViewById(R.id.productPrice);
            productImage=itemView.findViewById(R.id.productImage);
            productId=itemView.findViewById(R.id.productId);
            productOrder= itemView.findViewById(R.id.productOrder);

            Log.d("Adapter","view holder initialised");


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(itemView.getContext(), productDetails.class);
                    intent.putExtra("productId",productId.getText());
                    itemView.getContext().startActivity(intent);



                }
            });

            productOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2=new Intent(itemView.getContext(), Order.class);
                    intent2.putExtra("productId",productId.getText());
                    itemView.getContext().startActivity(intent2);
                }
            });






        }
    }
}
