package com.example.secondhandplatforms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // 设置默认显示的Fragment
        loadFragment(new ProductFragment());

        // 设置底部导航栏的点击监听器
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Fragment fragment = null;
                //卖闲置
                if(item.getItemId() == R.id.navigation_products){
                        fragment = new MySecondHandFragment();
                    Log.d("my", String.valueOf(MainActivity.UserId));
                        //我的
                }else if(item.getItemId() == R.id.navigation_profile){
                      fragment = new MyFragment();
                      //消息
                }else if(item.getItemId() == R.id.navigation_message){
                    fragment = new MessageFragment();
                }else if(item.getItemId() == R.id.navigation_home){
                    fragment  = new ProductFragment();
                }else{
                    fragment = new OrderFragment();
                }
                return loadFragment(fragment);
            }
        });
    }

    //
    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment);
            transaction.commit();
            return true;
        }
        return false;
    }
}