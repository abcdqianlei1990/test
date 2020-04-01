package cn.xzj.agent.ui.adapter.common;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;


import java.util.ArrayList;

import cn.xzj.agent.R;
import cn.xzj.agent.core.common.BaseActivity;
import cn.xzj.agent.entity.common.ShareMenuInfo;
import cn.xzj.agent.util.ShareUtil;

/**
 * Created by channey on 2016/11/21.
 * version:1.0
 * desc:
 */

public class ShareMenuListAdapter extends RecyclerView.Adapter<ShareMenuListAdapter.ShareMenuListVH> {
    ArrayList<ShareMenuInfo> menus = new ArrayList<>();
    Context context;
    ShareMenuItemClickListener listener;
    public ShareMenuListAdapter(Context context) {
        this.context = context;
//        initMenus();
    }

//    private void initMenus(){
//        menus.add(new ShareMenuInfo(R.mipmap.ic_share_wechat,"微信", ShareUtil.INSTANCE.getSHARE_TYPE_WECHAT()));
//        menus.add(new ShareMenuInfo(R.mipmap.ic_share_moments,"微信朋友圈",ShareUtil.INSTANCE.getSHARE_TYPE_MOMENTS()));
//        menus.add(new ShareMenuInfo(R.mipmap.ic_share_qq,"QQ",ShareUtil.INSTANCE.getSHARE_TYPE_QQ()));
//        menus.add(new ShareMenuInfo(R.mipmap.ic_share_qzone,"QQ空间",ShareUtil.INSTANCE.getSHARE_TYPE_QZONE()));
//        menus.add(new ShareMenuInfo(R.mipmap.ic_share_sina,"新浪微博",ShareUtil.INSTANCE.getSHARE_TYPE_SINA()));
//    }
    @Override
    public ShareMenuListVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_share_menu_list,null);
        return new ShareMenuListVH(view);
    }

    @Override
    public void onBindViewHolder(final ShareMenuListVH holder, final int position) {
        ShareMenuInfo info = menus.get(position);
//        context.getResources().getDrawable(info.getIcon());
        holder.icon.setImageResource(info.getIcon());
        holder.title.setText(info.getTitle());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v,position,menus.get(position).getPlatform());
            }
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    public void setOnRecyclerViewItemClickListener(ShareMenuItemClickListener listener){
        this.listener = listener;
    }

    public void setMenus(ArrayList<ShareMenuInfo> menus){
        this.menus = menus;
    }
    public class ShareMenuListVH extends RecyclerView.ViewHolder {
        public ImageView icon;
        public AppCompatTextView title;
        public LinearLayout content;
        public ShareMenuListVH(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.item_share_menus_icon);
            title = (AppCompatTextView) itemView.findViewById(R.id.item_share_menus_title);
            content = (LinearLayout) itemView.findViewById(R.id.item_share_menus_content);
        }
    }

    public interface  ShareMenuItemClickListener {
        void onClick(View view, int position, String platform);
    }
}
