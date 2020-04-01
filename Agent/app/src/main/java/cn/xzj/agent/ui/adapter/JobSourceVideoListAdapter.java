package cn.xzj.agent.ui.adapter;

import android.annotation.SuppressLint;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kk.taurus.playerbase.log.PLog;

import java.util.List;

import cn.xzj.agent.R;
import cn.xzj.agent.avplayer.play.ListPlayer;
import cn.xzj.agent.entity.job.VideoBean;
import cn.xzj.agent.util.DisplayUtil;

public class JobSourceVideoListAdapter extends RecyclerView.Adapter<JobSourceVideoListAdapter.VideoItemHolder> {

    private final String TAG = "JobSourceVideoListAdapter";

    private Context mContext;
    private List<VideoBean> mItems;

    private RecyclerView mRecycler;

    private OnListListener onListListener;

    private int mScreenUseW;

    private int mScreenH;

    private int mPlayPosition = -1;
    private int mVerticalRecyclerStart;

    public JobSourceVideoListAdapter(Context context, RecyclerView recyclerView, List<VideoBean> list) {
        this.mContext = context;
        this.mRecycler = recyclerView;
        this.mItems = list;
        mScreenUseW = DisplayUtil.getScreenW(context) - DisplayUtil.dip2px(6 * 2);
        init();
    }

    public void reset() {
        mPlayPosition = -1;
        notifyDataSetChanged();
    }

    private void init() {
        mScreenH = DisplayUtil.getScreenH(mRecycler.getContext());
        mRecycler.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                int[] location = new int[2];
                mRecycler.getLocationOnScreen(location);
                mVerticalRecyclerStart = location[1];
                mRecycler.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int itemVisibleRectHeight = getItemVisibleRectHeight(mPlayPosition);
                if (mPlayPosition >= 0 && itemVisibleRectHeight <= 0 && dy != 0) {
                    PLog.d(TAG, "onScrollStateChanged stop itemVisibleRectHeight = " + itemVisibleRectHeight);
                    ListPlayer.get().stop();
                    notifyItemChanged(mPlayPosition);
                    mPlayPosition = -1;
                }
            }
        });
    }

    public void setOnListListener(OnListListener onListListener) {
        this.onListListener = onListListener;
    }

    @NonNull
    @Override
    public VideoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoItemHolder(View.inflate(mContext, R.layout.item_job_source_redio, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final VideoItemHolder holder, @SuppressLint("RecyclerView") final int position) {
        ViewCompat.setElevation(holder.card, DisplayUtil.dip2px(3));
        updateWH(holder);
        final VideoBean item = getItem(position);
        assert item != null;
        holder.title.setText(item.getLabel());
        holder.layoutContainer.removeAllViews();
        holder.playIcon.setVisibility(mPlayPosition == position ? View.GONE : View.VISIBLE);
        if (onListListener != null) {
            holder.albumLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (mPlayPosition >= 0)
//                        notifyItemChanged(mPlayPosition);
//                    holder.playIcon.setVisibility(View.GONE);
//                    mPlayPosition = position;
//                    onListListener.playItem(holder, item, position);
                    //放弃视频播放器播放，改为跳转浏览器播放，因为url不是一个mp4格式的播放文件
                    toBrowserPlay(holder.itemView.getContext(),item.getUrl());
                }
            });
            holder.copyBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onListListener.onCopyClick(holder, item, position);
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    assert cm != null;
                    cm.setText(item.getUrl());

                }
            });
        }
    }

    private void toBrowserPlay(Context context, String url) {
        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        mIntent.setData(uri);
        context.startActivity(mIntent);

    }

    private void updateWH(JobSourceVideoListAdapter.VideoItemHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.layoutBox.getLayoutParams();
        layoutParams.width = mScreenUseW;
        layoutParams.height = mScreenUseW * 9 / 16;
        holder.layoutBox.setLayoutParams(layoutParams);
    }

    private VideoBean getItem(int position) {
        if (mItems == null)
            return null;
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        if (mItems == null)
            return 0;
        return mItems.size();
    }

    public static class VideoItemHolder extends RecyclerView.ViewHolder {

        View card;
        public FrameLayout layoutContainer;
        private RelativeLayout layoutBox;
        View albumLayout;
        ImageView albumImage;
        ImageView playIcon;
        TextView title;
        TextView copyBtn;

        VideoItemHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.card);
            layoutContainer = itemView.findViewById(R.id.layoutContainer);
            layoutBox = itemView.findViewById(R.id.layBox);
            albumLayout = itemView.findViewById(R.id.album_layout);
            albumImage = itemView.findViewById(R.id.iv_job_source);
            playIcon = itemView.findViewById(R.id.ic_job_source_play_btn);
            title = itemView.findViewById(R.id.tv_job_source_video_title);
            copyBtn = itemView.findViewById(R.id.tv_job_source_video_copy);
        }

    }

    public VideoItemHolder getCurrentHolder() {
        if (mPlayPosition < 0)
            return null;
        return getItemHolder(mPlayPosition);
    }

    /**
     * 获取Item中渲染视图的可见高度
     */
    private int getItemVisibleRectHeight(int position) {
        VideoItemHolder itemHolder = getItemHolder(position);
        if (itemHolder == null)
            return 0;
        int[] location = new int[2];
        itemHolder.layoutBox.getLocationOnScreen(location);
        int height = itemHolder.layoutBox.getHeight();

        int visibleRect;
        if (location[1] <= mVerticalRecyclerStart) {
            visibleRect = location[1] - mVerticalRecyclerStart + height;
        } else {
            if (location[1] + height >= mScreenH) {
                visibleRect = mScreenH - location[1];
            } else {
                visibleRect = height;
            }
        }
        return visibleRect;
    }

    private VideoItemHolder getItemHolder(int position) {
        RecyclerView.ViewHolder viewHolder = mRecycler.findViewHolderForAdapterPosition(position);
        if (viewHolder instanceof VideoItemHolder) {
            return (VideoItemHolder) viewHolder;
        }
        return null;
    }

    public interface OnListListener {
        void onCopyClick(VideoItemHolder holder, VideoBean item, int position);

        void playItem(VideoItemHolder holder, VideoBean item, int position);
    }

}
