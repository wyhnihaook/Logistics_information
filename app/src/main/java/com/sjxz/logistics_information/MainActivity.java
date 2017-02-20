package com.sjxz.logistics_information;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    LogisticsInformationView logistics_InformationView;

    List<LogisticsData> logisticsDataList;

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
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 11:23:07").setContext("[杭州市]浙江杭州江干区新杭派公司进行签收扫描，快件已被  已签收  签收，感谢使用韵达快递，期待再次为您服务"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 07:23:06").setContext("[杭州市]浙江杭州江干区新杭派公司派件员：吕敬桥  15555555555正在为您派件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 06:48:37").setContext("到达目的地网点浙江杭州江干区新杭派，快件很快进行派送"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:11:37").setContext("[苏州市]江苏苏州分拨中心  已发出"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:08:06").setContext("[苏州市]快件已到达  江苏苏州分拨中心"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 15:52:43").setContext("[泰州市]韵达快递  江苏靖江市公司收件员  已揽件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 12:39:15").setContext("包裹正等待揽件"));
        logisticsDataList.add(new LogisticsData().setTime("2016-6-28 15:13:02").setContext("快件在【相城中转仓】装车,正发往【无锡分拨中心】已签收,签收人是【王漾】,签收网点是【忻州原平】"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 11:23:07").setContext("[杭州市]浙江杭州江干区新杭派公司进行签收扫描，快件已被  已签收  签收，感谢使用韵达快递，期待再次为您服务"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 07:23:06").setContext("[杭州市]浙江杭州江干区新杭派公司派件员：吕敬桥  15555555555正在为您派件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-20 06:48:37").setContext("到达目的地网点浙江杭州江干区新杭派，快件很快进行派送"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:11:37").setContext("[苏州市]江苏苏州分拨中心  已发出"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 23:08:06").setContext("[苏州市]快件已到达  江苏苏州分拨中心"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 15:52:43").setContext("[泰州市]韵达快递  江苏靖江市公司收件员  已揽件"));
        logisticsDataList.add(new LogisticsData().setTime("2017-1-19 12:39:15").setContext("包裹正等待揽件"));


        logistics_InformationView.setLogisticsDataList(logisticsDataList);

    }

}
