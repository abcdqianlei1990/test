package cn.xzj.agent.ui.adapter.common;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by yemao on 2017/2/13.
 */

public class CommonViewHolder extends RecyclerView.ViewHolder{
    private View contentView;
    private SparseArray<View> viewList;
    private Context mContext;

    public CommonViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext=context;
        this.contentView=itemView;
        init();
    }

    public CommonViewHolder(View itemView) {
        super(itemView);
        this.contentView=itemView;
        init();
    }

    private void init() {
        viewList=new SparseArray<>();
    }
    public static CommonViewHolder get(Context context, ViewGroup parent, View contview, int layoutId){
        CommonViewHolder mCommonCiewHolder;
        View mView= LayoutInflater.from(context).inflate(layoutId,parent);
        mCommonCiewHolder=new CommonViewHolder(context,mView);
        return mCommonCiewHolder;
    }

    public <T extends View>T getView(int viewId){
        View view= viewList.get(viewId);
        if (view==null) {
             view = contentView.findViewById(viewId);
             viewList.put(viewId,view);
        }
        return (T) view;
    }
    public View getContentView(){
        return contentView;
    }
    public void setImage(int viewId,int image){
        ImageView mImageView=getView(viewId);
        Glide.with(mContext)
                .load(image)
                .into(mImageView);

    }
    public void  setText(int viewId,String value){
        TextView mTv= getView(viewId);
        mTv.setText(value);
    }

    public void  setText(int viewId,int value){
        TextView mTv= getView(viewId);
        mTv.setText(mContext.getString(value));
    }
    public void setVisibility(int viewId,int visivility){
        View view=getView(viewId);
        view.setVisibility(visivility);
    }

    public CommonViewHolder setTextView(@IdRes int viewId, String value) {
        TextView mTextview = getView(viewId);
        mTextview.setText(value);
        return this;
    }

}
