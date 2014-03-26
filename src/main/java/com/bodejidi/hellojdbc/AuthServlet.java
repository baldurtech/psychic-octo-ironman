package com.bodejidi.hellojdbc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if(isNotLogin(req)) {
            showLoginPage(req, resp);
        } else {
            resp.sendRedirect("/hellojdbc");
        }
    }

    public boolean isNotLogin(HttpServletRequest req)
        throws IOException, ServletException {

        Long memberId = (Long)req.getSession().getAttribute("memberId");
        return null == memberId;
    }

    public void showLoginPage(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("  <head>");
        out.println("    <title>Login</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>Please Login!</h1>");
        out.println("    <form action=\"member\" method=\"POST\">");
        out.println("      <label>Username: <input type=\"text\" name=\"username\"/></label>");
        out.println("      <label>Password: <input type=\"password\" name=\"password\"/></label>");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Login\" />");
        out.println("    </form>");
        out.println("  </body>");
        out.println("</html>");
    }

}
