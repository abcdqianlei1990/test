package cn.xzj.agent.entity

/**
 * {
"footerOffset": 0,
"headerOffset": 0,
"items": [
],
"pageNo": 0,
"pageSize": 0,
"totalCount": 0
}
 */
data class CommonListBody<T>(var items: List<T>, var pageNo: Int, var pageSize: Int, var totalCount: Int, var footerOffset: Int, var headerOffset: Int)