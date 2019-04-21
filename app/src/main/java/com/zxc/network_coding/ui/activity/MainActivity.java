package com.zxc.network_coding.ui.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.zxc.network_coding.R;
import com.zxc.network_coding.controller.listener.MainNavItemSelectedListener;
import com.zxc.network_coding.ui.view.MainContentView;

public class MainActivity extends AppCompatActivity {


    private NavigationView mMenuSide;
    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        /**/
    }

    /**
     * 初始化数据
     */
    private void initData() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        mMenuSide.setNavigationItemSelectedListener(new MainNavItemSelectedListener(this));
    }

    /**
     * 初始化界面
     */
    private void initView() {
        //setContentView(R.layout.activity_main);
        View view = View.inflate(this, R.layout.activity_main, null);
        setContentView(view);
        mMenuSide = findViewById(R.id.menu_side);
        mToolbar = findViewById(R.id.toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);

    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        mMenuSide = null;
        mDrawerLayout = null;
        mToolbar = null;
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (MainContentView.mMainContentView != null) {
            MainContentView.mMainContentView.onActivityResult(requestCode, resultCode, data);
        }
    }
}
