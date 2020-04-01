package cn.xzj.agent.ui.message

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.agentinfo.MsgInfo
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.IMsgListView
import cn.xzj.agent.presenter.MsgListPresenter
import cn.xzj.agent.ui.adapter.MsgListAdapter
import cn.xzj.agent.ui.customer.CustomerDetailActivity
import cn.xzj.agent.util.CommonUtils
import kotlinx.android.synthetic.main.activity_msg_list.*

class MsgListActivity : MVPBaseFragment<MsgListPresenter>(), View.OnClickListener,IMsgListView {

    override fun context(): Context {
        return context!!
    }

    private lateinit var mAdapter: MsgListAdapter
    private var mList = ArrayList<MsgInfo>()
    private var mCurrentPage = 1;
    private var mTotalCount = 0


    override fun initLayout(): Int {
        return R.layout.activity_msg_list
    }

    override fun initParams() {
    }

    override fun initViews() {
//        setLifeBack()
        setTitle("消息")
        hideTitleLeftBtn()
        initRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        CommonUtils.statistics(context, Constants.STATISTICS_MESSAGE_ID, EnumValue.STATISTICS_TYPE_PAGE, EnumValue.STATISTICS_METRIC_VIEW)
    }

    override fun initData() {
        smartRefreshLayout.autoRefresh()
    }

    override fun setListeners() {
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
//            R.id.title_back -> finish()
        }
    }


    private fun initRecyclerView() {
        mAdapter = MsgListAdapter(context!!, mList)
        mAdapter.setOnItemLongClickListener(object : OnRecyclerViewItemClickListener {
            override fun onClick(view: View, position: Int) {
                val map = HashMap<String, String>()
                map["user_id"] = mList[position].userId
                mPresenter.getCustomerDetailInfo(map)
            }
        })
        activity_msg_list_recyclerview.adapter = mAdapter
        activity_msg_list_recyclerview.layoutManager =  LinearLayoutManager(context!!)
        smartRefreshLayout.isEnableLoadMore = true
        smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getMsgList()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getMsgList()
        }
    }

    private fun refreshList() {
        if (mList.size == 0) {
//            mStatusLayoutManager!!.showEmptyData()
        } else {
//            mStatusLayoutManager!!.showContent()
        }
        loadingComplete()
        mAdapter.notifyDataSetChanged()
    }

    private fun loadingComplete() {
        if (mCurrentPage == 1) {
            smartRefreshLayout.finishRefresh()
        } else {
            smartRefreshLayout.finishLoadMore()
        }
        smartRefreshLayout.setNoMoreData(!(mTotalCount > mList.size))
    }

    private fun getMsgList() {
        mPresenter.getMsgList(mCurrentPage)
    }

    override fun onMsgListGetSuccess(info: CommonListBody<MsgInfo>) {
        mTotalCount = info.totalCount
        if (mCurrentPage == 1) {
            mList.clear()
        }
        mList.addAll(info.items)
        refreshList()
    }

    override fun onMsgListGetFailure(msg: String) {
        showToast(msg)
    }

    override fun onReTry() {
        super.onReTry()
        getMsgList()
    }

    override fun onCustomerDetailInfoGetSuccess(content: CustomerDetailInfo) {
        CustomerDetailActivity.jump(context!!, content)
    }

    override fun onCustomerDetailInfoGetFailure() {

    }

}