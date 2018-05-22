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
	 * 获取数据库连接对象
	 */
	public Connection getConnection() {
		Connection conn = null;
		Context ctx = null;
		//捕获连接并捕获异常
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
			System.out.println("无法建立数据库连接");
		}
		return conn;
	}
	
	/**
	 * 关闭数据库连接
	 * @param conn
	 * 				数据库连接
	 * @param stmt
	 * 				Statement对象
	 * @param rs
	 * 				结果集
	 */
	public void closeAll(Connection conn, Statement stmt, ResultSet rs) {
		//若结果集对象不为空，则关闭
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//若Statement对象不为空，则关闭
		if(stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//若数据库连接对象不为空，则关闭
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
	 * 增、删、改操作
	 * 
	 * @param sql
	 * 			sql语句
	 * @param params
	 * 			参数数组
	 * @return
	 * 			执行结果
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
	 * 查询语句执行
	 * 
	 * @param processor
	 * 				解析返回的结果
	 * @param sql
	 * 				sql语句
	 * @param params
	 * 				参照数组
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
