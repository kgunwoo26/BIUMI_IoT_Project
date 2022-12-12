package com.example.biumi_iot_project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.biumi_iot_project.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHomeBinding binding;
    private HomeFragment frag1;
    private HistoryFragment frag2;
    private MyHistoryFragment frag3;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavi);

        bottomNavigationView.setOnItemReselectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.action_home:
                    setFrag(0);
                    break;
                case R.id.action_history:
                    setFrag(1);
                    break;
                case R.id.action_myhistory:
                    setFrag(2);
                    break;
                case R.id.action_logout:
                    startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                    break;
            }
        });

        frag1=new HomeFragment();
        frag2=new HistoryFragment();
        frag3=new MyHistoryFragment();
        setFrag(0);

//        MyHistoryDBHelper myHistoryDBHelper = new MyHistoryDBHelper(this);
//        myHistoryDBHelper.insert("1","16","20","16","25","상상관","1F","1");
//        myHistoryDBHelper.insert("2","16","25","16","25","탐구관","1F","1");
//        myHistoryDBHelper.insert("","16","30","0","0","상상관","2F","2");
//        myHistoryDBHelper.insert("","16","35","0","0","탐구관","2F","2");
//        myHistoryDBHelper.insert("1","16","40","16","50","상상 빌리지","3F","3");
//        myHistoryDBHelper.insert("2","16","45","16","50","공학관","3F","3");
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void setFrag(int n) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.content_main,frag1);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.content_main,frag2);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.content_main,frag3);
                ft.commit();
                break;
        }
    }
}

