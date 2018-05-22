package com.leezp.driver.business.interfaces;

import java.util.Map;

public interface RelationCompleteOrderBusinessInterface {
	//获取关于完成订单的相关信息
	Map<String, String> getRelationCompleteOrder(String role, String id, String studentID, String driverID);
}
