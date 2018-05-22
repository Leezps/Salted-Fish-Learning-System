package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leezp.driver.dao.interfaces.MessageCenterDaoInterface;
import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.entity.MessageCenterEntity;

public class MessageCenterDao extends BaseDao implements MessageCenterDaoInterface{

	@Override
	public List<MessageCenterEntity> getDriverMessageCenter(String driverID) {
		String sql = "select * from driver_message_center where driverID=? order by id desc";
		Object[] params = {driverID};
		RSProcessor getDriverMessageCenterRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<MessageCenterEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int driverID = rs.getInt("driverID");
						String title = rs.getString("title");
						String content = rs.getString("content");
						String date = rs.getDate("date").toString();
						int isOpen = rs.getInt("isOpen");
						
						entities.add(new MessageCenterEntity(id, driverID, title, content, date, isOpen));
					}
				}
				return entities;
			}
		};
		return (List<MessageCenterEntity>) this.executeQuery(getDriverMessageCenterRSProcessor, sql, params);
	}

	@Override
	public int removeDriverMessage(String driverID, String id) {
		String sql = "delete from driver_message_center where id=? and driverID=?";
		Object[] params = {id, driverID};
		return this.executeUpdate(sql, params);
	}

	@Override
	public List<MessageCenterEntity> getStudentMessageCenter(String studentID) {
		String sql = "select * from student_message_center where studentID=? order by id desc";
		Object[] params = {studentID};
		RSProcessor getStudentMessageCenterRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<MessageCenterEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int studentID = rs.getInt("studentID");
						String title = rs.getString("title");
						String content = rs.getString("content");
						String date = rs.getDate("date").toString();
						int isOpen = rs.getInt("isOpen");
						
						entities.add(new MessageCenterEntity(id, studentID, title, content, date, isOpen));
					}
				}
				return entities;
			}
		};
		return (List<MessageCenterEntity>) this.executeQuery(getStudentMessageCenterRSProcessor, sql, params);
	}

	@Override
	public int removeStudentMessage(String studentID, String id) {
		String sql = "delete from student_message_center where id=? and studentID=?";
		Object[] params = {id, studentID};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int openStudentMessage(String studentID, String id) {
		String sql = "update student_message_center set isOpen=1 where id=? and studentID=?";
		Object[] params = {id, studentID};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int openDriverMessage(String driverID, String id) {
		String sql = "update driver_message_center set isOpen=1 where id=? and driverID=?";
		Object[] params = {id, driverID};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int sendStudentMessage(MessageCenterEntity entity) {
		String sql = "insert into student_message_center(studentID, title, content, date) values(?, ?, ?, ?)";
		Object[] params = {entity.getUserID(), entity.getTitle(), entity.getContent(), entity.getDate()};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int sendDriverMessage(MessageCenterEntity entity) {
		String sql = "insert into driver_message_center(driverID, title, content, date) values(?, ?, ?, ?)";
		Object[] params = {entity.getUserID(), entity.getTitle(), entity.getContent(), entity.getDate()};
		return this.executeUpdate(sql, params);
	}

}
