package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.RegionJsonBean
import cn.xzj.agent.entity.RegionJsonPickerBean
import cn.xzj.agent.entity.customer.City
import cn.xzj.agent.entity.customer.RemarkVO
import cn.xzj.agent.iview.IUserInfoRemarkView
import cn.xzj.agent.net.Client
import cn.xzj.agent.net.QuickObserver
import cn.xzj.agent.util.Util
import com.alibaba.fastjson.JSON
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserInfoRemarkPresenter : BasePresenter<IUserInfoRemarkView>() {

    fun remark(body: RemarkVO) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.commitUserInfoRemark(body)
        val mObserver = object : QuickObserver<Boolean>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<Boolean>?) {
                mView.onRemarkSuccess()
            }

            override fun onCodeError(t: BaseResponseInfo<Boolean>?) {
                super.onCodeError(t)
                mView.onRemarkFailure()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRemarkFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getRemarkInfo(userId: String) {
        val apiManager = Client.getInstance(mView.context()).apiManager
        val observable = apiManager.getUserInfoRemark(userId)
        val mObserver=object : QuickObserver<RemarkVO>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog(true)
            }

            override fun onSuccessful(t: BaseResponseInfo<RemarkVO>) {
                mView.onRemarkGetSuccess(t.content)
            }

            override fun onCodeError(t: BaseResponseInfo<RemarkVO>?) {
                super.onCodeError(t)
                mView.onRemarkGetFailure()
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.onRemarkGetFailure()
            }

        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    /**
     * 获取所有期望城市
     */
    fun getRegions() {
        val  mObserver=object : QuickObserver<List<City>>(mView.context()) {
            override fun onRequestStart() {
                super.onRequestStart()
                showProgressDialog()
            }

            override fun onSuccessful(t: BaseResponseInfo<List<City>>?) {
                mView.getRegionsSuccess(t!!.content as ArrayList<City>)
            }

            override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                mView.getRegionsFail()
            }

        }
         Client.getInstance(mView.context()).apiManager
                .regions
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver)
        addSubscribe(mObserver)
    }

    fun getLocationRegions() {
        val subscription = Observable.create<RegionJsonPickerBean> {
            val json = Util.getJsonFromAssets(mView.context(), "province_data.json")
            val jsonArray = JSON.parseArray(json, RegionJsonBean::class.java)
            val options1Items = ArrayList<String>()
            val options2Items = ArrayList<ArrayList<String>>()
            val options3Items = ArrayList<ArrayList<ArrayList<String>>>()

            //  遍历数据组
            for (i in 0 until jsonArray.size) {//遍历省份
                options1Items.add(jsonArray[i].name)
                val cityList = ArrayList<String>()//该省的城市列表（第二级）
                val provinceAreaList = ArrayList<ArrayList<String>>()//该省的所有地区列表（第三极）

                if (jsonArray[i].city == null)
                    break
                for (j in 0 until jsonArray[i].city.size) {
                    cityList.add(jsonArray[i].city[j].name)//添加城市
                    val cityAreaList = ArrayList<String>()//该城市的所有地区列表
                    //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                    if (jsonArray[i].city[j].area == null || jsonArray[i].city[j].area.size == 0) {
                        cityAreaList.add("")
                    } else {
                        cityAreaList.addAll(jsonArray[i].city[j].area)
                    }
                    provinceAreaList.add(cityAreaList)
                }
                /**
                 * 添加城市数据
                 */
                options2Items.add(cityList)

                /**
                 * 添加地区数据
                 */
                options3Items.add(provinceAreaList)
            }

            val mRegionJsonPickerBean = RegionJsonPickerBean()
            mRegionJsonPickerBean.options1Items = options1Items
            mRegionJsonPickerBean.options2Items = options2Items
            mRegionJsonPickerBean.options3Items = options3Items
            it.onNext(mRegionJsonPickerBean)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    mView.getLocationRegionSuccess(it)
                }

    }

}