package com.leezp.driver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.leezp.driver.dao.interfaces.RSProcessor;

public class BaseDao {
	/**
	 * ��ȡ���ݿ����Ӷ���
	 */
	public Connection getConnection() {
		Connection conn = null;
		Context ctx = null;
		//�������Ӳ������쳣
		try {
			ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/jdbc/car_learning");
			conn = ds.getConnection();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (conn == null) {
			System.out.println("�޷��������ݿ�����");
		}
		return conn;
	}
	
	/**
	 * �ر����ݿ�����
	 * @param conn
	 * 				���ݿ�����
	 * @param stmt
	 * 				Statement����
	 * @param rs
	 * 				�����
	 */
	public void closeAll(Connection conn, Statement stmt, ResultSet rs) {
		//�����������Ϊ�գ���ر�
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//��Statement����Ϊ�գ���ر�
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//�����ݿ����Ӷ���Ϊ�գ���ر�
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void closeAll(Connection conn, Statement stmt) {
		closeAll(conn, stmt, null);
	}
	
	/**
	 * ����ɾ���Ĳ���
	 * 
	 * @param sql
	 * 			sql���
	 * @param params
	 * 			��������
	 * @return
	 * 			ִ�н��
	 */
	public int executeUpdate(String sql, Object... params) {
		int result = 0;
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = this.getConnection();
			if(conn != null && !conn.isClosed()) {
				pstmt = conn.prepareStatement(sql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						pstmt.setObject(i+1, params[i]);
					}
				}
				result = pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			closeAll(conn, pstmt);
		}
		return result;
	}
	
	/**
	 * ��ѯ���ִ��
	 * 
	 * @param processor
	 * 				�������صĽ��
	 * @param sql
	 * 				sql���
	 * @param params
	 * 				��������
	 * @return
	 */
	public Object executeQuery(RSProcessor processor, String sql, Object... params) {
		Object result = null;
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = this.getConnection();
			if (conn != null && conn.isClosed() == false) {
				pstmt = conn.prepareStatement(sql);
				if (params != null) {
					for (int i = 0; i < params.length; i++) {
						pstmt.setObject(i+1, params[i]);
					}
				}
				rs = pstmt.executeQuery();
				result = processor.process(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return result;
	}
}
