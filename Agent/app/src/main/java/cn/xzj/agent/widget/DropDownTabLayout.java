package cn.xzj.agent.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.xzj.agent.R;
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration;
import cn.xzj.agent.util.DisplayUtil;

/**
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/16
 * @ Des 多条件菜单
 */
public class DropDownTabLayout extends LinearLayout {
    private OnItemOnClickListener mOnItemOnClickListener;
    private SparseArray<View> mItemViews;
    private boolean isShowing;
    private int currentShowPosition = -1;
    private int sumItemSize = 0;
    private RecyclerView mRecyclerView;

    public DropDownTabLayout(Context context) {
        super(context);
        init();
    }

    public DropDownTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DropDownTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mItemViews = new SparseArray<>();
//        initPopupWindow();
    }

    public void setSumItemSize(int sumItemSize) {
        this.sumItemSize = sumItemSize;
    }

    public void addItem(String title) {

        final View mItemView = LayoutInflater.from(getContext()).inflate(R.layout.item_pull_down_tab, null);
        //设置tag为当前 mItemViews index
        mItemView.setTag(mItemViews.size());
        mItemViews.put(mItemViews.size(), mItemView);
        TextView mTv = mItemView.findViewById(R.id.tv_title);
        mTv.setText(title);
        mItemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = (Integer) view.getTag();
                if (!isShowing) {
                    if (mOnItemOnClickListener != null)
                        mOnItemOnClickListener.onItemOnClick(view, position);
                }
                setMenuState(view);
                isShowing = !isShowing;
                if (isShowing) {
                    currentShowPosition = position;
                }


            }
        });
        addView(mItemView, new LinearLayout.LayoutParams(DisplayUtil.getWindowWidth((Activity) getContext()) / sumItemSize, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void initPopupWindow() {
        FrameLayout mFrameLayout = new FrameLayout(getContext());
        mFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRecyclerView = new RecyclerView(getContext());
        mRecyclerView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SimpleItemDecoration.Builder(getContext())
                .build());
        mFrameLayout.addView(mRecyclerView);

        PopupWindow mPopupWindow = new PopupWindow(mFrameLayout, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(null);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        mPopupWindow.update();
    }

    /**
     * isShowing 是否展开 True为有展开
     */
    public boolean isShowing() {
        return isShowing;
    }

    public int getCurrentShowPosition() {
        return currentShowPosition;
    }

    private void setMenuState(View view) {
        if (!isShowing) {
            TextView mTv = view.findViewById(R.id.tv_title);
            ImageView mIv = view.findViewById(R.id.iv_arrow);
            mTv.setTextColor(getResources().getColor(R.color.green29AC3E));
            mIv.setImageResource(R.mipmap.triangle_up);
        } else {
            for (int i = 0; i < mItemViews.size(); i++) {
                getTextView(i).setTextColor(getResources().getColor(R.color.black333333));
                getImageView(i).setImageResource(R.mipmap.triangle_down);
            }
        }
    }

    public void closeMenu() {
        getTextView(currentShowPosition).setTextColor(getResources().getColor(R.color.black333333));
        getImageView(currentShowPosition).setImageResource(R.mipmap.triangle_down);
        isShowing = false;
    }


    /**
     * 设置item 标题
     *
     * @param title    标题
     * @param position 下标
     */
    public void setItemTitle(String title, int position) {
        getTextView(position).setText(title);
    }

    public void setItemTitle(String title) {
        if (currentShowPosition != -1)
            setItemTitle(title, currentShowPosition);
    }

    public String getItemTitle() {
        if (currentShowPosition != -1) {
            return getItemTitle(currentShowPosition);
        }
        return "";
    }

    public String getItemTitle(int position) {
        return getTextView(position).getText().toString();
    }

    private TextView getTextView(int position) {
        View mItemView = mItemViews.get(position);
        return mItemView.findViewById(R.id.tv_title);
    }

    private ImageView getImageView(int position) {
        View mItemView = mItemViews.get(position);
        return mItemView.findViewById(R.id.iv_arrow);
    }

    public void setOnItemOnClickListener(OnItemOnClickListener itemOnClickListener) {
        this.mOnItemOnClickListener = itemOnClickListener;
    }

   public interface OnItemOnClickListener {
        void onItemOnClick(View view, int position);
    }
}
