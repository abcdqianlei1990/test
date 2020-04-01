package cn.xzj.agent.ui.goldenbeans

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.UrlConfig
import cn.xzj.agent.core.common.mvp.MVPBaseActivity
import cn.xzj.agent.entity.CommonListBody
import cn.xzj.agent.entity.goldenbeans.GoldenBeansProductListInfo
import cn.xzj.agent.entity.goldenbeans.PurchasableItemInfo
import cn.xzj.agent.entity.reward.HierarchicalRewardInfo
import cn.xzj.agent.iview.IMyGoldenBeans
import cn.xzj.agent.iview.IMyRewardView
import cn.xzj.agent.presenter.MyGoldenBeansPresenter
import cn.xzj.agent.ui.WebViewActivity
import cn.xzj.agent.ui.adapter.GoldenBeansProductAdapter
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.LineSplitDecoration
import cn.xzj.agent.util.StatusBarUtil
import com.channey.utils.ShapeUtil
import com.channey.utils.StringUtils
import kotlinx.android.synthetic.main.activity_my_goldenbeans.*

class MyGoldenBeansActivity: MVPBaseActivity<MyGoldenBeansPresenter>(), IMyGoldenBeans, View.OnClickListener {

    private var mCount:Int = 0
    private var mGoldenBeanList = ArrayList<PurchasableItemInfo>()
    private var mGoldenBeansProductListInfo:GoldenBeansProductListInfo? = null
    companion object {
        val key_count = "beanscount"
        fun jump(context: Context,count:Int) {
            val intent = Intent(context, MyGoldenBeansActivity::class.java)
            intent.putExtra(key_count,count)
            context.startActivity(intent)
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_my_goldenbeans
    }

    override fun context(): Context {
        return this
    }

    override fun initParams() {
        super.initParams()
        mCount = intent.getIntExtra(key_count,0)
        activity_myGoldenBeans_count_tv.text = mCount.toString()
    }

    override fun initViews() {
        com.jaeger.library.StatusBarUtil.setColor(this,resources.getColor(R.color.green29AC3E),0)
        setTitle("我的金豆")
        setTitleBgGreen()
        setRightBtn("金豆说明", View.OnClickListener {
            WebViewActivity.jump(this,url = UrlConfig.goldenbeansRuleIIntro)
        })

        ShapeUtil.setShape(
                activity_myGoldenBeans_details_btn,
                radius = resources.getDimension(R.dimen.dp_12),
                strokeColor = resources.getColor(R.color.yellowD68C33)
        )
        ShapeUtil.setShape(
                activity_myGoldenBeans_bottomGroups,
                radius = resources.getDimension(R.dimen.dp3),
                solidColor = resources.getColor(R.color.white)
        )
        ShapeUtil.setShape(
                activity_myGoldenBeans_purchase_btn,
                radius = resources.getDimension(R.dimen.dp3),
                solidColor = resources.getColor(R.color.redFF7731)
        )
        initRecyclerView()
    }

    override fun setListeners() {
        super.setListeners()
        activity_myGoldenBeans_details_btn.setOnClickListener(this)
        activity_myGoldenBeans_purchase_btn.setOnClickListener(this)
        activity_myGoldenBeans_purchaseCount_ed.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (mGoldenBeansProductListInfo != null){
                    if (s != null && StringUtils.isNotEmpty(s.toString()) && s.toString().toInt() > 0){
                        var count = s.toString().toInt()
                        var amount = count * mGoldenBeansProductListInfo!!.unitPrice
                        activity_myGoldenBeans_purchase_amount_tv.text = "￥${StringUtils.doubleFormat(amount)}"
                        activity_myGoldenBeans_purchase_amount_tv.visibility = View.VISIBLE
                    }else{
                        activity_myGoldenBeans_purchase_amount_tv.text = "￥0"
                        activity_myGoldenBeans_purchase_amount_tv.visibility = View.GONE
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                
            }
        })
    }
    private lateinit var mAdapter:GoldenBeansProductAdapter
    private fun initRecyclerView(){
        mAdapter = GoldenBeansProductAdapter()
        mAdapter.data = mGoldenBeanList
        mAdapter.setOnPurchaseBtnClickListener(object : QuickAdapter.OnItemClickListener<PurchasableItemInfo> {
            override fun onItemClick(view: View, itemData: PurchasableItemInfo, i: Int) {
                purchase(itemData.gold.toInt(),itemData.price)
            }
        })
        activity_myGoldenBeans_list.adapter = mAdapter
        var layoutManager = LinearLayoutManager(this)
        activity_myGoldenBeans_list.layoutManager = layoutManager
        activity_myGoldenBeans_list.addItemDecoration(LineSplitDecoration(this,resources.getDimension(R.dimen.dp_10).toInt()))
    }

    override fun initData() {
        super.initData()
        mPresenter.getGoldenBeansProductList()
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.activity_myGoldenBeans_details_btn -> {
                    GoldenBeansChangeRecordActivity.jump(this)
                }
                R.id.activity_myGoldenBeans_purchase_btn -> {
                    var count = activity_myGoldenBeans_purchaseCount_ed.text.toString()
                    if (StringUtils.isNotEmpty(count) && count.toInt() > 0 && mGoldenBeansProductListInfo != null){
                        var temp = count.toInt()
                        var amount = temp * mGoldenBeansProductListInfo!!.unitPrice
                        purchase(count.toInt(),amount)
                    }
                }
            }
        }
    }

    private fun purchase(count:Int,amount:Double){
        GoldenBeansPurchaseActivity.jump(this,count,amount)
    }
    override fun onPurchasableGetSuccess() {

    }

    override fun onPurchasableGetFailure() {

    }

    override fun onProductListGetSuccess(info: GoldenBeansProductListInfo) {
        mGoldenBeansProductListInfo = info
        mGoldenBeanList.clear()
        mGoldenBeanList.addAll(info.items)
        mAdapter.notifyDataSetChanged()
    }

    override fun onProductListGetFailure() {

    }

    override fun onGoldenBeansCountGetSuccess(count: Int) {
        mCount = count
        activity_myGoldenBeans_count_tv.text = count.toString()
    }

    override fun onGoldenBeansCountGetFail() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            Code.RequestCode.GoldenBeansPurchaseActivity -> {
                if (resultCode == Code.ResultCode.OK){
                    mPresenter.getGoldenBeansCount()
                }
            }
        }
    }
}