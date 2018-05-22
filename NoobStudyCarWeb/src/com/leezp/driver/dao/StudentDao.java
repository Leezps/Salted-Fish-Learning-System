package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.dao.interfaces.StudentDaoInterface;
import com.leezp.driver.entity.StudentEntity;

public class StudentDao extends BaseDao implements StudentDaoInterface{

	@Override
	public StudentEntity findStudentByPhone(String phone) {
		String sql = "select * from student where phone=?";
		Object[] params = {phone};
		
		RSProcessor findStudentByPhone = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				StudentEntity entity = null;
				
				if (rs != null) {
					if (rs.next()) {
						int id = rs.getInt("id");
						String phone = rs.getString("phone");
						String password = rs.getString("password");
						String name = rs.getString("name");
						int sex = rs.getInt("sex");
						int age = rs.getInt("age");
						String identify_number = rs.getString("identify_number");
						int driver_process = rs.getInt("driver_process");
						String head_url = rs.getString("head_url");
						String identify_image_url = rs.getString("identify_image_url");
						
						entity = new StudentEntity(id, phone, password, name, sex, age, identify_number, driver_process, head_url, identify_image_url);
					}
				}
				return entity;
			}
		};
		return (StudentEntity) this.executeQuery(findStudentByPhone, sql, params);
	}

	@Override
	public int insertStudent(String phone, String password) {
		String sql = "insert into student (phone, password) values(?, ?)";
		Object[] params = {phone, password};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int countStudentByPhone(String phone) {
		String sql = "select count(*) as student_count from student where phone=?";
		Object[] params = {phone};
		
		RSProcessor countStudentByPhoneProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				int count = 0;
				if(rs != null) {
					if(rs.next()) {
						count = rs.getInt("student_count");
					}
				}
				return new Integer(count);
			}
		};
		return (Integer) this.executeQuery(countStudentByPhoneProcessor, sql, params);
	}

	@Override
	public int studentIDByPhone(String phone) {
		String sql = "select id from student where phone=?";
		Object[] params = {phone};
		
		RSProcessor studentIDByPhoneProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				int id = 0;
				if(rs != null) {
					if(rs.next()) {
						id = rs.getInt("id");
					}
				}
				return new Integer(id);
			}
		};
		return (Integer) this.executeQuery(studentIDByPhoneProcessor, sql, params);
	}

	@Override
	public int updateStudent(StudentEntity entity) {
		String sql = "update student set name=?, sex=?, age=?, identify_number=?, driver_process=?, head_url=?, identify_image_url=? where phone=?";
		Object[] params = {entity.getName(), entity.getSex(), entity.getAge(), entity.getIdentify_number(), entity.getDriver_process(), entity.getHead_url(), entity.getIdentify_image_url(), entity.getPhone()};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int updateStudentPassword(String phone, String password) {
		String sql = "update student set password=? where phone=?";
		Object[] params = {password, phone};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int updateStudentHeadUrl(String headUrl, String id) {
		String sql = "update student set head_url=? where id=?";
		Object[] params = {headUrl, id};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int updateStudentIdentifyImageUrl(String identifyImageUrl, String id) {
		String sql = "update student set identify_image_url=? where id=?";
		Object[] params = {identifyImageUrl, id};
		return this.executeUpdate(sql, params);
	}

	@Override
	public Map<String, String> findHeadAndNamePhoneById(String id) {
		String sql = "select phone, name, head_url from student where id=?";
		Object[] params = {id};
		RSProcessor findInforByIdRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				Map<String, String> map = new HashMap<>();
				if(rs != null) {
					if(rs.next()) {
						String phone = rs.getString("phone");
						String name = rs.getString("name");
						String head_url = rs.getString("head_url");
						map.put("phone", phone);
						map.put("name", name);
						map.put("head_url", head_url);
					}
				}
				return map;
			}
		};
		return (Map<String, String>) this.executeQuery(findInforByIdRSProcessor, sql, params);
	}
	
}
