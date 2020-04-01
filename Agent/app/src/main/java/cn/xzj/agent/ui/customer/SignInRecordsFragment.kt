package cn.xzj.agent.ui.customer

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.core.common.BaseActivity
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.RefreshCustomerRecordesMessage
import cn.xzj.agent.entity.customer.CustomerDetailInfo
import cn.xzj.agent.entity.customer.QiandaoRecordInfo
import cn.xzj.agent.i.OnRecyclerViewItemClickListener
import cn.xzj.agent.iview.IQiandaoRecordsView
import cn.xzj.agent.presenter.QiandaoRecordsPresenter
import cn.xzj.agent.ui.adapter.QiandaoRecordAdapter
import kotlinx.android.synthetic.main.fragment_qiandao_records.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * 签到记录
 */
class SignInRecordsFragment: MVPBaseFragment<QiandaoRecordsPresenter>(),OnRecyclerViewItemClickListener,IQiandaoRecordsView{


    private lateinit var mAdapter: QiandaoRecordAdapter
    private var mList = ArrayList<QiandaoRecordInfo>()
    private var mCurrentPage = 1
    private var mTotalCount = 0
    private lateinit var mDetailInfo: CustomerDetailInfo
    override fun context(): Context {
        return context!!
    }
    override fun initLayout(): Int {
        return R.layout.fragment_qiandao_records
    }

    override fun initParams() {
        mDetailInfo = (activity as CustomerDetailActivity).getDetailInfoForFragment()
    }

    override fun initViews() {
        initRecyclerView()
    }

    override fun initData() {
        getRecords()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDefaultEvent(event: RefreshCustomerRecordesMessage) {
        loadData()
    }
    
    override fun onRecordsGetSuccess(info:CommonListBody<QiandaoRecordInfo>){
        mTotalCount = info.totalCount
        if (mCurrentPage == 1){
            mList.clear()
        }
        mList.addAll(info.items)
//        mList.add(QiandaoRecordInfo(positionName = "我是中国人",returnFee = 10.00,signInTime = "fwef",storeName = "我是中国人",positionTypes = arrayListOf("日日饭","周薪","日日饭","周薪","日日饭","周薪","日日饭")))
//        mList.add(QiandaoRecordInfo(positionName = "我是中国人",returnFee = 10.00,signInTime = "fwef",storeName = "我是中国人",positionTypes = arrayListOf("日日饭","周薪")))
        refreshList()
    }
    
    override fun onRecordsGetFailure(msg:String){ showToast(msg)
        refreshList()
    }

    override fun onClick(view: View, position: Int) {

    }

    private fun initRecyclerView(){
        mAdapter=QiandaoRecordAdapter(context!!,mList)
        mAdapter.setOnUploadBtnClickListener(object : OnRecyclerViewItemClickListener {
            override fun onClick(view: View, position: Int) {
                AddWorkExperienceActivity.jump(activity as BaseActivity,mDetailInfo!!.userId)
            }
        })
        qiandao_records_recyclerView.adapter = mAdapter
        val layoutManager = LinearLayoutManager(context)
        qiandao_records_recyclerView.layoutManager = layoutManager
        smartRefreshLayout.isEnableLoadMore = true
        smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getRecords()
        }
        smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage ++
            getRecords()
        }
    }
    
    private fun refreshList(){
        loadingComplete()
        mAdapter.notifyDataSetChanged()
    }

    private fun loadingComplete(){
        if (mCurrentPage==1){
            smartRefreshLayout.finishRefresh()
        }else{
            smartRefreshLayout.finishLoadMore()
        }
        smartRefreshLayout.setNoMoreData(!(mTotalCount > mList.size))
    }

    override fun loadData() {
        super.loadData()
        getRecords()
    }

    private fun getRecords(){
        mPresenter.getCustomerQiandaoRecords(mDetailInfo.userId,mCurrentPage)
    }

    override fun onResume() {
        super.onResume()
        smartRefreshLayout.autoRefresh()
    }

}