package cn.xzj.agent.ui.job.source

import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentTransaction
import cn.xzj.agent.R
import cn.xzj.agent.core.common.QuickActivity
import kotlinx.android.synthetic.main.activity_download.*

/**
 *
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/23
 * @Des 下载文件页面
 */
class DownloadActivity : QuickActivity() {
    private lateinit var mDownloadingFragment: DownloadingFragment
    private lateinit var mDownloadedFragment: DownloadedFragment
    private lateinit var mFragmentTransaction: FragmentTransaction
    override fun getLayoutId(): Int {
        return R.layout.activity_download
    }

    override fun initViews() {
        setLifeBack()
        setTitle(getString(R.string.activity_download_title))
        initFragment()
        initTabLayout()
        //默认选中下载完成
        tabLayoutActivityDownload.getTabAt(1)!!.select()
//        selectFragment(1)
    }

    private fun initFragment() {
        mDownloadingFragment = DownloadingFragment()
        mDownloadedFragment = DownloadedFragment()
        mFragmentTransaction = supportFragmentManager.beginTransaction()
        mFragmentTransaction.add(R.id.frameLayoutActivityDownloadContent, mDownloadingFragment)
        mFragmentTransaction.add(R.id.frameLayoutActivityDownloadContent, mDownloadedFragment)
        mFragmentTransaction.commit()
    }

    private fun initTabLayout() {
        tabLayoutActivityDownload.addTab(tabLayoutActivityDownload.newTab().setText("正在下载"))
        tabLayoutActivityDownload.addTab(tabLayoutActivityDownload.newTab().setText("下载完成"))
        tabLayoutActivityDownload.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                selectFragment(tab!!.position)
            }
        })
    }

    private fun selectFragment(position: Int) {
        mFragmentTransaction = supportFragmentManager.beginTransaction()
        when (position) {
            0 -> {
                mFragmentTransaction.show(mDownloadingFragment)
                        .hide(mDownloadedFragment)
                        .commitAllowingStateLoss()
                mDownloadingFragment.onRefresh()
            }
            1 -> {
                mFragmentTransaction.show(mDownloadedFragment)
                        .hide(mDownloadingFragment)
                        .commitAllowingStateLoss()
                mDownloadedFragment.onRefresh()
            }
        }
    }

}