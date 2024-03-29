package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.*;

@WebServlet("/JoinCourse")
public class JoinCourse extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
    public JoinCourse() {
        super();
    }
    
    /*
     * This servlet adds an instructor to a course and updates the appropriate tables 
     */
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(); 
        String username = (String) session.getAttribute("username");
        String course = request.getParameter("course");  // TODO - FIX 
        
        PreparedStatement ps = null; 
        Connection conn = null;
        Statement st = null; 
        ResultSet rs = null;
        
        // TODO - UPDATE WITH FINAL DATABASE
        String sql = "jdbc:mysql://google/OHScheduler"
				+ "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
				+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
				+ "&user=zhoue&password=password1234";
        try {
            conn = DriverManager.getConnection(sql);
            st = conn.createStatement(); 
            
            // insert instructor and course into instructorCourse table   
            String statement = "insert into instructorCourse (courseID, instructorID) values ( (select courseID from course where courseName=\'"+ course + "\'), "
                + "(select instructorID from instructor where username=\'" + username + "\'))"; 
            ps = conn.prepareStatement(statement);
            ps.executeUpdate(); 
            
        } catch (SQLException sqle) {
            System.out.println(sqle.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                System.out.println(sqle.getMessage());
            }
        }
        
        // forward to instructor jsp  - TODO FIX FORWARD 
         RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/Instructor.jsp");
        
        try {
            dispatch.forward(request, response);
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ServletException e) {
            e.printStackTrace();
        } 
	}

}
