package cn.xzj.agent.i

import cn.xzj.agent.ui.fragment.TaskListFragment

interface DataGetSuccessListener {
    fun onDataGetSuccess(size:Int,instance:TaskListFragment)
}