package com.example.a14178.xhviewpager;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;


import java.util.ArrayList;

/**
 * XINHAO_HAN
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *                 特此注意,本Demo不完整,数据入口与View入口未写,如果需要集成到你当前项目,需谨慎
 *
 *                 已知问题:缩放比例,随着Item越多缩放程度越小(后期会修复该问题)如果哥们你想练练手,那么这个代码对你来说最合适不过了,暂无其它缺陷
 *
 *                 下方有个存RelativeLayout的ArrayList对于真实开发项目当中是不需要的,但是在这里为了能好理解一点所以我写了一个多余的View集合,本是想让View在
 *
 *                 能看得见的地方操作,你也可以用  getChildAt() 配合      getChildCount() 来使用,这样占内存更小哟~~
 *
 *                 简书地址:https://www.jianshu.com/p/dd708aa444a0
 *
 *
 *
 *
 *
 */

public class XHViewPager extends FrameLayout {

    private Context context;
    //先初始化一个备用的LayoutView文件以便于后期添加View
    private ArrayList<RelativeLayout> arrayList;

    //View的左右边
    private int viewL;
    private int viewR;

    //View的高/宽度
    private int vH;
    private int vW;
    private int startX;
    private int endX;
    //滚动器
    private Scroller scroller;


    public XHViewPager(Context context) {
        super(context);
        initView(context);


    }

    public XHViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public XHViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    //初始化

    private void initView(Context context) {
        this.context = context;
        arrayList = new ArrayList<>();
        scroller = new Scroller(context);
        setBackgroundResource(R.drawable.hxh);
        //先添加5个
        for (int i = 0; i < 5; i++) {

            RelativeLayout rl = new RelativeLayout(context);
            if (i % 2 == 0) {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.timg);
                imageView.setLayoutParams(layoutParams);
                rl.addView(imageView);
            } else {
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                ImageView imageView = new ImageView(context);
                imageView.setImageResource(R.drawable.timg);
                imageView.setLayoutParams(layoutParams);
                rl.addView(imageView);
            }


            arrayList.add(rl);

            addView(rl);

        }


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        vH = h;
        vW = w;
        viewL = 0;
        viewR = vW;
        vW = vW - 300;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //开始测量
        for (int i = 0; i < arrayList.size(); i++) {


            viewL = (((vW * i) + vW) - vW) + 50;
            viewR = ((vW * i) + vW) - 50;

            arrayList.get(i).layout(viewL, 50, viewR, vH - 50);
            if (i != 0) {
                arrayList.get(i).setScaleY((float) 0.8);

            }


        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                endX = (int) event.getX();

                int mid = endX - startX;
                scrollBy(-mid, 0);

                sc();
                moveError();
                Log.e("触摸", "onTouchEvent: " + -mid);
                startX = endX;
                break;
            case MotionEvent.ACTION_UP:
                number();
                break;

        }


        return true;
    }


    //计算缩放
    private void sc() {

        //View的宽
        int w = viewR - viewL;

        int scrollX = getScrollX();

        //位置
        int r = scrollX / (w);


        scrollX = scrollX - (r * w);


        float sc = (float) scrollX / w;

        float v = (float) (sc * 0.2);

        Log.e("缩放至", "sc: " + (sc) + "   " + (r + 1));

        try {
            arrayList.get(r + 1).setScaleY((float) (v + 0.8));
        } catch (Exception e) {

        }


    }

    //计算数值回滚
    private void number() {

        int scrollX = getScrollX();

        //右边
        int r = scrollX / (viewR - viewL);

        //中间值
        int mid = (vW) / 2;

        //当前的View左边
        int viewLift = (r * vW);
        //当前View的右边
        int viewRight = (r * vW) + (vW);

        //当前View的中间值
        int viewMid = (r * vW) + mid;

        //小于中间值滑向左边
        if (scrollX < viewMid) {
            scrollIndex(scrollX, viewLift);
        }

        //大于中间值滑向右边
        if (scrollX > viewMid) {
            scrollIndex(scrollX, viewRight);

        }


    }

    //滑动
    private void scrollIndex(int start, int end) {

        int i = end - start;
        scroller.startScroll(start, 0, i, 0, 500);
    }


    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());

            sc();


            //
        }
        invalidate();

    }

    //边界判断
    private void moveError() {

        float scaleX = getScrollX();
        if (scaleX < 0) {
            scrollTo(0, 0);
        }

        if (scaleX > viewL) {
            scrollTo(viewL, 0);
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
