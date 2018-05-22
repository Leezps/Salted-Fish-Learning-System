package com.leezp.driver.business.interfaces;

import java.util.List;

import com.leezp.driver.entity.DriverEntity;

public interface DriverShowBusinessInterface {
	//通过地址查询出所有同意地点的教练
	List<DriverEntity> getDriversByPlace(String place);
}
