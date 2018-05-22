package com.leezp.driver.business;

import java.util.Map;

import com.leezp.driver.business.interfaces.RelationCompleteOrderBusinessInterface;
import com.leezp.driver.dao.CompleteOrderDao;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.dao.StudentDao;

public class RelationCompleteOrderBusiness implements RelationCompleteOrderBusinessInterface{
	private CompleteOrderDao completeOrder = new CompleteOrderDao();
	private DriverDao driver = new DriverDao();
	private StudentDao student = new StudentDao();

	@Override
	public Map<String, String> getRelationCompleteOrder(String role, String id, String studentID, String driverID) {
		Map<String, String> map = completeOrder.getStudentAndDriverById(Integer.valueOf(id));
		if(map.get("driverID").equals(driverID) && map.get("studentID").equals(studentID)) {
			if(role.equals("0")) {
				return driver.findHeadAndNamePhoneById(Integer.valueOf(driverID));
			} else if(role.equals("1")) {
				return student.findHeadAndNamePhoneById(studentID);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

}
