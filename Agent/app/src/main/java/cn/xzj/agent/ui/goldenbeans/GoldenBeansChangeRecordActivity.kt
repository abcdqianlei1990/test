package cn.xzj.agent.ui.goldenbeans

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.goldenbeans.GoldenBeansChangeRecordInfo
import cn.xzj.agent.iview.IGoldenBeansChangeRecord
import cn.xzj.agent.presenter.GoldenBeansChangeRecordPresenter
import cn.xzj.agent.ui.adapter.GoldenBeansChangeRecordAdapter
import kotlinx.android.synthetic.main.activity_goldenbean_changerecord.*

class GoldenBeansChangeRecordActivity: MVPBaseActivity<GoldenBeansChangeRecordPresenter>(), IGoldenBeansChangeRecord {
    private lateinit var mAdapter:GoldenBeansChangeRecordAdapter
    companion object {
        fun jump(context: Context) {
            val intent = Intent(context, GoldenBeansChangeRecordActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_goldenbean_changerecord
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
    }

    override fun initViews() {
        setTitle("金豆明细")
        initRecyclerView()
    }

    private var mTotalCount = 0
    private var mCurrentPage = 1
    private var mList = ArrayList<GoldenBeansChangeRecordInfo>()
    private fun initRecyclerView() {
        mAdapter = GoldenBeansChangeRecordAdapter()
        mAdapter.data = mList
        activity_goldenbean_records_list.layoutManager = LinearLayoutManager(this)
        activity_goldenbean_records_list.adapter = mAdapter
        activity_goldenbean_records_curSmartRefreshLayout.isEnableLoadMore = true
        activity_goldenbean_records_curSmartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            mPresenter.getGoldenBeansChangeRecords(mCurrentPage)
        }
        activity_goldenbean_records_curSmartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            mPresenter.getGoldenBeansChangeRecords(mCurrentPage)
        }
    }

    private fun loadingComplete() {
        if (mCurrentPage <= 1) {
            activity_goldenbean_records_curSmartRefreshLayout.finishRefresh()
        } else {
            activity_goldenbean_records_curSmartRefreshLayout.finishLoadMore()
        }

        activity_goldenbean_records_curSmartRefreshLayout.setNoMoreData(mTotalCount <= mAdapter.data!!.size)
    }

    override fun initData() {
        super.initData()
        mPresenter.getGoldenBeansChangeRecords(mCurrentPage)
    }

    override fun onRecordsGetSuccess(info: CommonListBody<GoldenBeansChangeRecordInfo>) {
        mTotalCount  = info.totalCount
        if (mCurrentPage == 1) mList.clear()
        mList.addAll(info.items)
        mAdapter.notifyDataSetChanged()
        loadingComplete()
    }

    override fun onRecordsGetFailure() {
        loadingComplete()
    }
}