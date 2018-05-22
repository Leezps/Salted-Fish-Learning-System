package com.leezp.driver.business;

import java.util.List;

import com.leezp.driver.business.interfaces.DriverShowBusinessInterface;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.entity.DriverEntity;

public class DriverShowBusiness implements DriverShowBusinessInterface{
	private DriverDao driverDao = new DriverDao();

	@Override
	public List<DriverEntity> getDriversByPlace(String place) {
		return driverDao.findDriversByPlaceAndEvaluate(place);
	}

}
