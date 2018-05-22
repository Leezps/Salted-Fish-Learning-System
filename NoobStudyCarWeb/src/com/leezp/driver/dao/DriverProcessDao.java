package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leezp.driver.dao.interfaces.DriverProcessDaoInterface;
import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.entity.DriverProcessEntity;

public class DriverProcessDao extends BaseDao implements DriverProcessDaoInterface{

	@Override
	public List<DriverProcessEntity> getDriverProcess() {
		String sql = "select * from driver_process";
		RSProcessor getDriverProcessRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<DriverProcessEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int process_order = rs.getInt("process_order");
						String title = rs.getString("title");
						String content = rs.getString("content");
						
						entities.add(new DriverProcessEntity(id, process_order, title, content));
					}
				}
				return entities;
			}
		};
		return (List<DriverProcessEntity>) this.executeQuery(getDriverProcessRSProcessor, sql);
	}

}
