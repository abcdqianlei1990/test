package cn.xzj.agent.ui.job.source;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.kk.taurus.playerbase.player.IPlayer;
import com.ye.widget.StatusLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.xzj.agent.R;
import cn.xzj.agent.avplayer.play.DataInter;
import cn.xzj.agent.avplayer.play.ListPlayer;
import cn.xzj.agent.avplayer.play.OnHandleListener;
import cn.xzj.agent.core.common.BaseFragment;
import cn.xzj.agent.entity.job.JobInfo;
import cn.xzj.agent.entity.job.VideoBean;
import cn.xzj.agent.ui.adapter.common.BaseHolder;
import cn.xzj.agent.ui.adapter.common.QuickAdapter;
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration;
import cn.xzj.agent.util.LogLevel;
import cn.xzj.agent.widget.SimpleToast;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/22
 * @Des
 */
public class VideoSourcePlayFragment extends BaseFragment {
    private RecyclerView mRecycler;
    private FrameLayout mPlayerContainer;

    private boolean isLandScape;
    //    private JobSourceVideoListAdapter mAdapter;
    private boolean toDetail;
    private List<VideoBean> sourceData;
    private JobInfo mJobInfo;
    private StatusLayout mStatusLayout;
    private QuickAdapter<VideoBean> mAdapter = new QuickAdapter<VideoBean>(R.layout.item_job_source_redio1) {
        @Override
        public void convert(@NotNull BaseHolder holder, final VideoBean item, int position) {
            holder.setText(R.id.tv_job_source_video_title, item.getLabel());
            holder.getView(R.id.tv_job_source_video_copy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    assert cm != null;
                    cm.setText(item.getUrl());
                    SimpleToast.showNormal("已复制");
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toBrowserPlay(getContext(), item.getUrl());
                }
            });
        }
    };

    private void toBrowserPlay(Context context, String url) {
        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        mIntent.setData(uri);
        context.startActivity(mIntent);

    }


    @Override
    public int initLayout() {
        return R.layout.fragment_job_video_source;
    }

    @Override
    public void initParams() {
        assert getArguments() != null;
        mJobInfo = (JobInfo) getArguments().getSerializable(JobSourceActivity.SOURCE_DATA);
        if (mJobInfo != null) {
            if (mJobInfo.getCompanyInnerVideos() != null)
                sourceData = mJobInfo.getCompanyInnerVideos();
            else {
                sourceData = new ArrayList<>();
            }
//            sourceData.addAll(getVideoList());
        }
    }

    @Override
    public void initViews() {

        mRecycler = getActivity().findViewById(R.id.recyclerViewFragmentJobVideoSource);
        mPlayerContainer = getActivity().findViewById(R.id.frameLayoutFragmentJobVideoSourcePlayContainer);
        mStatusLayout = getActivity().findViewById(R.id.statusLayoutFragmentJobVideoSource);
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecycler.addItemDecoration(SimpleItemDecoration.builder(getContext())
                .dividerHeight(8)
                .dividerColor(getResources().getColor(R.color.commonBackground))
        .build());
//        mAdapter = new JobSourceVideoListAdapter(getContext(), mRecycler, sourceData);
        mAdapter.setNewData(sourceData);
        mRecycler.setAdapter(mAdapter);
        if (sourceData.size() == 0) {
            mStatusLayout.showEmpty();
        } else {
            mStatusLayout.showContent();
        }

    }

    @Override
    public void initData() {

    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        isLandScape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE;
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            mPlayerContainer.setBackgroundColor(Color.BLACK);
//            ListPlayer.get().attachContainer(mPlayerContainer, false);
//            ListPlayer.get().setReceiverConfigState(getContext(), ISPayer.RECEIVER_GROUP_CONFIG_FULL_SCREEN_STATE);
//        } else {
//            mPlayerContainer.setBackgroundColor(Color.TRANSPARENT);
//            mRecycler.post(new Runnable() {
//                @Override
//                public void run() {
//                    JobSourceVideoListAdapter.VideoItemHolder currentHolder = mAdapter.getCurrentHolder();
//                    if (currentHolder != null) {
//                        ListPlayer.get().attachContainer(currentHolder.layoutContainer, false);
//                        ListPlayer.get().setReceiverConfigState(
//                                getContext(), ISPayer.RECEIVER_GROUP_CONFIG_LIST_STATE);
//                    }
//                }
//            });
//        }
//        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, isLandScape);
//        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_IS_LANDSCAPE, isLandScape);
    }

    private void toggleScreen() {
        getActivity().setRequestedOrientation(isLandScape ?
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    @Override
    public void onResume() {
        super.onResume();
        ListPlayer.get().updateGroupValue(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, isLandScape);
        ListPlayer.get().setOnHandleListener(new OnHandleListener() {
            @Override
            public void onBack() {
                onBackPressed();
            }

            @Override
            public void onToggleScreen() {
                toggleScreen();
            }
        });
        if (!toDetail && ListPlayer.get().isInPlaybackState()) {
            ListPlayer.get().resume();
        }
        toDetail = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        int state = ListPlayer.get().getState();
        if (state == IPlayer.STATE_PLAYBACK_COMPLETE)
            return;
        if (!toDetail) {
            ListPlayer.get().pause();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        ListPlayer.get().attachActivity(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void onBackPressed() {
        if (isLandScape) {
            toggleScreen();
        }
    }

//    @Override
//    public void onCopyClick(JobSourceVideoListAdapter.VideoItemHolder holder, VideoBean item, int position) {
//        SimpleToast.showNormal("已复制");
//    }
//
//    @Override
//    public void playItem(JobSourceVideoListAdapter.VideoItemHolder holder, VideoBean item, int position) {
//        ListPlayer.get().setReceiverConfigState(getContext(), ISPayer.RECEIVER_GROUP_CONFIG_LIST_STATE);
//        ListPlayer.get().attachContainer(holder.layoutContainer);
//        ListPlayer.get().play(new DataSource(item.getUrl()));
//    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ListPlayer.get().destroy();
    }

    //获取真实播放地址
    public String getRealPlayerUrl(String url) {
        if (url.indexOf("youku") > 0) {
            //如果是优酷的视频
            String realPlayerUrl = "http://player.youku.com/embed/" + Arrays.toString(url.split(".html")[0].split("/id_"));
            LogLevel.w("realPlayerUrl", realPlayerUrl);
            return realPlayerUrl;
        }
        return url;

    }
}
