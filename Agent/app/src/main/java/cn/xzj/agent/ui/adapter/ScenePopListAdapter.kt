package cn.xzj.agent.ui.adapter

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.view.View
import cn.xzj.agent.R
import cn.xzj.agent.entity.common.KeyValue
import cn.xzj.agent.ui.adapter.common.BaseHolder
import cn.xzj.agent.ui.adapter.common.QuickAdapter

class ScenePopListAdapter : QuickAdapter<KeyValue>(R.layout.item_scene_list) {
    private var select:KeyValue? = null
    override fun convert(holder: BaseHolder, item: KeyValue, position: Int) {
        var contentTv = holder.getView<AppCompatTextView>(R.id.item_scene_content_tv)
        var radioBtn = holder.getView<AppCompatImageView>(R.id.item_scene_rb)
        var line = holder.getView<View>(R.id.item_scene_line)
        contentTv.text = item.value
        //选中状态
        if (select != null){
            setChecked(radioBtn,select!!.key == item.key)
        }
        //分割线
        if (position == itemCount - 1){
            line.visibility = View.INVISIBLE
        }else{
            line.visibility = View.VISIBLE
        }
    }

    private fun setChecked(radioBtn:AppCompatImageView,checked:Boolean){
        if (checked){
            radioBtn.setImageResource(R.mipmap.ic_selected)
        }else{
            radioBtn.setImageResource(R.mipmap.ic_select)
        }
    }

    fun setDefaultSelect(select:KeyValue?){
        this.select = select
    }
}