package cn.xzj.agent.ui.job.source

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.avplayer.cover.ControllerCover
import cn.xzj.agent.avplayer.cover.LoadingCover
import cn.xzj.agent.constants.Config
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.core.common.QuickActivity
import cn.xzj.agent.entity.DownloadInfo
import cn.xzj.agent.entity.DownloadInfo_
import cn.xzj.agent.net.download.DownloadFileManager
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.DB
import cn.xzj.agent.util.LogLevel
import cn.xzj.agent.util.StatusBarUtil
import cn.xzj.agent.widget.SimpleToast
import com.bumptech.glide.Glide
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler
import com.kk.taurus.playerbase.entity.DataSource
import com.kk.taurus.playerbase.event.OnPlayerEventListener
import com.kk.taurus.playerbase.player.IPlayer
import com.kk.taurus.playerbase.receiver.ReceiverGroup
import com.kk.taurus.playerbase.render.AspectRatio
import com.kk.taurus.playerbase.widget.BaseVideoView
import kotlinx.android.synthetic.main.activity_job_source_detail.*

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/8
 * @Des 岗位相关资源详情 图片资源或视频资源
 */
class JobSourceDetailActivity : QuickActivity(), OnPlayerEventListener {
    private lateinit var source: String
    private var sourceType: Int = JobSourceActivity.SHOW_SOURCE_IMAGE
    private var userPause = false
    private var hasStart = false
    private val TAG = JobSourceDetailActivity::class.java.simpleName

    companion object {
        val SOURCE = "source"
        val SOURCE_TYPE = "sourceType"
        fun jump(context: Context, source: String, sourceType: Int) {
            val mIntent = Intent()
            mIntent.putExtra(SOURCE, source)
            mIntent.putExtra(SOURCE_TYPE, sourceType)
            mIntent.setClass(context, JobSourceDetailActivity::class.java)
            context.startActivity(mIntent)
        }
    }

    override fun initParams() {
        super.initParams()
        source = intent.getStringExtra(SOURCE)
        sourceType = intent.getIntExtra(SOURCE_TYPE, JobSourceActivity.SHOW_SOURCE_IMAGE)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_job_source_detail
    }

    override fun initViews() {
        hideToolbar()
        StatusBarUtil.setPadding(this, linearLayoutJobSourceDetailTopParent)
        if (JobSourceActivity.SHOW_SOURCE_IMAGE == sourceType) {
            Glide.with(this)
                    .load(source)
                    .into(iv_job_source_detail_image)
            iv_job_source_detail_image.visibility = View.VISIBLE
            baseVideoViewJobSourceDetail.visibility = View.GONE
        } else {
            iv_job_source_detail_image.visibility = View.GONE
            baseVideoViewJobSourceDetail.visibility = View.VISIBLE
        }
        val mReceiverGroup = ReceiverGroup()
        mReceiverGroup.addReceiver(Constants.AVPLAYER_COVER_LOADING, LoadingCover(this))
        mReceiverGroup.addReceiver(Constants.AVPLAYER_COVER_CONTROLLER, ControllerCover(this))
        baseVideoViewJobSourceDetail.setReceiverGroup(mReceiverGroup)
        //设置一个事件处理器
        baseVideoViewJobSourceDetail.setEventHandler(mOnVideoViewEventHandler)
        baseVideoViewJobSourceDetail.setOnPlayerEventListener(this)
        //设置播放模式
        baseVideoViewJobSourceDetail.setAspectRatio(AspectRatio.AspectRatio_FILL_PARENT)

    }

    override fun setListeners() {
        super.setListeners()
        iv_job_source_detail_back.setOnClickListener {
            finish()
        }
        iv_job_source_detail_download.setOnClickListener {
            SimpleToast.showLong("图片已下载至 内部存储/${Config.ROOT_FILE_NAME}/${Config.FILE_DOWNLOAD_ROOT_NAME} (相册中可查看)")
            val mData = DB.getDB().downloadInfoBox.query().equal(DownloadInfo_.url, source).build().find()
            if (mData.isNotEmpty())
                return@setOnClickListener
            val downloadInfo = DownloadInfo()
            downloadInfo.url = source
            downloadInfo.name = lastName(source)
            downloadInfo.savePath = CommonUtils.getAgentDownloadRootFile().path + "/" + lastName(source)
            DownloadFileManager.getInstance().startDown(downloadInfo)
        }
        btnJobSourceDetailParent.setOnClickListener {
            finish()
        }
    }

    private fun lastName(url: String): String {
        val index = url.lastIndexOf("/")
        return url.substring(index + 1)
    }

    override fun onPlayerEvent(eventCode: Int, bundle: Bundle?) {
        LogLevel.d(TAG, "onPlayerEvent eventCode $eventCode")
    }

    private val mOnVideoViewEventHandler = object : OnVideoViewEventHandler() {
        override fun onAssistHandle(assist: BaseVideoView?, eventCode: Int, bundle: Bundle?) {
            super.onAssistHandle(assist, eventCode, bundle)
            LogLevel.d(TAG, "onAssistHandle eventCode $eventCode")
        }
    }

    private fun initPlay() {
        if (!hasStart) {
            baseVideoViewJobSourceDetail.setDataSource(DataSource("http://jiajunhui.cn/video/kaipao.mp4"))
            baseVideoViewJobSourceDetail.start()
            hasStart = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (JobSourceActivity.SHOW_SOURCE_VIDEO == sourceType) {
            val state = baseVideoViewJobSourceDetail.state
            if (state == IPlayer.STATE_PLAYBACK_COMPLETE)
                return
            if (baseVideoViewJobSourceDetail.isInPlaybackState) {
                if (!userPause)
                    baseVideoViewJobSourceDetail.resume()
            } else {
                baseVideoViewJobSourceDetail.rePlay(0)
            }
            initPlay()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (JobSourceActivity.SHOW_SOURCE_VIDEO == sourceType) {
            baseVideoViewJobSourceDetail.stopPlayback()
        }
    }

}
