package com.sjxz.logistics_information;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author WYH_Healer
 * @email 3425934925@qq.com
 * Created by xz on 2017/2/20.
 * Role:自定义的物流信息界面
 */
public class LogisticsInformationView extends View {

    Context context;

    /**
     *  绘制工具：画笔
     * */

    Paint paint;
    Paint mPaintPhone;
    TextPaint textPaintPhone;

    /**
     * 绘制参数
     * */

    int interval;//间隔：本文中指代物流信息和物流时间的间隔

    float radius;//圆形半径

    int left = 20;//距离左边边距
    int top = 20;//距离顶部边距（相当于XML中的Margin或者Padding效果）

    int windowWidth;//获取屏幕的宽高，避免定义数据过长超出屏幕
    int windowHeight;

    float width;

    String phoneNumber;//电话号码
    int phoneNumberWidth;//11位电话号码宽度是固定的
    int phoneNumberHeight;//11位电话号码高度是固定的

    /**
     * 绘制界面参数
     * */
    List<LogisticsData> logisticsDataList;

    List<Integer> heightList;//获取每一条文本所占的高度，这里默认时间就占一行

    int heightTotal=0;//获取总高度，用于绘制当前界面所占的高

    /**
     * 使用Map记录存在电话号码的坐标用于点击事件的存储
     * 起始坐标以及截止坐标的记录
     * */
    HashMap<Float,String> phoneYNumber= new HashMap<Float,String>();
    HashMap<Float,Float> stopYX= new HashMap<Float,Float>();
    List<Float> stopYList=new ArrayList<>();//用于快速取出初始化的值

