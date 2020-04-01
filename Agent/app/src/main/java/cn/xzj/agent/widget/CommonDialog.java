package cn.xzj.agent.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.xzj.agent.R;

/**
 * 通用对话框
 */

public class CommonDialog extends Dialog implements View.OnClickListener {


    private View mContentView;
    private Context mContext;
    private CharSequence content;
    private String positiveName;
    private String negativeName;
    private String title;
    private boolean isCancelable;
    private int contentGravity;
    private OnClickListener mNegativeButtonOnClickListener;
    private OnClickListener mPositiveButtonOnClickListener;


    private CommonDialog(Builder builder) {
        super(builder.mContext, builder.themeResId);
        this.isCancelable=builder.isCancelable;
        this.mContentView = builder.mContentView;
        this.mContext = builder.mContext;
        this.content = builder.content;
        this.positiveName = builder.positiveName;
        this.negativeName = builder.negativeName;
        this.title = builder.title;
        this.contentGravity=builder.contentGravity;
        this.mPositiveButtonOnClickListener = builder.mPositiveButtonOnClickListener;
        this.mNegativeButtonOnClickListener = builder.mNegativeButtonOnClickListener;

    }

    public <T extends View> T getView(@IdRes int viewId) {
        if (mContentView == null)
            return null;
        return mContentView.findViewById(viewId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_commom);
//        setCanceledOnTouchOutside(false);
        initView();
    }


    @Override
    public void show() {
        super.show();
        setCanceledOnTouchOutside(isCancelable);
        setCancelable(isCancelable);

    }

    public CommonDialog showCenter() {
        show();
        return this;
    }

    /**
     * 设置宽度全屏，要设置在show的后面
     */
    public CommonDialog setBottomShow() {
        if (!isShowing()){
            show();
        }
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getWindow().setWindowAnimations(R.style.popWindowAnim);
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().getDecorView().setBackgroundColor(mContext.getResources().getColor(R.color.white));
        getWindow().setAttributes(layoutParams);
        return this;
    }

    private void initView() {
        TextView contentTxt;
        TextView titleTxt;
        TextView submitTxt;
        TextView cancelTxt;
        LinearLayout mContentViewRoot;
        View viewTopLine, viewBottomLine, viewBottomParent, mViewBtnDivisionLine;

        contentTxt = findViewById(R.id.tv_content);
        mContentViewRoot = findViewById(R.id.ll_content);
        titleTxt = findViewById(R.id.title);
        submitTxt = findViewById(R.id.submit);
        cancelTxt = findViewById(R.id.cancel);
        mViewBtnDivisionLine = findViewById(R.id.btn_division_line);
        viewTopLine = findViewById(R.id.view_top_line);
        viewBottomLine = findViewById(R.id.view_bottom_line);
        viewBottomParent = findViewById(R.id.ll_bottom_parent);

        cancelTxt.setOnClickListener(this);
        submitTxt.setOnClickListener(this);
         if (contentGravity!=0){
             contentTxt.setGravity(contentGravity);
         }
        if (mContentView != null) {
            contentTxt.setVisibility(View.GONE);
            mContentViewRoot.setVisibility(View.VISIBLE);
            mContentViewRoot.removeAllViews();
            mContentViewRoot.addView(mContentView);
        }
        if (!TextUtils.isEmpty(content)) {
            contentTxt.setText(content);
        }
        contentTxt.setText(content);

        if (!TextUtils.isEmpty(positiveName)) {
            submitTxt.setText(positiveName);
        } else {
            submitTxt.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(negativeName)) {
            cancelTxt.setText(negativeName);
            cancelTxt.setVisibility(View.VISIBLE);

        } else {
            cancelTxt.setVisibility(View.GONE);
            mViewBtnDivisionLine.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(title)) {
            titleTxt.setText(title);
        } else {
            titleTxt.setVisibility(View.GONE);
            viewTopLine.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(negativeName) && TextUtils.isEmpty(positiveName)) {
            viewBottomParent.setVisibility(View.GONE);
            viewBottomLine.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel:
                if (mNegativeButtonOnClickListener != null) {
                    mNegativeButtonOnClickListener.onClick(this);
                }
                this.dismiss();
                this.cancel();
                break;
            case R.id.submit:
                if (mPositiveButtonOnClickListener != null) {
                    mPositiveButtonOnClickListener.onClick(this);
                }
                break;
        }
    }

    public interface OnClickListener {
        void onClick(CommonDialog dialog);
    }

    public static Builder newBuilder(Context cxt) {
        return new Builder(cxt);
    }

    public static class Builder {
        private boolean isCancelable;
        private int themeResId;
        private View mContentView;
        private Context mContext;
        private CharSequence content;
        private String positiveName;
        private String negativeName;
        private String title;
        private OnClickListener mNegativeButtonOnClickListener;
        private OnClickListener mPositiveButtonOnClickListener;
        private int contentGravity;

        public Builder(Context context) {
            this.mContext = context;
            this.themeResId = R.style.dialog;
        }

        public Builder setTheme(int themeResId) {
            this.themeResId = themeResId;
            return this;
        }

        public Builder setCancelable(boolean isCancelable) {
            this.isCancelable = isCancelable;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setPositiveButton(String name, OnClickListener mPositiveButtonOnClickListener) {
            this.positiveName = name;
            this.mPositiveButtonOnClickListener = mPositiveButtonOnClickListener;
            return this;
        }

        public Builder setNegativeButton(String name, OnClickListener mNegativeButtonOnClickListener) {
            this.negativeName = name;
            this.mNegativeButtonOnClickListener = mNegativeButtonOnClickListener;
            return this;
        }

        public Builder setMessage(CharSequence content) {
            this.content = content;
            return this;
        }

        public Builder setView(View view) {
            this.mContentView = view;
            return this;
        }

        public Builder setView(@LayoutRes int viewLayout) {
            this.mContentView = LayoutInflater.from(mContext).inflate(viewLayout, null);
            return this;
        }
        public Builder setContentGravity(int gravity){
            this.contentGravity=gravity;
            return this;
        }


        public CommonDialog create() {
            return new CommonDialog(this);
        }
    }

}
