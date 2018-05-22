package com.leezp.driver.business.interfaces;

import com.leezp.driver.entity.DriverEntity;

public interface DriverImageUploadBusinessInterface {
	int changeImageUrl(String code, String path, String id, String phone, String password);
	DriverEntity getDriverInformation(String phone);
}
