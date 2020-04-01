package cn.xzj.agent.widget;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import cn.xzj.agent.R;
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/30
 * @Des
 */
public class DropDownSelectMenu {
    private Activity mContext;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private PopupWindow mPopupWindow;

    public DropDownSelectMenu(Activity context, RecyclerView.Adapter mAdapter) {
        this.mContext = context;
        this.mAdapter = mAdapter;
        init();
    }

    private void init() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.layout_drop_down_select_menu, null);
        mRecyclerView = mView.findViewById(R.id.recyclerViewDropDownSelectMenu);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(SimpleItemDecoration.builder(mContext).build());
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.update();
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPopupWindow.dismiss();
            }
        });
    }

    public void show(View attachView) {
        if (Build.VERSION.SDK_INT >= 24) {
            //解决弹窗的位置问题
            Rect visibleFrame = new Rect();
            attachView.getGlobalVisibleRect(visibleFrame);
            int height = attachView.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
            mPopupWindow.setHeight(height);
            mPopupWindow.showAsDropDown(attachView, 0, 0);
        } else {
            mPopupWindow.showAsDropDown(attachView);
        }
    }
    public void dismiss(){
        mPopupWindow.dismiss();
    }
    public PopupWindow getPopupWindow(){
        return mPopupWindow;
    }

}
