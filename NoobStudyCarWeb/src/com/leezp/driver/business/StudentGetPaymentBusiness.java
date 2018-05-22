package com.leezp.driver.business;

import java.util.List;

import com.leezp.driver.business.interfaces.StudentGetPaymentBusinessInterface;
import com.leezp.driver.dao.GetPaymentDao;
import com.leezp.driver.dao.StudentDao;
import com.leezp.driver.entity.GetPaymentEntity;
import com.leezp.driver.entity.StudentEntity;

public class StudentGetPaymentBusiness implements StudentGetPaymentBusinessInterface{
	StudentDao student = new StudentDao();
	GetPaymentDao getPayment = new GetPaymentDao();

	@Override
	public int insertPaymentWay(String id, String phone, String password, GetPaymentEntity entity) {
		if(isExist(id, phone, password)) {
			if(String.valueOf(entity.getStudentID()).equals(id) && getPayment.getPaymentNum(entity.getStudentID()) < 5) {
				int rowNum = getPayment.insertPaymentWay(entity);
				return rowNum;
			} else {
				return 0;
			}
		}
		return -1;
	}

	@Override
	public List<GetPaymentEntity> getPaymentWay(String id, String phone, String password) {
		if(isExist(id, phone, password)) {
			return getPayment.getPaymentWay(Integer.valueOf(id));
		}
		return null;
	}

	@Override
	public int removePaymentWay(String id, String phone, String password, String payment_id) {
		if (isExist(id, phone, password)) {
			return getPayment.removePaymentWay(Integer.valueOf(payment_id), Integer.valueOf(id));
		}
		return -1;
	}
	
	private boolean isExist(String id, String phone, String password) {
		StudentEntity entity = student.findStudentByPhone(phone);
		if(entity == null || !String.valueOf(entity.getId()).equals(id) || !entity.getPassword().equals(password)) {
			return false;
		} else {
			return true;
		}
	}

}
