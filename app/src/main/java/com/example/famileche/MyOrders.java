package com.example.famileche;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.famileche.fragments.My_ordersFragment;

public class MyOrders extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameMyOrdersContainer,new My_ordersFragment()).commit();

    }
}