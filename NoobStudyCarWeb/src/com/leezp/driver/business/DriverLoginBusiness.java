package com.leezp.driver.business;

import com.leezp.driver.business.interfaces.DriverLoginBusinessInterface;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.entity.DriverEntity;

public class DriverLoginBusiness implements DriverLoginBusinessInterface{
	private DriverDao driverDao = new DriverDao();
	
	@Override
	public DriverEntity driverLogin(String phone, String password) {
		DriverEntity entity = driverDao.findDriverByPhone(phone);
		if(entity != null && entity.getPassword().equals(password)) {
			return entity;
		}
		return null;
	}

}
