package spms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import spms.vo.Member;

public class MemberDao {
	Connection connection;
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	
	public List<Member> selectList() throws Exception {
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
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
			try {if (rs != null) rs.close(); } catch (Exception e) {}
			try {if (stmt != null) stmt.close(); } catch (Exception e) {}
		}
	}
	
	public int insert(Member member) throws Exception {
		PreparedStatement pstmt = null;
	
		try {
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
		}
	}

	public int delete(int no) throws Exception {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement("delete from MEMBERS where MNO=?");
			pstmt.setInt(1, no);
			int result = pstmt.executeUpdate();
			
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			try { if (pstmt != null) pstmt.close(); } catch (Exception e) {}
		}

		
		
		
	}
//	
//	public Member selectOne(int no) throws Exception {
//		
//	}
//	
//	public int update(Member member) throws Exception {
//		
//	}
//	
//	public Member exist(String email, String password) throws Exception {
//		// 있으면 return member object , 없으면 null
//	}
	
}
