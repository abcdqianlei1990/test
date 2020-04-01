package cn.xzj.agent.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import cn.xzj.agent.entity.task.TaskTypeInfo

class ViewAdapter(var fm:FragmentManager,var list: List<Fragment>,var types:List<TaskTypeInfo>):FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var type = types[position]
//        Log.d("qian","getPageTitle "+type.toString())
        var name = type.name.replace("任务","")
        return if (type.quantity == 0) name else name+type.quantity
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
    }
}