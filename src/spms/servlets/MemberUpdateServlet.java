package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.vo.Member;


@WebServlet("/member/update")
public class MemberUpdateServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			ServletContext sc = this.getServletContext();
			conn = (Connection)sc.getAttribute("conn");
			stmt = conn.createStatement();
			rs = stmt.executeQuery(
						"select MNO, EMAIL, MNAME, CRE_DATE from MEMBERS" +
						" where MNO=" + request.getParameter("no"));
			rs.next();
			request.setAttribute(
					"member", new Member().setNo(rs.getInt("MNO"))
										  .setName(rs.getString("MNAME"))
										  .setEmail(rs.getString("EMAIL"))
										  .setCreatedDate(rs.getDate("CRE_DATE")));
			RequestDispatcher rd = request.getRequestDispatcher("/member/MemberUpdate.jsp");
			rd.forward(request, response);
			
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e) {}
			try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			ServletContext sc = this.getServletContext();
			conn = (Connection)sc.getAttribute("conn");
			pstmt = conn.prepareStatement(
					"update MEMBERS SET EMAIL=?, MNAME=?, MOD_DATE=now() WHERE MNO=?");
			pstmt.setString(1, request.getParameter("email"));
			pstmt.setString(2, request.getParameter("name"));
			pstmt.setInt(3, Integer.parseInt(request.getParameter("no")));
			
			pstmt.executeUpdate();
			response.sendRedirect("list");
			
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try { if ( pstmt != null ) pstmt.close();} catch (Exception e) {}
		}

	}
}
