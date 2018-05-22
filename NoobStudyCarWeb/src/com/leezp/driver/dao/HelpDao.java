package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leezp.driver.dao.interfaces.HelpDaoInterface;
import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.entity.HelpEntity;

public class HelpDao extends BaseDao implements HelpDaoInterface{

	@Override
	public List<HelpEntity> getStudentHelp() {
		String sql = "select * from student_application_help";
		RSProcessor getStudentHelpRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<HelpEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						String title = rs.getString("title");
						String content = rs.getString("content");
						
						entities.add(new HelpEntity(id, title, content));
					}
				}
				return entities;
			}
		};
		return (List<HelpEntity>) this.executeQuery(getStudentHelpRSProcessor, sql);
	}

	@Override
	public List<HelpEntity> getDriverHelp() {
		String sql = "select * from driver_application_help";
		RSProcessor getDriverHelpRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<HelpEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						String title = rs.getString("title");
						String content = rs.getString("content");
						
						entities.add(new HelpEntity(id, title, content));
					}
				}
				return entities;
			}
		};
		return (List<HelpEntity>) this.executeQuery(getDriverHelpRSProcessor, sql);
	}

}
