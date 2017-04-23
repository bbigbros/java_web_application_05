package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/member/delete")
public class MemberDeleteServlet extends HttpServlet {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			ServletContext sc = this.getServletContext();
			conn = (Connection)sc.getAttribute("conn");
			pstmt = conn.prepareStatement(
						"delete from MEMBERS where MNO=?");
			pstmt.setInt(1, Integer.parseInt(request.getParameter("no")));
			pstmt.executeUpdate();
			
			response.sendRedirect("list");
		} catch (Exception e) {
			throw new ServletException(e);
		} finally {
			try { if (pstmt != null) pstmt.close(); } catch(Exception e) {}
		}
	}
}
