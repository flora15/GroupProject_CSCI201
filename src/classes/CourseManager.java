package classes;

import java.sql.*;
import java.util.ArrayList;

public class CourseManager {
    static String sql ="jdbc:mysql://google/OHScheduler?cloudSqlInstance=tough-shard-260720:us-west1:csci201final&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false";


    /*
     * This function returns an ArrayList of strings containing all the course names that exist in
     * the database.
     */
    public static ArrayList<String> getAllCourses() {
        ArrayList<String> allCourses = new ArrayList<String>();
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;

        // TODO - UPDATE WITH FINAL DATABASE
//        String sql = "jdbc:mysql://google/Hmwk4Database?cloudSqlInstance=cs201-lab:us-central1:sql-db-2&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=root&password=111";
//      String sql = "jdbc:mysql://google/OHScheduler"
//              + "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//              + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//              + "&user=zhoue&password=password1234";
        try {
            conn = DriverManager.getConnection(sql, "root", "root");

            // selecting all courses from 'course' table;
            String statement = "select * from course";
            ps = conn.prepareStatement(statement);
            boolean selectedCourses = ps.execute(statement);

            // fill array if successful
            if (selectedCourses) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    String courseName = rs.getString("courseName");
                    allCourses.add(courseName);
                }
            } else {
                throw new SQLException("Could not retrieve courses");
            }

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

        return allCourses;
    }

    /*
     * This function returns an ArrayList of strings containing the courses for a specific
     * instructor 
     * Parameters: instructor's email
     */
    public static ArrayList<String> getInstructorCourses(String username) {
        System.out.println("In course manager get instructor courses for: " + username);
        ArrayList<String> instructorCourses = new ArrayList<String>();
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;

        // TODO - UPDATE WITH FINAL DATABASE
//        String sql = "jdbc:mysql://google/Hmwk4Database?cloudSqlInstance=cs201-lab:us-central1:sql-db-2&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=root&password=111";
////      String sql = "jdbc:mysql://google/OHScheduler"
//              + "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//              + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//              + "&user=zhoue&password=password1234";
        
        try {
            conn = DriverManager.getConnection(sql, "root", "root");

            // get instructor's id
            String statement =
                "select instructorID from instructor where username=\"" + username + "\"";
            int id = -1;

            ps = conn.prepareStatement(statement);
            boolean selectedCourses = ps.execute(statement);

            if (selectedCourses) {
                rs = ps.getResultSet();
                rs.next();
                id = rs.getInt("instructorID");
            } else {
                throw new SQLException("Could not retrieve instructor id");
            }
            ps.close();
            
            // select instructor's courses
            statement =
                "select c.courseName from course c inner join instructorCourse ic on ic.instructorID="
                    + id + " and ic.courseID = c.courseID";
            ps = conn.prepareStatement(statement);
            selectedCourses = ps.execute(statement);

            if (selectedCourses) {
                rs = ps.getResultSet();
                while (rs.next()) {
                    String courseName = rs.getString("courseName");
                    instructorCourses.add(courseName);
                }
            } else {
                throw new SQLException("Could not retrieve instructor courses");
            }

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

        return instructorCourses;
    }


    /*
     * This function returns an ArrayList containing all the information for an instructor's office
     * hours. 
     * Parameters: instructor's email
     */
    public static ArrayList<String> getOfficeHoursInstructor(String username) {
        ArrayList<String> hours = new ArrayList<String>();
/*
        ArrayList<String> courseNames = new ArrayList<String>();
        ArrayList<String> day = new ArrayList<String>();
        ArrayList<String> hourStart = new ArrayList<String>();
        ArrayList<String> hourEnd = new ArrayList<String>();
*/
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;

//        // TODO - UPDATE WITH FINAL DATABASE
//        String sql = "jdbc:mysql://google/Hmwk4Database?cloudSqlInstance=cs201-lab:us-central1:sql-db-2&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=root&password=111";
////      String sql = "jdbc:mysql://google/OHScheduler"
//              + "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//              + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//              + "&user=zhoue&password=password1234";
       
        try {
            conn = DriverManager.getConnection(sql, "root","root");
            String statement =
                "select courseID from instructorCourse where instructorID=(select instructorID from instructor where username=\'"
                    + username + "\')";

            ps = conn.prepareStatement(statement);
            boolean success = ps.execute(statement);

            if (success) {
                rs = ps.getResultSet();
                ps.close();
                while (rs.next()) {  // iterate through all courses that instructor is teaching 
                    int courseID = rs.getInt("courseID");
                    rs.close(); 
                    String statement2 =
                        "select courseName, day, hourStart, hourEnd from officeHour o inner join course c on c.courseID=o.courseID and c.courseID="
                            + courseID;
                    ps = conn.prepareStatement(statement2);
                    success = ps.execute(statement2);

                    if (success) {
                        rs = ps.getResultSet();
                        while (rs.next()) {  // iterate through all of instructor's office hours for that course 
                            String oh = rs.getString("courseName") + ": " + rs.getString("day") + " Start: " + rs.getInt("hourStart")
                                + " End: " + rs.getInt("hourEnd"); 
                           /* courseNames.add(rs.getString("courseName"));
                            day.add(rs.getString("day"));
                            hourStart.add(Integer.toString(rs.getInt("hourStart")));
                            hourEnd.add(Integer.toString(rs.getInt("hourEnd"))); */
                            hours.add(oh);
                        }
                    }
                }
            }

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
       /* hours.add(courseNames); 
        hours.add(day);
        hours.add(hourStart);
        hours.add(hourEnd);
        */
        return hours;
    }

    /* This function returns all the office hours for a specific course. 
     * Parameters: course name 
     */
    public static ArrayList<String> getOfficeHoursCourse(int courseID) {
        ArrayList<String> hours = new ArrayList<String>();
/*
        ArrayList<String> day = new ArrayList<String>();
        ArrayList<String> hourStart = new ArrayList<String>();
        ArrayList<String> hourEnd = new ArrayList<String>();
*/
        PreparedStatement ps = null;
        Connection conn = null;
        ResultSet rs = null;

        // TODO - UPDATE WITH FINAL DATABASE
//        String sql = "jdbc:mysql://google/Hmwk4Database?cloudSqlInstance=cs201-lab:us-central1:sql-db-2&socketFactory=com.google.cloud.sql.mysql.SocketFactory&useSSL=false&user=root&password=111";
//
//        String sql = "jdbc:mysql://google/OHScheduler"
//            + "?cloudSqlInstance=zhoue-csci201l-lab7:us-central1:sql-db-lab7"
//            + "&socketFactory=com.google.cloud.sql.mysql.SocketFactory" + "&useSSL=false"
//            + "&user=zhoue&password=password1234";
        try {
            conn = DriverManager.getConnection(sql, "root", "root");
            String statement =
                "select day, hourStart, hourEnd from officeHour where courseID=" + courseID;
            ps = conn.prepareStatement(statement);
            boolean success = ps.execute(statement);

            if (success) {
                rs = ps.getResultSet();
                while (rs.next()) {  // iterate through all course's office hours 
                    /*day.add(rs.getString("day"));
                    hourStart.add(Integer.toString(rs.getInt("hourStart")));
                    hourEnd.add(Integer.toString(rs.getInt("hourEnd")));
                    */
                    String oh = rs.getString("day") + " Start: " + rs.getInt("hourStart") + " End: " + rs.getInt("hourEnd");
                    hours.add(oh);
                }
            }

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
        /*
        hours.add(day);
        hours.add(hourStart);
        hours.add(hourEnd);
        */
        return hours;
    }
}
