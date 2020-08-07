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
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.famileche.Constant;
import com.example.famileche.HomeActivity;
import com.example.famileche.MainActivity;
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
    private Button deleteButton;



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
        deleteButton=view.findViewById(R.id.DeleteMyAccount);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAccount();
            }
        });

        setUser();
    }

    private void deleteAccount() {
        Volley.newRequestQueue(getContext()).getCache().clear();

        StringRequest request=new StringRequest(Request.Method.DELETE, Constant.USERS+sharedPreferences.getString("_id",""), response -> {
            try {

                JSONObject object=new JSONObject(response);

                Log.d("DeleteAccount",response);
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(getContext(),"Account Deleted",Toast.LENGTH_LONG).show();


            } catch (JSONException err){
                Toast.makeText(getContext(),"Error Occured Deleting your account",Toast.LENGTH_SHORT).show();
                err.printStackTrace();
            }



        },error -> {
            Toast.makeText(getContext(),"Error Occured Deleting your account",Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }){

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

        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private void setUser() {
        Picasso.get().load(Constant.URL+sharedPreferences.getString("profilePic","")).placeholder(R.drawable.ic_baseline_person_24).into(UserImage);
        Email.setText(sharedPreferences.getString("email",""));
        Id.setText(sharedPreferences.getString("_id",""));
        Name.setText(sharedPreferences.getString("name",""));
    }


}
