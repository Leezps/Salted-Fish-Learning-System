package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leezp.driver.dao.interfaces.CompleteOrderDaoInterface;
import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.entity.CompleteOrderEntity;

public class CompleteOrderDao extends BaseDao implements CompleteOrderDaoInterface{

	@Override
	public List<CompleteOrderEntity> getStudentCompleteOrders(String studentID, String date) {
		String sql = "select * from complete_driver_order where studentID=? and beginTime>? and (student_remove=0 or driver_remove=0)";
		Object[] params = {studentID, date};
		RSProcessor getStudentCompleteOrdersRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<CompleteOrderEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int publishOrderID = rs.getInt("publishOrderID");
						int driverID = rs.getInt("driverID");
						int studentID = rs.getInt("studentID");
						String beginTime = rs.getTimestamp("beginTime").toString();
						String endTime = rs.getTimestamp("endTime").toString();
						String place = rs.getString("place");
						float price = rs.getFloat("price");
						
						entities.add(new CompleteOrderEntity(id, publishOrderID, driverID, studentID, beginTime, endTime, place, price));
					}
				}
				return entities;
			}
		};
		return (List<CompleteOrderEntity>) this.executeQuery(getStudentCompleteOrdersRSProcessor, sql, params);
	}

	@Override
	public List<CompleteOrderEntity> getDriverCompleteOrders(String driverID, String date) {
		String sql = "select * from complete_driver_order where driverID=? and beginTime>? and (student_remove=0 or driver_remove=0)";
		Object[] params = {driverID, date};
		RSProcessor getDriverCompleteOrdersRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<CompleteOrderEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int publishOrderID = rs.getInt("publishOrderID");
						int driverID = rs.getInt("driverID");
						int studentID = rs.getInt("studentID");
						String beginTime = rs.getTimestamp("beginTime").toString();
						String endTime = rs.getTimestamp("endTime").toString();
						String place = rs.getString("place");
						float price = rs.getFloat("price");
						
						entities.add(new CompleteOrderEntity(id, publishOrderID, driverID, studentID, beginTime, endTime, place, price));
					}
				}
				return entities;
			}
		};
		return (List<CompleteOrderEntity>) this.executeQuery(getDriverCompleteOrdersRSProcessor, sql, params);
	}

	@Override
	public int removeStudentCompleteOrder(String id, String studentID) {
		String sql = "delete from complete_driver_order where student_remove=1 and driver_remove=1";
		this.executeUpdate(sql);
		sql = "update complete_driver_order set student_remove=1 where id=? and studentID=?";
		Object[] params = {id, studentID};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int removeDriverCompleteOrder(String id, String driverID) {
		String sql = "delete from complete_driver_order where student_remove=1 and driver_remove=1";
		this.executeUpdate(sql);
		sql = "update complete_driver_order set driver_remove=1 where id=? and driverID=?";
		Object[] params = {id, driverID};
		return this.executeUpdate(sql, params);
	}

	@Override
	public Map<String, Integer> selectCompleteOrder(boolean student, String id, String userID) {
		String sql = null;
		if (student) {
			sql = "select driver_remove as state, driverID as otherUserID from complete_driver_order where id=? and studentID=?";
		} else {
			sql = "select student_remove as state, studentID as otherUserID from complete_driver_order where id=? and driverID=?";
		}
		Object[] params = {id, userID};
		RSProcessor selectCompleteOrderRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				Map<String, Integer> map = new HashMap<>();
				if(rs != null) {
					if (rs.next()) {
						int state = rs.getInt("state");
						int otherUserID = rs.getInt("otherUserID");
						map.put("state", state);
						map.put("otherUserID", otherUserID);
					}
				}
				return map;
			}
		};
		return (Map<String, Integer>) this.executeQuery(selectCompleteOrderRSProcessor, sql, params);
	}

	@Override
	public int insertCompleteOrder(CompleteOrderEntity entity) {
		String sql = "insert into complete_driver_order(publishOrderID, driverID, studentID, beginTime, endTime, place, price) values(?, ?, ?, ?, ?, ?, ?)";
		Object[] params = {entity.getPublishOrderID(), entity.getDriverID(), entity.getStudentID(), entity.getBeginTime(), entity.getEndTime(), entity.getPlace(), entity.getPrice()};
		return this.executeUpdate(sql, params);
	}

	@Override
	public boolean notExistsClashCompleteOrder(int publishOrderId, String beginTime, String endTime) {
		String sql = "select 1 from complete_driver_order where publishOrderID=? and (beginTime<? and endTime>? or beginTime<? and endTime>? or beginTime>? and endTime<?) and (student_remove=0 or driver_remove=0)";
		Object[] params = {publishOrderId, beginTime, beginTime, endTime, endTime, beginTime, endTime};
		RSProcessor noClashProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				boolean noClash = true;
				if (rs != null) {
					if(rs.next()) {
						noClash = false;
					}
				}
				return noClash;
			}
		};
		return (boolean) this.executeQuery(noClashProcessor, sql, params);
	}

	@Override
	public List<CompleteOrderEntity> getCompleteOrdersByPublishOrder(int publishOrderId) {
		String sql = "select * from complete_driver_order where publishOrderID=? and (student_remove=0 or driver_remove=0)";
		Object[] params = {publishOrderId};
		RSProcessor completeOrderByPublishRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<CompleteOrderEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int publishOrderID = rs.getInt("publishOrderID");
						int driverID = rs.getInt("driverID");
						int studentID = rs.getInt("studentID");
						String beginTime = rs.getTimestamp("beginTime").toString();
						String endTime = rs.getTimestamp("endTime").toString();
						String place = rs.getString("place");
						float price = rs.getFloat("price");
						
						entities.add(new CompleteOrderEntity(id, publishOrderID, driverID, studentID, beginTime, endTime, place, price));
					}
				}
				return entities;
			}
		};
		return (List<CompleteOrderEntity>) this.executeQuery(completeOrderByPublishRSProcessor, sql, params);
	}

	@Override
	public Map<String, String> getStudentAndDriverById(int id) {
		String sql = "select driverID, studentID from complete_driver_order where id=?";
		Object[] params = {id};
		RSProcessor getStudentAndDriverRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				Map<String, String> map = new HashMap<>();
				if(rs != null) {
					if(rs.next()) {
						String driverID = rs.getString("driverID");
						String studentID = rs.getString("studentID");
						map.put("driverID", driverID);
						map.put("studentID", studentID);
					}
				}
				return map;
			}
		};
		return (Map<String, String>) this.executeQuery(getStudentAndDriverRSProcessor, sql, params);
	}

}
