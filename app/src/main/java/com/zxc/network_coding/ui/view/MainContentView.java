package com.zxc.network_coding.ui.view;

import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public abstract class MainContentView extends LinearLayout {
    public static MainContentView mMainContentView ;
    public MainContentView(Context context) {
        super(context);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){}
}
