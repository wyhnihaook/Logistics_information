package com.sjxz.logistics_information;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LogisticsInformationView logistics_InformationView;

    List<LogisticsData> logisticsDataList;

    CallPhoneDialog callPhoneDialog;

    final int REQUEST_CODE = 0x1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    private void initView() {
        logistics_InformationView=(LogisticsInformationView)findViewById(R.id.logistics_InformationView);
    }

    private void initData() {

        logisticsDataList=new ArrayList<>();
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 07:23:06").setContext("[杭州市]浙江杭州江干公司派件员：吕敬桥  15555555551  正在为您派件正在为您派件正在为您派件正在为您派件正在为您派件正在为您派件正在为您派件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:11:37").setContext("[杭州市]浙江杭州江干区新杭派公司派件员：吕敬桥  15555555552  正在为您派件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:08:06").setContext("[杭州市]浙江派件员：吕敬桥  15555555553  正在为您派件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:08:06").setContext("[杭州市]员：吕敬桥  15555555554  正在为您派件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 11:23:07").setContext("[杭州市]浙江杭州江干区新杭派公司进行签收扫描，快件已被  已签收  签收，感谢使用韵达快递，期待再次为您服务"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 15:52:43").setContext("[泰州市]韵达快递  江苏靖江市公司收件员  已揽件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 12:39:15").setContext("包裹正等待揽件"));
        logisticsDataList.add(new LogisticsData().setTime("2016-6-28 15:13:02").setContext("快件在【相城中转仓】装车,正发往【无锡分拨中心】已签收,签收人是【王漾】,签收网点是【忻州原平】"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 11:23:07").setContext("[杭州市]浙江杭州江干区新杭派公司进行签收扫描，快件已被  已签收  签收，感谢使用韵达快递，期待再次为您服务"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 06:48:37").setContext("到达目的地网点浙江杭州江干区新杭派，快件很快进行派送"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:11:37").setContext("[苏州市]江苏苏州分拨中心  已发出"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:08:06").setContext("[苏州市]快件已到达  江苏苏州分拨中心"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 15:52:43").setContext("[泰州市]韵达快递  江苏靖江市公司收件员  已揽件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 12:39:15").setContext("菜鸟驿站代收，请及时取件，如有疑问请联系 程先生:18061208980"));


        logistics_InformationView.setLogisticsDataList(logisticsDataList);

        logistics_InformationView.setOnPhoneClickListener(new LogisticsInformationView.OnPhoneClickListener() {
            @Override
            public void onPhoneClick(String phoneNumber) {
                dialogCreateCall(phoneNumber).show();
            }
        });


        initPermissionChecker();
    }

    /**
     * 当系统为6.0以上都要手动配置权限
     * 如果想避免手动配置权限，在gradle中配置targetSdkVersion 23以下即可，不包括23
     * */
    private void initPermissionChecker() {
        if (Build.VERSION.SDK_INT >= 23) {

            //判断有没有拨打电话权限
            if (PermissionChecker.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                //请求拨打电话权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

            }

        }
    }

    /**
     * 请求权限的回调方法
     * @param requestCode    请求码
     * @param permissions    请求的权限
     * @param grantResults   权限的结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE && PermissionChecker.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            //开启成功
        }else{
            Toast.makeText(MainActivity.this,"拨打电话授权失败，请在设置中手动开启",Toast.LENGTH_LONG);
        }
    }


    private CallPhoneDialog dialogCreateCall(String phoneNumber) {
        if (callPhoneDialog == null) {
            callPhoneDialog = new CallPhoneDialog(this,phoneNumber);
        }else{
            callPhoneDialog.setPhoneNumber(phoneNumber);
        }

        return callPhoneDialog;
    }
}
