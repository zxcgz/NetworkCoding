package com.zxc.network_coding.controller.listener;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.zxc.network_coding.R;
import com.zxc.network_coding.ui.view.MainContentView;
import com.zxc.network_coding.ui.view.MainHistoryContentView;
import com.zxc.network_coding.ui.view.MainSendContentView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
        MainContentView.mMainContentView = new MainHistoryContentView(mContext) ;
        mLayout = mActivity.findViewById(R.id.content_main) ;
        mLayout.removeAllViews();
        mLayout.removeAllViewsInLayout();
        mLayout.addView(MainContentView.mMainContentView);

    }
    private MainNavItemSelectedListener(){}

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation mView item clicks here.
        MainContentView.mMainContentView = null ;
        mLayout.removeAllViews();
        mLayout.removeAllViewsInLayout();
        //Menu menu = toolbar.getMenu();
        //menu.findItem(R.menu.view_send) ;
        int id = item.getItemId();
        switch (id){
            case R.id.history:
                MainContentView.mMainContentView = new MainHistoryContentView(mContext) ;
                break;
            case R.id.send :
                MainContentView.mMainContentView = new MainSendContentView(mContext) ;
                break;
            case R.id.receive:
                MainContentView.mMainContentView = new MainHistoryContentView(mContext) ;
                break;

        }
        mLayout.addView(MainContentView.mMainContentView);
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
