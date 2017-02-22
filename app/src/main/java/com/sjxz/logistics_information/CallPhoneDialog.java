package com.sjxz.logistics_information;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * @author WYH_Healer
 * @email 3425934925@qq.com
 * Created by xz on 2017/2/22.
 * Role:点击手机号码弹出的Dialog用于拨打电话
 */
public class CallPhoneDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private String phoneNumber;

    private TextView tv_call_phone;

    private TextView tv_cancle;

    public CallPhoneDialog(Context context, String phoneNumber) {
        super(context, R.style.DialogStyle);
        this.context = context;
        this.phoneNumber = phoneNumber;

        View view = LayoutInflater.from(context).inflate(R.layout.layout_call_phone, null);
        setContentView(view);
        // 设置窗口大小
        Window mWindow = getWindow();
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {// 竖屏
            lp.width = Utils.getScreenWidth(context);
        } else {
            lp.width = Utils.getScreenHeight(context);
        }
        mWindow.setAttributes(lp);
        // 设置可以动画
        mWindow.setWindowAnimations(R.style.dialogAnim);
        // 位置设置在底部
        mWindow.setGravity(Gravity.BOTTOM);
        // 设置可取消
        setCancelable(true);
        setCanceledOnTouchOutside(true);

        initView();
        initData();

    }


    private void initView() {
        tv_call_phone = (TextView) findViewById(R.id.tv_call_phone);
        tv_cancle = (TextView) findViewById(R.id.tv_cancle);

        tv_call_phone.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
    }

    private void initData() {
        tv_call_phone.setText(phoneNumber + "");
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        tv_call_phone.setText(phoneNumber + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_call_phone:
                //实现拨打电话的操作
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
                        + phoneNumber));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(intent);
                break;
            case R.id.tv_cancle:
                this.dismiss();
                break;
        }
    }
}
