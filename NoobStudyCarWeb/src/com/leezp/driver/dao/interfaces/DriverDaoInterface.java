package com.leezp.driver.dao.interfaces;

import java.util.List;
import java.util.Map;

import com.leezp.driver.entity.DriverEntity;

public interface DriverDaoInterface {
	//通过手机号获取教练信息
	DriverEntity findDriverByPhone(String phone);
	//通过教练id获取教练的单位价格以及学车地点
	Map<String, Object> findUnitPriceAndPlaceById(int id);
	//注册教练手机号以及密码
	int insertBaseDriver(String phone, String password);
	//上传或修改教练信息
	int uploadOrUpdateDriverInformation(DriverEntity entity);
	//修改密码
	int updateDriverPassword(String phone, String password);
	//根据城市按照评分下降方式获取教练信息进行预约
	List<DriverEntity> findDriversByPlaceAndEvaluate(String place);
	//根据教练id获取教练的头像地址、姓名、手机号
	Map<String, String> findHeadAndNamePhoneById(int id);
}
