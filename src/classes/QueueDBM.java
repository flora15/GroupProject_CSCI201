package classes;

import java.sql.*;
import java.util.Vector;

public class QueueDBM {
	static String sql ="jdbc:mysql://google/OHScheduler?cloudSqlInstance=tough-shard-260720:us-west1:csci201final&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false";

	/**
	 * public static void makeTable(int courseID) { Connection conn = null;
	 * PreparedStatement ps = null; String sql = "jdbc:mysql://google/OHScheduler"
	 * // TODO db name +
	 * "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7" +
	 * "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false" +
	 * "&user=zhoue&password=password1234"; try {
	 * Class.forName("com.mysql.jdbc.Driver"); conn =
	 * DriverManager.getConnection(sql); String statement = ""; // TODO ps =
	 * conn.prepareStatement(statement); // ps.setInt(1, ++countValue); //
	 * ps.setInt(2, pageID); ps.executeUpdate();
	 * 
	 * 
	 * } catch (SQLException sqle) { System.out.println(sqle.getMessage()); } catch
	 * (ClassNotFoundException e) { e.printStackTrace(); } finally { try { if (ps !=
	 * null) { ps.close(); } if (conn != null) { conn.close(); } } catch
	 * (SQLException sqle) { System.out.println(sqle.getMessage()); } } }
	 */

	public static void insertEntry(int courseID, int studentID, String text) {
		Connection conn = null;
		PreparedStatement ps = null;
//		String sql = "jdbc:mysql://google/OHScheduler" // TODO db name
//				+ "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//				+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//				+ "&user=zhoue&password=password1234";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(sql, "root", "root");
			ps = conn.prepareStatement("insert into queue(studentID, text, courseID) values(?, ?, ?)");
			ps.setInt(1, studentID);
			ps.setString(2, text);
			ps.setInt(3, courseID);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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
	}

	/**
	 * public static void promoteEntry(int courseID, int studentID) { Connection
	 * conn = null; PreparedStatement ps = null; String sql =
	 * "jdbc:mysql://google/OHScheduler" // TODO db name +
	 * "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7" +
	 * "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false" +
	 * "&user=zhoue&password=password1234"; try {
	 * Class.forName("com.mysql.jdbc.Driver"); conn =
	 * DriverManager.getConnection(sql); String statement = ""; // TODO ps =
	 * conn.prepareStatement(statement); // ps.setInt(1, ++countValue); //
	 * ps.setInt(2, pageID); ps.executeUpdate(); } catch (SQLException sqle) {
	 * System.out.println(sqle.getMessage()); } catch (ClassNotFoundException e) {
	 * e.printStackTrace(); } finally { try { if (ps != null) { ps.close(); } if
	 * (conn != null) { conn.close(); } } catch (SQLException sqle) {
	 * System.out.println(sqle.getMessage()); } } }
	 */

