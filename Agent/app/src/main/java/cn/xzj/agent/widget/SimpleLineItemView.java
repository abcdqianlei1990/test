package cn.xzj.agent.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xzj.agent.R;


/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/3/1
 * @ Des 普通 list item 封装
 */

public class SimpleLineItemView extends LinearLayout {
    private View mContentView;
    private EditText editContent;
    private TextView tvRightText;
    private Object mRootTag;

    public SimpleLineItemView(Context context) {
        this(context, null, 0);
    }

    public SimpleLineItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleLineItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private SimpleLineItemView(Builder builder) {
        this(builder.mContext, null, 0);
        this.mContentView = builder.mContentView;
        this.editContent = builder.editContent;
        this.tvRightText = builder.tvRightText;
        this.mRootTag = builder.mRootTag;
        addView(mContentView);
        setTag(mRootTag);
    }

    public EditText getEditText() {
        return editContent;
    }

    /**
     * 设置标记
     */
    public SimpleLineItemView setOnlyMark(Object tag) {
        setTag(tag);
        return this;
    }
    public Object getOnlyMark() {
        return mRootTag;
    }

    public TextView getRightTextView() {
        return tvRightText;
    }

    public SimpleLineItemView setRightText(String resId) {
        tvRightText.setText(resId);
        return this;
    }

    public static class Builder {
        /**
         * 上下分割线，默认隐藏上面分割线
         */
        private View dividerTop, dividerBottom;

        /**
         * 最外层容器
         */
        private LinearLayout llRoot, llOnClick;
        /**
         * 最左边的Icon
         */
        private ImageView ivLeftIcon;
        /**
         * 中间的文字内容
         */
        private TextView tvTextContent;

        /**
         * 中间的输入框
         */
        private EditText editContent;

        /**
         * 右边的文字
         */
        private TextView tvRightText;

        /**
         * 右边的icon 通常是箭头
         */
        private ImageView ivRightIcon;
        /**
         * 根tag
         */
        private Object mRootTag;


        private View mContentView;
        private Context mContext;

        public Builder(Context mContext) {
            this.mContext = mContext;
            init();
        }


        private void init() {
            mContentView = LayoutInflater.from(mContext).inflate(R.layout.layout_my_one_line, null);
            llRoot = mContentView.findViewById(R.id.ll_root);
            llOnClick = mContentView.findViewById(R.id.ll_onclick);
            dividerTop = mContentView.findViewById(R.id.divider_top);
            dividerBottom = mContentView.findViewById(R.id.divider_bottom);
            ivLeftIcon = mContentView.findViewById(R.id.iv_left_icon);
            tvTextContent = mContentView.findViewById(R.id.tv_text_content);
            editContent = mContentView.findViewById(R.id.edit_content);
            tvRightText = mContentView.findViewById(R.id.tv_right_text);
            ivRightIcon = mContentView.findViewById(R.id.iv_right_icon);

            dividerTop.setVisibility(GONE);
            ivLeftIcon.setVisibility(GONE);
            tvTextContent.setVisibility(GONE);
            editContent.setVisibility(GONE);
            tvRightText.setVisibility(GONE);
            ivRightIcon.setVisibility(INVISIBLE);

        }

        public Builder content(String content) {
            tvTextContent.setText(content);
            tvTextContent.setVisibility(VISIBLE);
            return this;
        }

        public Builder showDividerTop() {
            dividerTop.setVisibility(VISIBLE);
            return this;
        }

        public Builder showDividerBottom() {
            dividerBottom.setVisibility(VISIBLE);
            return this;
        }

        public Builder leftIcon(int srcId) {
            ivLeftIcon.setImageResource(srcId);
            ivLeftIcon.setVisibility(VISIBLE);
            return this;
        }

        public Builder editHint(String resId) {
            editContent.setHint(resId);
            editContent.setVisibility(VISIBLE);
            return this;
        }

        public Builder value(String resId) {
            editContent.setText(resId);
            editContent.setVisibility(VISIBLE);
            return this;
        }

        public Builder editEnable(boolean isEnable) {
            editContent.setEnabled(isEnable);
            return this;
        }

        public Builder inputType(int type) {
            editContent.setInputType(type);
            return this;
        }

        public Builder rightText(String resId) {
            tvRightText.setText(resId);
            tvRightText.setVisibility(VISIBLE);
            return this;
        }

        public Builder rightArrow(int resId) {
            ivRightIcon.setImageResource(resId);
            return this;
        }

        public Builder showRightArrow() {
            ivRightIcon.setVisibility(VISIBLE);
            return this;
        }

        public Builder onlyMark(Object mRootTag) {
            this.mRootTag = mRootTag;
            return this;
        }
        //----------------------下面是一些点击事件

        public Builder setOnRootClickListener(final OnRootClickListener onRootClickListener, final Object tag) {
            llOnClick.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContentView.setTag(tag);
                    onRootClickListener.onRootClick(mContentView);
                }
            });
            return this;
        }

        public Builder setOnArrowClickListener(final OnArrowClickListener onArrowClickListener, final Object tag) {

            ivRightIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivRightIcon.setTag(tag);
                    onArrowClickListener.onArrowClick(ivRightIcon);
                }
            });
            return this;
        }

        public SimpleLineItemView build() {
            return new SimpleLineItemView(this);
        }


    }

    /**
     * 整个一行被点击
     */
    public interface OnRootClickListener {
        void onRootClick(View view);
    }

    /**
     * 右边箭头的点击事件
     */
    public interface OnArrowClickListener {
        void onArrowClick(View view);
    }


}
