package com.zxc.network_coding.ui.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zxc.network_coding.R;
import com.zxc.network_coding.service.sendFile.SendFileService;
import com.zxc.network_coding.service.sendFile.SendFileServiceConnection;
import com.zxc.network_coding.utils.FileUtils;

import org.w3c.dom.Text;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;

public class SendDialogView extends AlertDialog
        implements View.OnClickListener {


    public static final String TAG = "SendDialogView";

    private Context mContext = null ;
    private Activity mActivity = null ;
    private Uri mUri = null ;
    private String mName = null ;
    private TextView mFileName = null ;
    private AppCompatImageView mFileIcon = null ;
    private String mPositiveString = null ;
    private String mNegativeString = null ;
    private TextView mPositiveButton = null;
    private TextView mNegativeButton = null ;
    private View mContentView = null ;

    /**
     * 构造方法
     *
     * @param context 上下文
     * @param uri     文件的uri
     */
    public SendDialogView(@NonNull Context context, @NonNull Uri uri) {
        super(context);
        mContext = context;
        mActivity = (Activity) mContext;
        mUri = uri;
        mName = FileUtils.getName(mUri);
        mPositiveString = mContext.getResources().getString(R.string.positive);
        mNegativeString = mContext.getResources().getString(R.string.negative);
        initView();
        initData();
    }

    private void initData() {
        setCancelable(false) ;
        mFileName.setText(mName);
        mPositiveButton.setOnClickListener(this);
        mNegativeButton.setOnClickListener(this);
    }

    private void initView() {
        mContentView = View.inflate(mContext, R.layout.content_send_dialog, null);
        setView(mContentView);
        mFileIcon = mContentView.findViewById(R.id.file_icon);
        mFileName = mContentView.findViewById(R.id.file_name);
        mPositiveButton = mContentView.findViewById(R.id.positive);
        mNegativeButton = mContentView.findViewById(R.id.negative);
    }

    void finish() {
        mContext = null;
        mUri = null;
        mActivity = null;
        mName = null;
        mFileIcon = null;
        mFileName = null;
        mNegativeButton = null ;
        mPositiveButton = null ;
        mNegativeString = null ;
        mPositiveString = null ;
        mContentView = null ;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive:
                this.setCancelable(false) ;
                //dismiss之前将相关数据存储到数据库中，并在后台启动相关服务来进行数据处理与传输
                Intent intent = new Intent(mContext, SendFileService.class) ;
                intent.setData(mUri) ;
                mContext.bindService(intent, SendFileServiceConnection.getInstance(),Context.BIND_AUTO_CREATE) ;
                dismiss();
                break;
            case R.id.negative:
                //取消
                this.dismiss();
                break;
        }
    }

    @Override
    public void dismiss() {
        finish();
        super.dismiss();
    }
}
