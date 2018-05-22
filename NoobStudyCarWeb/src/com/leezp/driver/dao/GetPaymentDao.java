package com.leezp.driver.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.leezp.driver.dao.interfaces.GetPaymentDaoInterface;
import com.leezp.driver.dao.interfaces.RSProcessor;
import com.leezp.driver.entity.GetPaymentEntity;

public class GetPaymentDao extends BaseDao implements GetPaymentDaoInterface{

	@Override
	public List<GetPaymentEntity> getPaymentWay(int studentID) {
		String sql = "select * from payment_way where studentID=? limit 5";
		Object[] params = {studentID};
		RSProcessor getPaymentWayRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				List<GetPaymentEntity> entitys = new ArrayList<>();
				if(rs != null) {
					while(rs.next()) {
						int id = rs.getInt("id");
						int studentID = rs.getInt("studentID");
						int paymentType = rs.getInt("paymentType");
						String paymentName = rs.getString("paymentName");
						
						entitys.add(new GetPaymentEntity(id, studentID, paymentType, paymentName));
					}
				}
				return entitys;
			}
		};
		return (List<GetPaymentEntity>) this.executeQuery(getPaymentWayRSProcessor, sql, params);
	}

	@Override
	public int getPaymentNum(int studentID) {
		String sql = "select count(*) as payment_way_count from payment_way where studentID=?";
		Object[] params = {studentID};
		RSProcessor getPaymentNumRSProcessor = new RSProcessor() {
			
			@Override
			public Object process(ResultSet rs) throws SQLException {
				int count = 0;
				if(rs != null) {
					if(rs.next()) {
						count = rs.getInt("payment_way_count");
					}
				}
				return count;
			}
		};
		return (int) this.executeQuery(getPaymentNumRSProcessor, sql, params);
	}

	@Override
	public int removePaymentWay(int id, int studentID) {
		String sql = "delete from payment_way where id=? and studentID=?";
		Object[] params = {id, studentID};
		return this.executeUpdate(sql, params);
	}

	@Override
	public int insertPaymentWay(GetPaymentEntity entity) {
		String sql = "insert into payment_way(studentID, paymentType, paymentName) values (?, ?, ?)";
		Object[] params = {entity.getStudentID(), entity.getPaymentType(), entity.getPaymentName()};
		return this.executeUpdate(sql, params);
	}

}
