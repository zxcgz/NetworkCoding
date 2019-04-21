package com.zxc.network_coding.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.zxc.network_coding.R;
import com.zxc.network_coding.controller.listener.MainOnMenuItemClickListener;
import com.zxc.network_coding.utils.DataUtils;
import com.zxc.network_coding.utils.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class MainSendContentView extends MainContentView {
    Context mContext = null;
    Activity mActivity = null;
    private RecyclerView mList;
    private Toolbar mToolbar;
    private Menu mMenu;
    private MenuItem mMenuItem;
    private static final String TAG = "MainSendContentView";

    public MainSendContentView(Context context) {
        super(context);
        mContext = context;
        mActivity = (Activity) context;
        initView();
        initData();
    }

    private void initData() {
        int size = mMenu.size();
        mToolbar.setTitle(R.string.send);
        Log.d("initData", TAG);
        Log.d(TAG, size + "");
        if (size == 0) {
            mToolbar.inflateMenu(R.menu.view_send);
        } else if (null != mMenuItem) {
            mMenuItem.setVisible(true);
        }
        mMenuItem = mMenu.findItem(R.id.action_send);
        mMenuItem.setOnMenuItemClickListener(new MainOnMenuItemClickListener(mContext));
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.content_main_send, this);
        mList = findViewById(R.id.list_content_main_send);
        mToolbar = mActivity.findViewById(R.id.toolbar);
        mMenu = mToolbar.getMenu();
        //为了防止第一次切换出现问题，此处的和onDetachedFromWindow方法中都需要获取MenuItem
        mMenuItem = mMenu.findItem(R.id.action_send);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mMenuItem = mMenu.findItem(R.id.action_send);
        if (mMenuItem != null) {
            mMenuItem.setVisible(false);
        }
        mMenu.close();
        mActivity.closeOptionsMenu();
        mActivity = null;
        mContext = null;
        mToolbar = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (null != data && null != data.getData() && requestCode == DataUtils.CHOOSE_FILE_CODE) {
            Log.d(TAG, "connectServer");
            String path = data.getData().getPath();
            if (path != null) {
                //弹出Dialog显示操作进度
                SendDialogView view = new SendDialogView(mContext, data.getData());
                view.show();
            }
        }
    }

}
