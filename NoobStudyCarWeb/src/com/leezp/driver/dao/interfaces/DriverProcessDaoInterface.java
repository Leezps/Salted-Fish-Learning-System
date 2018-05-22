package com.leezp.driver.dao.interfaces;

import java.util.List;

import com.leezp.driver.entity.DriverProcessEntity;

public interface DriverProcessDaoInterface {
	//查询学车流程
	List<DriverProcessEntity> getDriverProcess();
}
