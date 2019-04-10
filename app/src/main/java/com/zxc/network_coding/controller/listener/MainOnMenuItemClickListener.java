package com.zxc.network_coding.controller.listener;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.zxc.network_coding.R;
import com.zxc.network_coding.utils.DataUtils;

public class MainOnMenuItemClickListener
        implements MenuItem.OnMenuItemClickListener {

    private Context mContext ;
    private Activity mActivity ;

    public MainOnMenuItemClickListener(Context context){
        mContext = context ;
        mActivity = (Activity) context;
    }

    private MainOnMenuItemClickListener(){}

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send:
                //弹出对话框选择文件
                chooseFile();
                break;
        }
        return true;
    }
    // 调用系统文件管理器
    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*").addCategory(Intent.CATEGORY_OPENABLE);
        try {
            mActivity.startActivityForResult(Intent.createChooser(intent, "Choose File"), DataUtils.CHOOSE_FILE_CODE);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "亲，木有文件管理器啊-_-!!", Toast.LENGTH_SHORT).show();
        }
    }

}
