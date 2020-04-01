package cn.xzj.agent.presenter

import cn.xzj.agent.core.common.mvp.BasePresenter
import cn.xzj.agent.entity.DownloadInfo
import cn.xzj.agent.iview.IJobSourceView
import cn.xzj.agent.net.download.DownloadFileManager
import cn.xzj.agent.util.CommonUtils

/**
 * @Author MarkYe
 * @Email yrmao9893@163.com
 * @Date 2019/1/22
 * @Des
 */
class JobSourcePresenter : BasePresenter<IJobSourceView>() {

    fun downloadImage(selectImageUrls: ArrayList<String>) {
        for (url in selectImageUrls) {
            val downloadInfo = DownloadInfo()
            downloadInfo.url = url
            downloadInfo.name = lastName(url)
            downloadInfo.savePath = CommonUtils.getAgentDownloadRootFile().path + "/" + lastName(url)
            DownloadFileManager.getInstance().startDown(downloadInfo)
        }

    }

    private fun lastName(url: String): String {
        val index = url.lastIndexOf("/")
        return url.substring(index + 1)
    }

}
