package cn.xzj.agent.ui.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class RecordsPageAdapter(fm:FragmentManager, var list: List<Fragment>):FragmentPagerAdapter(fm){
    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return list.size
    }
}