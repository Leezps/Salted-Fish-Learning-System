package com.leezp.driver.business.interfaces;

import java.util.Map;

public interface RelationCompleteOrderBusinessInterface {
	//��ȡ������ɶ����������Ϣ
	Map<String, String> getRelationCompleteOrder(String role, String id, String studentID, String driverID);
}