    public LogisticsInformationView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public LogisticsInformationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LogisticsInformationView);
        width = typedArray.getDimension(R.styleable.LogisticsInformationView_width, 5);//获取XML中设置的高度（圆形半径）
        radius = typedArray.getDimension(R.styleable.LogisticsInformationView_radius, 10);
        //程序在运行时维护了一个 TypedArray的池，程序调用时，会向该池中请求一个实例
        //用完之后，调用 recycle() 方法来释放该实例，从而使其可被其他模块复用。所以一定要调用

        typedArray.recycle();
        init();
    }

    public LogisticsInformationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    /**
     * 初始化相关参数
     * */
    private void init() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.normalColor));

        mPaintPhone = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintPhone.setColor(getResources().getColor(R.color.colorPrimaryDark));
        mPaintPhone.setTextSize(35.0F);

        textPaintPhone= new TextPaint();
        textPaintPhone.setColor(getResources().getColor(R.color.colorPrimaryDark));
        textPaintPhone.setTextSize(35.0F);
        textPaintPhone.setAntiAlias(true);

        interval = dip2px(context,24);

        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        windowWidth = wm.getDefaultDisplay().getWidth();
        windowHeight = wm.getDefaultDisplay().getHeight();


    }


    /**
     *传递相关物流信息对象，这里的对象可以更改为你的自定义的对象
     * */
    public void setLogisticsDataList( List<LogisticsData> logisticsDataList){
        this.logisticsDataList=logisticsDataList;

        heightList=new ArrayList<>();
        TextPaint textPaint=new TextPaint();
        textPaint.setTextSize(35.0F);

        //获取模拟电话宽度数据
        StaticLayout phoneLayout = new StaticLayout("15555555555", textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        phoneNumberWidth= (int) phoneLayout.getLineWidth(0);
        phoneNumberHeight=phoneLayout.getHeight();
        //计算每行字符所占的高度
        for(int i=0;i<logisticsDataList.size();i++){
            StaticLayout layout = new StaticLayout((logisticsDataList.get(i)).getContext()+"", textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            heightList.add(layout.getHeight());
            heightTotal=heightTotal+layout.getHeight()+interval+(i==1?top:top*2);//获取总共高度
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (logisticsDataList == null || logisticsDataList.size() == 0)
            return;

        List data = logisticsDataList;
        canvas.drawRect(left, top, width + left, heightTotal + top, paint);//首先确认画布的高度绘制矩形（就是贯穿圆心的连接直线,画笔默认是fill模式）
        for (int i = 0; i < logisticsDataList.size(); i++) {

            if (i == 0) {
                Paint mPaint = new Paint();
                mPaint.setAntiAlias(true);
                mPaint.setColor(getResources().getColor(R.color.checkColor));
                //画文字
                mPaint.setTextSize(30);
                canvas.drawText(((LogisticsData)data.get(i)).getTime()+"", left * 2 + radius * 2 + 10, heightList.get(i) + interval  , mPaint);
                //文字换行
                TextPaint textPaint = new TextPaint();
                textPaint.setColor(getResources().getColor(R.color.checkColor));
                textPaint.setTextSize(35.0F);
                textPaint.setAntiAlias(true);
                //自动换行的textview超出边界dwidth*0.8就换行
//                StaticLayout layout = new StaticLayout(((LogisticsData)data.get(i)).getContext()+"", textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//                LogUtils.i("获取最后一行占的宽度"+layout.getLineWidth(layout.getLineCount()-1));


                //画圆
                canvas.drawCircle(width / 2 + left,top+5, radius + 2, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);//设置为空心
                mPaint.setStrokeWidth(8);//空心宽度
                mPaint.setAlpha(88);
                canvas.drawCircle(width / 2 + left,top+5, radius + 4, mPaint);


                /**
                 * 分割带电话的数据
                 * */
                String[] splitData=splitString(((LogisticsData)data.get(i)).getContext()+"");
                if(splitData!=null){
                    //说明有电话号码
                    splitPhoneData(splitData,canvas,textPaint,0,true);
                }else{
                    //说明没有电话号码，直接用StaticLayout绘制文本
                    StaticLayout layout = new StaticLayout(((LogisticsData)data.get(i)).getContext()+"", textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                    canvas.save();//一定要save之后才可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。避免影响后续的绘制
                    canvas.translate(left * 2 + radius * 2 + 10, 0);
                    layout.draw(canvas);
                    canvas.restore();//操作完当前绘制的图像后要重置之前平移等操作，避免影响
                }
            } else {
                int heightData=0;
                for(int j=0;j<i;j++){
                    heightData=heightData+heightList.get(j)+interval+(j==0?top:top*2);
                }
                paint.setColor(getResources().getColor(R.color.normalColor));
                canvas.drawCircle(width / 2 + left, heightData+44, radius, paint);
                canvas.drawLine(left * 2 + radius * 2, heightData , windowWidth,heightData , paint); //画线
               //画文字
                paint.setTextSize(30);
                canvas.drawText(((LogisticsData)data.get(i)).getTime()+"", left * 2 + radius * 2 + 10,interval+heightList.get(i)+ heightData+top, paint);
                //文字换行
                TextPaint textPaint = new TextPaint();
                textPaint.setColor(getResources().getColor(R.color.normalColor));
                textPaint.setTextSize(35.0F);
                textPaint.setAntiAlias(true);


                /**
                 * 分割带电话的数据
                 * */
                String[] splitData=splitString(((LogisticsData)data.get(i)).getContext()+"");
                if(splitData!=null){
                    //说明有电话号码
                    splitPhoneData(splitData,canvas,textPaint,heightData,false);
                }else{
                    //说明没有电话号码，直接用StaticLayout绘制文本
                    StaticLayout layout = new StaticLayout(((LogisticsData)data.get(i)).getContext()+"", textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                    canvas.save();//很重要，不然会样式出错
                    canvas.translate(left * 2 + radius * 2 + 10, heightData +top);
                    layout.draw(canvas);
                    canvas.restore();//重置
                }

            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //当没有数据的时候截断绘制过程，当set数据进来的时候还是会走这里绘制图形
        if (logisticsDataList == null || logisticsDataList.size() == 0)
            return;
        //这里绘制所需的宽高
        setMeasuredDimension(widthMeasureSpec, heightTotal+ top);
    }



    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public String[] splitString(String data){
        String initData=getPhoneNumber(data);
        phoneNumber=initData;
        if(initData!=null){
            //说明有电话号码，进行数据分割,默认每一段content中最多包含一个手机号码
            String[] splitData=data.split(initData);
            return splitData;
        }
        //没有电话号码就返回null
        return null;
    }


    /**
     *  获取字符串中是否包含11位的手机号码
     * */
    public String getPhoneNumber(String numer)
    {
        char[] temp = numer.toCharArray();
        String value="";
        int licz=0;

        for(int i=0;i<temp.length;i++)
        {
            if(licz<11)
            {
                if(Character.toString(temp[i]).matches("[0-9]"))
                {
                    value+=Character.toString(temp[i]);
                    licz++;
                }
                else if(Character.toString(temp[i]).matches("\u0020|\\-|\\(|\\)"))
                {

                }
                else
                {
                    value="";
                    licz=0;
                }
            }
        }

        if(value.length()!=11)
        {
            value=null;
        }
        else
        {
            value=value.trim();
        }
        LogUtils.i("手机号码="+value);
        return value;
    }

    /**
     * 逻辑操作电话号码变色可点
     * */
    public void splitPhoneData(String[] splitData ,Canvas canvas,TextPaint textPaint,int heightData,boolean isTop){
        StaticLayout layoutPhone = new StaticLayout(phoneNumber, textPaintPhone, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);

        //1.首先绘制电话前段的数据
        StaticLayout layout = new StaticLayout(splitData[0], textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
        int layoutFirst=layout.getHeight();
        canvas.save();//很重要，不然会样式出错
        canvas.translate(left * 2 + radius * 2 + 10, heightData +(isTop?0:top));
        layout.draw(canvas);
        canvas.restore();//重置

        //判断截取端是多少
        if(splitData.length<=1){
            //没有后续
            //2.判断是剩下的宽度是否能够容纳手机号码宽度
            if((int) (windowWidth * 0.8)-layout.getLineWidth(layout.getLineCount()-1)>phoneNumberWidth){
                phoneYNumber.put((float) ( heightData+(isTop?-10:10)+layoutPhone.getHeight()),phoneNumber);
                stopYX.put((float) ( heightData+(isTop?-10:10)+layoutPhone.getHeight()),left * 2 + radius * 2 + 10+(layout.getLineWidth(layout.getLineCount()-1))+phoneNumberWidth);
                stopYList.add((float) ( heightData+(isTop?-10:10)+layoutPhone.getHeight()));
                mPaintPhone.setColor(getResources().getColor(R.color.colorPrimaryDark));
                canvas.drawText(phoneNumber, left * 2 + radius * 2 + 10+(layout.getLineWidth(layout.getLineCount()-1)), layoutFirst+ heightData+(isTop?-10:10), mPaintPhone);
            }else{
                phoneYNumber.put((float) (layoutFirst+heightData +(isTop?0:top)+layoutPhone.getHeight()),phoneNumber);
                stopYX.put((float) (layoutFirst+heightData +(isTop?0:top)+layoutPhone.getHeight()),left * 2 + radius * 2 + 10+phoneNumberWidth);
                stopYList.add((float) (layoutFirst+heightData +(isTop?0:top)+layoutPhone.getHeight()));
                //5.1获取之前的高度
                canvas.save();//很重要，不然会样式出错
                canvas.translate(left * 2 + radius * 2 + 10, layoutFirst+heightData +(isTop?0:top));
                layoutPhone.draw(canvas);
                canvas.restore();//重置
            }
        }else{
            //有后续
        //获取最后一段的宽度
        StaticLayout layoutLast = new StaticLayout(splitData[1], textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);

        //2.判断是剩下的宽度是否能够容纳手机号码宽度
        if((int) (windowWidth * 0.8)-layout.getLineWidth(layout.getLineCount()-1)>phoneNumberWidth){
            //3.1.如果是可以容纳的情况
            //记录Map坐标轴数据
            phoneYNumber.put((float) ( heightData+(isTop?-10:10)+layoutPhone.getHeight()),phoneNumber);
            stopYX.put((float) ( heightData+(isTop?-10:10)+layoutPhone.getHeight()),left * 2 + radius * 2 + 10+(layout.getLineWidth(layout.getLineCount()-1))+phoneNumberWidth);
            stopYList.add((float) ( heightData+(isTop?-10:10)+layoutPhone.getHeight()));
            mPaintPhone.setColor(getResources().getColor(R.color.colorPrimaryDark));
            canvas.drawText(phoneNumber, left * 2 + radius * 2 + 10+(layout.getLineWidth(layout.getLineCount()-1)), layoutFirst+ heightData+(isTop?-10:10), mPaintPhone);
            if((int) (windowWidth * 0.8)-layout.getLineWidth(layout.getLineCount()-1)-phoneNumberWidth>layoutLast.getLineWidth(0)){
                //4.1.如果一行就可以绘制完成
                mPaintPhone.setColor(getResources().getColor(isTop?R.color.checkColor:R.color.normalColor));
                canvas.drawText(splitData[1], left * 2 + radius * 2 + 10+(layout.getLineWidth(layout.getLineCount()-1)+phoneNumberWidth),layoutFirst+ heightData+(isTop?-10:10), mPaintPhone);
            }else{
                //4.2.一行不可以完成的情况下继续分割成两份，一份是drawText拼接到最后，一份是StaticLayout另起一行绘制
                double percentLast=(((int) (windowWidth * 0.8)-layout.getLineWidth(layout.getLineCount()-1)-phoneNumberWidth));
                //获取字符能显示的最大Length
                double maxLength=percentLast/35.0F;//这里的35.0是一个中文字体的大小
                String lastStringPre=splitData[1].substring(0, (int) maxLength+1);//获取最大数据的长度的字符串拼接
                String lastStringLas=splitData[1].substring((int) maxLength+1);
                mPaintPhone.setColor(getResources().getColor(isTop?R.color.checkColor:R.color.normalColor));
                canvas.drawText(lastStringPre, left * 2 + radius * 2 + 10+(layout.getLineWidth(layout.getLineCount()-1)+phoneNumberWidth),layoutFirst+ heightData+(isTop?-10:10), mPaintPhone);

                //另起一行写剩余的数据

                StaticLayout layoutlastString = new StaticLayout(lastStringLas, textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                canvas.save();//很重要，不然会样式出错
                canvas.translate(left * 2 + radius * 2 + 10,layoutFirst +heightData +(isTop?0:top));
                layoutlastString.draw(canvas);
                canvas.restore();//重置
            }
        }else{
            //3.2.如果是不可以容纳的情况,现在默认电话号码不可以容纳的情况就另起一行使用StaticLayout绘制
            phoneYNumber.put((float) (layoutFirst+heightData +(isTop?0:top)+layoutPhone.getHeight()),phoneNumber);
            stopYX.put((float) (layoutFirst+heightData +(isTop?0:top)+layoutPhone.getHeight()),left * 2 + radius * 2 + 10+phoneNumberWidth);
            stopYList.add((float) (layoutFirst+heightData +(isTop?0:top)+layoutPhone.getHeight()));
            //5.1获取之前的高度
            canvas.save();//很重要，不然会样式出错
            canvas.translate(left * 2 + radius * 2 + 10, layoutFirst+heightData +(isTop?0:top));
            layoutPhone.draw(canvas);
            canvas.restore();//重置
            if((int) (windowWidth * 0.8)-phoneNumberWidth>layoutLast.getLineWidth(0)){
                //4.1.如果一行就可以绘制完成
                mPaintPhone.setColor(getResources().getColor(isTop?R.color.checkColor:R.color.normalColor));
                canvas.drawText(splitData[1], left * 2 + radius * 2 + 10+phoneNumberWidth, layoutPhone.getHeight()+layoutFirst+heightData+(isTop?-10:10), mPaintPhone);
            }else{
                //4.2.一行不可以完成的情况下继续分割成两份，一份是drawText拼接到最后，一份是StaticLayout另起一行绘制
                double percentLast=(int) (windowWidth * 0.8)-phoneNumberWidth;
                //获取字符能显示的最大Length
                double maxLength=percentLast/35.0F;//这里的35.0是一个中文字体的大小

                String lastStringPre=splitData[1].substring(0, (int) maxLength+1);//获取最大数据的长度的字符串拼接
                String lastStringLas=splitData[1].substring((int) maxLength+1);
                mPaintPhone.setColor(getResources().getColor(isTop?R.color.checkColor:R.color.normalColor));
                canvas.drawText(lastStringPre, left * 2 + radius * 2 + 10+phoneNumberWidth,layoutPhone.getHeight()+layoutFirst+ heightData+(isTop?-10:10), mPaintPhone);

                //另起一行写剩余的数据

                StaticLayout layoutlastString = new StaticLayout(lastStringLas, textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                canvas.save();//很重要，不然会样式出错
                canvas.translate(left * 2 + radius * 2 + 10,layoutPhone.getHeight()+layoutFirst +heightData +(isTop?0:top));
                layoutlastString.draw(canvas);
                canvas.restore();//重置
            }
        }

        }
    }


    /**
     * 实现电话号码的点击事件
     * */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //按下(这里默认点击一下就弹出Dialog样式)
                float x = event.getX();
                float y = event.getY();
                //判断点击的范围类型
                for(int i=0;i<stopYList.size();i++){
                    if(y<stopYList.get(i)&&(stopYList.get(i)-y)<=phoneNumberHeight
                            &&x<stopYX.get(stopYList.get(i))&&(stopYX.get(stopYList.get(i))-x)<=phoneNumberWidth){
                        //成立,获取X轴的相关信息
                        if(mOnPhoneClickListener != null) {
                            mOnPhoneClickListener.onPhoneClick(phoneYNumber.get(stopYList.get(i)));
                        }

                        break;
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                //抬起
                break;
            case MotionEvent.ACTION_MOVE:
                //取消
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }


        return super.onTouchEvent(event);
    }

    /**
     * 提供接口
     * */
    private OnPhoneClickListener mOnPhoneClickListener;

    public interface OnPhoneClickListener{
        void onPhoneClick(String phoneNumber);
    }
    public void setOnPhoneClickListener(OnPhoneClickListener l) {
        mOnPhoneClickListener = l;
    }

}
