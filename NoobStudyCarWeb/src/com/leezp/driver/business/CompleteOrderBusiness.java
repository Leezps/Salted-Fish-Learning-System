package com.leezp.driver.business;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.leezp.driver.business.interfaces.CompleteOrderBusinessInterface;
import com.leezp.driver.dao.CompleteOrderDao;
import com.leezp.driver.dao.DriverDao;
import com.leezp.driver.dao.MessageCenterDao;
import com.leezp.driver.dao.StudentDao;
import com.leezp.driver.entity.CompleteOrderEntity;
import com.leezp.driver.entity.DriverEntity;
import com.leezp.driver.entity.MessageCenterEntity;
import com.leezp.driver.entity.StudentEntity;

public class CompleteOrderBusiness implements CompleteOrderBusinessInterface {
	private StudentDao student = new StudentDao();
	private DriverDao driver = new DriverDao();
	private CompleteOrderDao completeOrder = new CompleteOrderDao();
	private MessageCenterDao messageCenter = new MessageCenterDao();
	private StudentEntity studentEntity = null;
	private DriverEntity driverEntity = null;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<CompleteOrderEntity> getCompleteOrders(String role, String userID, String phone, String password) {
		if(role.equals("0")) {
			if(isExistStudent(userID, phone, password)) {
				String date = dateFormat.format(new Date());
				return completeOrder.getStudentCompleteOrders(userID, date);
			}
		} else if(role.equals("1")) {
			if(isExistDriver(userID, phone, password)) {
				String date = dateFormat.format(new Date());
				return completeOrder.getDriverCompleteOrders(userID, date);
			}
		}
		return null;
	}

	@Override
	public int removeCompleteOrder(String role, String userID, String phone, String password, String orderID, String remove_message) {
		if(role.equals("0")) {
			if(isExistStudent(userID, phone, password)) {
				Map<String, Integer> map = completeOrder.selectCompleteOrder(true, orderID, userID);
				if (!map.isEmpty()) {
					int state = map.get("state");
					int otherUserID = map.get("otherUserID");
					String date = dateFormat.format(new Date());
					MessageCenterEntity entity = null;
					if(state == 0) {
						String title = "订单取消请求";
						String content = "取消原因：\r\n"+remove_message+"\r\n 来自"+studentEntity.getName()+"的请求";
						entity = new MessageCenterEntity(0, otherUserID, title, content, date, 0);
					} else if(state == 1) {
						String title = "同意订单取消";
						String content = "订单取消成功！\r\n"+remove_message+"\r\n"+studentEntity.getName()+"同意取消订单";
						entity = new MessageCenterEntity(0, otherUserID, title, content, date, 0);
					}
					messageCenter.sendDriverMessage(entity);
					return completeOrder.removeStudentCompleteOrder(orderID, userID);
				}
			}
		} else if(role.equals("1")) {
			if(isExistDriver(userID, phone, password)) {
				Map<String, Integer> map = completeOrder.selectCompleteOrder(false, orderID, userID);
				if (!map.isEmpty()) {
					int state = map.get("state");
					int otherUserID = map.get("otherUserID");
					String date = dateFormat.format(new Date());
					MessageCenterEntity entity = null;
					if(state == 0) {
						String title = "订单取消请求";
						String content = "取消原因：\r\n"+remove_message+"\r\n 来自"+driverEntity.getName()+"的请求";
						entity = new MessageCenterEntity(0, otherUserID, title, content, date, 0);
					} else if(state == 1) {
						String title = "同意订单取消";
						String content = "订单取消成功！\r\n"+remove_message+"\r\n"+driverEntity.getName()+"同意取消订单";
						entity = new MessageCenterEntity(0, otherUserID, title, content, date, 0);
					}
					messageCenter.sendStudentMessage(entity);
					return completeOrder.removeDriverCompleteOrder(orderID, userID);
				}
			}
		}
		return -1;
	}
	
	private boolean isExistStudent(String id, String phone, String password) {
		studentEntity = student.findStudentByPhone(phone);
		if(studentEntity == null || !String.valueOf(studentEntity.getId()).equals(id) || !studentEntity.getPassword().equals(password)) {
			return false;
		} else {
			return true;
		}
	}
	
	private boolean isExistDriver(String id, String phone, String password) {
		driverEntity = driver.findDriverByPhone(phone);
		if(driverEntity == null || !String.valueOf(driverEntity.getId()).equals(id) || !driverEntity.getPassword().equals(password)) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public List<CompleteOrderEntity> getCompleteOrdersByPublishOrderId(String publishOrderId) {
		return completeOrder.getCompleteOrdersByPublishOrder(Integer.valueOf(publishOrderId));
	}

}
