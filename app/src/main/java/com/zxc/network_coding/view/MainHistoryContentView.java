package com.zxc.network_coding.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zxc.network_coding.R;
import com.zxc.network_coding.controller.MainHistoryContentListAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainHistoryContentView extends LinearLayout {

    private RecyclerView mList;
    private Context mContext ;
    public MainHistoryContentView(Context context) {
        super(context);
        mContext = context ;
        initView() ;
        initData() ;
    }
    private void initData() {
        mList.setLayoutManager(new LinearLayoutManager(mContext));
        mList.setAdapter(new MainHistoryContentListAdapter());
        Log.d("work","MainHistoryContentView");
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.content_main_history,this) ;
        mList = findViewById(R.id.list_content_main_history);
    }
}
