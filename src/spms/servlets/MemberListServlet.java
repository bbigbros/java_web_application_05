package spms.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spms.vo.Member;


@WebServlet("/member/list")
public class MemberListServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			ServletContext sc = this.getServletContext();
			Class.forName(sc.getInitParameter("driver"));
			conn = DriverManager.getConnection(
					sc.getInitParameter("url"),
					sc.getInitParameter("username"),
					sc.getInitParameter("password"));
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select MNO, MNAME, EMAIL, CRE_DATE" + " from MEMBERS order by MNO ASC");
			
			response.setContentType("text/html; charset=UTF-8");
			ArrayList<Member> members = new ArrayList<Member>();
			// 데이터베이스에서 회원 정보를 가져와 Member에 담는다.
			// 이후 members에 가져온 정보를 하나씩 넣는다.
			while (rs.next()) {
				members.add(
						new Member().setNo(rs.getInt("MNO"))
									.setName(rs.getString("MNAME"))
									.setEmail(rs.getString("EMAIL"))
									.setCreatedDate(rs.getDate("CRE_DATE"))
						);
			}
			// requset에 회원정보를 보관한다.
			request.setAttribute("members", members);
			
			// Dispatcher를 사용해 jsp로 출력을 위임한다.
			RequestDispatcher rd = request.getRequestDispatcher("/member/MemberList.jsp");
			rd.include(request, response);
			
		} catch(Exception e) {
			throw new ServletException(e);
		} finally {
			try { if (rs != null) rs.close(); } catch (Exception e){}
			try { if (stmt != null) stmt.close(); } catch (Exception e){}
			try { if (conn != null) conn.close(); } catch (Exception e){}
		}
	}

}
