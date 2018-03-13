package study.com.circlemenudemo.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import study.com.circlemenudemo.R;

/**
 * 圆形菜单
 */

public class CircleMenuView extends ViewGroup {
    private int width;
    private int height;
    private int min;
    private float radius;
    private Context context;

    public CircleMenuView(Context context) {
        this(context, null);
    }

    public CircleMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

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
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //给child制定测量规则
            int size = (int) (radius / 3);

            TextView tv = (TextView) child.findViewById(R.id.tv);
            tv.setTextSize(size * 0.08f);
            ImageView iv = (ImageView) child.findViewById(R.id.iv);
            LayoutParams ivLayoutParams = iv.getLayoutParams();
            ivLayoutParams.width = (int) (size * 0.7f);
            ivLayoutParams.height = (int) (size * 0.7f);
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
    public void setData(int[] resIds, final String[] texts) {
        for (int i = 0; i < resIds.length; i++) {
            View view = View.inflate(getContext(), R.layout.circle_item, null);
            final String text = texts[i];
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
                }
            });
            ImageView iv = (ImageView) view.findViewById(R.id.iv);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            iv.setImageResource(resIds[i]);
            tv.setText(texts[i]);
            addView(view);
        }
    }


    float downAngle = 0;
    float moveAngle = 0;
    float changeAngle;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        changeAngle = 0;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downAngle = xyToAngle(x - radius, y - radius);
                break;
            case MotionEvent.ACTION_MOVE:
                moveAngle = xyToAngle(x - radius, y - radius);
                changeAngle = (moveAngle - downAngle) % 360;

                startAngle += changeAngle;
                requestLayout();
                downAngle = moveAngle;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        // starAngle = 0;
        return true;
    }

    //转化角度
    public float xyToAngle(float x, float y) {
        float transAngle = (float) Math.toDegrees(Math.atan2(y, x));  //计算得出的值的范围为[-π,π];
        return transAngle >= 0 ? transAngle : transAngle + 360;
    }

//    private float xyToAngle(float downX, float downY) {
//        float originalAngle = 0f;
//        if (downX > 0 && downY < 0) {  //第四象限
//            originalAngle += 360;
//        } else if (downX < 0 && downY < 0) {  //第三象限
//            originalAngle += 180;
//        } else if (downX < 0 && downY > 0) {  //第二象限
//            originalAngle += 180;
//        }
//        originalAngle = (float) (originalAngle + Math.toDegrees(Math.atan(downY / downX))) % 360;
//        return originalAngle;
//    }

}
