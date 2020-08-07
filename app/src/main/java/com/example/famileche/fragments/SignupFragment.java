package com.example.famileche.fragments;


import android.content.ContentResolver;
import android.content.Intent;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;

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
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;

import com.android.volley.toolbox.Volley;

import com.example.famileche.Constant;

import com.example.famileche.HomeActivity;
import com.example.famileche.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;





import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class SignupFragment extends Fragment {
    private View view;

    private TextInputLayout layoutEmail, layoutPassword,layoutConfirm;
    private TextInputEditText txtEmail,txtPassword,txtConfirm,txtNameSignUp;
    private TextView txtSignIn,selectPhotoText;
    private Button btnSignUp;
    private CircleImageView imgUserInfo;
    private static final int GALLERY_ADD_PROFILE=1;
    private static final int PICK_IMAGE=100;



    private String value;
    private String imagePath;


    public SignupFragment(){}


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstance){
        view=inflater.inflate(R.layout.layout_sign_up, container,false);
        init();
        return view;
    }

    private void init() {
        layoutPassword=view.findViewById(R.id.txtLayoutPasswordSignUp);
        layoutEmail=view.findViewById(R.id.txtLayoutEmailSignUp);
        layoutConfirm=view.findViewById(R.id.txtLayoutConfirmSignUp);
        txtConfirm=view.findViewById(R.id.txtConfirmSignUp);
        txtEmail=view.findViewById(R.id.txtEmailSignUp);
        txtPassword=view.findViewById(R.id.txtPasswordSignUp);
        txtSignIn=view.findViewById(R.id.txtSignIn);
        btnSignUp=view.findViewById(R.id.btnSignUp);
        imgUserInfo=view.findViewById(R.id.imgUserInfo);
        selectPhotoText=view.findViewById(R.id.selectPhotoText);
        txtNameSignUp=view.findViewById(R.id.txtNameSignUp);

        selectPhotoText.setOnClickListener(v->{
            Intent I=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.INTERNAL_CONTENT_URI);
            I.setType("image/*");
            startActivityForResult(I,PICK_IMAGE);
        });

        txtSignIn.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SigninFragment()).commit();
        });

        btnSignUp.setOnClickListener(v->{
            if (validate()){
                register();
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

        txtConfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (txtConfirm.getText().toString().equals(txtPassword.getText().toString())){
                    layoutConfirm.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult( requestCode,  resultCode, data);
        if (requestCode==PICK_IMAGE && resultCode==RESULT_OK){

            Uri imgUri=data.getData();
            imgUserInfo.setImageURI(imgUri);

            String uri=imgUri.getPath();
            File file=new File(uri);

            try {
                final String[] split=file.getPath().split("raw");
                imagePath = split[1];
            } catch (Exception e) {
                e.printStackTrace();

                imagePath=getRealPathFromURI(imgUri);
                Log.d("ImagePath",imagePath);


            }

        }
    }

    private String getRealPathFromURI(Uri imgUri) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(imgUri, null, null, null, null);
        if (cursor == null) {
            result = imgUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void register() {
        //request here
        Volley.newRequestQueue(getContext()).getCache().clear();
        SimpleMultiPartRequest smr = new SimpleMultiPartRequest(Request.Method.POST, Constant.SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        Toast.makeText(getActivity().getApplicationContext(), "User Registerd", Toast.LENGTH_SHORT).show();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameAuthContainer, new SigninFragment()).commit();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity().getApplicationContext(), "User already exists", Toast.LENGTH_SHORT).show();
            }
        });

        smr.addStringParam("email", txtEmail.getText().toString().trim());
        smr.addStringParam("password", txtPassword.getText().toString());
        smr.addStringParam("name", txtNameSignUp.getText().toString());
        smr.addFile("profilePic", imagePath);
        RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        mRequestQueue.add(smr);



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
        if (!txtConfirm.getText().toString().equals(txtPassword.getText().toString())){
            layoutConfirm.setErrorEnabled(true);
            layoutConfirm.setError("Password does not match");
            return false;
        }

        return true;

    }



}
