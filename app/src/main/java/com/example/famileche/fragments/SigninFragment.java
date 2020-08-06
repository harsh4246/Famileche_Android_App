package com.example.famileche.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.famileche.Models.User;
import com.example.famileche.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class SigninFragment extends Fragment {
    private View view;

    private TextInputLayout layoutEmail, layoutPassword;
    private TextInputEditText txtEmail,txtPassword;
    private TextView txtSignup;
    private Button btnSignIn;


    public SigninFragment(){}




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        view=inflater.inflate(R.layout.layout_sign_in, container,false);
        init();
        return view;
    }

    private void init() {
        layoutPassword=view.findViewById(R.id.txtLayoutPasswordSignIn);
        layoutEmail=view.findViewById(R.id.txtLayoutEmailSignIn);
        txtEmail=view.findViewById(R.id.txtEmailSignIn);
        txtPassword=view.findViewById(R.id.txtPasswordSignIn);
        txtSignup=view.findViewById(R.id.txtSignUp);
        btnSignIn=view.findViewById(R.id.btnSignIn);

        txtSignup.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SignupFragment()).commit();
        });

        btnSignIn.setOnClickListener(v->{
            if (validate()){
                login();
            }
        });

        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (! txtEmail.getText().toString().isEmpty()){
                    layoutEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtPassword.getText().toString().length()>=8){
                    layoutPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    private void login() {


        Volley.newRequestQueue(getContext()).getCache().clear();

        StringRequest request=new StringRequest(Request.Method.POST, Constant.LOGIN, response -> {
            try {

                JSONObject object=new JSONObject(response);

                if (object.getBoolean("Success")){
                    JSONObject user =object.getJSONObject("user");
                    SharedPreferences userPref=getActivity().getApplication().getSharedPreferences("user",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor=userPref.edit();
                    editor.putString("value",object.getString("value"));
                    editor.putString("name",user.getString("name"));
                    editor.putString("profilePic",user.getString("profilePic"));
                    editor.putString("_id",user.getString("_id"));
                    editor.putString("email",user.getString("email"));
                    editor.apply();



                    



                    Toast.makeText(getContext(),"Login Success",Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getActivity(), HomeActivity.class));
                    getActivity().finish();


                }

            } catch (JSONException err){
                err.printStackTrace();
            }



        },error -> {
            error.printStackTrace();
        }){
            @Override
            protected Map<String, String> getParams()  {
                HashMap<String,String>map=new HashMap<>();
                map.put("email",txtEmail.getText().toString().trim());
                map.put("password",txtPassword.getText().toString());
                Log.d("signin","params delivered");
                return map;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getContext());
        queue.add(request);
    }

    private  boolean validate(){
        if (txtEmail.getText().toString().isEmpty()){
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email is required");
            return false;
        }

        if (txtPassword.getText().toString().length()<8){
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Password too short");
            return false;
        }

        return true;

    }
}



