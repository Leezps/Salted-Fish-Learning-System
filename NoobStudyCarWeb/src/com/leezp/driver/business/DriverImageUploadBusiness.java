package com.leezp.driver.business;

import com.leezp.driver.business.interfaces.DriverImageUploadBusinessInterface;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.entity.DriverEntity;

public class DriverImageUploadBusiness implements DriverImageUploadBusinessInterface{
	private DriverDao driver = new DriverDao();

	@Override
	public int changeImageUrl(String code, String path, String id, String phone, String password) {
		DriverEntity entity = getDriverInformation(phone);
		if(entity != null && password.equals(entity.getPassword()) && id.equals(String.valueOf(entity.getId()))) {
			if (code.equals("0")) {
				entity.setHead_url(path);
				return driver.uploadOrUpdateDriverInformation(entity);
			} else if(code.equals("1")) {
				entity.setIdentify_image_url(path);
				return driver.uploadOrUpdateDriverInformation(entity);
			} else if(code.equals("2")) {
				entity.setTrainer_image_url(path);
				return driver.uploadOrUpdateDriverInformation(entity);
			}
		}
		return -1;
	}

	@Override
	public DriverEntity getDriverInformation(String phone) {
		return driver.findDriverByPhone(phone);
	}

}
