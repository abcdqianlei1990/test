package cn.xzj.agent.ui.location

import android.content.Context
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.constants.Code
import cn.xzj.agent.constants.Keys
import cn.xzj.agent.core.common.QuickActivity
import cn.xzj.agent.entity.customer.City
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter
import cn.xzj.agent.ui.adapter.common.decoration.SimpleItemDecoration
import kotlinx.android.synthetic.main.activity_select_expect_city.*

/**
 * 选择城市页面
 */
class SelectExpectCityActivity : QuickActivity() {
    private var mCityList: ArrayList<City>? = null

    companion object {
        fun newInstance(context: Context, cityList: ArrayList<City>): Intent {
            val mIntent = Intent()
            mIntent.setClass(context, SelectExpectCityActivity::class.java)
            mIntent.putExtra(Keys.DATA_KEY_1, cityList)
            return mIntent
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_select_expect_city
    }


    override fun initParams() {
        mCityList = intent.getSerializableExtra(Keys.DATA_KEY_1) as ArrayList<City>?
    }

    override fun initViews() {
        setTitle("选择城市")
        setLifeBack()
        rv_region.layoutManager = LinearLayoutManager(this)
        rv_region.adapter = mCityAdapter
        mCityAdapter.setOnItemClickListener(object : QuickAdapter.OnItemClickListener<City> {
            override fun onItemClick(view: View, itemData: City, i: Int) {
                val intent = Intent()
                intent.putExtra(Keys.CITY, itemData)
                setResult(Code.ResultCode.OK, intent)
                finish()
            }
        })
        rv_region.addItemDecoration(SimpleItemDecoration.builder(this)
                .build())
        mCityAdapter.setNewData(mCityList!!)
    }

    override fun initData() {
    }

    private var mCityAdapter = object : QuickAdapter<City>(R.layout.item_province_city) {
        override fun convert(holder: BaseHolder, item: City, position: Int) {
            holder.setText(R.id.item_city_tv, item.name)
        }
    }
}