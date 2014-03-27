package com.bodejidi.hellojdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.ResultSet;

public class MemberServlet extends HttpServlet {

    static final String MEMBER_TABLE = "member";
    static final String MEMBER_ID = "id";
    static final String MEMBER_FIRST_NAME = "first_name";
    static final String MEMBER_LAST_NAME = "last_name";
    static final String MEMBER_FORM_ID = "id";
    static final String MEMBER_FORM_FIRST_NAME = "first_name";
    static final String MEMBER_FORM_LAST_NAME = "last_name";
    static final String FORM_SUBMIT_ACTION = "action";

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");
        if(null == action || "".equals(action)) {
            action = "List";
        }

        switch(action.toLowerCase()) {
        case "create" : create(req, resp); break;
        case "show"   : show(req, resp); break;
        case "list"   :
        default       :
            list(req, resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter(FORM_SUBMIT_ACTION);

        if("Save".equalsIgnoreCase(action)) {
            save(req, resp);
        } else if ("Delete".equalsIgnoreCase(action)) {
            delete(req ,resp);
        } else if("Update".equalsIgnoreCase(action)){
            update(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Operation not allowed.");
        }
    }

    public void debug(String str) {
        System.out.println("[DEBUG] " + (new Date()) + " " + str);
    }

    public String showLoginInfo(HttpServletRequest req) {
        return "Welcome, admin. <a href=\"" + req.getContextPath() + "/auth/logout\">logout</a>";
    }

    public void create(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/WEB-INF/member/create.jsp");
        dispatcher.forward(req, resp);
    }

    public void list(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        try {
            out.println("<html><head><title>Member List</title></head>\n"
                        + "<body>\n"
                        + showLoginInfo(req)
                        + "<h1>Member List</h1>\n"
                        + "<table border=\"1\"><tr><td>ID</td>"
                        + "<td>Name</td></tr>\n");

            for(Member member: findAllMember()) {
                out.println("<tr><td><a href=\"?action=Show&id=" + member.getId() + "\">"
                            + member.getId()
                            + "</a></td><td>" + member.getFirstName()
                            + " " + member.getLastName()
                            + "</td></tr>\n");
            }
            out.println("</table>");
            out.println("<p><a href=\"?action=Create\">Add member</a></p>");
            out.println("</body></html>");
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
    }

    public void show(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();
        try {
            String paramId = req.getParameter(MEMBER_FORM_ID);
            Member member = getMemberById(paramId);

            out.println("<html><head><title>Member</title></head><body>"
                        + showLoginInfo(req)
                        + "  <h1>Member</h1>"
                        + "  <form action=\"member\" method=\"POST\">"
                        + "    <table border=\"1\">\n");

            out.println("      <tr><td>ID</td><td>" + member.getId() + "</td></tr>");

            out.println("      <tr><td>First Name</td><td>\n"
                        + "<input type=\"text\" name=\"first_name\""
                        + " value=\"" + member.getFirstName() + "\" /></td></tr>");

            out.println("      <tr><td>Last Name</td><td>\n"
                        + "<input type=\"text\" name=\"last_name\""
                        + " value=\"" + member.getLastName() + "\" /></td></tr>");

            out.println("    </table>");

            out.println("    <input type=\"hidden\" name=\"id\""
                        + " value=\"" + member.getId() + "\" />");

            out.println("    <input type=\"submit\" name=\"action\""
                        + " value=\"Update\" />");

            out.println("    <input type=\"submit\" name=\"action\""
                        + " value=\"Delete\" />");

            out.println("  </form>");
            out.println("  <p><a href=\"member\">Member list</a></p>");
            out.println("</body></html>");
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
    }

    public void save(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        try{
            String firstName = req.getParameter(MEMBER_FORM_FIRST_NAME);
            String lastName = req.getParameter(MEMBER_FORM_LAST_NAME);

            if(firstName != null && firstName.length() > 0 && lastName != null && lastName.length() > 0) {
                String sql = "INSERT INTO " + MEMBER_TABLE+ "(" + MEMBER_FIRST_NAME + ", " + MEMBER_LAST_NAME + ", date_created, last_updated) "
                    + "VALUES('" + firstName + "', '" + lastName + "', now(), now());";
                debug("SQL: " + sql);
                DatabaseService ds = DatabaseService.newInstance();
                ds.execute(sql);
                ds.close();
                out.println("Add " + firstName + " " + lastName + " success!");
            } else {
                out.println("Error: first name or last name cannot be empty.");
            }
            out.println("<br/><a href=\"\">Member List</a>");
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error: Cannot create member!");
        }
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        String id = req.getParameter(MEMBER_FORM_ID);

        try{
            String sql = "DELETE FROM " + MEMBER_TABLE + " where " + MEMBER_ID + "=" + id;
            debug("SQL: " + sql);

            DatabaseService ds = DatabaseService.newInstance();
            ds.execute(sql);
            ds.close();

            out.println("Delete ID=" + id + " success!");
            out.println("<br/><a href=\"\">Member List</a>");

        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error: Cannot delete member, id=" + id + "!");
        }
    }

    public void update(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        String id = req.getParameter(MEMBER_FORM_ID);
        String firstName = req.getParameter(MEMBER_FORM_FIRST_NAME);
        String lastName = req.getParameter(MEMBER_FORM_LAST_NAME);

        try{
            String sql = "update " + MEMBER_TABLE + " set " + MEMBER_FIRST_NAME + "='" + firstName + "', " + MEMBER_LAST_NAME + "='" + lastName + "' where " + MEMBER_ID + "="+id;
            debug("SQL: " + sql);

            DatabaseService ds = DatabaseService.newInstance();
            ds.execute(sql);
            ds.close();

            out.println("Update id=" + id + ": " + firstName + " " + lastName + " success!");
            out.println("<br/><a href=\"\">Member List</a>");
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error: Cannot Update member, id=" + id + "!");
        }
    }

    public Member getMemberById(String paramId)
        throws SQLException {

        Member member = new Member();

        String sql = "SELECT * from " + MEMBER_TABLE;
        sql = sql + " WHERE " + MEMBER_ID + "=" + paramId;
        debug("SQL: " + sql);

        DatabaseService databaseService = DatabaseService.newInstance();

        ResultSet rs = databaseService.executeQuery(sql);

        rs.next();
        member.setId(rs.getLong(MEMBER_ID));
        member.setFirstName(rs.getString(MEMBER_FIRST_NAME));
        member.setLastName(rs.getString(MEMBER_LAST_NAME));

        databaseService.close();

        return member;
    }

    public List<Member> findAllMember() throws SQLException {
        List<Member> memberList = new ArrayList<Member>();

        DatabaseService databaseService = DatabaseService.newInstance();

        String sql = "SELECT * from " + MEMBER_TABLE;
        debug("SQL: " + sql);

        ResultSet rs = databaseService.executeQuery(sql);

        while(rs.next()) {
            Member member = new Member();
            member.setId(rs.getLong(MEMBER_ID));
            member.setFirstName(rs.getString(MEMBER_FIRST_NAME));
            member.setLastName(rs.getString(MEMBER_LAST_NAME));
            memberList.add(member);
        }

        databaseService.close();

        return memberList;
    }
}
