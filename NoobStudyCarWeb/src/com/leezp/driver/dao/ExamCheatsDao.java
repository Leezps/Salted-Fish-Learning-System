package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leezp.driver.dao.interfaces.ExamCheatsDaoInterface;
import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.entity.ExamCheatsEntity;

public class ExamCheatsDao extends BaseDao implements ExamCheatsDaoInterface{

	@Override
	public List<ExamCheatsEntity> getExamCheats(String process_order) {
		String sql = "select * from driver_exam_cheats where process_order=?";
		Object[] params = {process_order};
		RSProcessor getExamCheatsRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<ExamCheatsEntity> entities = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int process_order = rs.getInt("process_order");
						String content = rs.getString("content");
						entities.add(new ExamCheatsEntity(id, process_order, content));
					}
				}
				return entities;
			}
		};
		return (List<ExamCheatsEntity>) this.executeQuery(getExamCheatsRSProcessor, sql, params);
	}

}
