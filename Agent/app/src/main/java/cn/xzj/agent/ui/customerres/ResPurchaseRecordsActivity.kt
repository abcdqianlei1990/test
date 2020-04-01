package cn.xzj.agent.ui.customerres

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customerres.ResPurchaseRecordInfo
import cn.xzj.agent.iview.IResPurchaseRecord
import cn.xzj.agent.presenter.ResPurchaseRecordPresenter
import cn.xzj.agent.ui.adapter.ResPurchaseRecordsAdapter
import cn.xzj.agent.ui.adapter.common.decoration.LineSplitDecoration
import kotlinx.android.synthetic.main.activity_res_purchase_records.*

class ResPurchaseRecordsActivity: MVPBaseActivity<ResPurchaseRecordPresenter>(), IResPurchaseRecord {

    companion object {
        fun jump(context: Context) {
            val intent = Intent(context, ResPurchaseRecordsActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_res_purchase_records
    }

    override fun context(): Context {
        return this
    }

    override fun initViews() {
        setTitle("购买记录")
        initRecyclerView()
    }

    private lateinit var mAdapter:ResPurchaseRecordsAdapter
    private var mTotalCount = 0
    private var mCurrentPage = 1
    private var mList = ArrayList<ResPurchaseRecordInfo>()
    private fun initRecyclerView() {
        mAdapter = ResPurchaseRecordsAdapter()
        mAdapter.data = mList
        activity_resPurchaseRecord_list.layoutManager = LinearLayoutManager(this)
        activity_resPurchaseRecord_list.adapter = mAdapter
        activity_resPurchaseRecord_list.addItemDecoration(LineSplitDecoration(this,splitSize = resources.getDimension(R.dimen.dp_10).toInt()))
        activity_resPurchaseRecord_smartRefreshLayout.isEnableLoadMore = true
        activity_resPurchaseRecord_smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getResPurchaseRecords()
        }
        activity_resPurchaseRecord_smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getResPurchaseRecords()
        }
    }

    private fun loadingComplete() {
        if (mCurrentPage <= 1) {
            activity_resPurchaseRecord_smartRefreshLayout.finishRefresh()
        } else {
            activity_resPurchaseRecord_smartRefreshLayout.finishLoadMore()
        }

        activity_resPurchaseRecord_smartRefreshLayout.setNoMoreData(mTotalCount <= mAdapter.data!!.size)
    }

    override fun initData() {
        super.initData()
        getResPurchaseRecords()
    }

    private fun getResPurchaseRecords(){
        mPresenter.getResPurchaseRecords(mCurrentPage)
    }

    override fun onRecordsGetSuccess(body: CommonListBody<ResPurchaseRecordInfo>) {
        mTotalCount  = body.totalCount
        if (mCurrentPage == 1) mList.clear()
        mList.addAll(body.items)
        mAdapter.notifyDataSetChanged()
        loadingComplete()
    }

    override fun onRecordsGetFail() {
        loadingComplete()
    }
}