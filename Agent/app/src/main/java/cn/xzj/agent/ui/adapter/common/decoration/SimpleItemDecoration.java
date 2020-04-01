package cn.xzj.agent.ui.adapter.common.decoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import java.util.HashMap;
import java.util.Map;
import cn.xzj.agent.R;

/**
 * @Author yemao
 * @Email yrmao9893@163.com
 * @Date 2017/11/26
 * @Des RecyclerView item 装饰器 !
 */

public class SimpleItemDecoration extends RecyclerView.ItemDecoration {
    private Context mContext;
    private Map<Integer, String> headerData = new HashMap<>();//头数据

    private Paint dividerPaint;//分割线画笔
    private Paint headerPaint;//头部画笔
    private Paint headerTextPaint;//头文字的画笔

    private int dividerHeight;//分割线高度
    private int headerHeight;//头的高度
    private float headerTextHeight;//头文字高度
    private float headerTextSize;

    private int dividerColor;
    private int headerTextColor;
    private int headerBackgroundColor;
    private boolean isDrawTopFirstLine;//是否绘制第一行上方的线

    private float headerTextMarginLeftHeight;//头文字对左边的距离

    private SimpleItemDecoration(Builder builder) {
        this.mContext = builder.mContext;
        this.headerData = builder.headerData;
        this.dividerPaint = builder.dividerPaint;
        this.headerPaint = builder.headerPaint;
        this.headerTextPaint = builder.headerTextPaint;
        this.dividerHeight = builder.dividerHeight;
        this.headerHeight = builder.headerHeight;
        this.headerTextHeight = builder.headerTextHeight;
        this.headerTextSize = builder.headerTextSize;
        this.dividerColor = builder.dividerColor;
        this.headerTextColor = builder.headerTextColor;
        this.headerBackgroundColor = builder.headerBackgroundColor;
        this.headerTextMarginLeftHeight = builder.headerTextMarginLeftHeight;
        this.isDrawTopFirstLine = builder.isDrawTopFirstLine;
    }

    public static Builder builder(Context context) {
        return new Builder(context);
    }

    public final static class Builder {
        private Context mContext;
        private Map<Integer, String> headerData = new HashMap<>();//头数据

        private Paint dividerPaint;//分割线画笔
        private Paint headerPaint;//头部画笔
        private Paint headerTextPaint;//头文字的画笔

        private int dividerHeight;//分割线高度
        private int headerHeight;//头的高度
        private float headerTextHeight;//头文字高度
        private float headerTextSize;

        private int dividerColor;
        private int headerTextColor;
        private int headerBackgroundColor;

        private float headerTextMarginLeftHeight;//头文字对左边的距离
        private boolean isDrawTopFirstLine;

        public Builder(Context context) {
            this.mContext = context;
            dividerPaint = new Paint();
            headerTextPaint = new Paint();
            headerPaint = new Paint();
            headerTextPaint.setAntiAlias(true);

            dividerColor = context.getResources().getColor(R.color.dividerColor);
            headerTextColor = context.getResources().getColor(R.color.dividerHeaderTextColor);
            headerBackgroundColor = context.getResources().getColor(R.color.dividerHeaderColor);

            dividerPaint.setColor(dividerColor);
            headerPaint.setColor(headerBackgroundColor);
            headerTextPaint.setColor(headerTextColor);

            dividerHeight = 1;
            headerHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, mContext.getResources().getDisplayMetrics());

            headerTextMarginLeftHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, mContext.getResources().getDisplayMetrics());
            Paint.FontMetrics fm = headerTextPaint.getFontMetrics();
            headerTextHeight = fm.bottom - fm.top;

            headerTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, mContext.getResources().getDisplayMetrics());
            headerTextPaint.setTextSize(headerTextSize);
            isDrawTopFirstLine = true;

        }

        public Builder headerPaint(Paint headerPaint) {
            this.headerPaint = headerPaint;
            return this;
        }

        public Builder dividerPaint(Paint dividerPaint) {
            this.dividerPaint = dividerPaint;
            return this;
        }

        public Builder headerTextPaint(Paint headerTextPaint) {
            this.headerTextPaint = headerTextPaint;
            return this;
        }

        public Builder dividerHeight(int dividerHeight) {
            this.dividerHeight = (int) applyDimension(mContext, TypedValue.COMPLEX_UNIT_DIP, dividerHeight);
            ;
            return this;
        }

        public Builder headerHeight(int headerHeight) {
            this.headerHeight = (int) applyDimension(mContext, TypedValue.COMPLEX_UNIT_DIP, headerHeight);
            return this;
        }

        public Builder dividerColor(int dividerColor) {
            this.dividerColor = dividerColor;
            dividerPaint.setColor(dividerColor);
            return this;
        }

        public Builder headerBackgroundColor(int headerBackgroundColor) {
            this.headerBackgroundColor = headerBackgroundColor;
            this.headerPaint.setColor(headerBackgroundColor);
            return this;
        }

        public Builder headerTextColor(int headerTextColor) {
            this.headerTextColor = headerTextColor;
            this.headerTextPaint.setColor(headerTextColor);
            return this;
        }

        public Builder headerData(Map<Integer, String> headerData) {
            this.headerData = headerData;
            return this;
        }

        public Builder headerTextMarginLeftHeight(int headerTextMarginLeftHeight) {
            this.headerTextMarginLeftHeight = applyDimension(mContext, TypedValue.COMPLEX_UNIT_DIP, headerTextMarginLeftHeight);
            return this;
        }

        public Builder isDrawTopFirstLine(boolean isDrawTopFirstLine) {
            this.isDrawTopFirstLine = isDrawTopFirstLine;
            return this;
        }

        public SimpleItemDecoration build() {
            return new SimpleItemDecoration(this);
        }
    }


    public SimpleItemDecoration setHeaderData(Map<Integer, String> headerData) {
        this.headerData = headerData;
        return this;
    }

    /**
     * 判断是否为头部
     *
     * @param position
     * @return
     */
    private boolean isHeader(int position) {
        return headerData.containsKey(position);
    }

    private String getHeaderItem(int position) {
        return headerData.get(position);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildViewHolder(view).getAdapterPosition();
        int top = 0;
        if (position == 0) {
            if (isHeader(position)) {
                if (isDrawTopFirstLine) {
                    top = headerHeight + dividerHeight * 2;
                } else {
                    top = headerHeight + dividerHeight;
                }

            } else {
                if (isDrawTopFirstLine) {
                    top = dividerHeight;
                }
            }
        } else {
            if (isHeader(position)) {
                top = headerHeight + dividerHeight;
            }
        }
        outRect.set(0, top, 0, dividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            float top = view.getTop();
            float bottom = view.getBottom() + dividerHeight;
            if (isHeader(i)) {

                if (i == 0 && isDrawTopFirstLine) {
                    //绘制上方分割线
                    c.drawRect(left, top - headerHeight - 2*dividerHeight, right, top - headerHeight - dividerHeight, dividerPaint);
                }
                //1. 绘制矩形2.绘制文字
                c.drawRect(left, top - headerHeight - dividerHeight, right, top, headerPaint);

                float y = headerHeight / 2 + headerTextHeight;
                if (!TextUtils.isEmpty(getHeaderItem(i))) {
                    c.drawText(getHeaderItem(i), headerTextMarginLeftHeight, y, headerTextPaint);
                }
                //绘制头部下边分割线
                c.drawRect(left, top - dividerHeight, right, top, dividerPaint);

            }
            if (i == 0 && isDrawTopFirstLine) {
                c.drawRect(left, top - dividerHeight, right, top, dividerPaint);
                c.drawRect(left, top, right, bottom, dividerPaint);
            } else {
                c.drawRect(left, top, right, bottom, dividerPaint);
            }


        }
    }


    public static float applyDimension(Context context, int convertType, int value) {
        float convertValue = value;
        switch (convertType) {
            case TypedValue.COMPLEX_UNIT_SP:
                convertValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, context.getResources().getDisplayMetrics());
                break;
            case TypedValue.COMPLEX_UNIT_PX:
                convertValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, value, context.getResources().getDisplayMetrics());
                break;
            case TypedValue.COMPLEX_UNIT_DIP:
                convertValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, context.getResources().getDisplayMetrics());
                break;
        }
        return convertValue;
    }

}
