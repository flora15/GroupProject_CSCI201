package servlets;

import classes.UserManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Servlet implementation class InstructorRegisterServlet
 */
@WebServlet("/InstructorRegisterServlet")
public class InstructorRegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void service (HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Get username and password from form
		HttpSession session = request.getSession();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String first_name = request.getParameter("first_name");
		String last_name = request.getParameter("last_name");
		String confirmpw = request.getParameter("confirmpassword");
		String next = "/Instructor.jsp";
		if(username.matches("^(.+)@(.+)(.com|.edu|.net)$") ) {
			if (confirmpw.trim().equals(password.trim()) && password.trim().length() != 0) {
				if (UserManager.register(username, password, first_name, last_name, 2)) {
					ArrayList<String> temp = UserManager.login(username, password, 2);
					session.setAttribute("userType", "instructor");
					session.setAttribute("id", temp.get(0));
					session.setAttribute("first_name", temp.get(3));
					session.setAttribute("last_name", temp.get(4));
					session.setAttribute("username", temp.get(1));
					session.setAttribute("hashedPassword", temp.get(2));
				} else {
					next = "/InstructorRegister.jsp";
					request.setAttribute("error", "Username already exists in our database, please use another email.");
					//TODO - username already exists in db
				}
			} else {
				next = "/InstructorRegister.jsp";
				request.setAttribute("error", "Unfortunately, either your password field is blank or your confirmation password and actual password are not the same.");
				//TODO - password doesn't match
			}
		}else {
			next = "/InstructorRegister.jsp";
			request.setAttribute("error","Your username is not an email. Please enter an email in the username field.");
			//TODO - username is not an email
		}
		RequestDispatcher dispatch = getServletContext().getRequestDispatcher(next);
        
        try {
            dispatch.forward(request, response);
        } catch(IOException | ServletException e) {
            e.printStackTrace();
        }
	}
}
