package com.example.famileche.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.famileche.Constant;
import com.example.famileche.HomeActivity;
import com.example.famileche.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {
    private View view;

    private TextView Name, Email, Id;
    private CircleImageView UserImage;
    private SharedPreferences sharedPreferences;



    public AccountFragment(){}




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        view=inflater.inflate(R.layout.layout_account, container,false);
        init();
        return view;
    }

    private void init() {
        Name= view.findViewById(R.id.userDetailsName);
        Email=view.findViewById(R.id.userDetailsEmail);
        Id=view.findViewById(R.id.userDetailsId);
        UserImage=view.findViewById(R.id.userDetailsImage);
        sharedPreferences=getContext().getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        setUser();
    }

    private void setUser() {
        Picasso.get().load(Constant.URL+sharedPreferences.getString("profilePic","")).placeholder(R.drawable.ic_baseline_person_24).into(UserImage);
        Email.setText(sharedPreferences.getString("email",""));
        Id.setText(sharedPreferences.getString("_id",""));
        Name.setText(sharedPreferences.getString("name",""));
    }


}
