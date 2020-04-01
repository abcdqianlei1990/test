package cn.xzj.agent.ui.customerres

import android.content.Context
import android.content.Intent
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.core.common.mvp.MVPBaseFragment
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.customerres.PurchasbleCustomerResResp
import cn.xzj.agent.entity.customerres.PurchasbleResInfo
import cn.xzj.agent.entity.customerres.ResPurchaseResp
import cn.xzj.agent.iview.IResPurchase
import cn.xzj.agent.presenter.ResPurchasePresenter
import cn.xzj.agent.ui.WebViewActivity
import cn.xzj.agent.ui.adapter.PurchasbleResAdapter
import cn.xzj.agent.ui.adapter.common.CommonListAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.LineSplitDecoration
import cn.xzj.agent.util.DialogUtil
import kotlinx.android.synthetic.main.activity_res_purchase.*
import java.lang.StringBuilder

class ResPurchaseActivity: MVPBaseFragment<ResPurchasePresenter>(), IResPurchase,View.OnClickListener {
    override fun initLayout(): Int {
        return R.layout.activity_res_purchase
    }

    override fun initParams() {
        
    }

    companion object {
        fun jump(context: Context) {
            val intent = Intent(context, ResPurchaseActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun context(): Context {
        return context!!
    }

    override fun initViews() {
//        setTitle("购买资源")
//        setRightBtn("购买记录",View.OnClickListener {
//            ResPurchaseRecordsActivity.jump(this)
//        })
        initRecyclerView()
    }

    override fun setListeners() {
        super.setListeners()
        activity_resPurchase_purchase_btn.setOnClickListener(this)
        activity_resPurchase_notice_tv.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.activity_resPurchase_notice_tv -> {
                    WebViewActivity.jump(activity!!,"https://h5activity.xiaozhijie.com/activity/buysay.html")
                }
                R.id.activity_resPurchase_purchase_btn -> {
                    if (mSelectedList.isEmpty()){
                        showToast("请选择要购买的资源")
                        return
                    }
                    mPresenter.getGoldenBeansCount()
                }
            }
        }
    }

    private lateinit var mAdapter:PurchasbleResAdapter
    private var mTotalCount = 0
    private var mCurrentPage = 1
    private var mList = ArrayList<PurchasbleResInfo>()
    private var mSelectedList = ArrayList<PurchasbleResInfo>()
    private var mTotalCost = 0  //购买的总花费
    private fun initRecyclerView() {
        mAdapter = PurchasbleResAdapter()
        mAdapter.data = mList
        mAdapter.mOnItemClickListener = object : QuickAdapter.OnItemClickListener<PurchasbleResInfo> {
            override fun onItemClick(view: View, itemData: PurchasbleResInfo, i: Int) {
                if (mSelectedList.contains(itemData)){
                    mSelectedList.remove(itemData)
                    mTotalCost -= itemData.gold
                    itemData.selected = false
                    mAdapter.notifyItemChanged(i)
                }else{
                    if (mSelectedList.size >= mRemainingQuantity){
                        showToast("您可购买的用户数为$mRemainingQuantity,当前已不可继续购买")
                    }else{
                        mSelectedList.add(itemData)
                        mTotalCost += itemData.gold
                        itemData.selected = true
                        mAdapter.notifyItemChanged(i)
                    }
                }
                activity_resPurchase_costTitle_tv.text = "共计${mSelectedList.size}人，合计："
                activity_resPurchase_cost_tv.text = "${mTotalCost}金豆"
            }
        }
        activity_resPurchase_list.layoutManager = LinearLayoutManager(context())
        activity_resPurchase_list.adapter = mAdapter
        activity_resPurchase_list.addItemDecoration(LineSplitDecoration(context(),splitSize = resources.getDimension(R.dimen.dp_10).toInt()))
        activity_resPurchase_smartRefreshLayout.isEnableLoadMore = true
        activity_resPurchase_smartRefreshLayout.setOnRefreshListener {
            mCurrentPage = 1
            getPurchasbleCustomerRes()
        }
        activity_resPurchase_smartRefreshLayout.setOnLoadMoreListener {
            mCurrentPage++
            getPurchasbleCustomerRes()
        }
    }

    private fun loadingComplete() {
        if (mCurrentPage <= 1) {
            activity_resPurchase_smartRefreshLayout.finishRefresh()
        } else {
            activity_resPurchase_smartRefreshLayout.finishLoadMore()
        }

        activity_resPurchase_smartRefreshLayout.setNoMoreData(mTotalCount <= mAdapter.data!!.size)
    }

    override fun initData() {
        getPurchasbleCustomerRes()
    }

    private fun getPurchasbleCustomerRes(){
        mPresenter.getPurchasbleCustomerRes(mCurrentPage)
    }

    private var mRemainingQuantity = 0
    override fun onPurchasbleResGetSuccess(info: PurchasbleCustomerResResp) {
        mTotalCount  = info.purchasable.totalCount
        mRemainingQuantity  = info.remainingQuantity
        if (mCurrentPage == 1) mList.clear()
        mList.addAll(info.purchasable.items)
        mAdapter.notifyDataSetChanged()
        loadingComplete()
    }

    override fun onPurchasbleResGetFailure() {
        loadingComplete()
    }

    override fun onGoldenBeansCountGetSuccess(count: Int) {
        if (mTotalCost > count){
            DialogUtil.showNoticeDialog(context(),title = "您的金豆数量不够，请购买金豆")
        }else{
            DialogUtil.showNoticeDialogWithCancelConfirm(context(),title = "确定使用${mTotalCost}金豆购买${mSelectedList.size}个用户？",confirmListener = View.OnClickListener{
                DialogUtil.dismissNoticeDialog()
                var list = ArrayList<String>()
                for (info in mSelectedList){
                    list.add(info.userId)
                }
                mPresenter.customerResPurchase(list)
            })
        }
    }

    override fun onGoldenBeansCountGetFail() {

    }

    override fun onResPurchaseSuccess(resp: ResPurchaseResp) {
        var sb = StringBuilder()
        var remainingQuantityStr = "还可以购买的用户数：${resp.remainingQuantity}"
        sb.append(remainingQuantityStr)
        sb.append("\n")
        if (resp.failureInOrder.isNotEmpty()){
            var failureInOrderStr = StringBuilder("下单失败的客户：")
            for (i  in 0 until resp.failureInOrder.size){
                failureInOrderStr.append(resp.failureInOrder[i].phone)
                if (i < resp.failureInOrder.size){
                    failureInOrderStr.append(",")
                }
            }
            sb.append(failureInOrderStr)
        }
        DialogUtil.showNoticeDialog(context(),title = "购买成功",content = sb.toString())
        mTotalCost = 0
        mSelectedList.clear()
        activity_resPurchase_costTitle_tv.text = "共计0人，合计："
        activity_resPurchase_cost_tv.text = "0金豆"
        mCurrentPage = 1
        mPresenter.getPurchasbleCustomerRes(mCurrentPage)
    }

    override fun onResPurchaseFail() {

    }
}