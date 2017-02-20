package com.sjxz.logistics_information;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
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

    /**
     * 绘制界面参数
     * */
    List<LogisticsData> logisticsDataList;

    List<Integer> heightList;//获取每一条文本所占的高度，这里默认时间就占一行

    int heightTotal=0;//获取总高度，用于绘制当前界面所占的高

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

        canvas.drawRect(left, top, width + left, heightTotal + top, paint);//首先确认画布的宽高
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
                StaticLayout layout = new StaticLayout(((LogisticsData)data.get(i)).getContext()+"", textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                canvas.save();//一定要save之后才可以调用Canvas的平移、放缩、旋转、错切、裁剪等操作。避免影响后续的绘制
                canvas.translate(left * 2 + radius * 2 + 10, 0);
                layout.draw(canvas);
                canvas.restore();//操作完当前绘制的图像后要重置之前平移等操作，避免影响
                //画圆
                canvas.drawCircle(width / 2 + left,top+5, radius + 2, mPaint);
                mPaint.setStyle(Paint.Style.STROKE);//设置为空心
                mPaint.setStrokeWidth(8);//空心宽度
                mPaint.setAlpha(88);
                canvas.drawCircle(width / 2 + left,top+5, radius + 4, mPaint);
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
                StaticLayout layout = new StaticLayout(((LogisticsData)data.get(i)).getContext()+"", textPaint, (int) (windowWidth * 0.8), Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
                canvas.save();//很重要，不然会样式出错
                canvas.translate(left * 2 + radius * 2 + 10, heightData +top);
                layout.draw(canvas);
                canvas.restore();//重置


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


}
