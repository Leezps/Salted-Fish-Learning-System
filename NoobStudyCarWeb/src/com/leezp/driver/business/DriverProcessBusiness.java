package com.leezp.driver.business;

import java.util.List;

import com.leezp.driver.business.interfaces.DriverProcessBusinessInterface;
import com.leezp.driver.dao.DriverProcessDao;
import com.leezp.driver.entity.DriverProcessEntity;

public class DriverProcessBusiness implements DriverProcessBusinessInterface{
	DriverProcessDao driverProcess = new DriverProcessDao();

	@Override
	public List<DriverProcessEntity> getDriverProcess() {
		return driverProcess.getDriverProcess();
	}

}
