package com.leezp.driver.business;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.leezp.driver.business.interfaces.PublishOrderBusinessInterface;
import com.leezp.driver.dao.CompleteOrderDao;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.dao.PublishOrderDao;
import com.leezp.driver.dao.StudentDao;
import com.leezp.driver.entity.CompleteOrderEntity;
import com.leezp.driver.entity.DriverEntity;
import com.leezp.driver.entity.PublishOrderEntity;
import com.leezp.driver.entity.StudentEntity;

public class PublishOrderBusiness implements PublishOrderBusinessInterface{
	private DriverDao driver = new DriverDao();
	private StudentDao student = new StudentDao();
	private PublishOrderDao publishOrder = new PublishOrderDao();
	private CompleteOrderDao completeOrder = new CompleteOrderDao();
	private float bookOrderTime = 0;

	/**
	 * @return
	 * 		-1������ͬһ��
	 * 		-2����ʼʱ���ڽ���ʱ��֮��
	 * 		-3�������Ķ���ʱ���С��2��Сʱ
	 * 		-4��ʱ�����ݽ�����������
	 * 		-5���������ݸ�ʽ�����û�и��û�
	 * 		 0����ʱ��εĶ�������ǰ�����Ķ������ֳ�ͻ
	 * 		>0: ���뷢������
	 */
	@Override
	public int insertPublishOrder(String id, String phone, String password, PublishOrderEntity entity) {
		if(id.equals(String.valueOf(entity.getDriverID())) && isExistDriver(id, phone, password)) {
			int state = isRightPublishOrder(entity, false);
			if(state != 0) {
				return state;
			}
			if(publishOrder.isExistTimeSlotOrder(0, entity.getDriverID(), entity.getBeginTime(), entity.getEndTime()) == 0) {
				return publishOrder.insertPublishOrder(entity);
			}
			return 0;
		}
		return -5;
	}
	
	private boolean isExistDriver(String id, String phone, String password) {
		DriverEntity entity = driver.findDriverByPhone(phone);
		if(entity != null && id.equals(String.valueOf(entity.getId())) && password.equals(entity.getPassword())) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public List<PublishOrderEntity> getPublishOrderByDriverID(String driverID) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 1);
		String date = dateFormat.format(calendar.getTime());
		return publishOrder.getPublishOrderByDriverAndDate(Integer.valueOf(driverID), date);
	}

	/**
	 * @return
	 * 		-1������ͬһ��
	 * 		-2����ʼʱ���ڽ���ʱ��֮��
	 * 		-3��ԤԼ������ʱ��С��1Сʱ
	 * 		-4��ʱ�����ݽ�����������
	 * 		-5���������ݸ�ʽ�����û�и��û�
	 * 		-6: ��ɶ������ж�����Ԥ��������ͻ
	 * 		 0��û�и�ʱ��εķ�������
	 * 		>0: ������ɶ���
	 */
	@Override
	public int studentBookOrder(String id, String phone, String password, PublishOrderEntity entity) {
		if(isExistStudent(id, phone, password)) {
			int state = isRightPublishOrder(entity, true);
			if(state != 0) {
				return state;
			}
			if(publishOrder.isExistTimeSlotOrder(entity.getId(), entity.getDriverID(), entity.getBeginTime(), entity.getEndTime()) == 1) {
				Map<String, Object> map = driver.findUnitPriceAndPlaceById(entity.getDriverID());
				String place = String.valueOf(map.get("place"));
				float price = Float.valueOf(String.valueOf(map.get("unit_price")))*bookOrderTime;
				CompleteOrderEntity orderEntity = new CompleteOrderEntity(0, entity.getId(), entity.getDriverID(), Integer.valueOf(id), entity.getBeginTime(), entity.getEndTime(), place, price);
				synchronized (PublishOrderBusiness.class) {
					if(completeOrder.notExistsClashCompleteOrder(entity.getId(), entity.getBeginTime(), entity.getEndTime())) {
						return completeOrder.insertCompleteOrder(orderEntity);
					} else {
						return -6;
					}
				}
			}
			return 0;
		}
		return -5;
	}
	
	private boolean isExistStudent(String id, String phone, String password) {
		StudentEntity entity = student.findStudentByPhone(phone);
		if(entity != null && id.equals(String.valueOf(entity.getId())) && password.equals(entity.getPassword())) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * У�鷢����ǰ��ʱ���Ƿ���ȷ
	 * @param entity
	 * 			������Ӧ�ķ�������ʵ��
	 * @return
	 * 		-1������ͬһ��
	 * 		-2����ʼʱ���ڽ���ʱ��֮��
	 * 		-3�����������Ķ���ʱ���С��2��Сʱ��ѧ��Ԥ���Ķ���ʱ���С��1Сʱ
	 * 		-4��ʱ�����ݽ�����������
	 * 		 0�������Ķ���ʵ��
	 */
	private int isRightPublishOrder(PublishOrderEntity entity, boolean isStudent) {
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date begin = dateFormat.parse(entity.getBeginTime());
			Date end = dateFormat.parse(entity.getEndTime());
			if(begin.compareTo(end) != 0) {
				return -1;
			}
			Date beginTime = dateTimeFormat.parse(entity.getBeginTime());
			Date endTime = dateTimeFormat.parse(entity.getEndTime());
			if(beginTime.compareTo(endTime) >= 0) {
				return -2;
			}
			bookOrderTime = (float) ((endTime.getTime()-beginTime.getTime())/(1000.0*60.0*60.0));
			long betweenHours = (long) bookOrderTime;
			if(!isStudent && betweenHours < 2) {
				return -3;
			} else if(isStudent && betweenHours < 1) {
				return -3;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			return -4;
		}
		return 0;
	}
}
