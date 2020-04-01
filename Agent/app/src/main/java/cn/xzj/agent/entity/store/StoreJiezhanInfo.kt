package cn.xzj.agent.entity.store

/**
 * 门店接站信息
 *
 *address (string, optional): 地址 ,
 *location (string, optional): 地点 ,
 *storeId (string, optional): 门店ID ,
 *storeName (string, optional): 门店名称
 */
data class StoreJiezhanInfo(var address:String,var location:String,var storeId:String,var storeName:String)