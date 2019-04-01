package com.zxc.network_coding.controller;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.MenuItem;
import android.view.View;
import com.google.android.material.navigation.NavigationView;
import com.zxc.network_coding.R;
import com.zxc.network_coding.view.MainHistoryContentView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class MainNavItemSelectedListener
        implements NavigationView.OnNavigationItemSelectedListener{

    private Context mContext = null ;
    private Activity mActivity = null ;
    private ConstraintLayout mLayout = null ;
    public MainNavItemSelectedListener(Context context){
        mContext = context ;
        mActivity = (Activity) mContext;
        mLayout = mActivity.findViewById(R.id.content_main) ;
        mLayout.removeAllViews();
        mLayout.addView(new MainHistoryContentView(mContext));
    }
    private MainNavItemSelectedListener(){}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        mLayout.removeAllViews();
        View view = null ;
        int id = item.getItemId();
        switch (id){
            case R.id.history:
                view = new MainHistoryContentView(mContext) ;
                Toolbar toolbar = mActivity.findViewById(R.id.toolbar);
                toolbar.setTitle("History");
                break;
            case R.id.send :
                view = new MainHistoryContentView(mContext) ;
                break;
            case R.id.receive:
                view = new MainHistoryContentView(mContext) ;
                break;

        }
        mLayout.addView(view);
        DrawerLayout drawer = mActivity.findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void finalize() throws Throwable {
        mContext = null ;
        mLayout = null ;
        super.finalize();
    }
}
