package com.bodejidi.hellojdbc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class MemberServlet extends HttpServlet {

    static final String jdbcUrl = "jdbc:mysql://localhost/hellojdbc?user=root&password=";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        PrintWriter out = resp.getWriter();
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }

        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn =
                DriverManager.getConnection(jdbcUrl);

            resp.setContentType("text/html; charset=UTF-8");
            stmt = conn.createStatement();
            String paramId = req.getParameter("id");
            String sql = "SELECT * from member";
            if(null == paramId) {
                System.out.println("SQL: " + sql);
                rs = stmt.executeQuery(sql);
                out.println("<html><head><title>Member List</title></head><body><h1>Member List</h1><table border=\"1\"><tr><td>ID</td><td>Name</td></tr>\n");
                while(rs.next()) {
                    Long id = rs.getLong("id");
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    out.println("<tr><td><a href=\"?id=" + id + "\">" + id + "</a></td><td>" + firstName + " " + lastName + "</td></tr>\n");
                }
                out.println("</table>");
                out.println("<p><a href=\".\">Add member</a></p>");
                out.println("</body></html>");
            } else {
                sql = sql + " WHERE id=" + paramId;
                System.out.println("SQL: " + sql);
                rs = stmt.executeQuery(sql);

                rs.next();
                Long id = rs.getLong("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");

                out.println("<html><head><title>Member</title></head><body>"
                                         + "  <h1>Member</h1>"
                                         + "  <form action=\"member\" method=\"POST\">"
                                         + "    <table border=\"1\">\n");
                out.println("      <tr><td>ID</td><td>" + id + "</td></tr>\n");
                out.println("      <tr><td>First Name</td><td><input type=\"text\" name=\"first_name\" value=\"" + firstName + "\" /></td></tr>\n");
                out.println("      <tr><td>Last Name</td><td><input type=\"text\" name=\"last_name\" value=\"" + lastName + "\" /></td></tr>\n");
                out.println("    </table>");
                out.println("    <input type=\"hidden\" name=\"id\" value=\"" + id + "\" />");
                out.println("    <input type=\"submit\" name=\"action\" value=\"Update\" />");
                out.println("    <input type=\"submit\" name=\"action\" value=\"Delete\" />");
                out.println("  </form>");
                out.println("  <p><a href=\"member\">Member list</a></p>");
                out.println("</body></html>");
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                rs = null;
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                conn = null;
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        String id = req.getParameter("id");
        String firstName = req.getParameter("first_name");
        String lastName = req.getParameter("last_name");
        String action = req.getParameter("action");

        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            // handle the error
        }

        Connection conn = null;
        Statement stmt = null;

        try {
            conn =
                DriverManager.getConnection(jdbcUrl);
            stmt = conn.createStatement();

            if(id == null) {
                if(firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0) {
                    String sql = "INSERT INTO member(first_name, last_name, date_created, last_updated) "
                        + "VALUES('" + firstName + "', '" + lastName + "', now(), now());";
                    System.out.println("SQL: " + sql);
                    stmt.execute(sql);
                    out.println("Add " + firstName + " " + lastName + " success!");
                } else {
                    out.println("Error: first name or last name cannot be empty.");
                }
                out.println("<br/><a href=\"\">Member List</a>");
            } else if ("Delete".equalsIgnoreCase(action)) {
                String sql = "DELETE FROM member where id=" + id;
                System.out.println("SQL: " + sql);
                stmt.execute(sql);
                out.println("Delete ID=" + id + " success!");
                out.println("<br/><a href=\"\">Member List</a>");
            } else if("Update".equalsIgnoreCase(action)){
                String sql = "update member set first_name='" + firstName + "', last_name='" + lastName + "' where id="+id;
                System.out.println("SQL: " + sql);
                stmt.execute(sql);
                out.println("Update id=" + id + ": " + firstName + " " + lastName + " success!");
                out.println("<br/><a href=\"\">Member List</a>");
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        } finally {
            // it is a good idea to release
            // resources in a finally{} block
            // in reverse-order of their creation
            // if they are no-longer needed

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                stmt = null;
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqlEx) {
                    // ignore
                }
                conn = null;
            }
        }
    }
}
