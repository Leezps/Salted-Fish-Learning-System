package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leezp.driver.dao.interfaces.PublishOrderDaoInterface;
import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.entity.PublishOrderEntity;

public class PublishOrderDao extends BaseDao implements PublishOrderDaoInterface {

	@Override
	public int insertPublishOrder(PublishOrderEntity entity) {
		String sql = "insert into publish_driver_order(driverID, beginTime, endTime) values(?, ?, ?)";
		Object[] params = {entity.getDriverID(), entity.getBeginTime(), entity.getEndTime()};
		return this.executeUpdate(sql, params);
	}

	@Override
	public List<PublishOrderEntity> getPublishOrderByDriverAndDate(int driverID, String date) {
		String sql = "delete from publish_driver_order where endTime<=?";
		Object[] deleteParams = {date};
		this.executeUpdate(sql, deleteParams);
		sql = "select * from publish_driver_order where driverID=? and beginTime>=?";
		Object[] params = {driverID, date};
		RSProcessor getPublishOrderByDriverAndDateRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<PublishOrderEntity> entities = new ArrayList<>();
				if (rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int driverID = rs.getInt("driverID");
						String beginTime = rs.getTimestamp("beginTime").toString();
						String endTime = rs.getTimestamp("endTime").toString();
						entities.add(new PublishOrderEntity(id, driverID, beginTime, endTime));
					}
				}
				return entities;
			}
		};
		return (List<PublishOrderEntity>) this.executeQuery(getPublishOrderByDriverAndDateRSProcessor, sql, params);
	}

	@Override
	public int isExistTimeSlotOrder(int id, int driverID, String beginTime, String endTime) {
		String sql = null;
		RSProcessor existRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				int result = 0;
				if (rs != null) {
					if(rs.next()) {
						result = 1;
					}
				}
				return result;
			}
		};
		if(id == 0) {
			sql = "select 1 from publish_driver_order where driverID=? and beginTime<? and endTime>?";
			Object[] params_1 = {driverID, beginTime, beginTime};
			Object[] params_2 = {driverID, endTime, endTime};
			if((int)this.executeQuery(existRSProcessor, sql, params_1) > 0 || (int)this.executeQuery(existRSProcessor, sql, params_2) > 0) {
				return 1;
			}
			sql = "select 1 from publish_driver_order where driverID=? and beginTime>=? and endTime<=?";
			Object[] params_3 = {driverID, beginTime, endTime};
			if ((int)this.executeQuery(existRSProcessor, sql, params_3) > 0) {
				return 1;
			}
			return 0;
		} else if (id > 0) {
			sql = "select 1 from publish_driver_order where id=? and driverID=? and beginTime<=? and endTime>=?";
			Object[] params = {id, driverID, beginTime, endTime};
			return (int) this.executeQuery(existRSProcessor, sql, params);
		} else {
			return -1;
		}
	}

}
