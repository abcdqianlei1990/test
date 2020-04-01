package cn.xzj.agent.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

import cn.xzj.agent.R;
import cn.xzj.agent.ui.adapter.common.BaseHolder;
import cn.xzj.agent.ui.adapter.common.QuickAdapter;
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration;

/**
 * @author yemao
 * @ Date 2017/4/10
 * @ Description BottomSheetDialog封装, 公共条目 取消、确定,标题 可有!
 */

public class SimpleBottomSheetDialog {
    private BottomSheetDialog mBottomSheetDialog;
    private View contentView = null;
    private Context mContext;
    private RecyclerView recy_dialog;
    private OnItemClickListener mOnItemClickListener;
    private QuickAdapter<String> mAdapter;

    SimpleBottomSheetDialog(Builder builder) {
        this.contentView = builder.mContentView;
        this.mAdapter = builder.mAdapter;
        this.mBottomSheetDialog = builder.mBottomSheetDialog;
        this.mContext = builder.mContext;
        this.mOnItemClickListener = builder.mOnItemClickListener;
        this.recy_dialog = builder.recy_dialog;
    }
    public static Builder newBuilder(Context cxt){
        return new Builder(cxt);
    }

    public static class Builder {
        private BottomSheetDialog mBottomSheetDialog;
        private View mContentView = null;
        private Context mContext;
        private RecyclerView recy_dialog;
        private OnItemClickListener mOnItemClickListener;
        private QuickAdapter<String> mAdapter;

        public Builder(Context context) {
            init(context);
        }

        private void init(Context cxt) {
            mContext = cxt;
            mBottomSheetDialog = new BottomSheetDialog(cxt);
            mContentView = LayoutInflater.from(mContext).inflate(R.layout.bottom_sheet_dialog_base, null);
            recy_dialog = mContentView.findViewById(R.id.recy_dialog);

            mAdapter = new QuickAdapter<String>(R.layout.item_bottom_dialog_base) {
                @Override
                public void convert(@NotNull BaseHolder holder, String item, int position) {
                    holder.setText(R.id.tv_name, item);
                }


            };
            mAdapter.setOnItemClickListener(new QuickAdapter.OnItemClickListener<String>() {
                @Override
                public void onItemClick(@NonNull View view, String itemData, int i) {
                    mBottomSheetDialog.cancel();
                    if (mOnItemClickListener != null)
                        mOnItemClickListener.onItemOnClick(view, itemData, i);
                }
            });
            recy_dialog.setLayoutManager(new LinearLayoutManager(mContext));
            recy_dialog.setAdapter(mAdapter);
            recy_dialog.addItemDecoration(new SimpleItemDecoration.Builder(mContext).build());

            mContentView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.cancel();
                }
            });
            mBottomSheetDialog.setContentView(mContentView);

        }

        public Builder setData(List<String> list) {
            if (list == null)
                return this;
            mAdapter.setNewData(list);
            return this;
        }

        public Builder setData(String... mData) {
            if (mData != null && mAdapter != null) {
                List<String> tList = Arrays.asList(mData);
                mAdapter.setNewData(tList);

            }
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            mBottomSheetDialog.setCancelable(cancelable);
            return this;
        }

        public void show() {
            builder();
            mBottomSheetDialog.show();
        }

        public Builder setItemClicklistener(OnItemClickListener onItemClicklistener) {
            this.mOnItemClickListener = onItemClicklistener;
            return this;
        }

        private void builder() {
            new SimpleBottomSheetDialog(this);
        }
    }

    public interface OnItemClickListener {
        void onItemOnClick(View v, String s, int position);
    }
}
