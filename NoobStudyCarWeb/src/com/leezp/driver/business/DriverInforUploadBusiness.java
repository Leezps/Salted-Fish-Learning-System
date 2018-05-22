package com.leezp.driver.business;

import com.leezp.driver.business.interfaces.DriverInforUploadBusinessInterface;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.entity.DriverEntity;

public class DriverInforUploadBusiness implements DriverInforUploadBusinessInterface{
	private DriverDao driverDao = new DriverDao();

	@Override
	public int updateDriverInfor(DriverEntity entity) {
		if(entity == null) {
			return -1;
		}
		DriverEntity driver = driverDao.findDriverByPhone(entity.getPhone());
		if(driver != null && driver.getId() == entity.getId()) {
			driver.setName(entity.getName());
			driver.setSex(entity.getSex());
			driver.setPlace(entity.getPlace());
			driver.setIdentify_number(entity.getIdentify_number());
			driver.setInformation(entity.getInformation());
			driver.setUnit_price(entity.getUnit_price());
			return driverDao.uploadOrUpdateDriverInformation(driver);
		} else {
			return -1;
		}
	}

}
