package cn.xzj.agent.ui.adapter;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import cn.xzj.agent.R;
import cn.xzj.agent.entity.DownloadInfo;
import cn.xzj.agent.net.download.DownloadFileManager;
import cn.xzj.agent.net.download.DownloadListener;
import cn.xzj.agent.net.download.DownloadStatus;
import cn.xzj.agent.util.LogLevel;

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/12
 * @Des
 */
public class DownloadingAdapter extends RecyclerView.Adapter<DownloadingAdapter.DownloadViewHolder> {
    private List<DownloadInfo> downloadInfoList;
    private String rootPath;

    public DownloadingAdapter(List<DownloadInfo> downloadInfoList) {
        this.downloadInfoList = downloadInfoList;
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_downloading, null);
        return new DownloadViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder downloadViewHolder, int i) {
        downloadViewHolder.bindData(downloadInfoList.get(i));
    }

    @Override
    public int getItemCount() {
        return downloadInfoList.size();
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar mProgressBar;
        private Button mBtnDownloadPauseOrStart;
        private TextView mTvDownloadProgress, mTvStatusMessage, mTvTitle, mTvSavePath;
        private DownloadFileManager mDownloadManager;
        private DownloadInfo mDownloadInfo;

        DownloadViewHolder(@NonNull View itemView) {
            super(itemView);
            mDownloadManager = DownloadFileManager.getInstance();
            mProgressBar = itemView.findViewById(R.id.progressBarItemDownload);
            mBtnDownloadPauseOrStart = itemView.findViewById(R.id.btnItemDownload);
            mTvDownloadProgress = itemView.findViewById(R.id.tvItemDownloadProgress);
            mTvStatusMessage = itemView.findViewById(R.id.tvItemDownloadStatusMessage);
            mTvTitle = itemView.findViewById(R.id.tvItemDownloadTitle);
            mTvSavePath = itemView.findViewById(R.id.tvItemDownloadSavePath);
            mBtnDownloadPauseOrStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mBtnDownloadPauseOrStart.getText().toString().equals("继续")) {
                        mDownloadManager.startDown(mDownloadInfo);
                        mBtnDownloadPauseOrStart.setText("暂停");
                    } else {
                        mDownloadManager.pause(mDownloadInfo);
                        mBtnDownloadPauseOrStart.setText("继续");
                    }

                }
            });
        }

        @SuppressLint("SetTextI18n")
        private void bindData(DownloadInfo downloadInfo) {
            if (downloadInfo.getName() != null)
                mTvTitle.setText("文件名称：" + downloadInfo.getName());
            //得到下载管理器中downloadInfo
            mDownloadInfo = DownloadFileManager.getInstance().findDownloadInfo(downloadInfo.getUrl());
            if (mDownloadInfo == null) {
                DownloadFileManager.getInstance().putDownloadInfo(downloadInfo);
                mDownloadInfo = downloadInfo;
            }
            refresh();
            if (!mDownloadInfo.getStatus().equals(DownloadStatus.FINISH)) {
                mDownloadInfo.setListener(new DownloadListener() {
                    @Override
                    public void onStart() {
                        refresh();
                    }

                    @Override
                    public void onComplete() {
                        refresh();
                        LogLevel.w("onComplete", "下载完成 " + mDownloadInfo.getName());
                        notifyItemRemoved(getAdapterPosition());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        refresh();
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void updateProgress(long readLength, long countLength, boolean isFinish) {
                        refresh();
                    }

                    @Override
                    public void onPause() {
                        super.onPause();
                        refresh();
                    }

                    @Override
                    public void onStop() {
                        super.onStop();
                        refresh();
                    }
                });
                DownloadFileManager.getInstance().putDownloadInfo(mDownloadInfo);

            }
        }

        @SuppressLint("SetTextI18n")
        private void refresh() {
            try {
                if (mDownloadInfo.getCountLength() > 0 && mDownloadInfo.getReadLength() > 0) {
                    int progress = (int) ((float) mDownloadInfo.getReadLength() / mDownloadInfo.getCountLength() * 100);
                    mProgressBar.setProgress(progress);
                    mTvDownloadProgress.setText(progress + "%");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            switch (mDownloadInfo.getStatus()) {
                case DownloadStatus.READY:
                    mTvStatusMessage.setText("待下载");
                    //隐藏按钮
                    mBtnDownloadPauseOrStart.setVisibility(View.INVISIBLE);
                    break;
                case DownloadStatus.START:
                    mTvStatusMessage.setText("开始");
                    mBtnDownloadPauseOrStart.setVisibility(View.VISIBLE);
                    break;
                case DownloadStatus.PAUSE:
                    mTvStatusMessage.setText("暂停中");
                    mBtnDownloadPauseOrStart.setVisibility(View.VISIBLE);
                    break;
                case DownloadStatus.DOWN:
                    mTvStatusMessage.setText("下载中");
                    mBtnDownloadPauseOrStart.setVisibility(View.VISIBLE);
                    break;
                case DownloadStatus.STOP:
                    mTvStatusMessage.setText("下载停止");
                    mBtnDownloadPauseOrStart.setVisibility(View.VISIBLE);
                    break;
                case DownloadStatus.ERROR:
                    mTvStatusMessage.setText("下载错误");
                    mBtnDownloadPauseOrStart.setVisibility(View.VISIBLE);
                    break;
                case DownloadStatus.FINISH:
                    mTvStatusMessage.setText("下载完成");
                    //下载完成隐藏按钮
                    mBtnDownloadPauseOrStart.setVisibility(View.INVISIBLE);
                    if (mDownloadInfo.getSavePath() != null) {
                        mTvSavePath.setText("存储位置：" + mDownloadInfo.getSavePath().replace(rootPath, "内部储存") + "(相册中可查看)");
                    }
                    break;
            }

        }
    }


}