	public static void deleteEntry(int courseID, int studentID) {
		Connection conn = null;
		PreparedStatement ps = null;
//		String sql = "jdbc:mysql://google/OHScheduler" // TODO db name
//				+ "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//				+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//				+ "&user=zhoue&password=password1234";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(sql, "root", "root");
			if (studentID == -1) { // delete top student
				ps = conn.prepareStatement("delete top(1) from queue where courseID=? order by time");
				ps.setInt(1, courseID);
				ps.executeUpdate();
			} else {
				ps = conn.prepareStatement("delete from queue where courseID=? and studentID=?");
				ps.setInt(1, courseID);
				ps.setInt(2, studentID);
				ps.executeUpdate();
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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
	}

	// returns queue for specified course as string w ea entry as username and text
	// 0:username:text\n
	public static String selectQueue(int courseID) {
		String queue = "";
		String queue1="";
		String queue2="";
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
//		String sql = "jdbc:mysql://google/OHScheduler" // TODO db name
//				+ "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//				+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//				+ "&user=zhoue&password=password1234";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(sql, "root","root");
			ps = conn.prepareStatement("select * from queue where courseID=?"); // prepare statement
			ps.setInt(1, courseID);
			rs = ps.executeQuery();
			int i = 0;
			while (rs.next()) {
				String text = rs.getString("text");
				int studentID = rs.getInt("studentID");
				// get username assoc w studentID
				ps = conn.prepareStatement("select username from student where studentID=?"); // prepare statement
				ps.setInt(1, studentID);
				rs = ps.executeQuery();
				String username = null;
				while (rs.next()) {
					username = rs.getString("username");
				}
				queue1 +="<td>" + username +"</td>";
				queue2 += "<td>" + text + "</td>";
				i++;
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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
		queue1="<tr>"+queue1+"</tr>";
		queue2="<tr>"+queue2+"</tr>";
		queue= "<table border=\'1\' style=\'border-color: #990000; border-style: hidden; font-size: 20px; color: #990000; background-color: #FFCC00;\'>\r\n"+"<tr><th>Student Email</th><th>Student Text</th></tr>"+queue1+queue2+"</table>";
		
		return queue;
	}

	public static void deleteQueue(int courseID) {
		Connection conn = null;
		PreparedStatement ps = null;
//		String sql = "jdbc:mysql://google/OHScheduler" // TODO db name
//				+ "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//				+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//				+ "&user=zhoue&password=password1234";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(sql, "root", "root");
			ps = conn.prepareStatement("delete from queue where courseID=?");
			ps.setInt(1, courseID);
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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
	}

	// returns studentID for top student in queue for given course
	public static int getTopStudentID(int courseID) {
		int studentID = -1;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
//		String sql = "jdbc:mysql://google/OHScheduler" + "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//				+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//				+ "&user=zhoue&password=password1234";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(sql, "root", "root");
			ps = conn.prepareStatement("select studentID from queue where courseID=? order by time limit 1"); // prepare
																												// statement
			ps.setInt(1, courseID);
			rs = ps.executeQuery();
			if (rs.next()) {
				studentID = rs.getInt("studentID");
			}
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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

		return studentID;
	}

	public static Vector<String> getSecondStudentInfo(int courseID) {
		Vector<String> info = new Vector<>();
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
//		String sql = "jdbc:mysql://google/OHScheduler" + "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//				+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//				+ "&user=zhoue&password=password1234";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(sql, "root", "root");

			// get courseName
			ps = conn.prepareStatement("select courseName from course where courseID=?"); 
			ps.setInt(1, courseID);
			rs = ps.executeQuery();
			String courseName = "";
			if (rs.next()) {
				courseName = rs.getString("courseName");
			}
			
			// get studentID for 2nd in line
			ps = conn.prepareStatement("select studentID from queue where courseID=? order by time limit 2"); // prepare
			ps.setInt(1, courseID);
			rs = ps.executeQuery();
			int studentID = -1;
			if (rs.next() && rs.next()) {
				studentID = rs.getInt("studentID");
			}

			// if only <2 students in queue, not send email
			if (studentID == -1) {
				return null;
			}
			// get student info for 2nd in line
			ps = conn.prepareStatement("select * from student where studentID=?"); // prepare statement
			ps.setInt(1, studentID);
			rs = ps.executeQuery();
			if (rs.next()) {
				info.add(rs.getString("username"));
				info.add(rs.getString("fName"));
				info.add(courseName);
			}

		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
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

		return info;
	}

	public static void addStrike(int studentID) {
		// wait to confirm user table's structure.
		Connection conn = null;
		PreparedStatement ps = null;
//		String sql = "jdbc:mysql://google/OHScheduler" // TODO db name
//				+ "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//				+ "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//				+ "&user=zhoue&password=password1234";
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(sql, "root", "root");
			ps = conn.prepareStatement("update student set strike=strike+1 where studentID=?");
			ps.executeUpdate();
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
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
	}

}
