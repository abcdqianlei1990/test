package cn.xzj.agent.ui.fragment

import cn.xzj.agent.R
import cn.xzj.agent.constants.Constants
import cn.xzj.agent.constants.EnumValue
import cn.xzj.agent.core.common.BaseFragment
import cn.xzj.agent.ui.customer.CustomerSearchActivity
import cn.xzj.agent.util.CommonUtils
import cn.xzj.agent.util.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_customer.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/2/26
 * @Des 客户列表
 */
class CustomerFragment : BaseFragment() {
    private lateinit var mNormalCustomerFragment: CustomerListFragment
    private lateinit var mCoreCustomerFragment: CoreCustomerFragment
    private lateinit var mActiveCustomerFragment: ActiveCustomerFragment

    override fun initLayout(): Int {
        return R.layout.fragment_customer
    }

    override fun initParams() {

    }

    override fun initViews() {
        initFragment()
//        viewPagerFragmentCustomer.offscreenPageLimit = 1
//        tabLayoutFragmentCustomer.setViewPager(viewPagerFragmentCustomer
//                , arrayOf("客户列表")
//                , activity, arrayListOf(mNormalCustomerFragment))


    }

    override fun setListeners() {
        super.setListeners()
        ivFragmentCustomerSearch.setOnClickListener {
            gotoCustomerSearch()
        }
    }

    override fun initData() {
    }

    private fun gotoCustomerSearch() {
//        when (tabLayoutFragmentCustomer.currentTab) {
//            0 -> {
//                CustomerSearchActivity.jump(context!!, CustomerSearchActivity.TYPE_NOMARL)
//                CommonUtils.statistics(context, Constants.STATISTICS_CUSTOMER_SEARCH_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
//            }
//            1 -> {
//                CustomerSearchActivity.jump(context!!, CustomerSearchActivity.TYPE_CORE)
//                CommonUtils.statistics(context, Constants.STATISTICS_coreCustomerSearch_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
//            }
//            2 -> {
//                CustomerSearchActivity.jump(context!!, CustomerSearchActivity.TYPE_ACTIVE)
//                CommonUtils.statistics(context, Constants.STATISTICS_activeCustomerSearch_ID, EnumValue.STATISTICS_TYPE_BTN, EnumValue.STATISTICS_METRIC_CLICK)
//            }
//        }
    }

    private fun initFragment() {
        mNormalCustomerFragment = CustomerListFragment()
        mCoreCustomerFragment = CoreCustomerFragment()
        mActiveCustomerFragment = ActiveCustomerFragment()
    }

    override fun onResume() {
        super.onResume()
    }

}