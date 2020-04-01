package cn.xzj.agent.ui.job.source

import android.support.v7.widget.LinearLayoutManager
import cn.xzj.agent.R
import cn.xzj.agent.core.common.BaseFragment
import cn.xzj.agent.entity.DownloadInfo
import cn.xzj.agent.entity.DownloadInfo_
import cn.xzj.agent.net.download.DownloadStatus
import cn.xzj.agent.ui.adapter.DownloadingAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import cn.xzj.agent.util.DB
import kotlinx.android.synthetic.main.fragment_downloading.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/23
 * @Des 下载中页面
 */
class DownloadingFragment : BaseFragment() {
    private var mData = ArrayList<DownloadInfo>()
    private lateinit var mAdapter: DownloadingAdapter

    override fun initLayout(): Int {
        return R.layout.fragment_downloading
    }

    override fun initParams() {
    }

    override fun initViews() {
        mAdapter = DownloadingAdapter(mData)
        recyclerViewFragmentDownload.layoutManager = LinearLayoutManager(context)
        recyclerViewFragmentDownload.adapter = mAdapter
        recyclerViewFragmentDownload.addItemDecoration(SimpleItemDecoration.builder(context)
                .isDrawTopFirstLine(false)
                .build())
        setStatusView()
    }

    override fun initData() {
        val mDownloadData = DB.getDB().downloadInfoBox.query()
                .filter {
                    return@filter it.status != DownloadStatus.FINISH
                }
                .orderDesc(DownloadInfo_.id)
                .build()
                .find()
        mData.clear()
        if (mDownloadData.isNotEmpty()) {
            mData.addAll(mDownloadData)
        }
    }

    fun onRefresh() {
        initData()
        mAdapter.notifyDataSetChanged()
        setStatusView()
    }
    private fun setStatusView(){
        if (mData.isNotEmpty()&&statusLayoutFragmentDownload!=null) {
            statusLayoutFragmentDownload.showContent()
        } else {
            statusLayoutFragmentDownload.showEmpty()
        }
    }
}