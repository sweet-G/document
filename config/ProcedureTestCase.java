package com.kaishengit.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Types;

import org.junit.Test;

public class ProcedureTestCase {

	/**
	 * 原始jdbc
	 * @throws Exception
	 */
	@Test
	public void testJdbc() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql:///db", "root", "root");
		String sql = "select * from t_student where id = ?";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1,1);
		
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			System.out.println(rs.getString("name"));
		}
		
		rs.close();
		ps.close();
		conn.close();
	}
	
	/**
	 * 无参无返回值的存储过程
	 * @throws Exception
	 */
	@Test
	public void testProc1() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql:///db", "root", "root");
		String sql ="{call proc1()}";
		CallableStatement call =  conn.prepareCall(sql);
		ResultSet rs = call.executeQuery();
		
		if(rs.next()) {
			double avg = rs.getDouble("avg_price");
			System.out.println(avg);
		}
		rs.close();
		call.close();
		conn.close();
	}
	
	/**
	 * 有返回值的存储过程
	 * @throws Exception
	 */
	@Test
	public void testProc3() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql:///db", "root", "root");
		String sql ="{call proc3(?,?)}";
		CallableStatement call = conn.prepareCall(sql);
		call.registerOutParameter(1, Types.DECIMAL);
		call.registerOutParameter(2, Types.DECIMAL);
		
		call.executeUpdate();
		
		float max = call.getFloat(1);
		float min = call.getFloat(2);
		
		System.out.println("max:" + max);
		System.out.println("min:" + min);
		
		call.close();
		conn.close();
		
	}
	
	/**
	 * 带有参数的存储过程
	 * @throws Exception
	 */
	@Test
	public void testProc4() throws Exception{
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql:///db", "root", "root");
		String sql ="{call proc4(?)}";
		CallableStatement call = conn.prepareCall(sql);
		call.setInt(1, 10002);
		
		ResultSet rs = call.executeQuery();
		if(rs.next()) {
			System.out.println(rs.getString("cust_name"));
		}
		rs.close();
		call.close();
		conn.close();
		
	}
	
	/**
	 * 事务
	 */
	@Test
	public void testTransaction() {
		Connection conn = null;
		PreparedStatement pstat = null;
		Savepoint s1 = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql:///kaishengit_db", "root", "rootroot");
			
			// 手动提交事务
			conn.setAutoCommit(false);
			
			String sql = "delete from student where id = ?";
			pstat = conn.prepareStatement(sql);
			pstat.setInt(1, 3);
			
			pstat.executeUpdate();
			
			// s1 = conn.setSavepoint("s1");
			
			String sql1 = "deletes from student where id = ?";
			PreparedStatement pstat1 = conn.prepareStatement(sql1);
			pstat1.setInt(1, 4);
			
			pstat1.executeUpdate();
			
			conn.commit();
			
		} catch (Exception e) {
			// 事务回滚
			try {
				conn.rollback();
//				conn.rollback(s1);
//				conn.commit();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				pstat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	
}
