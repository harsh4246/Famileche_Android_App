package com.example.famileche;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.famileche.fragments.AccountFragment;
import com.example.famileche.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportFragmentManager().beginTransaction().replace(R.id.frameHomeContainer,new HomeFragment(),HomeFragment.class.getSimpleName()).commit();
        init();

    }

    private void init(){
        navigationView=findViewById(R.id.bottom_nav);
        fab=findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,MyOrders.class);
                startActivity(intent);
            }
        });

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {


            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.item_home: {
                        Fragment account=getSupportFragmentManager().findFragmentByTag(AccountFragment.class.getSimpleName());
                        if (account!=null){
                            getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag(AccountFragment.class.getSimpleName())).commit();
                            getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName())).commit();

                        }

                        break;
                    }

                    case R.id.item_account:{

                        Fragment account=getSupportFragmentManager().findFragmentByTag(AccountFragment.class.getSimpleName());
                        getSupportFragmentManager().beginTransaction().hide(getSupportFragmentManager().findFragmentByTag(HomeFragment.class.getSimpleName())).commit();
                        if (account!=null){
                            getSupportFragmentManager().beginTransaction().show(getSupportFragmentManager().findFragmentByTag(AccountFragment.class.getSimpleName())).commit();

                        }
                        else {
                            getSupportFragmentManager().beginTransaction().add(R.id.frameHomeContainer,new AccountFragment(),AccountFragment.class.getSimpleName()).commit();
                        }

                        break;
                    }
                }


                return true;
            }
        });
    }


}