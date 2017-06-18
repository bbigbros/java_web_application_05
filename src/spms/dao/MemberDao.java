package spms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import spms.vo.Member;

public class MemberDao {
	DataSource ds;
	
	public void setDataSource(DataSource ds) {
		this.ds = ds;
	}
	
	public List<Member> selectList() throws Exception {
		Connection connection = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			connection = ds.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT MNO, MNAME, EMAIL, CRE_DATE" + 
					" FROM MEMBERS" + " ORDER BY MNO ASC");
			
			ArrayList<Member> members = new ArrayList<Member>();
			
			while (rs.next()) {
				members.add(new Member()
							.setNo(rs.getInt("MNO"))
							.setName(rs.getString("MNAME"))
							.setEmail(rs.getString("EMAIL"))
							.setCreatedDate(rs.getDate("CRE_DATE")));
			}
			return members;
		} catch (Exception e) {
			throw e;
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			try { if (connection != null) connection.close(); } catch (Exception e) {}
		}
	}
	
	public int insert(Member member) throws Exception {
		PreparedStatement pstmt = null;
		Connection connection = null;
		
		try {
			connection = ds.getConnection();
			pstmt = connection.prepareStatement(
					  "INSERT INTO MEMBERS(EMAIL, PWD, MNAME, CRE_DATE, MOD_DATE)"
					+ " VALUES (?, ?, ?, NOW(), NOW())");
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getName());
			int result = pstmt.executeUpdate();
			
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
			try { if (connection != null) connection.close(); } catch (Exception e) {}
		}
	}

	public int delete(int no) throws Exception {
		PreparedStatement pstmt = null;
		Connection connection = null;
		try {
			connection = ds.getConnection();
			pstmt = connection.prepareStatement("delete from MEMBERS where MNO=?");
			pstmt.setInt(1, no);
			int result = pstmt.executeUpdate();
			
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
			try { if (connection != null) connection.close(); } catch (Exception e) {}
		}		
	}
	
	public Member selectOne(int no) throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		Connection connection = null;
		try {
			connection = ds.getConnection();
			stmt = connection.createStatement();
			rs = stmt.executeQuery("SELECT MNO, MNAME, EMAIL, CRE_DATE FROM MEMBERS where MNO=" + no);
			rs.next();
			return new Member().setNo(rs.getInt("MNO"))
							   .setName(rs.getString("MNAME"))
							   .setEmail(rs.getString("EMAIL"))
							   .setCreatedDate(rs.getDate("CRE_DATE")); 
		} catch (Exception e) {
			throw e;
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			try { if (connection != null) connection.close(); } catch (Exception e) {}
		}
	}
	
	public int update(Member member) throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = ds.getConnection();
			pstmt = connection.prepareStatement("update MEMBERS SET EMAIL=?, MNAME=?, MOD_DATE=now() WHERE MNO=?");
			pstmt.setString(1, member.getEmail());
			pstmt.setString(2, member.getName());
			pstmt.setInt(3, member.getNo());
			
			int result = pstmt.executeUpdate();
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
			try { if (connection != null) connection.close(); } catch (Exception e) {}
		}
	}
	
	public Member exist(String email, String password) throws Exception {
		// 있으면 return member object , 없으면 null
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Connection connection = null;
		
		try {
			connection = ds.getConnection();
			pstmt = connection.prepareStatement("select MNAME, EMAIL from MEMBERS" + " where EMAIL=? AND PWD=?");
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				return new Member().setEmail(rs.getString("EMAIL")).setName(rs.getString("MNAME"));
			} else {
				return null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
			try { if (connection != null) connection.close(); } catch (Exception e) {}
		}
	}
}
