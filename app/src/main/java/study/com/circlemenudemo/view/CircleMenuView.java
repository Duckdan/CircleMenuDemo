package study.com.circlemenudemo.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import study.com.circlemenudemo.R;

/**
 * 圆形菜单
 */

public class CircleMenuView extends ViewGroup {
    private int width;
    private int height;
    private int min;
    private float radius;

    public CircleMenuView(Context context) {
        this(context, null);
    }

    public CircleMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {  //至多
            int minimumWidth = getSuggestedMinimumWidth();  //获取背景的高度
            int minimumHeight = getSuggestedMinimumHeight();
            int suggestedMini = Math.min(minimumWidth, minimumHeight);
            if (suggestedMini == 0) {  //背景为空时
                widthSize = getDefaultSize();
            } else {
                widthSize = Math.min(suggestedMini, getDefaultSize());
            }
        } else {
            int widthPixels = getResources().getDisplayMetrics().widthPixels;
            int heightPixels = getResources().getDisplayMetrics().heightPixels;
            int minPixels = Math.min(widthPixels, heightPixels);
            widthSize = Math.min(minPixels, Math.min(widthSize, heightSize));
        }
        radius = widthSize * 1.0f / 2;
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);
        radius = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //给child制定测量规则
            int size = (int) (radius / 3);

            TextView tv = (TextView) child.findViewById(R.id.tv);
            tv.setTextSize(size * 0.05f);
            ImageView iv = (ImageView) child.findViewById(R.id.iv);
            LayoutParams ivLayoutParams = iv.getLayoutParams();
            ivLayoutParams.width = (int) (size * 0.5f);
            ivLayoutParams.height = (int) (size * 0.5f);
            iv.setLayoutParams(ivLayoutParams);

            int measureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
            //进行子view的测量
            child.measure(measureSpec, measureSpec);
        }
    }

    private int getDefaultSize() {
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        return Math.min(widthPixels, heightPixels);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
        height = h;
        min = Math.min(w, h);
        radius = min * 1.0f / 2;
    }

    private int left;
    private int top;
    private int right;
    private int bottom;
    private float startAngle = 0;
    private float sweepAngle = 0;
    private int tempRadius = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        sweepAngle = 360 * 1.0f / getChildCount();
        tempRadius = (int) (radius * 0.6f);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            left = (int) (radius + tempRadius * Math.cos(Math.toRadians(startAngle)) - childAt.getMeasuredWidth() * 1.0f / 2);
            top = (int) (radius + tempRadius * Math.sin(Math.toRadians(startAngle)) - childAt.getMeasuredWidth() * 1.0f / 2);
            right = (int) (radius + tempRadius * Math.cos(Math.toRadians(startAngle)) + childAt.getMeasuredWidth() * 1.0f / 2);
            bottom = (int) (radius + tempRadius * Math.sin(Math.toRadians(startAngle)) + childAt.getMeasuredWidth() * 1.0f / 2);
            childAt.layout(left, top, right, bottom);
            startAngle += sweepAngle;
        }
    }

    /**
     * 设置数据
     *
     * @param texts
     * @param resIds
     */
    public void setData(int[] resIds, String[] texts) {
        for (int i = 0; i < resIds.length; i++) {
            View view = View.inflate(getContext(), R.layout.circle_item, null);
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            iv.setImageResource(resIds[i]);
            tv.setText(texts[i]);

            addView(view);
        }
    }
}
