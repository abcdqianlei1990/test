package cn.xzj.agent.ui.adapter.common;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yemao on 2017/3/20.
 */

public abstract class CommonListAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    protected Context mContext;
    protected View mContentView;
    protected int layoutId;

    public CommonListAdapter(Context context, @LayoutRes int layout) {
        mData = new ArrayList<>();
        mContext = context;
        this.layoutId = layout;
    }

    public void clear() {
        mData.clear();
    }

    public void removeAll() {
        for (int i = 0; i < mData.size(); i++) {
            mData.remove(i);
        }
        notifyDataSetChanged();
    }
    public void removeItem(int position){
        if (mData.size()<position+1){
            return;
        }
        mData.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        mData.add(item);
    }

    public void addItem(int posstion, T item) {
        mData.add(posstion, item);
    }

    public void addAllItem(List<T> mData) {

        if (mData==null){
            mData.addAll(new ArrayList<T>());
        }else {
            this.mData.addAll(mData);
        }
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData == null ? 0 : (mData.size() == position ? null : mData.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonViewHolder viewHolder = null;
        if (convertView == null) {
            mContentView = LayoutInflater.from(mContext).inflate(layoutId, null);
            convertView = mContentView;
            viewHolder = new CommonViewHolder(mContext, mContentView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonViewHolder) convertView.getTag();
        }
        if (position < mData.size())
            convert(viewHolder, mData.get(position), position);
        else
            convert(viewHolder, null, position);
        return convertView;
    }

    public abstract void convert(CommonViewHolder viewHolder, T item, int position);
}
