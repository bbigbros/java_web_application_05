package spms.listeners;

import java.sql.DriverManager;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import spms.dao.MemberDao;
import spms.util.DBConnectionPool;

@WebListener
public class ContextLoaderListener implements ServletContextListener {
	DBConnectionPool connPool;
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		try {
			System.out.println("ServletContextListener 시작");
			ServletContext sc = event.getServletContext();
			connPool = new DBConnectionPool(
						sc.getInitParameter("url"),
						sc.getInitParameter("username"),
						sc.getInitParameter("password"),
						sc.getInitParameter("password"));
			MemberDao memberDao = new MemberDao();
			memberDao.setDbConnectionPool(connPool);
			
			sc.setAttribute("memberDao", memberDao);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent event) {
		connPool.closeAll();
	}
}
