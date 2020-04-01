package cn.xzj.agent.ui.job.source

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.job.JobInfo
import cn.xzj.agent.iview.IJobSourceView
import cn.xzj.agent.presenter.JobSourcePresenter
import cn.xzj.agent.util.DB
import cn.xzj.agent.util.StatusBarUtil
import cn.xzj.agent.widget.CommonDialog
import cn.xzj.agent.widget.SimpleToast
import kotlinx.android.synthetic.main.activity_job_source.*

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/7
 * @Des 岗位资源展示页面，图片或视频
 */
class JobSourceActivity : MVPBaseActivity<JobSourcePresenter>(), IJobSourceView {

    private var showSourceType = 0 //0图片1视频
    private lateinit var mImageSourceFragment: ImageSourceFragment
    private lateinit var mVideoSourceFragment: VideoSourcePlayFragment
    private var mSaveBtnIsShowing = false//保存按钮是否显示
    private var mJobInfo: JobInfo? = null

    companion object {
        const val SHOW_SOURCE_TYPE = "showSourceType"
        const val SOURCE_DATA = "source_data"
        const val SHOW_SOURCE_IMAGE = 0
        const val SHOW_SOURCE_VIDEO = 1
        fun jump(context: Context, showSourceType: Int, mJobInfo: JobInfo) {
            val mIntent = Intent()
            mIntent.setClass(context, JobSourceActivity::class.java)
            mIntent.putExtra(SHOW_SOURCE_TYPE, showSourceType)
            mIntent.putExtra(SOURCE_DATA, mJobInfo)
            context.startActivity(mIntent)
        }
    }

    override fun context(): Context {
        return this
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_job_source
    }

    override fun initParams() {
        super.initParams()
        showSourceType = intent.getIntExtra(SHOW_SOURCE_TYPE, 0)
        mJobInfo = intent.getSerializableExtra(SOURCE_DATA) as JobInfo?
    }

    override fun initViews() {
        hideToolbar()
        StatusBarUtil.setPadding(this, linearLayoutJobSourceTopParent)
        initTabLayout()
        initFragment()
        changeFragment(showSourceType)
        tab_layout_job_source_type.getTabAt(showSourceType)!!.select()
    }

    private fun initFragment() {

        mImageSourceFragment = ImageSourceFragment()
        val mBundle = Bundle()
        mBundle.putSerializable(SOURCE_DATA, mJobInfo)
        mImageSourceFragment.arguments = mBundle

        mVideoSourceFragment = VideoSourcePlayFragment()
        mVideoSourceFragment.arguments = mBundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.linearLayoutJobSourceContent, mImageSourceFragment, "ImageSourceFragment")
        transaction.add(R.id.linearLayoutJobSourceContent, mVideoSourceFragment, "VideoSourceFragment")
        transaction.commit()
    }

    private fun initTabLayout() {
        val tabImage = tab_layout_job_source_type.newTab()
        tabImage.text = resources.getString(R.string.image)
        val tabVideo = tab_layout_job_source_type.newTab()
        tabVideo.text = resources.getString(R.string.video)
        tab_layout_job_source_type.addTab(tabImage)
        tab_layout_job_source_type.addTab(tabVideo)
        tab_layout_job_source_type.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeFragment(tab!!.position)
            }
        })
    }

    override fun setListeners() {
        super.setListeners()
        iv_job_source_back.setOnClickListener {
            finish()
        }
        iv_job_source_download.setOnClickListener {
            mSaveBtnIsShowing = !mSaveBtnIsShowing
            changeDownloadView()
            mImageSourceFragment.mOnDownloadButtonListener.onDownloadClick(mSaveBtnIsShowing)
        }
        btn_activity_job_source_save.setOnClickListener {
            mSaveBtnIsShowing = !mSaveBtnIsShowing
            //保存资源
            changeDownloadView()
            mImageSourceFragment.mOnDownloadButtonListener.onDownloadClick(mSaveBtnIsShowing)

            if (mImageSourceFragment.getSelectImageUrls().isEmpty()) {
                SimpleToast.showNormal("未选中图片")
                return@setOnClickListener
            }
            DB.getDB().downloadInfoBox.removeAll()
            mPresenter.downloadImage(mImageSourceFragment.getSelectImageUrls())
            CommonDialog.newBuilder(this)
                    .setMessage("已加入下载队列，下载完成相册中可看")
                    .setNegativeButton("我知道了") {
                        it.cancel()
                    }
                    .setPositiveButton("我的下载") {
                        it.cancel()
                        //跳转到我的下载
                        startActivity(Intent(this, DownloadActivity::class.java))
                    }
                    .create()
                    .show()
        }
    }

    private fun changeDownloadView() {
        if (mSaveBtnIsShowing) {
            iv_job_source_download.visibility = View.INVISIBLE
            btn_activity_job_source_save.visibility = View.VISIBLE
        } else {
            iv_job_source_download.visibility = View.VISIBLE
            btn_activity_job_source_save.visibility = View.GONE
        }
    }

    @SuppressLint("CommitTransaction")
    fun changeFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        when (position) {
            0 -> {
                transaction.show(mImageSourceFragment).hide(mVideoSourceFragment).commitAllowingStateLoss()
                changeDownloadView()
            }
            1 -> {
                transaction.show(mVideoSourceFragment).hide(mImageSourceFragment).commitAllowingStateLoss()
                iv_job_source_download.visibility = View.INVISIBLE
                btn_activity_job_source_save.visibility = View.GONE
            }
        }
    }

    interface OnDownloadButtonListener {
        fun onDownloadClick(downloadBtnIsShowing: Boolean)
    }

}
