package com.zxc.network_coding.ui.view;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;

import com.zxc.network_coding.R;
import com.zxc.network_coding.controller.adapter.MainHistoryContentListAdapter;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainHistoryContentView extends MainContentView {

    private RecyclerView mList;
    private Context mContext ;
    private Activity mActivity;
    private Toolbar mToolbar;
    private static final String TAG ="MainHistoryContentView" ;

    public MainHistoryContentView(Context context) {
        super(context);
        mContext = context ;
        mActivity = (Activity) context;
        initView() ;
        initData() ;
    }
    private void initData() {
        mList.setLayoutManager(new LinearLayoutManager(mContext));
        mList.setAdapter(new MainHistoryContentListAdapter());
        mToolbar.setTitle(R.string.history);
        Log.d("initData",TAG);
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.content_main_history,this) ;
        mList = findViewById(R.id.list_content_main_history);
        mToolbar = mActivity.findViewById(R.id.toolbar);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mList = null ;
        mContext = null ;
        mToolbar = null ;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mList = null ;
        mContext = null ;
        mToolbar = null ;
    }

}
