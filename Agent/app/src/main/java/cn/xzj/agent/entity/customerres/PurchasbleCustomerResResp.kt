package cn.xzj.agent.entity.customerres

import cn.xzj.agent.entity.CommonListBody

class PurchasbleCustomerResResp(var purchasable: CommonListBody<PurchasbleResInfo>,var remainingQuantity:Int) {
}