package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leezp.driver.dao.interfaces.DriverDaoInterface;
import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.entity.DriverEntity;

public class DriverDao extends BaseDao implements DriverDaoInterface{

	@Override
	public DriverEntity findDriverByPhone(String phone) {
		String sql = "select * from driver where phone=?";
		Object[] params = {phone};
		RSProcessor findDriverByPhoneRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				DriverEntity driver = null;
				if(rs != null) {
					if (rs.next()) {
						int id = rs.getInt("id");
						String phone = rs.getString("phone");
						String password = rs.getString("password");
						String name = rs.getString("name");
						int sex = rs.getInt("sex");
						String place = rs.getString("place");
						float evaluate = rs.getFloat("evaluate");
						String identify_number = rs.getString("identify_number");
						String information = rs.getString("information");
						String head_url = rs.getString("head_url");
						String identify_image_url = rs.getString("identify_image_url");
						String trainer_image_url = rs.getString("trainer_image_url");
						int taught_people = rs.getInt("taught_people");
						int unit_price = rs.getInt("unit_price");
						driver = new DriverEntity(id, phone, password, name, sex, place, evaluate, identify_number, information, head_url, identify_image_url, trainer_image_url, taught_people, unit_price);
					}
				}
				return driver;
			}
		};
		return (DriverEntity) this.executeQuery(findDriverByPhoneRSProcessor, sql, params);
	}

	@Override
	public Map<String, Object> findUnitPriceAndPlaceById(int id) {
		String sql = "select place, unit_price from driver where id=?";
		Object[] params = {id};
		RSProcessor priceAndPlaceRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				Map<String, Object> map = new HashMap<>();
				if(rs != null) {
					if(rs.next()) {
						String place = rs.getString("place");
						float unit_price = rs.getFloat("unit_price");
						map.put("place", place);
						map.put("unit_price", unit_price);
					}
				}
				return map;
			}
		};
		return (Map<String, Object>) this.executeQuery(priceAndPlaceRSProcessor, sql, params);
	}

	@Override
	public int insertBaseDriver(String phone, String password) {
		String sql = "insert into driver(phone, password) values(?, ?)";
		Object[] params = {phone, password};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int uploadOrUpdateDriverInformation(DriverEntity entity) {
		String sql = "update driver set name=?, sex=?, place=?, evaluate=?, identify_number=?, information=?, head_url=?, identify_image_url=?, trainer_image_url=?, taught_people=?, unit_price=? where phone=? and password=?";
		Object[] params = {entity.getName(), entity.getSex(), entity.getPlace(), entity.getEvaluate(), entity.getIdentify_number(), entity.getInformation(), entity.getHead_url(), entity.getIdentify_image_url(), entity.getTrainer_image_url(), entity.getTaught_people(), entity.getUnit_price(), entity.getPhone(), entity.getPassword()};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int updateDriverPassword(String phone, String password) {
		String sql = "update driver set password=? where phone=?";
		Object[] params = {password, phone};
		return this.executeUpdate(sql, params);
	}

	@Override
	public List<DriverEntity> findDriversByPlaceAndEvaluate(String place) {
		String sql = "select * from driver where place=? order by evaluate desc";
		Object[] params = {place};
		RSProcessor findDriversByPlaceProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<DriverEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						String phone = rs.getString("phone");
						String name = rs.getString("name");
						int sex = rs.getInt("sex");
						String place = rs.getString("place");
						float evaluate = rs.getFloat("evaluate");
						String information = rs.getString("information");
						String head_url = rs.getString("head_url");
						int taught_people = rs.getInt("taught_people");
						float unit_price = rs.getFloat("unit_price");
						entities.add(new DriverEntity(id, phone, null, name, sex, place, evaluate, null, information, head_url, null, null, taught_people, unit_price));
					}
				}
				return entities;
			}
		};
		return (List<DriverEntity>) this.executeQuery(findDriversByPlaceProcessor, sql, params);
	}

	@Override
	public Map<String, String> findHeadAndNamePhoneById(int id) {
		String sql = "select phone, name, head_url from driver where id=?";
		Object[] params = {id};
		RSProcessor findInforByIdRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				Map<String, String> map = new HashMap<>();
				if (rs != null) {
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
