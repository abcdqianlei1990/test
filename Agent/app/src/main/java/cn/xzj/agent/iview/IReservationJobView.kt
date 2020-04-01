package cn.xzj.agent.iview

import android.content.Context
import cn.xzj.agent.entity.BaseResponseInfo
import cn.xzj.agent.entity.job.PickLocationVO

/**
 *
 * @ Author yemao
 * @ Email yrmao9893@163.com
 * @ Date 2018/8/16
 * @ Des
 */
interface IReservationJobView {
    fun getContext(): Context
    fun getReservationDate(data: BaseResponseInfo<List<Long>>)
    fun getReservationLocations(data: BaseResponseInfo<List<PickLocationVO>>)
    fun jobReservationSuccess(data: BaseResponseInfo<Any>)
    fun jobReservationError(e: Throwable?, isNetWorkError: Boolean)
}