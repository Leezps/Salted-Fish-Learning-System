package com.leezp.driver.business.interfaces;

import com.leezp.driver.entity.DriverEntity;

public interface DriverLoginBusinessInterface {
	DriverEntity driverLogin(String phone, String password);
}
